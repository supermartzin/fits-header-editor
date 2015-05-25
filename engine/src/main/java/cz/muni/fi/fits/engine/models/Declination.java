package cz.muni.fi.fits.engine.models;

import cz.muni.fi.fits.models.DegreesObject;

/**
 * Class for computing decliantion of some object from provided coordinates
 *
 * @author Martin Vrábel
 * @version 2.0
 */
public final class Declination {

    private Declination() {}

    /**
     * Computes object's declination from provided object's coordinates
     *
     * @param degrees   number of object's degrees
     * @param minutes   number of object's minutes
     * @param seconds   number of object's seconds
     * @return          computed object's declination
     */
    public static double computeDeclination(double degrees, double minutes, double seconds) {
        if (Double.isNaN(degrees))
            throw new IllegalArgumentException("degrees parameter is not a number");
        if (Double.isNaN(minutes))
            throw new IllegalArgumentException("minutes parameter is not a number");
        if (Double.isNaN(seconds))
            throw new IllegalArgumentException("seconds parameter is not a number");

        return Double.compare(degrees, 0) < 0
                ? -1 * (Math.abs(degrees) + minutes / 60.0 + seconds / 3600.0)
                : Math.abs(degrees) + minutes / 60.0 + seconds / 3600.0;
    }

    /**
     * Computes object's declination from provided {@link DegreesObject} coordinates
     *
     * @param degreesParameters object's coordinates in form of {@link DegreesObject} value
     * @return                  computed object's declination
     */
    public static double computeDeclination(DegreesObject degreesParameters) {
        if (degreesParameters == null)
            throw new IllegalArgumentException("degrees parameters are null");

        return computeDeclination(degreesParameters.getDegrees(), degreesParameters.getMinutes(), degreesParameters.getSeconds());
    }
}
