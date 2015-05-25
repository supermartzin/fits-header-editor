package cz.muni.fi.fits.engine.models;

import cz.muni.fi.fits.models.TimeObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for computation of {@link RightAscension} from provided coordinates
 *
 * @author Martin Vrábel
 * @version 2.0
 */
public class RightAscensionTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testComputeRightAscension_HoursNaN() throws Exception {
        double hours = Double.NaN;
        double minutes = 2.51;
        double seconds = 0.145;

        exception.expect(IllegalArgumentException.class);
        RightAscension.computeRightAscension(hours, minutes, seconds);
    }

    @Test
    public void testComputeRightAscension_MinutesNaN() throws Exception {
        double hours = 24.53;
        double minutes = Double.NaN;
        double seconds = 0.145;

        exception.expect(IllegalArgumentException.class);
        RightAscension.computeRightAscension(hours, minutes, seconds);
    }

    @Test
    public void testComputeRightAscension_SecondsNaN() throws Exception {
        double hours = 147.36;
        double minutes = 2.51;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        RightAscension.computeRightAscension(hours, minutes, seconds);
    }

    @Test
    public void testComputeRightAscension_TimeObjectNull() throws Exception {
        TimeObject timeObject = null;

        exception.expect(IllegalArgumentException.class);
        RightAscension.computeRightAscension(timeObject);
    }

    @Test
    public void testComputeRightAscension_TimeParameters_1() throws Exception {
        double hours = 22.12;
        double minutes = 26.35;
        double seconds = 14.254;

        double rightAscension = RightAscension.computeRightAscension(hours, minutes, seconds);

        assertEquals(338.44689167, rightAscension, 0.00000001);
    }

    @Test
    public void testComputeRightAscension_TimeParameters_2() throws Exception {
        double hours = -36.89;
        double minutes = 145.12;
        double seconds = 235.236;

        double rightAscension = RightAscension.computeRightAscension(hours, minutes, seconds);

        assertEquals(-516.08985000, rightAscension, 0.00000001);
    }

    @Test
    public void testComputeRightAscension_TimeObject_1() throws Exception {
        TimeObject timeObject = new TimeObject(-26.85, 5.49, 6.482);

        double rightAscension = RightAscension.computeRightAscension(timeObject);

        assertEquals(-401.35049167, rightAscension, 0.00000001);
    }

    @Test
    public void testComputeRightAscension_TimeObject_2() throws Exception {
        TimeObject timeObject = new TimeObject(12, 36, 25.48);

        double rightAscension = RightAscension.computeRightAscension(timeObject);

        assertEquals(189.10616667, rightAscension, 0.00000001);
    }
}