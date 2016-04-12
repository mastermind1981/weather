package com.crossover.trial.weather.domain;

import com.crossover.trial.weather.WeatherException;

/**
 * Encapsulates sensor information for a particular location.
 */
public class AtmosphericInformation {

    /**
     * Error message.
     */
    private static final String MSG_UPDATE_ATMOSPHERIC_DATA_FAIL = "Couldn't update atmospheric data";

    /**
     * Temperature in degrees celsius.
     */
    private DataPoint temperature;

    /**
     * Wind speed in km/h.
     */
    private DataPoint wind;

    /**
     * Humidity in percent.
     */
    private DataPoint humidity;

    /**
     * Precipitation in cm.
     */
    private DataPoint precipitation;

    /**
     * Pressure in mmHg.
     */
    private DataPoint pressure;

    /**
     * Cloud cover percent from 0 - 100 (integer).
     */
    private DataPoint cloudCover;

    /**
     * The last time this data was updated, in milliseconds since UTC epoch.
     */
    private long lastUpdateTime = System.currentTimeMillis();

    /**
     * Default constructor.
     */
    public AtmosphericInformation() {
        // for cases when no init data available
    }

    /**
     * Convenience constructor.
     *
     * @param temperature   value to set
     * @param wind          value to set
     * @param humidity      value to set
     * @param percipitation value to set
     * @param pressure      value to set
     * @param cloudCover    value to set
     */
    protected AtmosphericInformation(final DataPoint temperature, final DataPoint wind,
                                     final DataPoint humidity, final DataPoint percipitation,
                                     final DataPoint pressure, final DataPoint cloudCover) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.precipitation = percipitation;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
    }


    /**
     * Update atmospheric information with the given data point for the given point type.
     *
     * @param pointType the data point type as a string
     * @param dp        the actual data point
     * @throws WeatherException if dataPoint type does not match any of the supported or {@see DataPointType}
     */
    public void update(final String pointType, final DataPoint dp) throws WeatherException {
        final DataPointType dataPointType = DataPointType.valueOf(pointType.toUpperCase());

        switch (dataPointType) {

            case WIND:
                setWind(dp);
                break;

            case TEMPERATURE:
                setTemperature(dp);
                break;

            case HUMIDTY:
                setHumidity(dp);
                break;

            case PRESSURE:
                setPressure(dp);
                break;

            case CLOUDCOVER:
                setCloudCover(dp);
                break;

            case PRECIPITATION:
                setPrecipitation(dp);
                break;

            default:
                throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }

        setLastUpdateTime(System.currentTimeMillis());
    }

    /**
     * @return Temperature in degrees celsius.
     */
    public DataPoint getTemperature() {
        return temperature;
    }

    /**
     * Sets temperature (in degrees celsius).
     *
     * @param temperature value to set
     */
    public void setTemperature(final DataPoint temperature) {
        if (temperature.getMean() >= Validation.TEMPERATURE_MIN
            && temperature.getMean() < Validation.TEMPERATURE_MAX) {
            this.temperature = temperature;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return Wind speed in km/h.
     */
    public DataPoint getWind() {
        return wind;
    }

    /**
     * Sets wind speed in km/h.
     *
     * @param wind value to set
     */
    public void setWind(final DataPoint wind) {
        if (wind.getMean() >= Validation.WIND_MIN) {
            this.wind = wind;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return Humidity in percent.
     */
    public DataPoint getHumidity() {
        return humidity;
    }

    /**
     * Set humidity in percent.
     *
     * @param humidity value to set
     */
    public void setHumidity(final DataPoint humidity) {
        if (humidity.getMean() >= Validation.HUMIDITY_MIN
            && humidity.getMean() < Validation.HUMIDITY_MAX) {
            this.humidity = humidity;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return Precipitation in cm.
     */
    public DataPoint getPrecipitation() {
        return precipitation;
    }

    /**
     * Set precipitation in cm.
     *
     * @param precipitation value to set
     */
    public void setPrecipitation(final DataPoint precipitation) {
        if (precipitation.getMean() >= Validation.PRECIPITATION_MIN
            && precipitation.getMean() < Validation.PRECIPITATION_MAX) {
            this.precipitation = precipitation;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return Pressure in mmHg.
     */
    public DataPoint getPressure() {
        return pressure;
    }

    /**
     * Set pressure in mmHg.
     *
     * @param pressure value to set
     */
    public void setPressure(final DataPoint pressure) {
        if (pressure.getMean() >= Validation.PRESSURE_MIN
            && pressure.getMean() < Validation.PRESSURE_MAX) {
            this.pressure = pressure;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return Cloud cover percent from 0 - 100 (integer).
     */
    public DataPoint getCloudCover() {
        return cloudCover;
    }

    /**
     * Set cloud cover percent from 0 - 100 (integer).
     *
     * @param cloudCover value to set
     */
    public void setCloudCover(final DataPoint cloudCover) {
        if (cloudCover.getMean() >= Validation.CLOUD_COVER_MIN
            && cloudCover.getMean() < Validation.CLOUD_COVER_MAX) {
            this.cloudCover = cloudCover;
        } else {
            throw new IllegalStateException(MSG_UPDATE_ATMOSPHERIC_DATA_FAIL);
        }
    }

    /**
     * @return The last time this data was updated, in milliseconds since UTC epoch.
     */
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }


    /**
     * Set the last time this data was updated, in milliseconds since UTC epoch.
     *
     * @param lastUpdateTime value to set
     */
    public void setLastUpdateTime(final long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return <code>true</code> if none of the fields is initialized (all nulls),
     * <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return cloudCover == null
            && humidity == null
            && precipitation == null
            && pressure == null
            && temperature == null
            && wind == null;
    }


    /**
     * Class containing validation constants for field values.
     */
    private static final class Validation {
        /**
         * Minimum possible temperature value.
         */
        static final double TEMPERATURE_MIN = -50;
        /**
         * Maximum possible temperature value.
         */
        static final double TEMPERATURE_MAX = 100;

        /**
         * Minimum possible wind speed.
         */
        static final double WIND_MIN = 0;

        /**
         * Minimum possible humidity value.
         */
        static final double HUMIDITY_MIN = 0;

        /**
         * Maximum possible humidity value.
         */
        static final double HUMIDITY_MAX = 100;

        /**
         * Minimum possible precipitation value.
         */
        static final double PRECIPITATION_MIN = 0;
        /**
         * Maximum possible precipitation value.
         */
        static final double PRECIPITATION_MAX = 100;

        /**
         * Minimum possible pressure value.
         */
        static final double PRESSURE_MIN = 650;
        /**
         * Maximum possible pressure value.
         */
        static final double PRESSURE_MAX = 800;

        /**
         * Minimum possible cloud cover value.
         */
        static final double CLOUD_COVER_MIN = 0;
        /**
         * Maximum possible cloud cover value.
         */
        static final double CLOUD_COVER_MAX = 100;

        /**
         * Hide utility class constructor.
         */
        private Validation() {
        }
    }
}
