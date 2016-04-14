package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.domain.AirportData;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * CRUD for Airport Data entity.
 */
@Component
public interface AirportDataRepository extends CrudRepository<AirportData, String> {
    @Override
    @CacheEvict(value = "findWeatherInRadius", allEntries = true)
    <S extends AirportData> S save(S entity);

    @Override
    @CacheEvict(value = "findWeatherInRadius", allEntries = true)
    void delete(String s);
}
