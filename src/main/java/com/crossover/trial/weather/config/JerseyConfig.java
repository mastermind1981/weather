package com.crossover.trial.weather.config;

import com.crossover.trial.weather.web.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.web.rest.RestWeatherQueryEndpoint;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
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

    /**
     * @return a shared {@link Gson} object.
     */
    @Bean
    public Gson getGson() {
        return new Gson();
    }
}
