package cz.muni.fi.fits.engine.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

/**
 * Tests for methods of {@link DateTimeUtils.DateTimeParser}
 * for parsing Date and Time values from {@link String}
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DateTimeParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testTryParseLocalDateTimeFormatter_Value_Null() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateTimeFormatter_Value_Valid() throws Exception {
        String value = "2012-05-12T15:42:56.789";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateTimeFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE_TIME, formatter);
    }

    @Test
    public void testTryParseLocalDateTimeFormatter_Value_NonParsable() throws Exception {
        String value = "2012.05.12'T'15.42.56.789";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_Value_Null() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_Value_Parsable() throws Exception {
        String value = "2015-03-28";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE, formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_Value_NonParsable() throws Exception {
        String value = "2015/3/008";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetDateFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_Value_Null() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_Value_Parsable() throws Exception {
        String value = "23:59:59.999";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetTimeFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_TIME, formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_Value_NonParsable() throws Exception {
        String value = "48:69:69.999";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryGetTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testParseLocalDateTime1_Value_Null() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDateTime1_Formatter_Null() throws Exception {
        String value = "2015-03-02T23:12:13.140";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);
    }

    @Test
    public void testParseLocalDateTime1_Value_Parsable() throws Exception {
        String value = "2015-03-02T23:12:13.140";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNotNull(dateTime);
        assertEquals(LocalDateTime.of(2015, 3, 2, 23, 12, 13, 140000000), dateTime);
    }

    @Test
    public void testParseLocalDateTime1_Value_NonParsable() throws Exception {
        String value = "2015/03/02'T'48:12:13.140";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDateTime2_Value_Null() throws Exception {
        String value = null;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDateTime2_Value_Parsable() throws Exception {
        String value = "2015-03-02T23:12:13.140";

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value);

        assertNotNull(dateTime);
        assertEquals(LocalDateTime.of(2015, 3, 2, 23, 12, 13, 140000000), dateTime);
    }

    @Test
    public void testParseLocalDateTime2_Value_NonParsable() throws Exception {
        String value = "2015/03/02'T'48:12:13.140";

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDate_Value_Null() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNull(date);
    }

    @Test
    public void testParseLocalDate_Formatter_Null() throws Exception {
        String value = "2015-11-23";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);
    }

    @Test
    public void testParseLocalDate_Value_Parsable() throws Exception {
        String value = "2015-11-23";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNotNull(date);
        assertEquals(LocalDate.of(2015, 11, 23), date);
    }

    @Test
    public void testParseLocalDate_Value_NonParsable() throws Exception {
        String value = "2015/11/23";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNull(date);
    }

    @Test
    public void testParseLocalTime_Value_Null() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNull(time);
    }

    @Test
    public void testParseLocalTime_Formatter_Null() throws Exception {
        String value = "12:34:56.789";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);
    }

    @Test
    public void testParseLocalTime_Value_Parsable() throws Exception {
        String value = "12:34:56.789";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNotNull(time);
        assertEquals(LocalTime.of(12, 34, 56, 789000000), time);
    }

    @Test
    public void testParseLocalTime_Value_NonParsable() throws Exception {
        String value = "12.94.56:789";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNull(time);
    }
}