package com.crossover.trial.weather.config;

import com.crossover.trial.weather.web.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.web.rest.RestWeatherQueryEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Configure Jersey resources.
 */
@Component
public class JerseyConfig extends ResourceConfig {

    /**
     * Redefining default constructor.
     */
    public JerseyConfig() {
        register(RestWeatherCollectorEndpoint.class);
        register(RestWeatherQueryEndpoint.class);
    }

}
