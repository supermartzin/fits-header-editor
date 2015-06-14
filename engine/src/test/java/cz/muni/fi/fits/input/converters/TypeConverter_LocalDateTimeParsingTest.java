package cz.muni.fi.fits.input.converters;

import cz.muni.fi.fits.exceptions.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Tests for parsing {@link java.time.LocalDateTime} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class TypeConverter_LocalDateTimeParsingTest {

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
    public void testTryParseLocalDateTime_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseLocalDateTime(value);
    }

    @Test
    public void testParseLocalDateTime_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseLocalDateTime(value);
    }

    @Test
    public void testTryParseLocalDateTime_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseLocalDateTime(value));
    }

    @Test
    public void testParseLocalDateTime_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseLocalDateTime(value));

        exception.expect(ParseException.class);
        _converter.parseLocalDateTime(value);
    }

    @Test
    public void testTryParseLocalDateTime_Values_NonParsable() throws Exception {
        String value1 = "2012:12:07T02:23:56";
        String value2 = "3-6-2-24:24:24";
        String value3 = "3.12.2014T14:15:16";

        assertFalse(_converter.tryParseLocalDateTime(value1));
        assertFalse(_converter.tryParseLocalDateTime(value2));
        assertFalse(_converter.tryParseLocalDateTime(value3));
    }

    @Test
    public void testParseLocalDateTime_Value_NonParsable() throws Exception {
        String value = "2012:12:7T02:23:56";

        exception.expect(ParseException.class);
        _converter.parseLocalDateTime(value);
    }

    @Test
    public void testTryParseLocalDateTime_Values_Parsable() throws Exception {
        String value1 = "2014-06-23T14:23:56";
        String value2 = "2012-12-08T12:12:12";

        assertTrue(_converter.tryParseLocalDateTime(value1));
        assertTrue(_converter.tryParseLocalDateTime(value2));
    }

    @Test
    public void testParseLocalDateTime_Values_Parsable() throws Exception {
        String value1 = "2014-06-23T14:23:56";
        String value2 = "2012-12-08T12:12:12";

        assertNotNull(_converter.parseLocalDateTime(value1));
        assertNotNull(_converter.parseLocalDateTime(value2));
    }
}