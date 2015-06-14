package cz.muni.fi.fits.input.converters;

import cz.muni.fi.fits.exceptions.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for parsing {@link java.math.BigDecimal} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class TypeConverter_BigDecimalParsingTest {

    private DefaultTypeConverter _converter;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        _converter = new DefaultTypeConverter();
    }

    @After
    public void tearDown() throws Exception {
        _converter = null;
    }


    @Test
    public void testTryParseBigDecimal_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseBigDecimal(value);
    }

    @Test
    public void testParseBigDecimal_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseBigDecimal(value);
    }

    @Test
    public void testTryParseBigDecimal_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseBigDecimal(value));
    }

    @Test
    public void testParseBigDecimal_Value_Empty() throws Exception {
        String value = "";

        exception.expect(ParseException.class);
        _converter.parseBigDecimal(value);
    }

    @Test
    public void testTryParseBigDecimal_Values_NonParsable() throws Exception {
        String value1 = "12354778996..1254";
        String value2 = "4.58963256x10^-659";
        String value3 = "-+5632.1254";

        assertFalse(_converter.tryParseBigDecimal(value1));
        assertFalse(_converter.tryParseBigDecimal(value2));
        assertFalse(_converter.tryParseBigDecimal(value3));
    }

    @Test
    public void testParseBigDecimal_Value_NonParsable() throws Exception {
        String value = "12354778996..1254";

        exception.expect(ParseException.class);
        _converter.parseBigDecimal(value);
    }

    @Test
    public void testTryParseBigDecimal_Values_Parsable() throws Exception {
        String value1 = "123.457896541232E12345";
        String value2 = "-8965874513269115.11445458478784789748787661";
        String value3 = "+1.236574588256285268216261861e123";

        assertTrue(_converter.tryParseBigDecimal(value1));
        assertTrue(_converter.tryParseBigDecimal(value2));
        assertTrue(_converter.tryParseBigDecimal(value3));
    }

    @Test
    public void testParseBigDecimal_Values_Parsable() throws Exception {
        String value1 = "123.457896541232E12345";
        String value2 = "-8965874513269115.11445458478784789748787661";
        String value3 = "+1.236574588256285268216261861e123";

        assertNotNull(_converter.parseBigDecimal(value1));
        assertNotNull(_converter.parseBigDecimal(value2));
        assertNotNull(_converter.parseBigDecimal(value3));
    }
}
