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
 * Tests for parsing {@link Double} values
 * in {@link DefaultTypeConverter} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class TypeConverter_DoubleParsingTest {

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
    public void testTryParseDouble_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.tryParseDouble(value);
    }

    @Test
    public void testParseDouble_Value_Null() throws Exception {
        String value = null;

        exception.expect(IllegalArgumentException.class);
        _converter.parseDouble(value);
    }

    @Test
    public void testTryParseDouble_Value_Empty() throws Exception {
        String value = "";

        assertFalse(_converter.tryParseDouble(value));
    }

    @Test
    public void testParseDouble_Value_Empty() throws Exception {
        String value = "";

        exception.expect(ParseException.class);
        _converter.parseDouble(value);
    }

    @Test
    public void testTryParseDouble_Value_TooBig() throws Exception {
        String value = "2514.5689E985";

        assertFalse(_converter.tryParseDouble(value));
    }

    @Test
    public void testParseDouble_Value_TooBig() throws Exception {
        String value = "2514.5689E985";

        exception.expect(ParseException.class);
        _converter.parseDouble(value);
    }

    @Test
    public void testTryParseDouble_Values_NonParsable() throws Exception {
        String value1 = "+-258749654511";
        String value2 = "258749654.511.2";
        String value3 = "258749654511abd";
        String value4 = "true";

        assertFalse(_converter.tryParseDouble(value1));
        assertFalse(_converter.tryParseDouble(value2));
        assertFalse(_converter.tryParseDouble(value3));
        assertFalse(_converter.tryParseDouble(value4));
    }

    @Test
    public void testParseDouble_Value_NonParsable() throws Exception {
        String value = "+-258749654511";

        exception.expect(ParseException.class);
        _converter.parseDouble(value);
    }

    @Test
    public void testTryParseDouble_Values_Parsable() throws Exception {
        String value1 = "+258749654511";
        String value2 = "-258749654.511";
        String value3 = "2.58749654511E25";
        String value4 = "45.";

        assertTrue(_converter.tryParseDouble(value1));
        assertTrue(_converter.tryParseDouble(value2));
        assertTrue(_converter.tryParseDouble(value3));
        assertTrue(_converter.tryParseDouble(value4));
    }

    @Test
    public void testParseDouble_Values_Parsable() throws Exception {
        String value1 = "+258749654511";
        String value2 = "-258749654.511";
        String value3 = "2.58749654511E25";
        String value4 = "45.";

        assertNotNull(_converter.parseDouble(value1));
        assertNotNull(_converter.parseDouble(value2));
        assertNotNull(_converter.parseDouble(value3));
        assertNotNull(_converter.parseDouble(value4));
    }

}
