package cz.muni.fi.fits.engine.models;

/**
 * Class for computing right acsension of some object from provided coordinates
 *
 * @author Martin Vrábel
 * @version 1.1.1
 */
public final class RightAscension {

    private final double _hours;
    private final double _minutes;
    private final double _seconds;

    /**
     * Creates new {@link RightAscension} object with provided object's coordinates
     *
     * @param hours     object's hours
     * @param minutes   object's minutes
     * @param seconds   object's seconds
     */
    public RightAscension(double hours, double minutes, double seconds) {
        if (Double.isNaN(hours))
            throw new  IllegalArgumentException("hours parameter is not a number");
        if (Double.isNaN(minutes))
            throw new  IllegalArgumentException("minutes parameter is not a number");
        if (Double.isNaN(seconds))
            throw new  IllegalArgumentException("seconds parameter is not a number");

        _hours = hours;
        _minutes = minutes;
        _seconds = seconds;
    }

    /**
     * Computes object's right ascension from provided object's coordinates
     *
     * @return  computed object's right ascension
     */
    public double getRightAscension() {
        return 15 * (_hours + _minutes / 60.0 + _seconds / 3600.0);
    }

    public double getHours() {
        return _hours;
    }

    public double getMinutes() {
        return _minutes;
    }

    public double getSeconds() {
        return _seconds;
    }
}
