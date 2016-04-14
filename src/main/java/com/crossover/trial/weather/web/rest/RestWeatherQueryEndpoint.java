package com.crossover.trial.weather.web.rest;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.google.gson.Gson;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory.
 * <p>
 * The end point deploys to a single container.
 *
 * @author code test administrator
 */
@Component
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger("WeatherQuery");

    /**
     * Earth radius in KM.
     */
    private static final double R = 6372.8;

    /**
     * Gson json to object factory.
     */
    @Inject
    private Gson gson;

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */

    /**
     * Requests frequency [IATA Code -> number of requests].
     */
    private static Map<String, Integer> requestFrequency = new ConcurrentHashMap<>();

    /**
     * Radius frequency.
     */
    private static Map<Double, Integer> radiusFreq = new ConcurrentHashMap<>();

    /**
     * Provides access to Airport Data Repository.
     */
    @Inject
    private AirportDataRepository airportDataRepository;

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    @GET
    @Path("/ping")
    public String ping() {
        Map<String, Object> result = new HashMap<>();

        long datasize = StreamSupport.stream(airportDataRepository.findAll().spliterator(), false)
            .map(AirportData::getAtmosphericInformation)
            // we only count recent readings updated in the last day
            .filter(atmosphericInformation -> !atmosphericInformation.isEmpty()
                && atmosphericInformation.getLastUpdateTime()
                > System.currentTimeMillis() - ChronoUnit.DAYS.getDuration().toMillis())
            .count();

        result.put("datasize", datasize);

        // fraction of queries
        Map<String, Double> iataRequestFractions =
            StreamSupport.stream(airportDataRepository.findAll().spliterator(), false)
                .map(AirportData::getIata)
                .collect(
                    Collectors.toMap(iata -> iata, iata -> requestFrequency.getOrDefault(iata, 0)
                        / (double) requestFrequency.size()));
        result.put("iata_freq", iataRequestFractions);

        // TODO: this code is very suspicious, and if I could communicate with stakeholders, I'd clarified why
        // algorithm implementation is like this. Most likely there would be some changes in this code.
        // So far leaving as is.
        int m = radiusFreq.keySet().stream()
            .max(Double::compare)
            .orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        result.put("radius_freq", hist);

        return gson.toJson(result);
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata         the iataCode
     * @param radiusString the radius in km
     * @return a findAll of atmospheric information
     */
    @Override
    @GET
    @Path("/weather/{iata}/{radius}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response weather(@PathParam("iata") final String iata, @PathParam("radius") final String radiusString) {
        Double radius = NumberUtils.toDouble(radiusString, 0.0d);
        updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> result = new ArrayList<>();
        AirportData centerAirportData = airportDataRepository.findOne(iata);
        if (centerAirportData == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }

        if (radius.equals(0.0d)) {
            result.add(centerAirportData.getAtmosphericInformation());
        } else {
            result = StreamSupport.stream(airportDataRepository.findAll().spliterator(), false)
                .filter(candidateAirport -> calculateDistance(centerAirportData, candidateAirport) <= radius)
                .filter(candidateRangedAirport -> !candidateRangedAirport.getAtmosphericInformation().isEmpty())
                .map(AirportData::getAtmosphericInformation)
                .collect(Collectors.toList());
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    /**
     * Records information about how often requests are made.
     *
     * @param iata   an iata code
     * @param radius query radius
     */
    private void updateRequestFrequency(final String iata, final Double radius) {
        requestFrequency.put(iata, requestFrequency.getOrDefault(iata, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    private double calculateDistance(final AirportData ad1, final AirportData ad2) {

        // there's an alternative formula, if this one will not work for some reason
        // http://www.movable-type.co.uk/scripts/latlong.html
        // the site also contains live checker that can be used to verify own code.
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
            + Math.pow(Math.sin(deltaLon / 2), 2)
            * Math.cos(Math.toRadians(ad1.getLatitude()))
            * Math.cos(Math.toRadians(ad2.getLatitude()));
        double c = 2 * Math.asin(Math.sqrt(a));
        double d = R * c;

        LOG.debug("\nad1.IATA={} ad1.lat={} ad1.lon={}\n"
                + "ad2.IATA={} ad2.lat ad2.lon={}\nd={}",
            ad1.getIata(), ad1.getLatitude(), ad1.getLongitude(),
            ad2.getIata(), ad2.getLatitude(), ad2.getLongitude(),
            d);

        return d;

    }

}
