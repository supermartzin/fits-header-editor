package cz.muni.fi.fits.engine.models;

import cz.muni.fi.fits.models.DegreesObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for computation of {@link Declination} from provided coordinates
 *
 * @author Martin Vrábel
 * @version 2.1
 */
public class DeclinationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testComputeDeclination_Degrees_NotANumber() throws Exception {
        double degrees = Double.NaN;
        double minutes = 23.56;
        double seconds = 147.89;

        exception.expect(IllegalArgumentException.class);
        Declination.computeDeclination(degrees, minutes, seconds);
    }

    @Test
    public void testComputeDeclination_Minutes_NotANumber() throws Exception {
        double degrees = -42.42;
        double minutes = Double.NaN;
        double seconds = 147.89;

        exception.expect(IllegalArgumentException.class);
        Declination.computeDeclination(degrees, minutes, seconds);
    }

    @Test
    public void testComputeDeclination_Seconds_NotANumber() throws Exception {
        double degrees = 86.2;
        double minutes = 23.56;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        Declination.computeDeclination(degrees, minutes, seconds);
    }

    @Test
    public void testComputeDeclination_DegreesObject_Null() throws Exception {
        DegreesObject degreesObject = null;

        exception.expect(IllegalArgumentException.class);
        Declination.computeDeclination(degreesObject);
    }

    @Test
    public void testComputeDeclination_DegreesObject_1() throws Exception {
        DegreesObject degreesObject = new DegreesObject(86.2, 23.56, 147.89);

        double declination = Declination.computeDeclination(degreesObject);

        assertEquals(86.63374723, declination, 0.00000001);
    }

    @Test
    public void testComputeDeclination_DegreesObject_2() throws Exception {
        DegreesObject degreesObject = new DegreesObject(-163.94, 2.5, 0.647);

        double declination = Declination.computeDeclination(degreesObject);

        assertEquals(-163.98184638, declination, 0.00000001);
    }

    @Test
    public void testComputeDeclination_DegreesCoordinates_1() throws Exception {
        double degrees = 69.25;
        double minutes = 37.4;
        double seconds = 12.541;

        double declination = Declination.computeDeclination(degrees, minutes, seconds);

        assertEquals(69.87681694, declination, 0.00000001);
    }

    @Test
    public void testComputeDeclination_DegreesCoordinates_2() throws Exception {
        double degrees = -79.89;
        double minutes = 132.5;
        double seconds = 471.126;

        double declination = Declination.computeDeclination(degrees, minutes, seconds);

        assertEquals(-82.22920167, declination, 0.00000001);
    }
}