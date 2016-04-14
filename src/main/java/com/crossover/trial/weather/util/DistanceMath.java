package com.crossover.trial.weather.util;

import com.crossover.trial.weather.domain.AirportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for distance calculations.
 */
@Component
public final class DistanceMath {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DistanceMath.class);

    /**
     * Earth radius in KM.
     */
    private static final double R = 6372.8;

    /**
     * Hide utility class constructor.
     */

    private DistanceMath() {
    }

    /**
     * Haversine distance between two airports.
     * <p>
     * Note: if necessary this clas can be turned into a bean and method cached
     * as {@literal @}Cacheable(cacheNames = "distances", key = "#ad1.hashCode() + #ad2.hashCode()")
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    public static double calculateDistance(final AirportData ad1, final AirportData ad2) {

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
                + "ad2.IATA={} ad2.lat={} ad2.lon={}\nd={}",
            ad1.getIata(), ad1.getLatitude(), ad1.getLongitude(),
            ad2.getIata(), ad2.getLatitude(), ad2.getLongitude(),
            d);

        return d;

    }
}
