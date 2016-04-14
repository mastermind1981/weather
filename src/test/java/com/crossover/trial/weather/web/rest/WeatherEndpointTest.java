package com.crossover.trial.weather.web.rest;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherEndpointTest {

    @Inject
    private WeatherQueryEndpoint _query;
    @Inject
    private WeatherCollectorEndpoint _update;
    @Inject
    private Gson _gson;

    private DataPoint _dp;

    @Before
    public void setUp() throws Exception {
        init();
        _dp = new DataPoint.Builder()
            .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(_dp));
        _query.weather("BOS", "0").getEntity();
    }

    /**
     * Backwards compatibility init method that loads data that was previously
     * hard coded inside rest service.
     */
    private void init() {
        _update.addAirport("BOS", "42.364347", "-71.005181");
        _update.addAirport("EWR", "40.6925", "-74.168667");
        _update.addAirport("JFK", "40.639751", "-73.778925");
        _update.addAirport("LGA", "40.777245", "-73.872608");
        _update.addAirport("MMU", "40.79935", "-74.4148747");
    }


    @Test
    public void testPing() throws Exception {
        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), _dp);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        DataPoint.Builder dataPointBuilder = new DataPoint.Builder()
            .withCount(_dp.getCount())
            .withFirst(_dp.getFirst())
            .withMedian(_dp.getSecond())
            .withLast(_dp.getThird())
            .withMean(_dp.getMean());

        _update.updateWeather("JFK", "wind", _gson.toJson(_dp));
        dataPointBuilder.withMean(40);
        _update.updateWeather("EWR", "wind", _gson.toJson(dataPointBuilder.build()));
        dataPointBuilder.withMean(30);
        _update.updateWeather("LGA", "wind", _gson.toJson(dataPointBuilder.build()));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder()
            .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(windDp));
        _query.weather("BOS", "0").getEntity();

        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint.Builder()
            .withCount(4).withFirst(10).withMedian(60).withLast(100).withMean(50).build();
        _update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

}
