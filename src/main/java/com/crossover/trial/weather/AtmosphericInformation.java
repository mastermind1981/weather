package com.crossover.trial.weather;

/**
 * Encapsulates sensor information for a particular location.
 */
class AtmosphericInformation {

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
    private long lastUpdateTime;

    /**
     * Default constructor.
     */
    AtmosphericInformation() {
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
        this.lastUpdateTime = System.currentTimeMillis();
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
        this.temperature = temperature;
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
        this.wind = wind;
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
        this.humidity = humidity;
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
        this.precipitation = precipitation;
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
        this.pressure = pressure;
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
        this.cloudCover = cloudCover;
    }

    /**
     * @return The last time this data was updated, in milliseconds since UTC epoch.
     */
    protected long getLastUpdateTime() {
        return this.lastUpdateTime;
    }


    /**
     * Set the last time this data was updated, in milliseconds since UTC epoch.
     *
     * @param lastUpdateTime value to set
     */
    protected void setLastUpdateTime(final long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
