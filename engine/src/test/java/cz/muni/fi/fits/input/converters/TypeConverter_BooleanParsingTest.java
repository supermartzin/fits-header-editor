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
 * Tests for parsing {@link Boolean} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class TypeConverter_BooleanParsingTest {

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
    public void testTryParseBoolean_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseBoolean(value);
    }

    @Test
    public void testParseBoolean_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseBoolean(value);
    }

    @Test
    public void testTryParseBoolean_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseBoolean(value));
    }

    @Test
    public void testParseBoolean_Value_Empty() throws Exception {
        String value = "";

        exception.expect(ParseException.class);
        _converter.parseBoolean(value);
    }

    @Test
    public void testTryParseBoolean_Values_NonParsable() throws Exception {
        String value1 = "tt";
        String value2 = "1";
        String value3 = "truel";

        assertFalse(_converter.tryParseBoolean(value1));
        assertFalse(_converter.tryParseBoolean(value2));
        assertFalse(_converter.tryParseBoolean(value3));
    }

    @Test
    public void testParseBoolean_Value_NonParsable() throws Exception {
        String value = "tt";

        exception.expect(ParseException.class);
        _converter.parseBoolean(value);
    }

    @Test
    public void testTryParseBoolean_Values_Parsable() throws Exception {
        String value1 = "true";
        String value2 = "t";
        String value3 = "FALSE";
        String value4 = "FaLsE";
        String value5 = "F";

        assertTrue(_converter.tryParseBoolean(value1));
        assertTrue(_converter.tryParseBoolean(value2));
        assertTrue(_converter.tryParseBoolean(value3));
        assertTrue(_converter.tryParseBoolean(value4));
        assertTrue(_converter.tryParseBoolean(value5));
    }

    @Test
    public void testParseBoolean_Values_Parsable() throws Exception {
        String value1 = "true";
        String value2 = "t";
        String value3 = "FALSE";
        String value4 = "FaLsE";
        String value5 = "F";

        assertNotNull(_converter.parseBoolean(value1));
        assertNotNull(_converter.parseBoolean(value2));
        assertNotNull(_converter.parseBoolean(value3));
        assertNotNull(_converter.parseBoolean(value4));
        assertNotNull(_converter.parseBoolean(value5));
    }
}
