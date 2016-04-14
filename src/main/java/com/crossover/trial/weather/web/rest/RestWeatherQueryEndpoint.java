package com.crossover.trial.weather.web.rest;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.crossover.trial.weather.service.QueryService;
import com.google.gson.Gson;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
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
     * Query service instance.
     */
    @Inject
    private QueryService queryService;

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

        // TODO: this code is very suspicious, and if I could communicate with stakeholders, I'd clarified
        // what exactly one was trying to implement. Otherwise no chance to figure out.
        // Most likely I'd changed this code, but since API modification is not allowed,
        // leaving it as is.
        // For this same reason I'm not applying any performance optimizations here.
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

        List<AtmosphericInformation> result = queryService.findWeatherInRadius(iata, radiusString);

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

}
