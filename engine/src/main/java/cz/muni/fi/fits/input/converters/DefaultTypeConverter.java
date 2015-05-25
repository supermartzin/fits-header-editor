package cz.muni.fi.fits.input.converters;

import cz.muni.fi.fits.exceptions.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Default converter class for converting different object types,
 * implments {@link TypeConverter} interface
 *
 * @author Martin Vrábel
 * @version 1.2.1
 */
public class DefaultTypeConverter implements TypeConverter {

    /**
     * Tries to parse an <code>Integer</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse an <code>Integer</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseInt(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfEx) {
            return false;
        }
    }

    /**
     * Parses an <code>Integer</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse an <code>Integer</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public int parseInt(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfEx) {
            throw new ParseException(nfEx.getMessage(), nfEx);
        }
    }

    /**
     * Tries to parse a <code>Long</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>Long</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseLong(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException nfEx) {
            return false;
        }
    }

    /**
     * Parses a <code>Long</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>Long</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public long parseLong(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfEx) {
            throw new ParseException(nfEx.getMessage(), nfEx);
        }
    }

    /**
     * Tries to parse a <code>Double</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>Double</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseDouble(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            double parsedValue = Double.parseDouble(value);
            return Double.isFinite(parsedValue);
        } catch (NumberFormatException nfEx) {
            return false;
        }
    }

    /**
     * Parses a <code>Double</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>Double</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public double parseDouble(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            double parsedValue = Double.parseDouble(value);
            if (Double.isFinite(parsedValue))
                return parsedValue;
            else
                throw new ParseException("Value is too big for Double type");
        } catch (NumberFormatException nfEx) {
            throw new ParseException(nfEx.getMessage(), nfEx);
        }
    }

    /**
     * Tries to parse a <code>Boolean</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>Boolean</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseBoolean(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        value = value.toUpperCase().trim();
        return value.equals("TRUE")
                || value.equals("T")
                || value.equals("FALSE")
                || value.equals("F");
    }

    /**
     * Parses a <code>Boolean</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>Boolean</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public boolean parseBoolean(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        value = value.toUpperCase().trim();
        if (value.equals("TRUE") || value.equals("T"))
            return true;

        if (value.equals("FALSE") || value.equals("F"))
            return false;

        throw new ParseException("String value does not contain valid boolean value");
    }

    /**
     * Tries to parse a <code>BigInteger</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>BigInteger</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseBigInteger(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            new BigInteger(value);
            return true;
        } catch (NumberFormatException nfEx) {
            return false;
        }
    }

    /**
     * Parses a <code>BigInteger</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>BigInteger</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public BigInteger parseBigInteger(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            return new BigInteger(value);
        } catch (NumberFormatException nfEx) {
            throw new ParseException(nfEx.getMessage(), nfEx);
        }
    }

    /**
     * Tries to parse a <code>BigDecimal</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>BigDecimal</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseBigDecimal(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException nfEx) {
            return false;
        }
    }

    /**
     * Parses a <code>BigDecimal</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>BigDecimal</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public BigDecimal parseBigDecimal(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException nfEx) {
            throw new ParseException(nfEx.getMessage(), nfEx);
        }
    }

    /**
     * Tries to parse a <code>LocalDateTime</code> value from given <code>String</code> value
     *
     * @param value <code>String</code> value from which to parse a <code>LocalDateTime</code>
     * @return      {@inheritDoc}
     */
    @Override
    public boolean tryParseLocalDateTime(String value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            LocalDateTime.parse(value);
            return true;
        } catch (DateTimeParseException dtpEx) {
            return false;
        }
    }

    /**
     * Parses a <code>LocalDateTime</code> value from given <code>String</code> value
     *
     * @param value             <code>String</code> value from which to parse a <code>LocalDateTime</code>
     * @return                  {@inheritDoc}
     * @throws ParseException   {@inheritDoc}
     */
    @Override
    public LocalDateTime parseLocalDateTime(String value) throws ParseException {
        if (value == null)
            throw new IllegalArgumentException("value is null");

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException dtpEx) {
            throw new ParseException(dtpEx.getMessage(), dtpEx);
        }
    }
}
