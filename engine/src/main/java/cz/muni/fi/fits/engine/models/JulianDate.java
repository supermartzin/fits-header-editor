package cz.muni.fi.fits.engine.models;

import java.time.LocalDateTime;

/**
 * Class for computing julian date from human readable datetime
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class JulianDate {

    private final int _year;
    private final int _month;
    private final int _day;
    private final double _hour;

    /**
     * Creates new {@link JulianDate} object with provided datetime parameters
     *
     * @param year          year
     * @param month         month (1-12)
     * @param day           day of month
     * @param hour          number of hours
     * @param minute        number of minutes
     * @param second        number of seconds
     * @param nanoseconds   number of nanoseconds
     */
    public JulianDate(int year, int month, int day, int hour, int minute, int second, int nanoseconds) {
        _year = year;
        _month = month;
        _day = day;
        _hour = hour
                + minute / 60.0
                + second / 3600.0
                + nanoseconds / 3600000000000.0;
    }

    /**
     * Creates new {@link JulianDate} object with provided {@link LocalDateTime} parameter
     *
     * @param datetime  DateTime parameter
     */
    public JulianDate(LocalDateTime datetime) {
        _year = datetime.getYear();
        _month = datetime.getMonthValue();
        _day = datetime.getDayOfMonth();
        _hour = datetime.getHour()
                + datetime.getMinute() / 60.0
                + datetime.getSecond() / 3600.0
                + datetime.getNano() / 3600000000000.0;
    }

    /**
     * Computes Julian Date from object's datetime parameters
     *
     * @return  computed julian date
     */
    public double getJulianDate() {
        return 367 * _year
                - Math.floor((_year + Math.floor((_month + 9) / 12.0)) * 7 / 4.0)
                + Math.floor(275 * _month / 9.0)
                + _day
                - 730531.5
                + _hour / 24.0
                + 2451545;
    }
}
