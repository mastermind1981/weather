package com.crossover.trial.weather;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A reference implementation for the weather client. Consumers of the REST API can look at WeatherClient
 * to understand API semantics. This existing client populates the REST endpoint with dummy data useful for
 * testing.
 *
 * @author code test administrator
 */
public class WeatherClient {

    /**
     * Constant URI where a running application is expected.
     */
    private static final String BASE_URI = "http://localhost:9090";

    /**
     * End point for read queries.
     */
    private WebTarget query;

    /**
     * End point to supply updates.
     */
    private WebTarget collect;

    /**
     * Constructor for the client.
     */
    public WeatherClient() {
        Client client = ClientBuilder.newClient();
        query = client.target(BASE_URI + "/query");
        collect = client.target(BASE_URI + "/collect");
    }

    /**
     * Main method that tests communication.
     *
     * @param args no arguments expected
     */
    public static void main(final String[] args) {
        WeatherClient wc = new WeatherClient();
        wc.pingCollect();
        wc.populate("wind", 0, 10, 6, 4, 20);

        wc.query("BOS");
        wc.query("JFK");
        wc.query("EWR");
        wc.query("LGA");
        wc.query("MMU");

        wc.pingQuery();
        wc.exit();
        System.out.print("complete");
        System.exit(0);
    }

    /**
     * Pings weather server, to see if it's ready.
     */
    public void pingCollect() {
        WebTarget path = collect.path("/ping");
        Response response = path.request().get();
        System.out.print("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    /**
     * Queries weather service for data by particulat IATA code.
     *
     * @param iata IATA code to query by
     */
    public void query(final String iata) {
        WebTarget path = query.path("/weather/" + iata + "/0");
        Response response = path.request().get();
        System.out.println("query." + iata + ".0: " + response.readEntity(String.class));
    }

    /**
     * Call ping service providing health and status information for the the query api.
     */
    public void pingQuery() {
        WebTarget path = query.path("/ping");
        Response response = path.request().get();
        System.out.println("query.ping: " + response.readEntity(String.class));
    }

    /**
     * Populates provided data into source.
     *
     * @param pointType type of collected data
     * @param first 1st percentile
     * @param last 3rd percentile
     * @param mean mean of the collected data
     * @param median median
     * @param count number of measurements
     */
    public void populate(final String pointType, final int first, final int last, final int mean,
                         final int median, final int count) {
        WebTarget path = collect.path("/weather/BOS/" + pointType);
        DataPoint dp = new DataPoint.Builder()
            .withFirst(first).withLast(last).withMean(mean).withMedian(median).withCount(count)
            .build();
        Response post = path.request().post(Entity.entity(dp, "application/json"));
    }

    /**
     * Calls exit method that stops remote server.
     */
    public void exit() {
        try {
            collect.path("/exit").request().get();
        } catch (Throwable t) {
            // swallow
        }
    }
}
