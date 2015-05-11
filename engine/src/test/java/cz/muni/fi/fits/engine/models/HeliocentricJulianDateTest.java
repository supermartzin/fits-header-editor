package cz.muni.fi.fits.engine.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for creation and computational methods of {@link HeliocentricJulianDate}
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class HeliocentricJulianDateTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testConstructHeliocentricJulianDate_NotNull_ObjectParams() throws Exception {
        JulianDate julianDate = new JulianDate(1990, 11, 25, 17, 47, 45, 12456321);
        RightAscension rightAscension = new RightAscension(12, 25, 48);
        Declination declination = new Declination(65, 14, 48);

        HeliocentricJulianDate heliocentricJulianDate = new HeliocentricJulianDate(
                julianDate, rightAscension, declination);

        assertNotNull(heliocentricJulianDate);
    }

    @Test
    public void testConstructHeliocentricJulianDate_NotNull_DoubleParams() throws Exception {
        double julianDate = 2448221.2414932;
        double rightAscension = 186.45;
        double declination = 65.24666666666667;

        HeliocentricJulianDate heliocentricJulianDate = new HeliocentricJulianDate(
                julianDate, rightAscension, declination);

        assertNotNull(heliocentricJulianDate);
    }

    @Test
    public void testConstructHeliocentricJulianDate_NullJulianDate() throws Exception {
        RightAscension rightAscension = new RightAscension(12, 25, 48);
        Declination declination = new Declination(65, 14, 48);

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(null, rightAscension, declination);
    }

    @Test
    public void testConstructHeliocentricJulianDate_NullRightAscension() throws Exception {
        JulianDate julianDate = new JulianDate(1990, 11, 25, 17, 47, 45, 12456321);
        Declination declination = new Declination(65, 14, 48);

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(julianDate, null, declination);
    }

    @Test
    public void testConstructHeliocentricJulianDate_NullDeclination() throws Exception {
        JulianDate julianDate = new JulianDate(1990, 11, 25, 17, 47, 45, 12456321);
        RightAscension rightAscension = new RightAscension(12, 25, 48);

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(julianDate, rightAscension, null);
    }

    @Test
    public void testConstructHeliocentricJulianDate_JulianDate_DoubleNaN() throws Exception {
        double julianDate = Double.NaN;
        double rightAscension = 186.45;
        double declination = 65.24666666666667;

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testConstructHeliocentricJulianDate_RightAscension_DoubleNaN() throws Exception {
        double julianDate = 2448221.2414932;
        double rightAscension = Double.NaN;
        double declination = 65.24666666666667;

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testConstructHeliocentricJulianDate_Declination_DoubleNaN() throws Exception {
        double julianDate = 2448221.2414932;
        double rightAscension = 186.45;
        double declination = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        new HeliocentricJulianDate(julianDate, rightAscension, declination);
    }

    @Test
    public void testComputeHeliocentricJulianDate_RealParams() throws Exception {
        JulianDate julianDate = new JulianDate(1990, 11, 25, 17, 47, 45, 12456321);
        RightAscension rightAscension = new RightAscension(12, 25, 48);
        Declination declination = new Declination(65, 14, 48);

        HeliocentricJulianDate heliocentricJulianDate = new HeliocentricJulianDate(
                julianDate, rightAscension, declination);
        double hjdValue = heliocentricJulianDate.computeHeliocentricJulianDate();

        assertNotNull(hjdValue);
        assertEquals(2448221.242056166, hjdValue, 0.00000000001);
    }

    @Test
    public void testComputeHeliocentricJulianDate_UnrealParams() throws Exception {
        JulianDate julianDate = new JulianDate(2159, 47, 156, 88, 117, 20, 12456321);
        RightAscension rightAscension = new RightAscension(-689, 12, 44);
        Declination declination = new Declination(754, 0, 10);

        HeliocentricJulianDate heliocentricJulianDate = new HeliocentricJulianDate(
                julianDate, rightAscension, declination);
        double hjdValue = heliocentricJulianDate.computeHeliocentricJulianDate();

        assertNotNull(hjdValue);
        assertEquals(2511177.2480348856, hjdValue, 0.00000000001);
    }
}