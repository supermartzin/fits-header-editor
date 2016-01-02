package cz.muni.fi.fits.engine.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

/**
 * Class for computing julian date from human readable datetime
 *
 * @author Martin Vrábel
 * @version 2.0
 */
public final class JulianDate {

    private JulianDate() {}

    /**
     * Computes Julian Date from full datetime parameters
     *
     * @param year          year
     * @param month         month (1-12)
     * @param day           day of month
     * @param hour          number of hours
     * @param minute        number of minutes
     * @param second        number of seconds
     * @param nanosecond    number of nanoseconds
     * @return              computed julian date
     */
    public static double computeJulianDate(int year, int month, int day, int hour, int minute, int second, int nanosecond) {
        double _hour = hour
                + minute / 60.0
                + second / 3600.0
                + nanosecond / 3600000000000.0;

        return 367 * year
                - Math.floor((year + Math.floor((month + 9) / 12.0)) * 7 / 4.0)
                + Math.floor(275 * month / 9.0)
                + day
                - 730531.5
                + _hour / 24.0
                + 2451545;
    }

    /**
     * Computes Julian Date from provided {@link LocalDateTime} object parameters
     *
     * @param datetime  datetime parameter in form of {@link LocalDateTime} object
     * @return          computed julian date
     */
    public static double computeJulianDate(LocalDateTime datetime) {
        if (datetime == null)
            throw new IllegalArgumentException("datetime parameter is null");

        return computeJulianDate(
                datetime.getYear(),
                datetime.getMonthValue(),
                datetime.getDayOfMonth(),
                datetime.getHour(),
                datetime.getMinute(),
                datetime.getSecond(),
                datetime.get(ChronoField.NANO_OF_SECOND));
    }
}
