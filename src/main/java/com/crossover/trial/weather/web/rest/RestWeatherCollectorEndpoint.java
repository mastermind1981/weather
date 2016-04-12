package com.crossover.trial.weather.web.rest;

import com.crossover.trial.weather.WeatherException;
import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;


/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * Note: JEE specification recommends copying annotations from interface to implementing class
 * in order to be fully-compatible with specification
 * .
 * @author code test administrator
 */

@Component
@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RestWeatherCollectorEndpoint.class);

    /**
     * Shared gson json to object factory.
     */
    private static final Gson GSON = new Gson();


    /**
     * Provides access to Airport Data Repository.
     */
    @Inject
    private AirportDataRepository airportDataRepository;

    @Override
    @GET
    @Path("/ping")
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    @POST
    @Path("/weather/{iata}/{pointType}")
    public Response updateWeather(@PathParam("iata") final String iataCode,
                                  @PathParam("pointType") final String pointType,
                                  final String datapointJson) {
        try {
            addDataPoint(iataCode, pointType, GSON.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            LOG.error("An error occurred while updating weather", e);
        }
        return Response.status(Response.Status.OK).build();
    }

    @Override
    @GET
    @Path("/airports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirports() {
        Set<String> retval = new HashSet<>();
        for (AirportData ad : airportDataRepository.findAll()) {
            retval.add(ad.getIata());
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }

    @Override
    @GET
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirport(@PathParam("iata") final String iata) {
        AirportData ad = airportDataRepository.findOne(iata);
        return Response.status(Response.Status.OK).entity(ad).build();
    }

    @Override
    @POST
    @Path("/airport/{iata}/{lat}/{long}")
    public Response addAirport(@PathParam("iata") final String iata,
                               @PathParam("lat") final String latString,
                               @PathParam("long") final String longString) {
        addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }

    @Override
    @DELETE
    @Path("/airport/{iata}")
    public Response deleteAirport(@PathParam("iata") final String iata) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    @GET
    @Path("/exit")
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }

    //
    // Internal support methods
    //

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode  the 3 letter IATA code
     * @param pointType the point type {@link com.crossover.trial.weather.domain.DataPointType}
     * @param dp        a datapoint object holding pointType data
     * @throws WeatherException if the update can not be completed
     */
    private void addDataPoint(final String iataCode, final String pointType,
                              final DataPoint dp) throws WeatherException {
        AirportData airportData = airportDataRepository.findOne(iataCode);
        airportData.getAtmosphericInformation().update(pointType, dp);
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode  3 letter code
     * @param latitude  in degrees
     * @param longitude in degrees
     * @return the added airport
     */

    private AirportData addAirport(final String iataCode, final double latitude, final double longitude) {
        AirportData ad = new AirportData();
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLongitude(longitude);
        airportDataRepository.save(ad);

        return ad;
    }
}
