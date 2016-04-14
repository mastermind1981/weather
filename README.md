# weather
SpringBoot-powered Crossover test assignment implementation.


# Summary of changes

NOTE: history of commits is available at github:
https://github.com/coldserenity/weather/tree/master


## Implemented
1. Added .gitignore
2. Added .editorconfig for uniform file formatting.
    * Configured source version (Java 1.8) and encoding (UTF-8) at property-level;  
    * Added PMD, Checkstyle and Findbugs plug-ins; configured reporting;
3. Reformatted code according to defined style
4. Fixed majority of checkstyle/findbus/sonar-way errors
5. Refactored project in order to further fix code violations   
    * Added spring boot to simplify project start-up and dependency management   
    * Changed logging framework to slf4j+logback;   
    * Restructured project to clearly isolate classes by responsibility;   
    * As a consequence         
         * refactored store, to use SpringData-based simple key-value store instead 
           of plain list (which might have had concurrency issues);
         * refactored application domain to achieve better encapsulation;
    * Updated tests to be SpringBoot-driven;
    * Fixed defects in application. Amongst major ones
         * Distance calculation formula was incorrect;
         * DataPoint.mean was Integer instead of Double.
6. Implement AirportLoader and missing REST methods   
    * Implemented AirportLoader;
    * Implemented create and delete Airport Data moethods;   
    * Fixed WeatherClient and WeatherEndpointTest, to populate required data;
    * Improved separation of concerns between DataPointType and AtmosphericInformation 
      classes (validation constants);
    * Fixed port setting;
    * Made Gson instance shared in spring context and injected where needed;
7. Added caching to most heavy distance calculation methods. Added cache eviction.


## What I'd like implemented to consider implementation "perfect"
1. More tests for changed classes (I have re-used existing ones with + 
   client runners to test application in debug mode);
2. Cache size configuration;
3. Replace existing performance measurers with with Spring Metrics;
4. Swagger integration;
5. Generated SonarQube code quality evolution graph;
6. Separate integration and Unit Test;
7. Create load tests.
