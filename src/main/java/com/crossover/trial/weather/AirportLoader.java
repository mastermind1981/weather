package com.crossover.trial.weather;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice.
 *
 * @author code test administrator
 */
@Component
public final class AirportLoader {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AirportLoader.class);

    /**
     * End point to supply updates.
     */
    private WebTarget collect;

    /**
     * Format of airport data file that we're going to parse.
     */
    private static final CSVFormat AIRPORT_DATA_FILE_FORMAT = CSVFormat.RFC4180.withHeader(Header.getHeaders());

    /**
     * Loading of airports must be done only via main method.
     *
     * @param host host where weather application is running (auto-injected)
     * @param port port where weather application is running (auto-injected)
     */
    private AirportLoader(@Value("${server.host}") final String host,
                          @Value("${server.port}") final String port) {
        Client client = ClientBuilder.newClient();

        // Constant URI where a running application is expected.
        final String baseUri = String.format("http://%s:%s", host, port);
        collect = client.target(baseUri + "/collect");
    }

    /**
     * Main method for running airport import.
     *
     * @param args accepts single argument - a file name to read airports from
     * @throws IOException if any
     */
    public static void main(final String... args) throws IOException {
        ApplicationContext ctx =
            new AnnotationConfigApplicationContext("com.crossover.trial.weather");

        AirportLoader airportLoader = ctx.getBean(AirportLoader.class);

        File airportDataFile = new File("src/main/resources/airports.dat");

        try (CSVParser parser = createCsvParserForResource(airportDataFile)) {
            airportLoader.upload(parser);
        }
    }

    /**
     * Method that sends airport data from iterable set to the web-service.
     *
     * @param resultSet data to send to web-service
     */
    public void upload(final Iterable<CSVRecord> resultSet) {

        // 1. Because previous implementation totally relied on the
        //    fact that IATA exists and it uniquely identifies airport
        //    we make same assumptions here.
        //    if needed this place can be changed to treat ICAO as well.
        //    so far there's not enough information about correlation between
        //    ICAO and IATA to do better processing.
        // 2. Only 3 fields that existed in original model are processed.
        //    Again, because there's not enough information on how to process
        //    the others. Especially taking into consideration that adding fields
        //    might have influenced external REST APIs.
        for (CSVRecord csvRecord : resultSet) {
            WebTarget path = collect.path(
                String.format("/airport/%s/%s/%s",
                    csvRecord.get(Header.IATA),
                    csvRecord.get(Header.ALTITUDE),
                    csvRecord.get(Header.LONGITUDE))
            );
            Response response = path.request().post(null);
            LOG.debug("Added airport {}. Response: {}", csvRecord, response);
        }
    }

    /**
     * Creates CSV parser to read data from file.
     *
     * @param airportDataFile file to read from
     * @return parser instance
     * @throws IOException if any
     */
    private static CSVParser createCsvParserForResource(final File airportDataFile) throws IOException {
        return CSVParser.parse(airportDataFile, Charset.forName("UTF-8"), AIRPORT_DATA_FILE_FORMAT);
    }


    /**
     * @return properties containing host and port information.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config/application.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }

    /**
     * Represents the names of column headers that will appear in the input airport data file.
     */
    private enum Header {
        /**
         * Record number.
         */
        RECORD_NUM("RecordNum"),

        /**
         * Main city served by airport. May be spelled differently from name.
         */
        CITY("City"),

        /**
         * Country or territory where airport is located.
         */
        COUNTRY("Country"),

        /**
         * 3-letter FAA code or IATA code (blank or "" if not assigned).
         */
        IATA("IATA/FAA"),

        /**
         * 4-letter ICAO code (blank or "" if not assigned).
         */
        ICAO("ICAO"),

        /**
         * Decimal degrees, up to 6 significant digits. Negative is South, positive is North.
         */
        LATTITUDE("Latitude"),

        /**
         * Decimal degrees, up to 6 significant digits. Negative is West, positive is East.
         */
        LONGITUDE("Longitude"),

        /**
         * In feet.
         */
        ALTITUDE("Altitude"),

        /**
         * Hours offset from UTC. Fractional hours are expressed as decimals. (e.g. India is 5.5).
         */
        TIME_ZONE("Timezone"),

        /**
         * One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown).
         */
        DST("DST");

        /**
         * String representation of the enum item.
         */
        private String value;

        /**
         * Constructor for initializing enum item with corresponding string value.
         *
         * @param value string value to set for enum item
         */
        Header(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        /**
         * @return list of headers' string representation
         */
        public static String[] getHeaders() {
            return Stream.of(Header.values()).map(Header::toString).toArray(String[]::new);
        }
    }
}
