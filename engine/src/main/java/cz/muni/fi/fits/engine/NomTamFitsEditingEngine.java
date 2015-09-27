package cz.muni.fi.fits.engine;

import cz.muni.fi.fits.engine.models.*;
import cz.muni.fi.fits.engine.models.converters.DeclinationParamsConverter;
import cz.muni.fi.fits.engine.models.converters.RightAscensionParamsConverter;
import cz.muni.fi.fits.engine.models.formatters.NumberFormatter;
import cz.muni.fi.fits.engine.utils.DateTimeUtils;
import cz.muni.fi.fits.engine.utils.MandatoryFITSKeywords;
import cz.muni.fi.fits.models.ChainValueType;
import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.TimeObject;
import cz.muni.fi.fits.common.utils.Constants;
import cz.muni.fi.fits.common.utils.Tuple;
import nom.tam.fits.*;
import nom.tam.util.BufferedFile;
import nom.tam.util.Cursor;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Editing engine class implementing {@link HeaderEditingEngine} interface
 * that uses external library <b>nom.tam.fits</b>, available as open source
 * on GitHub
 *
 * @author Martin Vrábel
 * @version 1.4.2
 * @see <a href="http://nom-tam-fits.github.io/nom-tam-fits/">nom.tam.fits - Project pages</a>
 */
public class NomTamFitsEditingEngine implements HeaderEditingEngine {

    private static final String FILE_OPENING_MODE = "rw";   // read-write

