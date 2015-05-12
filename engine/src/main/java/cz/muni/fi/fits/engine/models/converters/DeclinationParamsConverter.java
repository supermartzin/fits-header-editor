package cz.muni.fi.fits.engine.models.converters;

import cz.muni.fi.fits.models.DegreesObject;

/**
 * Helper class for converting {@link cz.muni.fi.fits.engine.models.Declination}
 * degrees data to its base from
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class DeclinationParamsConverter {

    private int _degrees;
    private int _minutes;
    private double _seconds;

    public DeclinationParamsConverter(DegreesObject degreesObject) {
        this(degreesObject.getDegrees(), degreesObject.getMinutes(), degreesObject.getSeconds());
    }

    public DeclinationParamsConverter(double degrees, double minutes, double seconds) {
        if (Double.isNaN(degrees))
            throw new IllegalArgumentException("degrees parameter is not a number");
        if (Double.isNaN(minutes))
            throw new IllegalArgumentException("minutes parameter is not a number");
        if (Double.isNaN(seconds))
            throw new IllegalArgumentException("seconds parameter is not a number");

        // set whole degrees
        _degrees = (int) degrees;
        // convert fraction of degrees to minutes
        double fracDegrees = Math.abs(degrees - _degrees);
        if (fracDegrees > 0.000001)
            minutes += fracDegrees * 60;

        // set whole minutes
        _minutes = (int) minutes;
        // convert fraction of minutes to seconds
        double fracMinutes = Math.abs(minutes - _minutes);
        if (fracMinutes > 0.0001)
            seconds += fracMinutes * 60;

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
            if (_degrees >= 0)
                _degrees += count;
            else
                _degrees -= count;
        }
    }

    public int getDegrees() {
        return _degrees;
    }

    public int getMinutes() {
        return _minutes;
    }

    public double getSeconds() {
        return _seconds;
    }
}
