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
 * @version 1.0
 */
public class DateTimeParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testTryParseLocalDateTimeFormatter_ValueNull() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateTimeFormatter_CorrectValue() throws Exception {
        String value = "2012-05-12T15:42:56.789";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateTimeFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE_TIME, formatter);
    }

    @Test
    public void testTryParseLocalDateTimeFormatter_IncorrectValue() throws Exception {
        String value = "2012.05.12'T'15.42.56.789";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_ValueNull() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_CorrectValue() throws Exception {
        String value = "2015-03-28";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE, formatter);
    }

    @Test
    public void testTryParseLocalDateFormatter_IncorrectValue() throws Exception {
        String value = "2015/3/008";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_ValueNull() throws Exception {
        String value = null;

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_CorrectValue() throws Exception {
        String value = "23:59:59.999";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalTimeFormatter(value);

        assertNotNull(formatter);
        assertEquals(DateTimeFormatter.ISO_LOCAL_TIME, formatter);
    }

    @Test
    public void testTryParseLocalTimeFormatter_IncorrectValue() throws Exception {
        String value = "48:69:69.999";

        DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalTimeFormatter(value);

        assertNull(formatter);
    }

    @Test
    public void testParseLocalDateTime_ValueNull() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDateTime_FormatterNull() throws Exception {
        String value = "2015-03-02T23:12:13.140";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);
    }

    @Test
    public void testParseLocalDateTime_CorrectValue() throws Exception {
        String value = "2015-03-02T23:12:13.140";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNotNull(dateTime);
        assertEquals(LocalDateTime.of(2015, 3, 2, 23, 12, 13, 140000000), dateTime);
    }

    @Test
    public void testParseLocalDateTime_IncorrectValue() throws Exception {
        String value = "2015/03/02'T'48:12:13.140";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime dateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(value, formatter);

        assertNull(dateTime);
    }

    @Test
    public void testParseLocalDate_ValueNull() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNull(date);
    }

    @Test
    public void testParseLocalDate_FormatterNull() throws Exception {
        String value = "2015-11-23";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);
    }

    @Test
    public void testParseLocalDate_CorrectValue() throws Exception {
        String value = "2015-11-23";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNotNull(date);
        assertEquals(LocalDate.of(2015, 11, 23), date);
    }

    @Test
    public void testParseLocalDate_IncorrectValue() throws Exception {
        String value = "2015/11/23";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = DateTimeUtils.DateTimeParser.parseLocalDate(value, formatter);

        assertNull(date);
    }

    @Test
    public void testParseLocalTime_ValueNull() throws Exception {
        String value = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNull(time);
    }

    @Test
    public void testParseLocalTime_FormatterNull() throws Exception {
        String value = "12:34:56.789";
        DateTimeFormatter formatter = null;

        exception.expect(IllegalArgumentException.class);
        DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);
    }

    @Test
    public void testParseLocalTime_CorrectValue() throws Exception {
        String value = "12:34:56.789";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNotNull(time);
        assertEquals(LocalTime.of(12, 34, 56, 789000000), time);
    }

    @Test
    public void testParseLocalTime_IncorrectValue() throws Exception {
        String value = "12.94.56:789";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalTime time = DateTimeUtils.DateTimeParser.parseLocalTime(value, formatter);

        assertNull(time);
    }
}