    /**
     * Adds new record to FITS header with specified arguments
     *
     * @param keyword        keyword of new record to add
     * @param value          value of new record to add
     * @param comment        comment of new record to add, insert
     *                       <code>null</code> when no comment to add to record
     * @param updateIfExists value indicating whether to update a record
     *                       if one with the same keyword already exists
     * @param fitsFile       FITS file to which add new record
     * @return {@inheritDoc}
     */
    @Override
    public Result addNewRecord(String keyword, Object value, String comment, boolean updateIfExists, File fitsFile) {
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (value == null)
            throw new IllegalArgumentException("value is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean updated = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // create new header card based on value type
            HeaderCard card = createNewHeaderCard(keyword, value, comment);

            if (card == null)
                return new Result(false, "Unknown type of value object");

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            if (keywordExists) {
                if (!updateIfExists)
                    return new Result(false, "Header already contains record with '" + keyword + "' keyword");

                // check for mandatory keyword
                if (MandatoryFITSKeywords.matchesMandatoryKeyword(keyword)) {
                    return new Result(false, "Record with keyword '" + keyword + "' already exists in header and is mandatory hence it cannot be changed");
                }

                // update existing header card
                header.updateLine(keyword, card);
                updated = true;
            } else {
                Cursor<String, HeaderCard> iterator = header.iterator();

                // move cursor to the end of header
                iterator.end();

                // insert new header card
                iterator.add(card);
            }

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (updated)
                return new Result(true, "Record '" + keyword + "' successfully added to header as update of existing record");
            else
                return new Result(true, "Record '" + keyword + "' successfully added to header");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Adds new record to FITS header with specified arguments to specific index
     *
     * @param index             index where to add new record
     * @param keyword           keyword of new record to add
     * @param value             value of new keyword to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> when no comment to add to record
     * @param removeOldIfExists value indicating whether to remove old record
     *                          with the same keyword if it already exists
     * @param fitsFile          FITS file to which add new record
     * @return {@inheritDoc}
     */
    @Override
    public Result addNewRecordToIndex(int index, String keyword, Object value, String comment, boolean removeOldIfExists, File fitsFile) {
        if (index < 0)
            throw new IllegalArgumentException("invalid index");
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (value == null)
            throw new IllegalArgumentException("value is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean oldRemoved = false;
        boolean insertedToEnd = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            if (keywordExists) {
                if (!removeOldIfExists)
                    return new Result(false, "Header already contains record with '" + keyword + "' keyword");

                // remove old header cards
                header.deleteKey(keyword);
                oldRemoved = true;
            }

            // create new header card based on value type
            HeaderCard card = createNewHeaderCard(keyword, value, comment);

            if (card == null)
                return new Result(false, "Unknown type of value object");

            Cursor<String, HeaderCard> iterator = header.iterator();

            // check if index is in range of header size
            if (indexInRangeOfHeaderSize(index, header)) {
                // iterate to specified index
                if (index > 1)
                    iterator.next(index - 1);

                // check for mandatory keyword at this index
                String indexKey = iterator.next().getKey();
                if (MandatoryFITSKeywords.matchesMandatoryKeyword(indexKey)) {
                    return new Result(false, "Record '" + keyword + "' cannot be inserted to index " + index + " because of mandatory keyword '" + indexKey + "'");
                }

                iterator.prev();
            } else {
                insertedToEnd = true;
                // move cursor to the end of header
                iterator.end();
            }

            // insert new card
            iterator.add(card);

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!oldRemoved && !insertedToEnd)
                return new Result(true, "Record '" + keyword + "' successfully added to header to index " + index);
            else if (oldRemoved && !insertedToEnd)
                return new Result(true, "Record '" + keyword + "' successfully added to header to index " + index + " removing the old one");
            else if (!oldRemoved)
                return new Result(true, "Record '" + keyword + "' successfully added to the end of header (index was out of range)");
            else
                return new Result(true, "Record '" + keyword + "'  successfully added to the end of header (index was out of range) removing the old one");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Removes record from FITS header with specified keyword
     *
     * @param keyword  keyword of a record to remove
     * @param fitsFile FITS file from which to remove a record
     * @return {@inheritDoc}
     */
    @Override
    public Result removeRecordByKeyword(String keyword, File fitsFile) {
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            if (!keywordExists)
                return new Result(false, "Header does not contain record with keyword '" + keyword + "'");

            // check for mandatory keyword
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(keyword)) {
                return new Result(false, "Record with keyword '" + keyword + "' is mandatory hence it cannot be removed");
            }

            // remove card with specified keyword
            header.deleteKey(keyword);

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            return new Result(true, "Record '" + keyword + "' successfully removed from header");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Removes record from FITS header from specified index
     *
     * @param index    index from which to remove a record
     * @param fitsFile FITS file from which to remove a record
     * @return {@inheritDoc}
     */
    @Override
    public Result removeRecordFromIndex(int index, File fitsFile) {
        if (index < 0)
            throw new IllegalArgumentException("invalid index");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if index is in range of header size
            if (!indexInRangeOfHeaderSize(index, header))
                return new Result(false, "Index " + index + " is not in range of header size");

            // move cursor to specified index
            Cursor<String, HeaderCard> iterator;
            if (index > 1)
                iterator = header.iterator(index - 1);
            else
                iterator = header.iterator();

            // check for mandatory keyword
            String indexKey = iterator.next().getKey();
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(indexKey))
                return new Result(false, "Record with keyword '" + indexKey + "' on index " + index + " is mandatory hence it cannot be removed");

            // remove record on the index
            iterator.remove();

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            return new Result(true, "Successfully removed from index " + index + " record '" + indexKey + "'");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Changes keyword of specified existing record in FITS header to new one
     *
     * @param oldKeyword               keyword defining existing record in which to change keyword
     * @param newKeyword               new keyword to set in record
     * @param removeValueOfNewIfExists value indicating whether to remove record with new keyword
     *                                 if it already exists in header
     * @param fitsFile                 FITS file in which to change a record
     * @return {@inheritDoc}
     */
    @Override
    public Result changeKeywordOfRecord(String oldKeyword, String newKeyword, boolean removeValueOfNewIfExists, File fitsFile) {
        if (oldKeyword == null)
            throw new IllegalArgumentException("oldKeyword is null");
        if (newKeyword == null)
            throw new IllegalArgumentException("newKeyword is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean valueOfNewRemoved = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if old keyword does already exist
            boolean oldExists = header.containsKey(oldKeyword);

            if (!oldExists)
                return new Result(false, "Header does not contain record with '" + oldKeyword + "' keyword");

            // check if new keyword does already exist
            boolean newExists = header.containsKey(newKeyword);

            if (newExists) {
                if (removeValueOfNewIfExists) {
                    // check for mandatory keyword
                    if (MandatoryFITSKeywords.matchesMandatoryKeyword(newKeyword)) {
                        return new Result(false, "Header already contains record with '" + newKeyword + "' keyword but it is mandatory hence it cannot be removed");
                    }
                    valueOfNewRemoved = true;

                    // remove already existing header card
                    header.deleteKey(newKeyword);
                } else {
                    return new Result(false, "Header already contains record with '" + newKeyword + "' keyword");
                }
            }

            // check for mandatory keyword
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(oldKeyword))
                return new Result(false, "Record with keyword '" + oldKeyword + "' is mandatory hence it cannot be changed");

            // get old header card and create updated one based on type of value
            HeaderCard newCard;
            HeaderCard oldCard = header.findCard(oldKeyword);
            if (oldCard.valueType() == String.class) {
                String value = header.getStringValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == Double.class) {
                Double value = header.getDoubleValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == Boolean.class) {
                boolean value = header.getBooleanValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == Integer.class) {
                int value = header.getIntValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == Long.class) {
                long value = header.getLongValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == BigInteger.class) {
                BigInteger value = header.getBigIntegerValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else if (oldCard.valueType() == BigDecimal.class) {
                BigDecimal value = header.getBigDecimalValue(oldKeyword);
                newCard = new HeaderCard(newKeyword, value, oldCard.getComment());
            } else {
                newCard = new HeaderCard(newKeyword, oldCard.getValue(), oldCard.getComment());
            }

            // update old header card with new one
            header.updateLine(oldKeyword, newCard);

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!valueOfNewRemoved)
                return new Result(true, "Keyword '" + oldKeyword + "' successfully changed to '" + newKeyword + "'");
            else
                return new Result(true, "Keyword '" + oldKeyword + "' successfully changed to '" + newKeyword + "' removing existing new keyword record");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Change value of specified existing record in FIT header to new one
     *
     * @param keyword           keyword defining existing record in which to change value
     * @param newValue          new value to be set in record
     * @param newComment        new comment to set in record, insert <code>null</code>
     *                          if want to use the original comment
     * @param addNewIfNotExists value indicating whether add new record if
     *                          record with specified keyword does not exist
     * @param fitsFile          FITS file in which to change a record
     * @return {@inheritDoc}
     */
    @Override
    public Result changeValueOfRecord(String keyword, Object newValue, String newComment, boolean addNewIfNotExists, File fitsFile) {
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (newValue == null)
            throw new IllegalArgumentException("newValue is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean newAdded = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            // if comment is null use the original one
            if (newComment == null) {
                HeaderCard existingCard = header.findCard(keyword);
                newComment = existingCard.getComment();
            }

            // create new header card based on value type
            HeaderCard card;
            if (newValue instanceof Integer) {
                card = new HeaderCard(keyword, (Integer) newValue, newComment);
            } else if (newValue instanceof Long) {
                card = new HeaderCard(keyword, (Long) newValue, newComment);
            } else if (newValue instanceof Double) {
                card = new HeaderCard(keyword, (Double) newValue, newComment);
            } else if (newValue instanceof Boolean) {
                card = new HeaderCard(keyword, (Boolean) newValue, newComment);
            } else if (newValue instanceof String) {
                card = new HeaderCard(keyword, (String) newValue, newComment);
            } else if (newValue instanceof BigInteger) {
                card = new HeaderCard(keyword, (BigInteger) newValue, newComment);
            } else if (newValue instanceof BigDecimal) {
                card = new HeaderCard(keyword, (BigDecimal) newValue, newComment);
            } else {
                return new Result(false, "Unknown type of value object");
            }

            if (keywordExists) {
                // check for mandatory keyword
                if (MandatoryFITSKeywords.matchesMandatoryKeyword(keyword)) {
                    return new Result(false, "Record with keyword '" + keyword + "' is mandatory hence it cannot be changed");
                }

                // update existing header card
                header.updateLine(keyword, card);
            } else {
                if (!addNewIfNotExists) {
                    return new Result(false, "Header does not contain record with '" + keyword + "' keyword");
                } else {
                    newAdded = true;

                    Cursor<String, HeaderCard> iterator = header.iterator();

                    // move cursor to the end of header
                    iterator.end();

                    // insert new header card
                    iterator.add(card);
                }
            }

            // write changes back to file
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!newAdded)
                return new Result(true, "Value of record '" + keyword + "' successfully changed");
            else
                return new Result(true, "Value of record '" + keyword + "' successfully added as new record");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Chain multiple records into new single record in FITS header by specified arguments
     *
     * @param keyword                keyword of new chained record
     * @param chainParameters        list of parameters containing constant and keyword
     *                               tuples of {@link ChainValueType} and
     *                               {@link String} which to chain
     * @param comment                comment to set in record, insert <code>null</code>
     *                               if no comment to add
     * @param updateIfExists         value indicating whether to update value of record
     *                               with specified keyword if it already exists
     * @param skipIfChainKwNotExists value indcating whether to skip keyword in chain parameters
     *                               if no such record exists in header
     * @param fitsFile               FITS file in which to chain records
     * @return {@inheritDoc}
     */
    @Override
    public Result chainMultipleRecords(String keyword, List<Tuple<ChainValueType, String>> chainParameters, String comment, boolean updateIfExists, boolean skipIfChainKwNotExists, File fitsFile) {
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (chainParameters == null)
            throw new IllegalArgumentException("chainParamaters is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean updated = false;
        boolean skippedKeyword = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // iterate over parameters and create new value
            String value = "";

            for (Tuple<ChainValueType, String> chainParameter : chainParameters) {
                switch (chainParameter.getFirst()) {
                    case CONSTANT:
                        value += chainParameter.getSecond();
                        break;
                    case KEYWORD:
                        String key = chainParameter.getSecond();
                        // check if header contains key
                        boolean keyExists = header.containsKey(key);

                        if (!keyExists) {
                            if (skipIfChainKwNotExists) {
                                skippedKeyword = true;
                                break;
                            } else
                                return new Result(false, "Header does not contain record with '" + key + "' keyword");
                        } else {
                            // add to value
                            value += header.findCard(key).getValue();
                        }
                        break;
                }
            }

            // check for validity of value
            if (value.isEmpty())
                return new Result(false, "Value of chained records cannot empty");
            if (value.length() > Constants.MAX_STRING_VALUE_LENGTH)
                return new Result(false, "Value of chained records is too long");
            if (comment != null
                    && value.length() + comment.length() > Constants.MAX_STRING_VALUE_COMMENT_LENGTH)
                return new Result(false, "Value along with comment are too long. Try to shorten the comment");

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            if (keywordExists) {
                if (!updateIfExists) {
                    return new Result(false, "Header already contains record with '" + keyword + "' keyword");
                } else {
                    // check for mandatory keyword
                    if (MandatoryFITSKeywords.matchesMandatoryKeyword(keyword)) {
                        return new Result(false, "Header already contains record with '" + keyword + "' keyword but it is mandatory hence it cannot be changed");
                    }

                    updated = true;

                    // update header card with new chained value
                    header.updateLine(keyword, new HeaderCard(keyword, value, comment));
                }
            } else {
                Cursor<String, HeaderCard> iterator = header.iterator();

                // move cursor to the end of header
                iterator.end();

                // insert new header card
                iterator.add(new HeaderCard(keyword, value, comment));
            }

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!updated && !skippedKeyword)
                return new Result(true, "Records successfully chained into record '" + keyword + "'");
            else if (updated && !skippedKeyword)
                return new Result(true, "Records successfully chained and updated into record '" + keyword + "'");
            else if (!updated)
                return new Result(true, "Records chained into record '" + keyword + "' with some non-existing keywords records skipped");
            else
                return new Result(true, "Records chained and updated into record '" + keyword + "' with some non-existing keywords records skipped");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Shifts time of time record with <code>keyword</code> by amount of time specified by arguments
     *
     * @param keyword         keyword of time record in which to shift time
     * @param yearShift       time shift for years
     * @param monthShift      time shift for months
     * @param dayShift        time shift for days
     * @param hourShift       time shift for hours
     * @param minuteShift     time shift for minutes
     * @param secondShift     time shift for seconds
     * @param nanosecondShift time shift for nanoseconds
     * @param fitsFile        FITS file in which to chain records
     * @return {@inheritDoc}
     */
    @Override
    public Result shiftTimeOfTimeRecord(String keyword, int yearShift, int monthShift, int dayShift,
                                        int hourShift, int minuteShift, int secondShift, int nanosecondShift, File fitsFile) {
        if (keyword == null)
            throw new IllegalArgumentException("keyword is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFiles is null");

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            // check if keyword does already exist
            boolean keywordExists = header.containsKey(keyword);

            if (!keywordExists)
                return new Result(false, "Header does not contain record with keyword '" + keyword + "'");

            // get header card with keyword
            HeaderCard oldCard = header.findCard(keyword);

            // try to parse LocalDateTime value
            DateTimeFormatter formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateTimeFormatter(oldCard.getValue());
            DateTimeUtils.DateTimeType recordType = DateTimeUtils.DateTimeType.DATETIME;
            // try to parse LocalDate value
            if (formatter == null) {
                formatter = DateTimeUtils.DateTimeParser.tryParseLocalDateFormatter(oldCard.getValue());
                recordType = DateTimeUtils.DateTimeType.DATE;
            }
            // try to parse LocalTime value
            if (formatter == null) {
                formatter = DateTimeUtils.DateTimeParser.tryParseLocalTimeFormatter(oldCard.getValue());
                recordType = DateTimeUtils.DateTimeType.TIME;
            }

            // cannot parse DateTime value
            if (formatter == null)
                return new Result(false, "Record with keyword '" + keyword + "' does not contain parsable DateTime value");

            HeaderCard newCard;
            switch (recordType) {
                case DATETIME:
                    // parse LocalDateTime value
                    LocalDateTime parsedDateTime = DateTimeUtils.DateTimeParser.parseLocalDateTime(oldCard.getValue(), formatter);
                    try {
                        // shift value
                        LocalDateTime newDateTime = parsedDateTime
                                .plus(yearShift, ChronoUnit.YEARS)
                                .plus(monthShift, ChronoUnit.MONTHS)
                                .plus(dayShift, ChronoUnit.DAYS)
                                .plus(hourShift, ChronoUnit.HOURS)
                                .plus(minuteShift, ChronoUnit.MINUTES)
                                .plus(secondShift, ChronoUnit.SECONDS)
                                .plus(nanosecondShift, ChronoUnit.NANOS);

                        // create updated header card
                        newCard = new HeaderCard(keyword, newDateTime.toString(), oldCard.getComment());
                    } catch (DateTimeException | ArithmeticException ex) {
                        return new Result(false, "Error shifting time for record '" + keyword + "': " + ex.getMessage());
                    }
                    break;

                case DATE:
                    // parse LocalDate value
                    LocalDate parsedDate = DateTimeUtils.DateTimeParser.parseLocalDate(oldCard.getValue(), formatter);
                    try {
                        // shift value
                        LocalDate newDate = parsedDate
                                .plus(yearShift, ChronoUnit.YEARS)
                                .plus(monthShift, ChronoUnit.MONTHS)
                                .plus(dayShift, ChronoUnit.DAYS);

                        // create updated header card
                        newCard = new HeaderCard(keyword, newDate.toString(), oldCard.getComment());
                    } catch (DateTimeException | ArithmeticException ex) {
                        return new Result(false, "Error shifting time for record '" + keyword + "': " + ex.getMessage());
                    }
                    break;

                case TIME:
                    // parse LocalTime value
                    LocalTime parsedTime = DateTimeUtils.DateTimeParser.parseLocalTime(oldCard.getValue(), formatter);
                    try {
                        // shift value
                        LocalTime newTime = parsedTime
                                .plus(hourShift, ChronoUnit.HOURS)
                                .plus(minuteShift, ChronoUnit.MINUTES)
                                .plus(secondShift, ChronoUnit.SECONDS)
                                .plus(nanosecondShift, ChronoUnit.NANOS);

                        // create updated header card
                        newCard = new HeaderCard(keyword, newTime.toString(), oldCard.getComment());
                    } catch (DateTimeException | ArithmeticException ex) {
                        return new Result(false, "Error shifting time for record '" + keyword + "': " + ex.getMessage());
                    }
                    break;

                default:
                    return new Result(false, "Record with keyword '" + keyword + "' does not contain parsable DateTime value");
            }

            // check for mandatory keyword
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(keyword)) {
                return new Result(false, "Header contains record with '" + keyword + "' keyword but it is mandatory hence it cannot be changed");
            }

            // update record in header
            header.updateLine(keyword, newCard);

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            return new Result(true, "'" + keyword + "' record successfully changed from '"
                    + oldCard.getValue() + "' to '"
                    + newCard.getValue() + "'");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Computes Julian Date from provided datetime and exposure parameters
     * and saves or updates (if record already exists) value to corresponding
     * record in FITS file header
     *
     * @param datetime {@link String} value as keyword of datetime record
     *                 or {@link java.time.LocalDateTime} as value of datetime
     * @param exposure {@link String} value as keyword of exposure record
     *                 or {@link Double} as value of exposure in seconds
     * @param comment  comment of JD record, insert
     *                 <code>null</code> when no comment to add
     * @param fitsFile FITS file in which to chain records
     * @return {@inheritDoc}
     */
    @Override
    public Result computeJulianDate(Object datetime, Object exposure, String comment, File fitsFile) {
        if (datetime == null)
            throw new IllegalArgumentException("datetime is null");
        if (exposure == null)
            throw new IllegalArgumentException("exposure is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean jdUpdated = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();

            LocalDateTime datetimeValue;
            double exposureValue;

            // load datetime value
            if (datetime instanceof LocalDateTime) {
                datetimeValue = (LocalDateTime) datetime;
            } else if (datetime instanceof String) {
                // get value from FITS file header
                String datetimeKeyword = (String) datetime;

                if (header.containsKey(datetimeKeyword)) {
                    HeaderCard datetimeCard = header.findCard(datetimeKeyword);

                    // parse LocalDateTime value from record
                    try {
                        datetimeValue = LocalDateTime.parse(datetimeCard.getValue());
                    } catch (DateTimeParseException dtpEx) {
                        return new Result(false, "Record with keyword '" + datetimeKeyword + "' does not contain valid DateTime value");
                    }
                } else {
                    return new Result(false, "Header does not contain DateTime record with keyword '" + datetimeKeyword + "'");
                }
            } else {
                return new Result(false, "Unknown type of DateTime object");
            }

            // load exposure value
            if (exposure instanceof Double) {
                exposureValue = (double) exposure;
            } else if (exposure instanceof String) {
                // get value from FITS file header
                String exposureKeyword = (String) exposure;

                if (header.containsKey(exposureKeyword)) {
                    HeaderCard exposureCard = header.findCard(exposureKeyword);

                    // get Double value from record
                    exposureValue = exposureCard.getValue(Double.class, Double.NaN);

                    if (Double.isNaN(exposureValue))
                        return new Result(false, "Record with keyword '" + exposureKeyword + "' does not contain valid Double value");
                } else {
                    return new Result(false, "Header does not contain Exposure record with keyword '" + exposureKeyword + "'");
                }
            } else {
                return new Result(false, "Unknown type of Exposure object");
            }

            // move datetime to center of exposure time
            double nanoseconds = exposureValue * 1000 * 1000 * 1000; // for greater precision
            datetimeValue = datetimeValue.plusNanos(Double.valueOf(nanoseconds).longValue());

            // compute Julian Date
            double julianDate = JulianDate.computeJulianDate(datetimeValue);

            HeaderCard jdCard = new HeaderCard(Constants.DEFAULT_JD_KEYWORD, julianDate, comment);

            // check for mandatory keyword
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(Constants.DEFAULT_JD_KEYWORD)) {
                return new Result(false, "Header already contains record with '" + Constants.DEFAULT_JD_KEYWORD + "' keyword but it is mandatory hence it cannot be changed");
            }

            // save/update to header as new record
            if (header.containsKey(Constants.DEFAULT_JD_KEYWORD)) {
                header.updateLine(Constants.DEFAULT_JD_KEYWORD, jdCard);
                jdUpdated = true;
            } else {
                Cursor<String, HeaderCard> iterator = header.iterator();
                iterator.end();
                iterator.add(jdCard);
            }

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!jdUpdated)
                return new Result(true, "Julian Date successfully saved to 'JD' record");
            else
                return new Result(true, "Julian Date successfully updated in 'JD' record");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }

    /**
     * Computes Heliocentric Julian Date, saves or updates (if record already exists)
     * the value to corresponding record in header, saves right ascension and declination values
     * to FITS file header (if provided as values, otherwise does not save)
     *
     * @param datetime       {@link String} value as keyword of datetime record
     *                       or {@link java.time.LocalDateTime} as value of datetime
     * @param exposure       {@link String} value as keyword of exposure record
     *                       or {@link Double} as value of exposure in seconds
     * @param rightAscension {@link String} value as keyword of right ascension record,
     *                       {@link cz.muni.fi.fits.models.TimeObject} as right ascension value parameters
     *                       or {@link Double} or {@link java.math.BigDecimal} value of right ascension
     * @param declination    {@link String} value as keyword of declination record,
     *                       {@link cz.muni.fi.fits.models.DegreesObject} as declination value parameters
     *                       or {@link Double} or {@link java.math.BigDecimal} as value of declination
     * @param comment        comment of HJD record, insert
     *                       <code>null</code> when no comment to add
     * @param fitsFile       FITS file in which to chain records
     * @return {@inheritDoc}
     */
    @Override
    public Result computeHeliocentricJulianDate(Object datetime, Object exposure, Object rightAscension, Object declination, String comment, File fitsFile) {
        if (datetime == null)
            throw new IllegalArgumentException("datetime is null");
        if (exposure == null)
            throw new IllegalArgumentException("exposure is null");
        if (rightAscension == null)
            throw new IllegalArgumentException("rightAscension is null");
        if (declination == null)
            throw new IllegalArgumentException("declination is null");
        if (fitsFile == null)
            throw new IllegalArgumentException("fitsFile is null");

        boolean hjdUpdated = false;

        try {
            Fits fits = new Fits(fitsFile);

            // get header of first HDU unit
            BasicHDU hdu = fits.getHDU(0);
            Header header = hdu.getHeader();


            boolean saveRightAscension = true;
            boolean saveDeclination = true;

            LocalDateTime datetimeValue;
            double exposureValue;
            double rightAscensionValue;
            double declinationValue;

            // load datetime value
            if (datetime instanceof LocalDateTime) {
                datetimeValue = (LocalDateTime) datetime;
            } else if (datetime instanceof String) {
                // get value from FITS file header
                String datetimeKeyword = (String) datetime;

                if (header.containsKey(datetimeKeyword)) {
                    HeaderCard datetimeCard = header.findCard(datetimeKeyword);

                    // parse LocalDateTime value from record
                    try {
                        datetimeValue = LocalDateTime.parse(datetimeCard.getValue());
                    } catch (DateTimeParseException dtpEx) {
                        return new Result(false, "Record with keyword '" + datetimeKeyword + "' does not contain valid DateTime value");
                    }
                } else {
                    return new Result(false, "Header does not contain DateTime record with keyword '" + datetimeKeyword + "'");
                }
            } else {
                return new Result(false, "Unknown type of DateTime object");
            }

            // load exposure value
            if (exposure instanceof Double) {
                exposureValue = (double) exposure;
            } else if (exposure instanceof String) {
                // get value from FITS file header
                String exposureKeyword = (String) exposure;

                if (header.containsKey(exposureKeyword)) {
                    HeaderCard exposureCard = header.findCard(exposureKeyword);

                    // get Double value from record
                    exposureValue = exposureCard.getValue(Double.class, Double.NaN);

                    if (Double.isNaN(exposureValue))
                        return new Result(false, "Record with keyword '" + exposureKeyword + "' does not contain valid Double value");
                } else {
                    return new Result(false, "Header does not contain Exposure record with keyword '" + exposureKeyword + "'");
                }
            } else {
                return new Result(false, "Unknown type of Exposure object");
            }

            // load right ascension value
            if (rightAscension instanceof Double) {
                // value is double
                rightAscensionValue = (double) rightAscension;
            } else if (rightAscension instanceof BigDecimal) {
                // value is BigDecimal
                double value = ((BigDecimal) rightAscension).doubleValue();
                if (Double.isFinite(value)) {
                    rightAscensionValue = value;
                } else {
                    return new Result(false, "Right Ascension value is too big");
                }
            } else if (rightAscension instanceof TimeObject) {
                // value is TimeObject
                rightAscensionValue = RightAscension.computeRightAscension((TimeObject) rightAscension);
            } else if (rightAscension instanceof String) {
                // value is String keyword
                saveRightAscension = false;

                // get value from FITS file header
                String rightAscensionKeyword = (String) rightAscension;

                if (!header.containsKey(rightAscensionKeyword))
                    return new Result(false, "Header does not contain Right Ascension record with keyword '" + rightAscensionKeyword + "'");

                HeaderCard rightAscensionCard = header.findCard(rightAscensionKeyword);

                // parse right ascension from record
                if (rightAscensionCard.valueType().equals(Double.class)) {
                    // double value
                    rightAscensionValue = rightAscensionCard.getValue(Double.class, Double.NaN);
                } else if (rightAscensionCard.getValue().trim().split(":").length == 3) {
                    // full time value
                    String[] values = rightAscensionCard.getValue().trim().split(":");
                    try {
                        rightAscensionValue = RightAscension.computeRightAscension(
                                Double.parseDouble(values[0].trim()),
                                Double.parseDouble(values[1].trim()),
                                Double.parseDouble(values[2].trim()));
                    } catch (NumberFormatException nfEx) {
                        return new Result(false, "Value of Right Ascension record '" + rightAscensionKeyword + "' is in invalid format");
                    }
                } else {
                    return new Result(false, "Record with keyword '" + rightAscensionKeyword + "' does not contain valid Right Ascension value");
                }
            } else {
                return new Result(false, "Unknown type of Right Ascension object");
            }

            // load declination value
            if (declination instanceof Double) {
                // value is Double
                declinationValue = (double) declination;
            } else if (declination instanceof BigDecimal) {
                // value is BigDecimal
                double value = ((BigDecimal) declination).doubleValue();
                if (Double.isFinite(value)) {
                    declinationValue = value;
                } else {
                    return new Result(false, "Declination value is too big");
                }
            } else if (declination instanceof DegreesObject) {
                // value is DegreesObject
                declinationValue = Declination.computeDeclination((DegreesObject) declination);
            } else if (declination instanceof String) {
                // value is String keyword
                saveDeclination = false;

                // get value from FITS file header
                String declinationKeyword = (String) declination;

                if (!header.containsKey(declinationKeyword))
                    return new Result(false, "Header does not contain Declination record with keyword '" + declinationKeyword + "'");

                HeaderCard declinationCard = header.findCard(declinationKeyword);

                // parse declination from record
                if (declinationCard.valueType().equals(Double.class)) {
                    // double value
                    declinationValue = declinationCard.getValue(Double.class, Double.NaN);
                } else if (declinationCard.getValue().trim().split(":").length == 3) {
                    // full degrees value
                    String[] values = declinationCard.getValue().trim().split(":");
                    try {
                        declinationValue = Declination.computeDeclination(
                                Double.parseDouble(values[0].trim()),
                                Double.parseDouble(values[1].trim()),
                                Double.parseDouble(values[2].trim()));
                    } catch (NumberFormatException nfEx) {
                        return new Result(false, "Value of Declination record '" + declinationKeyword + "' is in invalid format");
                    }
                } else {
                    return new Result(false, "Record with keyword '" + declinationKeyword + "' does not contain valid Declination value");
                }
            } else {
                return new Result(false, "Unknown type of Declination object");
            }

            // move datetime to center of exposure time
            double nanoseconds = exposureValue * 1000 * 1000 * 1000; // for greater precision
            datetimeValue = datetimeValue.plusNanos(Double.valueOf(nanoseconds).longValue());

            // compute Heliocentric Julian Date
            double julianDate = JulianDate.computeJulianDate(datetimeValue);
            double heliocentricJulianDate = HeliocentricJulianDate.computeHeliocentricJulianDate(julianDate, rightAscensionValue, declinationValue);

            HeaderCard hjdCard = new HeaderCard(Constants.DEFAULT_HJD_KEYWORD, heliocentricJulianDate, comment);

            // check for mandatory keyword
            if (MandatoryFITSKeywords.matchesMandatoryKeyword(Constants.DEFAULT_HJD_KEYWORD))
                return new Result(false, "Header already contains record with '" + Constants.DEFAULT_HJD_KEYWORD + "' keyword but it is mandatory hence it cannot be changed");

            if (!header.containsKey(Constants.DEFAULT_HJD_KEYWORD)) {
                // save to header as new record
                Cursor<String, HeaderCard> iterator = header.iterator();
                iterator.end();
                iterator.add(hjdCard);
            } else {
                // update existing record in header
                header.updateLine(Constants.DEFAULT_HJD_KEYWORD, hjdCard);
                hjdUpdated = true;
            }

            if (saveRightAscension) {
                HeaderCard raCard;

                if (rightAscension instanceof TimeObject) {
                    // save in full time format -> hh:mm:ss.SSS

                    TimeObject timeObject = (TimeObject) rightAscension;
                    // convert parameters to base form
                    RightAscensionParamsConverter converter = new RightAscensionParamsConverter(
                            timeObject.getHours(),
                            timeObject.getMinutes(),
                            timeObject.getSeconds());

                    String raValue = NumberFormatter.format(converter.getHours(), 2, 2) + ":"
                            + NumberFormatter.format(converter.getMinutes(), 2, 2) + ":"
                            + NumberFormatter.format(converter.getSeconds(), 2, 3);

                    raCard = new HeaderCard(Constants.DEFAULT_RA_KEYWORD, raValue, Constants.DEFAULT_RA_COMMENT);
                } else {
                    // save as number
                    raCard = new HeaderCard(Constants.DEFAULT_RA_KEYWORD, rightAscensionValue, Constants.DEFAULT_RA_COMMENT);
                }

                // check for mandatory keyword
                if (MandatoryFITSKeywords.matchesMandatoryKeyword(Constants.DEFAULT_RA_KEYWORD)) {
                    return new Result(false, "Header already contains record with '" + Constants.DEFAULT_RA_KEYWORD + "' keyword but it is mandatory hence it cannot be changed");
                }

                // save/update right ascension to header
                if (header.containsKey(Constants.DEFAULT_RA_KEYWORD))
                    header.updateLine(Constants.DEFAULT_RA_KEYWORD, raCard);
                else {
                    Cursor<String, HeaderCard> iterator = header.iterator();
                    iterator.end();
                    iterator.add(raCard);
                }
            }

            if (saveDeclination) {
                HeaderCard decCard;

                if (declination instanceof DegreesObject) {
                    // save in full degrees format -> hh:mm:ss.SSS

                    DegreesObject degreesObject = (DegreesObject) declination;
                    // convert parameters to base form
                    DeclinationParamsConverter converter = new DeclinationParamsConverter(
                            degreesObject.getDegrees(),
                            degreesObject.getMinutes(),
                            degreesObject.getSeconds());

                    String decValue = NumberFormatter.format(converter.getDegrees(), 2, 2) + ":"
                            + NumberFormatter.format(converter.getMinutes(), 2, 2) + ":"
                            + NumberFormatter.format(converter.getSeconds(), 2, 3);

                    decCard = new HeaderCard(Constants.DEFAULT_DEC_KEYWORD, decValue, Constants.DEFAULT_DEC_COMMENT);
                } else {
                    // save as number
                    decCard = new HeaderCard(Constants.DEFAULT_DEC_KEYWORD, declinationValue, Constants.DEFAULT_DEC_COMMENT);
                }

                // check for mandatory keyword
                if (MandatoryFITSKeywords.matchesMandatoryKeyword(Constants.DEFAULT_DEC_KEYWORD)) {
                    return new Result(false, "Header already contains record with '" + Constants.DEFAULT_DEC_KEYWORD + "' keyword but it is mandatory hence it cannot be changed");
                }

                // save/update declination to header
                if (header.containsKey(Constants.DEFAULT_DEC_KEYWORD))
                    header.updateLine(Constants.DEFAULT_DEC_KEYWORD, decCard);
                else {
                    Cursor<String, HeaderCard> iterator = header.iterator();
                    iterator.end();
                    iterator.add(decCard);
                }
            }

            // write changes
            writeChangesBackToFile(fits, fitsFile);

            // return success
            if (!hjdUpdated)
                return new Result(true, "Heliocentric Julian Date successfully saved to 'HJD' record");
            else
                return new Result(true, "Heliocentric Julian Date successfully updated in 'HJD' record");
        } catch (FitsException | IOException ex) {
            return new Result(false, "Error in editing engine: " + ex.getMessage());
        }
    }


    private HeaderCard createNewHeaderCard(String keyword, Object value, String comment)
            throws HeaderCardException {
        if (value instanceof Integer) {
            return new HeaderCard(keyword, (Integer) value, comment);
        } else if (value instanceof Long) {
            return new HeaderCard(keyword, (Long) value, comment);
        } else if (value instanceof Double) {
            return new HeaderCard(keyword, (Double) value, comment);
        } else if (value instanceof Boolean) {
            return new HeaderCard(keyword, (Boolean) value, comment);
        } else if (value instanceof String) {
            return new HeaderCard(keyword, (String) value, comment);
        } else if (value instanceof BigInteger) {
            return new HeaderCard(keyword, (BigInteger) value, comment);
        } else if (value instanceof BigDecimal) {
            return new HeaderCard(keyword, (BigDecimal) value, comment);
        } else {
            return null;
        }
    }

    private void writeChangesBackToFile(Fits fitsObject, File fitsFile)
            throws IOException, FitsException {
        BufferedFile bf = new BufferedFile(fitsFile, FILE_OPENING_MODE);
        fitsObject.write(bf);
    }

    private boolean indexInRangeOfHeaderSize(int index, Header header) {
        return index <= header.getNumberOfCards() - 1;
    }
}
