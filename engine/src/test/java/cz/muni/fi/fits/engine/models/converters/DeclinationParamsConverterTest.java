package cz.muni.fi.fits.engine.models.converters;

import cz.muni.fi.fits.models.DegreesObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for conversion of {@link cz.muni.fi.fits.engine.models.Declination}
 * degrees data to its baseform
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class DeclinationParamsConverterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testConversion_DegreesNaN() throws Exception {
        double degrees = Double.NaN;
        double minutes = 12.35;
        double seconds = 45.63;

        exception.expect(IllegalArgumentException.class);
        new DeclinationParamsConverter(degrees, minutes, seconds);
    }

    @Test
    public void testConversion_MinutesNaN() throws Exception {
        double degrees = 145.37;
        double minutes = Double.NaN;
        double seconds = 45.63;

        exception.expect(IllegalArgumentException.class);
        new DeclinationParamsConverter(degrees, minutes, seconds);
    }

    @Test
    public void testConversion_SecondsNaN() throws Exception {
        double degrees = -89.26;
        double minutes = 12.35;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        new DeclinationParamsConverter(degrees, minutes, seconds);
    }

    @Test
    public void testConvertion_1() throws Exception {
        double degrees = 195.25;
        double minutes = 82.35;
        double seconds = 146.127;

        DeclinationParamsConverter declinationParamsConverter = new DeclinationParamsConverter(degrees, minutes, seconds);

        assertNotNull(declinationParamsConverter);
        assertEquals(196, declinationParamsConverter.getDegrees());
        assertEquals(39, declinationParamsConverter.getMinutes());
        assertEquals(47.127, declinationParamsConverter.getSeconds(), 0.0001);
    }

    @Test
    public void testConvertion_2() throws Exception {
        double degrees = -195.25;
        double minutes = 82.35;
        double seconds = 146.127;

        DeclinationParamsConverter declinationParamsConverter = new DeclinationParamsConverter(degrees, minutes, seconds);

        assertNotNull(declinationParamsConverter);
        assertEquals(-196, declinationParamsConverter.getDegrees());
        assertEquals(39, declinationParamsConverter.getMinutes());
        assertEquals(47.127, declinationParamsConverter.getSeconds(), 0.0001);
    }

    @Test
    public void testConvertion_DegreesObject() throws Exception {
        double degrees = -25.42;
        double minutes = 14.78;
        double seconds = 39.85;
        DegreesObject degreesObject = new DegreesObject(degrees, minutes, seconds);

        DeclinationParamsConverter declinationParamsConverter = new DeclinationParamsConverter(degreesObject);

        assertNotNull(declinationParamsConverter);
        assertEquals(-25, declinationParamsConverter.getDegrees());
        assertEquals(40, declinationParamsConverter.getMinutes());
        assertEquals(38.65, declinationParamsConverter.getSeconds(), 0.0001);
    }
}