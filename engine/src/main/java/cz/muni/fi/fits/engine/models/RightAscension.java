package cz.muni.fi.fits.engine.models;

import cz.muni.fi.fits.models.TimeObject;

/**
 * Class for computing right acsension of some object from provided coordinates
 *
 * @author Martin Vrábel
 * @version 2.0
 */
public final class RightAscension {

    private RightAscension() {}

    /**
     * Computes object's right ascension from provided object's coordinates
     *
     * @param hours     object's hours
     * @param minutes   object's minutes
     * @param seconds   object's seconds
     * @return          computed object's right ascension
     */
    public static double computeRightAscension(double hours, double minutes, double seconds) {
        if (Double.isNaN(hours))
            throw new  IllegalArgumentException("hours parameter is not a number");
        if (Double.isNaN(minutes))
            throw new  IllegalArgumentException("minutes parameter is not a number");
        if (Double.isNaN(seconds))
            throw new  IllegalArgumentException("seconds parameter is not a number");

        return 15 * (hours + minutes / 60.0 + seconds / 3600.0);
    }

    /**
     * Computes object's right ascension from provided {@link TimeObject} coordinates
     *
     * @param timeParameters    object's cooredinates in form of {@link TimeObject} value
     * @return                  computed object's right ascension
     */
    public static double computeRightAscension(TimeObject timeParameters) {
        if (timeParameters == null)
            throw new IllegalArgumentException("time parameters are null");

        return computeRightAscension(timeParameters.getHours(), timeParameters.getMinutes(), timeParameters.getSeconds());
    }
}
