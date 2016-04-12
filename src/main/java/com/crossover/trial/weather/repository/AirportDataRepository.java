package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.domain.AirportData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * CRUD for Airport Data entity.
 */
@Component
public interface AirportDataRepository extends CrudRepository<AirportData, String> {
}
