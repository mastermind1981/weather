package com.crossover.trial.weather.service;

import com.crossover.trial.weather.domain.AirportData;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.crossover.trial.weather.util.DistanceMath;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Query service added only because Cacheable annotation does not work with
 * REST service's methods.
 */
@Service
public class QueryService {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(QueryService.class);

    /**
     * Provides access to Airport Data Repository.
     */
    @Inject
    private AirportDataRepository airportDataRepository;


    /**
     * Method that does the actual search {@see #weather()}.
     * <p>
     * Method was introduced to enable caching.
     *
     * @param iata         IATA code
     * @param radiusString radius to search in
     * @return list of weather information in given center/radius-area.
     */
    @Cacheable("findWeatherInRadius")
    public List<AtmosphericInformation> findWeatherInRadius(final String iata, final String radiusString) {
        Double radius = NumberUtils.toDouble(radiusString, 0.0d);

        List<AtmosphericInformation> result = new ArrayList<>();
        AirportData centerAirportData = airportDataRepository.findOne(iata);
        if (centerAirportData != null) {

            if (radius.equals(0.0d)) {
                result.add(centerAirportData.getAtmosphericInformation());
            } else {
                result = StreamSupport.stream(airportDataRepository.findAll().spliterator(), false)
                    .filter(candidateAirport -> DistanceMath.calculateDistance(centerAirportData, candidateAirport) <= radius)
                    .filter(candidateRangedAirport -> !candidateRangedAirport.getAtmosphericInformation().isEmpty())
                    .map(AirportData::getAtmosphericInformation)
                    .collect(Collectors.toList());
            }

        }

        LOG.debug("R[{}:{}]", iata, radius);
        return result;
    }
}
