package com.crossover.trial.weather.domain;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType {

    /**
     * Constant for "WIND" type of data point.
     */
    WIND(0, Integer.MAX_VALUE),

    /**
     * Constant for "TEMPERATURE" type of data point.
     */
    TEMPERATURE(-50, 100),

    /**
     * Constant for "HUMIDTY" type of data point.
     *
     * TODO: Rename to HUMIDITY if allowed by end clients
     * at least for code this can be achieved using this hackish approach
     * http://stackoverflow.com/a/21903339/3112116
     */
    HUMIDTY(0, 100),

    /**
     * Constant for "PRESSURE" type of data point.
     */
    PRESSURE(650, 800),

    /**
     * TODO: Rename to CLOUD_COVER if allowed by end clients
     * Constant for "CLOUDCOVER" type of data point.
     */
    CLOUDCOVER(0, 100),

    /**
     * Constant for "PRECIPITATION" type of data point.
     */
    PRECIPITATION(0, 100);

    /**
     * Minimum allowed value for data point type.
     */
    private final Integer min;

    /**
     * Maximum allowed value for data point type.
     */
    private final Integer max;

    /**
     * Constructor for enum instance, allowing provisioning of
     * min and max type value.
     * @param min minimum allowed value for data point type
     * @param max maximum allowed value for data point type
     */
    DataPointType(final Integer min, final Integer max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return Minimum allowed value for data point type.
     */
    public Integer min() {
        return min;
    }

    /**
     * @return Maximum allowed value for data point type.
     */
    public Integer max() {
        return max;
    }
}
