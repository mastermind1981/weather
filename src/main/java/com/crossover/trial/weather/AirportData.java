package com.crossover.trial.weather;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /**
     * IATA code.
     */
    private String iata;

    /**
     * Latitude value in degrees.
     */
    private double latitude;

    /**
     * Longitude value in degrees.
     */
    private double longitude;

    /**
     * @return The three letter IATA code
     */
    public String getIata() {
        return iata;
    }

    /**
     * Setter for IATA code.
     *
     * @param iata three letter IATA code to set
     */
    public void setIata(final String iata) {
        this.iata = iata;
    }

    /**
     * @return Latitude value in degrees.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter for latitude.
     *
     * @param latitude latitude in degrees to set
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return Longitude value in degrees.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter for longitude.
     *
     * @param longitude longitude in degrees to set
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
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
        if (!(o instanceof AirportData)) {
            return false;
        }

        AirportData that = (AirportData) o;

        return getIata().equals(that.getIata());

    }

    @Override
    public int hashCode() {
        return iata.hashCode();
    }
}
