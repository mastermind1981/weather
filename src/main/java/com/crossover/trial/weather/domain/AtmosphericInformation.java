package com.crossover.trial.weather.domain;

import com.crossover.trial.weather.WeatherException;

import static com.crossover.trial.weather.domain.DataPointType.CLOUDCOVER;
import static com.crossover.trial.weather.domain.DataPointType.HUMIDTY;
import static com.crossover.trial.weather.domain.DataPointType.PRECIPITATION;
import static com.crossover.trial.weather.domain.DataPointType.PRESSURE;
import static com.crossover.trial.weather.domain.DataPointType.TEMPERATURE;
import static com.crossover.trial.weather.domain.DataPointType.WIND;

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
        if (temperature.getMean() >= TEMPERATURE.min()
            && temperature.getMean() < TEMPERATURE.max()) {
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
        if (wind.getMean() >= WIND.min()) {
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
        if (humidity.getMean() >= HUMIDTY.min()
            && humidity.getMean() < HUMIDTY.max()) {
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
        if (precipitation.getMean() >= PRECIPITATION.min()
            && precipitation.getMean() < PRECIPITATION.max()) {
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
        if (pressure.getMean() >= PRESSURE.min()
            && pressure.getMean() < PRESSURE.max()) {
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
        if (cloudCover.getMean() >= CLOUDCOVER.min()
            && cloudCover.getMean() < CLOUDCOVER.max()) {
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

}
