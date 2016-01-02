package cz.muni.fi.fits.engine.models;

import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.TimeObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Tests for creation and computational methods of {@link HeliocentricJulianDate}
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class HeliocentricJulianDateTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testComputeHeliocentricJulianDate_JulianDate_Null() throws Exception {
        TimeObject rightAscension = new TimeObject(12, 25, 48);
        DegreesObject declination = new DegreesObject(65, 14, 48);

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(null, rightAscension, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_RightAscension_Null() throws Exception {
        LocalDateTime julianDate = LocalDateTime.of(1990, 11, 25, 17, 47, 45, 12456321);
        DegreesObject declination = new DegreesObject(65, 14, 48);

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, null, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_Declination_Null() throws Exception {
        LocalDateTime julianDate = LocalDateTime.of(1990, 11, 25, 17, 47, 45, 12456321);
        TimeObject rightAscension = new TimeObject(12, 25, 48);

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, rightAscension, null);
    }

    @Test
    public void testComputeHeliocentricJulianDate_JulianDate_DoubleValue_NotANumber() throws Exception {
        double julianDate = Double.NaN;
        double rightAscension = 186.45;
        double declination = 65.24666666666667;

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_RightAscension_DoubleValue_NotANumber() throws Exception {
        double julianDate = 2448221.2414932;
        double rightAscension = Double.NaN;
        double declination = 65.24666666666667;

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_Declination_DoubleValue_NotANumber() throws Exception {
        double julianDate = 2448221.2414932;
        double rightAscension = 186.45;
        double declination = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_RealParams() throws Exception {
        LocalDateTime julianDate = LocalDateTime.of(1990, 11, 25, 17, 47, 45, 12456321);
        TimeObject rightAscension = new TimeObject(12, 25, 48);
        DegreesObject declination = new DegreesObject(65, 14, 48);

        double heliocentricJulianDate = HeliocentricJulianDate.computeHeliocentricJulianDate(
                julianDate, rightAscension, declination);

        assertEquals(2448221.242056166, heliocentricJulianDate, 0.00000000001);
    }
}