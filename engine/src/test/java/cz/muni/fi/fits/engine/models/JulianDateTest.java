package cz.muni.fi.fits.engine.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Tests for creation and computational methods of {@link JulianDate}
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class JulianDateTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testComputeJulianDate_NullLocalDateTime() throws Exception {
        exception.expect(IllegalArgumentException.class);
        JulianDate.computeJulianDate(null);
    }

    @Test
    public void testComputeJulianDate_LocalDateTime() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2010, 11, 23, 20, 58, 26);

        double julianDate = JulianDate.computeJulianDate(dateTime);

        assertEquals(2455524.373912037, julianDate, 0.0000000001);
    }

    @Test
    public void testComputeJulianDate_RealParams() throws Exception {
        int year = 1990;
        int month = 5;
        int day = 20;
        int hour = 14;
        int minute = 26;
        int second = 47;
        int nanosecond = 12347856;

        double julianDate = JulianDate.computeJulianDate(year, month, day, hour, minute, second, nanosecond);

        assertEquals(2448032.101933013, julianDate, 0.0000000001);
    }

    @Test
    public void testComputeJulianDate_UnrealParams() throws Exception {
        int year = 1245;
        int month = -12;
        int day = 148;
        int hour = 63;
        int minute = 20;
        int second = 1452;
        int nanosecond = 12347856;

        double julianDate = JulianDate.computeJulianDate(year, month, day, hour, minute, second, nanosecond);

        assertEquals(2175535.155694587, julianDate, 0.0000000001);
    }
}