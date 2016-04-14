package com.crossover.trial.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.data.map.repository.config.EnableMapRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * This main method will be use by the automated functional grader.
 * You shouldn't move this class or remove the main method.
 * <p>
 * You may change the implementation, but we encourage caution.
 *
 * @author code test administrator
 */
@SpringBootApplication
@EnableMapRepositories("com.crossover.trial.weather.repository")
@EnableCaching
//@EnableConfigurationProperties( {SwaggerProperties.class})
public class WeatherServer extends SpringBootServletInitializer {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(WeatherServer.class);

    /**
     * Main method for running the server.
     *
     * @param args no input expected
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(final String... args) throws UnknownHostException {
        Environment env = new WeatherServer()
            .configure(new SpringApplicationBuilder(WeatherServer.class))
            .run(args).getEnvironment();


        LOG.info("\n----------------------------------------------------------\n\t"
                + "Application '{}' is running! Access URLs:\n\t"
                + "Local: \t\thttp://{}:{}\n\t"
                + "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            env.getProperty("util.localhost"),
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));

        String configServerStatus = env.getProperty("configserver.status");
        LOG.info("\n----------------------------------------------------------\n\t"
                + "Config Server: \t{}\n----------------------------------------------------------",
            configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);

    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(WeatherServer.class);
    }
}
