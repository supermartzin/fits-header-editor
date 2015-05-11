package cz.muni.fi.fits.engine.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for computation of {@link Declination} from provided coordinates
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DeclinationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetDeclination_NotNull() throws Exception {
        double degrees = 12;
        double minutes = 23.56;
        double seconds = 147.89;

        Declination declination = new Declination(degrees, minutes, seconds);

        assertNotNull(declination);
    }

    @Test
    public void testGetDeclination_DegreesNaN() throws Exception {
        double degrees = Double.NaN;
        double minutes = 23.56;
        double seconds = 147.89;

        exception.expect(IllegalArgumentException.class);
        new Declination(degrees, minutes, seconds);
    }

    @Test
    public void testGetDeclination_MinutesNaN() throws Exception {
        double degrees = -42.42;
        double minutes = Double.NaN;
        double seconds = 147.89;

        exception.expect(IllegalArgumentException.class);
        new Declination(degrees, minutes, seconds);
    }

    @Test
    public void testGetDeclination_SecondsNaN() throws Exception {
        double degrees = 86.2;
        double minutes = 23.56;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        new Declination(degrees, minutes, seconds);
    }

    @Test
    public void testGetDeclination_1() throws Exception {
        double degrees = 69.25;
        double minutes = 37.4;
        double seconds = 12.541;

        Declination declination = new Declination(degrees, minutes, seconds);
        double declinationValue = declination.getDeclination();

        assertEquals(69.87681694, declinationValue, 0.00000001);
    }

    @Test
    public void testGetDeclination_2() throws Exception {
        double degrees = -79.89;
        double minutes = 132.5;
        double seconds = 471.126;

        Declination declination = new Declination(degrees, minutes, seconds);
        double declinationValue = declination.getDeclination();

        assertEquals(-82.22920167, declinationValue, 0.00000001);
    }

    @Test
    public void testGetDeclination_3() throws Exception {
        double degrees = -163.94;
        double minutes = 2.5;
        double seconds = 0.647;

        Declination declination = new Declination(degrees, minutes, seconds);
        double declinationValue = declination.getDeclination();

        assertEquals(-163.98184638, declinationValue, 0.00000001);
    }
}