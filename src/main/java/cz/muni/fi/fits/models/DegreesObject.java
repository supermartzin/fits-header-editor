package cz.muni.fi.fits.models;

/**
 * Wrapper class for basic representation of extended degrees
 * as e.g. coordinate
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class DegreesObject {

    private final double _degrees;
    private final double _minutes;
    private final double _seconds;

    /**
     * Creates new {@link DegreesObject} with provided parameters
     *
     * @param degrees   number of degrees
     * @param minutes   number of minutes
     * @param seconds   number of seconds
     */
    public DegreesObject(double degrees, double minutes, double seconds) {
        this._degrees = degrees;
        this._minutes = minutes;
        this._seconds = seconds;
    }

    public double getDegrees() {
        return _degrees;
    }

    public double getMinutes() {
        return _minutes;
    }

    public double getSeconds() {
        return _seconds;
    }
}
