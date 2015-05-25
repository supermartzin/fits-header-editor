package cz.muni.fi.fits.engine.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for parsing and working with datetime values
 * from FITS file header in {@link cz.muni.fi.fits.engine.HeaderEditingEngine} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class DateTimeUtils {

    /**
     * Class for parsing Date and Time values from {@link String}
     *
     * @author Martin Vrábel
     * @version 1.0
     */
    public static class DateTimeParser {

        private static final Set<DateTimeFormatter> DATE_TIME_FORMATTERS = new HashSet<>();
        private static final Set<DateTimeFormatter> DATE_FORMATTERS = new HashSet<>();
        private static final Set<DateTimeFormatter> TIME_FORMATTERS = new HashSet<>();

        static {
            DATE_TIME_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            DATE_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE);

            TIME_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_TIME);
        }

        /**
         * Tries to parse {@link LocalDateTime} from {@link String}
         * and returns {@link DateTimeFormatter} if parsing was successful
         *
         * @param value {@link String} containing datetime value to parse
         * @return      {@link DateTimeFormatter} if parsing was successful,
         *              <code>null</code> otherwise
         */
        public static DateTimeFormatter tryParseLocalDateTimeFormatter(String value) {
            if (value == null)
                return null;

            for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
                try {
                    LocalDateTime.parse(value, formatter);
                    return formatter;
                } catch (DateTimeParseException ignored) { }
            }

            return null;
        }

        /**
         * Tries to parse {@link LocalDate} from {@link String}
         * and returns {@link DateTimeFormatter} if parsing was successful
         *
         * @param value {@link String} containing date value to parse
         * @return      {@link DateTimeFormatter} if parsing was successful,
         *              <code>null</code> otherwise
         */
        public static DateTimeFormatter tryParseLocalDateFormatter(String value) {
            if (value == null)
                return null;

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    LocalDate.parse(value, formatter);
                    return formatter;
                } catch (DateTimeParseException ignored) { }
            }

            return null;
        }

        /**
         * Tries to parse {@link LocalTime} from {@link String}
         * and returns {@link DateTimeFormatter} if parsing was successful
         *
         * @param value {@link String} containing time value to parse
         * @return      {@link DateTimeFormatter} if parsing was successful,
         *              <code>null</code> otherwise
         */
        public static DateTimeFormatter tryParseLocalTimeFormatter(String value) {
            if (value == null)
                return null;

            for (DateTimeFormatter formatter : TIME_FORMATTERS) {
                try {
                    LocalTime.parse(value, formatter);
                    return formatter;
                } catch (DateTimeParseException ignored) { }
            }

            return null;
        }

        /**
         * Parses {@link LocalDateTime} value from {@link String}
         * with provided {@link DateTimeFormatter}
         *
         * @param value     {@link String} containing datetime value to parse
         * @param formatter {@link DateTimeFormatter} used for parsing datetime value
         * @return          {@link LocalDateTime} value if parsing was successful,
         *                  <code>null</code> otherwise
         */
        public static LocalDateTime parseLocalDateTime(String value, DateTimeFormatter formatter) {
            if (formatter == null)
                throw new IllegalArgumentException("formatter parameter is null");

            if (value == null)
                return null;

            try {
                return LocalDateTime.parse(value);
            } catch (DateTimeParseException dtpEx) {
                return null;
            }
        }

        /**
         * Parses {@link LocalDate} value from {@link String}
         * with provided {@link DateTimeFormatter}
         *
         * @param value     {@link String} containing date value to parse
         * @param formatter {@link DateTimeFormatter} used for parsing date value
         * @return          {@link LocalDate} value if parsing was successful,
         *                  <code>null</code> otherwise
         */
        public static LocalDate parseLocalDate(String value, DateTimeFormatter formatter) {
            if (formatter == null)
                throw new IllegalArgumentException("formatter parameter is null");

            if (value == null)
                return null;

            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException dtpEx) {
                return null;
            }
        }

        /**
         * Parses {@link LocalTime} value from {@link String}
         * with provided {@link DateTimeFormatter}
         *
         * @param value     {@link String} containing time value to parse
         * @param formatter {@link DateTimeFormatter} used for parsing date value
         * @return          {@link LocalTime} value if parsing was successful,
         *                  <code>null</code> otherwise
         */
        public static LocalTime parseLocalTime(String value, DateTimeFormatter formatter) {
            if (formatter == null)
                throw new IllegalArgumentException("formatter parameter is null");

            if (value == null)
                return null;

            try {
                return LocalTime.parse(value);
            } catch (DateTimeParseException dtpEx) {
                return null;
            }
        }
    }

    /**
     * Enumeration of possible datetime types
     *
     * @author Martin Vrábel
     * @version 1.0.1
     */
    public enum DateTimeType {
        /**
         * Full date and time type, e.g.: <code>25-03-1996T12:34:56.789</code>
         */
        DATETIME,

        /**
         * Only date type, e.g.: <code>25-03-1996</code>
         */
        DATE,

        /**
         * Only time type, e.g.: <code>12:34:56.789</code>
         */
        TIME
    }

    private DateTimeUtils() {}
}
