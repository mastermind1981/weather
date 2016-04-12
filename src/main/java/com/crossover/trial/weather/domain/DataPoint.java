package com.crossover.trial.weather.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * A collected point, including some information about the range of collected values.
 *
 * @author code test administrator
 */
public class DataPoint {

    /**
     * The mean of observations.
     */
    private double mean = 0.0;

    /**
     * 1st quartile of observations -- useful as a lower bound.
     */
    private int first = 0;

    /**
     * 2nd quartile of observations -- median value.
     */
    private int second = 0;

    /**
     * 3rd quartile value of observations -- less noisy upper value.
     */
    private int third = 0;

    /**
     * Total number of measurements.
     */
    private int count = 0;

    /**
     * Private constructor, use the builder to create this object.
     */
    private DataPoint() {
    }

    /**
     * Convenience constructor for use in builder.
     *
     * @param builder builder to initialize from
     */
    protected DataPoint(final Builder builder) {
        setFirst(builder.first);
        setMean(builder.mean);
        setSecond(builder.median);
        setThird(builder.last);
        setCount(builder.count);
    }

    /**
     * @return the mean of the observations.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Sets the mean of the observations.
     *
     * @param mean value to set
     */
    protected void setMean(final double mean) {
        this.mean = mean;
    }

    /**
     * @return 1st quartile -- useful as a lower bound
     */
    public int getFirst() {
        return first;
    }

    /**
     * Setter method.
     *
     * @param first value of 1st quartile to set
     */
    protected void setFirst(final int first) {
        this.first = first;
    }

    /**
     * @return 2nd quartile -- median value
     */
    public int getSecond() {
        return second;
    }

    /**
     * Setter method.
     *
     * @param second value of 2nd quartile to set
     */
    protected void setSecond(final int second) {
        this.second = second;
    }

    /**
     * @return 3rd quartile value -- less noisy upper value
     */
    public int getThird() {
        return third;
    }


    /**
     * Setter method.
     *
     * @param third value of 3rd quartile value to set
     */
    protected void setThird(final int third) {
        this.third = third;
    }

    /**
     * @return the total number of measurements
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter method.
     *
     * @param count value of total number of measurements to set
     */
    protected void setCount(final int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataPoint)) {
            return false;
        }
        DataPoint dataPoint = (DataPoint) o;
        return Double.compare(dataPoint.getMean(), getMean()) == 0
            && getFirst() == dataPoint.getFirst()
            && getSecond() == dataPoint.getSecond()
            && getThird() == dataPoint.getThird()
            && getCount() == dataPoint.getCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMean(), getFirst(), getSecond(), getThird(), getCount());
    }

    /**
     * Builder class for constructing {@link DataPoint}s.
     */
    public static class Builder {

        /**
         * 1st quartile of observations -- useful as a lower bound.
         */
        private int first;

        /**
         * The mean of observations.
         */
        private double mean;

        /**
         * 2nd quartile of observations -- median value.
         */
        private int median;

        /**
         * 3rd quartile value of observations -- less noisy upper value.
         */
        private int last;

        /**
         * Total number of measurements.
         */
        private int count;

        /**
         * Set 1st quartile of observations.
         *
         * @param first value to set
         * @return builder to continue the chain of calls
         */
        public Builder withFirst(final int first) {
            this.first = first;
            return this;
        }

        /**
         * Set the mean of observations.
         *
         * @param mean value to set
         * @return builder to continue the chain of calls
         */
        public Builder withMean(final double mean) {
            this.mean = mean;
            return this;
        }

        /**
         * Set the median (2nd quartile) of observations.
         *
         * @param median value to set
         * @return builder to continue the chain of calls
         */
        public Builder withMedian(final int median) {
            this.median = median;
            return this;
        }

        /**
         * Set total number of measurements.
         *
         * @param count value to set
         * @return builder to continue the chain of calls
         */
        public Builder withCount(final int count) {
            this.count = count;
            return this;
        }

        /**
         * Set 3rd quartile value of observations.
         *
         * @param last value to set
         * @return builder to continue the chain of calls
         */
        public Builder withLast(final int last) {
            this.last = last;
            return this;
        }

        /**
         * Build data point from configured builder.
         *
         * @return constructed data point
         */
        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}
