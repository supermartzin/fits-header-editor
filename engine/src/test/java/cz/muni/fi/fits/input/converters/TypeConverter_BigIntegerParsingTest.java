package cz.muni.fi.fits.input.converters;

import cz.muni.fi.fits.common.exceptions.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for parsing {@link java.math.BigInteger} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class TypeConverter_BigIntegerParsingTest {

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
    public void testTryParseBigInteger_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseBigInteger(value);
    }

    @Test
    public void testParseBigInteger_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseBigInteger(value);
    }

    @Test
    public void testTryParseBigInteger_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseBigInteger(value));
    }

    @Test
    public void testParseBigInteger_Value_Empty() throws Exception {
        String value = "";

        exception.expect(ParseException.class);
        _converter.parseBigInteger(value);
    }

    @Test
    public void testTryParseBigInteger_Values_NonParsable() throws Exception {
        String value1 = "--12365889452123624789541";
        String value2 = "1234567890123456789.";
        String value3 = ".02365T";

        assertFalse(_converter.tryParseBigInteger(value1));
        assertFalse(_converter.tryParseBigInteger(value2));
        assertFalse(_converter.tryParseBigInteger(value3));
    }

    @Test
    public void testParseBigInteger_Value_NonParsable() throws Exception {
        String value = "--12365889452123624789541";

        exception.expect(ParseException.class);
        _converter.parseBigInteger(value);
    }

    @Test
    public void testTryParseBigInteger_Values_Parsable() throws Exception {
        String value1 = "12345678901234567890";
        String value2 = "-56324";
        String value3 = "+6658999898989784512154121";

        assertTrue(_converter.tryParseBigInteger(value1));
        assertTrue(_converter.tryParseBigInteger(value2));
        assertTrue(_converter.tryParseBigInteger(value3));
    }

    @Test
    public void testParseBigInteger_Values_Parsable() throws Exception {
        String value1 = "12345678901234567890";
        String value2 = "-56324";
        String value3 = "+6658999898989784512154121";

        assertNotNull(_converter.parseBigInteger(value1));
        assertNotNull(_converter.parseBigInteger(value2));
        assertNotNull(_converter.parseBigInteger(value3));
    }
}
