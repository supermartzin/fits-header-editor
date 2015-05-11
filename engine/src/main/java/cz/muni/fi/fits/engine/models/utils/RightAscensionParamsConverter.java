package cz.muni.fi.fits.engine.models.utils;

import cz.muni.fi.fits.models.TimeObject;

/**
 * Helper class for converting {@link cz.muni.fi.fits.engine.models.RightAscension}
 * time data to its base from
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class RightAscensionParamsConverter {

    private int _hours;
    private int _minutes;
    private double _seconds;

    /**
     * Creates new {@link RightAscensionParamsConverter} object with time parameters
     * taken from provided <code>timeObject</code> parameter and
     * saves them in their base form
     *
     * @param timeObject    object containing time data - hours, minutes, seconds
     */
    public RightAscensionParamsConverter(TimeObject timeObject) {
        this(timeObject.getHours(), timeObject.getMinutes(), timeObject.getSeconds());
    }

    /**
     * Creates new {@link RightAscensionParamsConverter} object with provided time parameters
     * and saves them in their base form
     *
     * @param hours     number of hours
     * @param minutes   number of minutes
     * @param seconds   number of seconds
     */
    public RightAscensionParamsConverter(double hours, double minutes, double seconds) {
        if (Double.isNaN(hours))
            throw new IllegalArgumentException("hours parameter in not a number");
        if (Double.isNaN(minutes))
            throw new IllegalArgumentException("minutes parameter is not a number");
        if (Double.isNaN(seconds))
            throw new IllegalArgumentException("hours parameter is not a number");

        // set whole hours
        _hours = (int) hours;
        // convert fraction of hours to minutes
        double fracHours = Math.abs(hours - _hours);
        if (fracHours > 0.000001) {
            if (hours >= 0)
                minutes += fracHours * 60;
            else
                minutes -= fracHours * 60;
        }

        // set whole minutes
        _minutes = (int) minutes;
        // convert fraction of minutes to seconds
        double fracMinutes = Math.abs(minutes - _minutes);
        if (fracMinutes > 0.0001) {
            if (_minutes >= 0)
                seconds += fracMinutes * 60;
            else
                seconds -= fracMinutes * 60;
        }

        // set seconds
        _seconds = seconds;

        // set seconds to range <0,60>
        if (Math.abs(_seconds - 60) > 0.00001) {
            int count = (int) _seconds / 60;
            _seconds %= 60;
            _minutes += count;
        }

        // set minutes to range <0,60>
        if (_minutes >= 60) {
            int count = _minutes / 60;
            _minutes %= 60;
            _hours += count;
        }
    }

    public int getHours() {
        return _hours;
    }

    public int getMinutes() {
        return _minutes;
    }

    public double getSeconds() {
        return _seconds;
    }
}
