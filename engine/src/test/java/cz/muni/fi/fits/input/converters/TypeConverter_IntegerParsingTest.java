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
 * Tests for parsing {@link Integer} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class TypeConverter_IntegerParsingTest {

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
    public void testTryParseInt_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseInt(value);
    }

    @Test
    public void testParseInt_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseInt(value);
    }

    @Test
    public void testTryParseInt_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseInt(value));
    }

    @Test
    public void testParseInt_Value_Empty() throws Exception {
        String value = "";

        exception.expect(ParseException.class);
        _converter.parseInt(value);
    }

    @Test
    public void testTryParseInt_Values_NonParsable() throws Exception {
        String value1 = "258749654511";
        String value2 = "258749654.511";
        String value3 = "258749654511abd";
        String value4 = "true";

        assertFalse(_converter.tryParseInt(value1));
        assertFalse(_converter.tryParseInt(value2));
        assertFalse(_converter.tryParseInt(value3));
        assertFalse(_converter.tryParseInt(value4));
    }

    @Test
    public void testParseInt_Value_NonParsable() throws Exception {
        String value = "258749654511";

        exception.expect(ParseException.class);
        _converter.parseInt(value);
    }

    @Test
    public void testTryParseInt_Values_Parsable() throws Exception {
        String value1 = "12453";
        String value2 = "0";
        String value3 = "-1254";
        String value4 = "+6589";

        assertTrue(_converter.tryParseInt(value1));
        assertTrue(_converter.tryParseInt(value2));
        assertTrue(_converter.tryParseInt(value3));
        assertTrue(_converter.tryParseInt(value4));
    }

    @Test
    public void testParseInt_Values_Parsable() throws Exception {
        String value1 = "12453";
        String value2 = "0";
        String value3 = "-1254";
        String value4 = "+6589";

        assertNotNull(_converter.parseInt(value1));
        assertNotNull(_converter.parseInt(value2));
        assertNotNull(_converter.parseInt(value3));
        assertNotNull(_converter.parseInt(value4));
    }
}
