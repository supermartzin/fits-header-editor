package cz.muni.fi.fits.models;

/**
 * Wrapper class for basic representation of time
 * as e.g. coordinate
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class TimeObject {

    private final double _hours;
    private final double _minutes;
    private final double _seconds;

    /**
     * Creates new {@link TimeObject} with provided parameters
     *
     * @param hours     number of hours
     * @param minutes   number of minutes
     * @param seconds   number of seconds
     */
    public TimeObject(double hours, double minutes, double seconds) {
        _hours = hours;
        _minutes = minutes;
        _seconds = seconds;
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
