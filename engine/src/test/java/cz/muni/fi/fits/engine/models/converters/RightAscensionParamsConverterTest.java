package cz.muni.fi.fits.engine.models.converters;

import cz.muni.fi.fits.models.TimeObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for conversion of {@link cz.muni.fi.fits.engine.models.RightAscension}
 * time data to its baseform
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public class RightAscensionParamsConverterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testConversion_Hours_NotANumber() throws Exception {
        double hours = Double.NaN;
        double minutes = 12.35;
        double seconds = 45.63;

        exception.expect(IllegalArgumentException.class);
        new RightAscensionParamsConverter(hours, minutes, seconds);
    }

    @Test
    public void testConversion_Minutes_NotANumber() throws Exception {
        double hours = 12.47;
        double minutes = Double.NaN;
        double seconds = 3.259;

        exception.expect(IllegalArgumentException.class);
        new RightAscensionParamsConverter(hours, minutes, seconds);
    }

    @Test
    public void testConversion_Seconds_NotANumber() throws Exception {
        double hours = -42.86;
        double minutes = 11.4;
        double seconds = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        new RightAscensionParamsConverter(hours, minutes, seconds);
    }

    @Test
    public void testConversion_1() throws Exception {
        double hours = 455.365;
        double minutes = 322.87;
        double seconds = 245.789;

        RightAscensionParamsConverter converter = new RightAscensionParamsConverter(hours, minutes, seconds);

        assertNotNull(converter);
        assertEquals(4, converter.getHours());
        assertEquals(48, converter.getMinutes());
        assertEquals(51.989, converter.getSeconds(), 0.0001);
    }

    @Test
    public void testConversion_2() throws Exception {
        double hours = -455.365;
        double minutes = 322.87;
        double seconds = 245.789;

        RightAscensionParamsConverter converter = new RightAscensionParamsConverter(hours, minutes, seconds);

        assertNotNull(converter);
        assertEquals(6, converter.getHours());
        assertEquals(5, converter.getMinutes());
        assertEquals(3.989, converter.getSeconds(), 0.0001);
    }

    @Test
    public void testConversion_3() throws Exception {
        double hours = -12.58;
        double minutes = 1.95;
        double seconds = 1.2;

        RightAscensionParamsConverter converter = new RightAscensionParamsConverter(hours, minutes, seconds);

        assertNotNull(converter);
        assertEquals(11, converter.getHours());
        assertEquals(27, converter.getMinutes());
        assertEquals(10.2, converter.getSeconds(), 0.0001);
    }

    @Test
    public void testConversion_TimeObject_Valid() throws Exception {
        double hours = -12.58;
        double minutes = 1.95;
        double seconds = 1.2;
        TimeObject timeObject = new TimeObject(hours, minutes, seconds);

        RightAscensionParamsConverter converter = new RightAscensionParamsConverter(timeObject);

        assertNotNull(converter);
        assertEquals(11, converter.getHours());
        assertEquals(27, converter.getMinutes());
        assertEquals(10.2, converter.getSeconds(), 0.0001);
    }
}