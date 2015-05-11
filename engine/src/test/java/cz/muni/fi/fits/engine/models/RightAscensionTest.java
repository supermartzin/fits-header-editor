package cz.muni.fi.fits.engine.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for computation of {@link RightAscension} from provided coordinates
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class RightAscensionTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testRightAscension_NotNull() throws Exception {
        double hours = 1;
        double minutes = 2.51;
        double seconds = 0.145;

        RightAscension rightAscension = new RightAscension(hours, minutes, seconds);

        assertNotNull(rightAscension);
    }

    @Test
    public void testRightAscension_HoursNaN() throws Exception {
        double hours = Double.NaN;
        double minutes = 2.51;
        double seconds = 0.145;

        exception.expect(IllegalArgumentException.class);
        new RightAscension(hours, minutes, seconds);
    }

    @Test
    public void testRightAscension_MinutesNaN() throws Exception {
        double hours = 24.53;
        double minutes = Double.NaN;
        double seconds = 0.145;

        exception.expect(IllegalArgumentException.class);
        new RightAscension(hours, minutes, seconds);
    }

    @Test
    public void testRightAscension_SecondsNaN() throws Exception {
        double hours = 147.36;
        double minutes = 2.51;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        new RightAscension(hours, minutes, seconds);
    }

    @Test
    public void testGetRightAscension_1() throws Exception {
        double hours = 22.12;
        double minutes = 26.35;
        double seconds = 14.254;

        RightAscension rightAscension = new RightAscension(hours, minutes, seconds);
        double rightAscensionValue = rightAscension.getRightAscension();

        assertEquals(338.44689167, rightAscensionValue, 0.00000001);
    }

    @Test
    public void testGetRightAscension_2() throws Exception {
        double hours = -36.89;
        double minutes = 145.12;
        double seconds = 235.236;

        RightAscension rightAscension = new RightAscension(hours, minutes, seconds);
        double rightAscensionValue = rightAscension.getRightAscension();

        assertEquals(-516.08985000, rightAscensionValue, 0.00000001);
    }

    @Test
    public void testGetRightAscension_3() throws Exception {
        double hours = -26.85;
        double minutes = 5.49;
        double seconds = 6.482;

        RightAscension rightAscension = new RightAscension(hours, minutes, seconds);
        double rightAscensionValue = rightAscension.getRightAscension();

        assertEquals(-401.3504916666667, rightAscensionValue, 0.00000001);
    }
}