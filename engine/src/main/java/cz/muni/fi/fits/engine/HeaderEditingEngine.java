package cz.muni.fi.fits.engine;

import cz.muni.fi.fits.engine.models.Result;
import cz.muni.fi.fits.models.ChainValueType;
import cz.muni.fi.fits.common.utils.Tuple;

import java.io.File;
import java.util.List;

/**
 * Interface defining methods of editing engine that can be performed
 * over FITS files
 *
 * @author Martin Vrábel
 * @version 1.3.5
 */
public interface HeaderEditingEngine {

    /**
     * Adds new record to FITS header with specified arguments
     *
     * @param keyword           keyword of new record to add
     * @param value             value of new record to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> when no comment to add to record
     * @param updateIfExists    value indicating whether to update a record
     *                          if one with the same keyword already exists
     * @param fitsFile          FITS file to which add new record
     * @return                  {@link Result} object with results of this operation
     */
    Result addNewRecord(String keyword, Object value, String comment, boolean updateIfExists, File fitsFile);

    /**
     * Adds new record to FITS header with specified arguments to specific <code>index</code>
     *
     * @param index             index where to add new record
     * @param keyword           keyword of new record to add
     * @param value             value of new keyword to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> when no comment to add to record
     * @param removeOldIfExists value indicating whether to remove old record
     *                          with the same keyword if it already exists
     * @param fitsFile          FITS file to which add new record
     * @return                  {@link Result} object with results of this operation
     */
    Result addNewRecordToIndex(int index, String keyword, Object value, String comment, boolean removeOldIfExists, File fitsFile);

    /**
     * Removes record from FITS header with specified <code>keyword</code>
     *
     * @param keyword   keyword of a record to remove
     * @param fitsFile  FITS file from which to remove a record
     * @return          {@link Result} object with results of this operation
     */
    Result removeRecordByKeyword(String keyword, File fitsFile);

    /**
     * Removes record from FITS header from specified <code>index</code>
     *
     * @param index     index from which to remove a record
     * @param fitsFile  FITS file from which to remove a record
     * @return          {@link Result} object with results of this operation
     */
    Result removeRecordFromIndex(int index, File fitsFile);

    /**
     * Changes keyword of specified existing record in FITS header to new one
     *
     * @param oldKeyword                keyword defining existing record in which to change keyword
     * @param newKeyword                new keyword to set in record
     * @param removeValueOfNewIfExists  value indicating whether to remove record with new keyword
     *                                  if it already exists in header
     * @param fitsFile                  FITS file in which to change a record
     * @return                          {@link Result} object with results of this operation
     */
    Result changeKeywordOfRecord(String oldKeyword, String newKeyword, boolean removeValueOfNewIfExists, File fitsFile);

    /**
     * Change value of specified existing record in FITS header to new one
     *
     * @param keyword           keyword defining existing record in which to change value
     * @param newValue          new value to be set in record
     * @param newComment        new comment to set in record, insert <code>null</code>
     *                          if want to use the original comment
     * @param addNewIfNotExists value indicating whether add new record if
     *                          record with specified keyword does not exist
     * @param fitsFile          FITS file in which to change a record
     * @return                  {@link Result} object with results of this operation
     */
    Result changeValueOfRecord(String keyword, Object newValue, String newComment, boolean addNewIfNotExists, File fitsFile);

    /**
     * Chain multiple records into new single record in FITS header by specified arguments
     *
     * @param keyword                   keyword of new chained record
     * @param chainParameters           list of parameters containing constant and keyword
     *                                  tuples of {@link ChainValueType} and
     *                                  {@link String} which to chain
     * @param comment                   comment to set in record, insert <code>null</code>
     *                                  if no comment to add
     * @param updateIfExists            value indicating whether to update value of record
     *                                  with specified keyword if it already exists
     * @param allowLongstrings          value indicating whether allow longstring values in header
     *                                  if chained value is longer than basic limit
     * @param fitsFile                  FITS file in which to chain records
     * @return                          {@link Result} object with results of this operation
     */
    Result chainMultipleRecords(String keyword, List<Tuple<ChainValueType, String>> chainParameters, String comment,
                                boolean updateIfExists, boolean allowLongstrings, File fitsFile);

    /**
     * Shifts time of time record with <code>keyword</code> by amount of time specified by arguments
     *
     * @param keyword           keyword of time record in which to shift time
     * @param yearShift         time shift for years
     * @param monthShift        time shift for months
     * @param dayShift          time shift for days
     * @param hourShift         time shift for hours
     * @param minuteShift       time shift for minutes
     * @param secondShift       time shift for seconds
     * @param nanosecondShift   time shift for nanoseconds
     * @param fitsFile          FITS file in which to chain records
     * @return                  {@link Result} object with results of this operation
     */
    Result shiftTimeOfTimeRecord(String keyword, int yearShift, int monthShift, int dayShift,
                                 int hourShift, int minuteShift, int secondShift, int nanosecondShift, File fitsFile);

    /**
     * Computes Julian Date from provided datetime and exposure parameters
     * and saves/updates value to <code>JD</code> keyword to FITS file header
     *
     * @param datetime  {@link String} value as keyword of datetime record
     *                  or {@link java.time.LocalDateTime} as value of datetime
     * @param exposure  {@link String} value as keyword of exposure record
     *                  or {@link Double} as value of exposure in seconds
     * @param comment   comment of JD record, insert
     *                  <code>null</code> when no comment to add
     * @param fitsFile  FITS file in which to chain records
     * @return          {@link Result} object with results of this operation
     */
    Result computeJulianDate(Object datetime, Object exposure, String comment, File fitsFile);

    /**
     * Computes Heliocentric Julian Date, saves/updates the value to <code>HJD</code> keyword,
     * saves right ascension to <code>RA</code> keyword and declination do <code>DEC</code>
     * keyword to FITS file header
     *
     * @param datetime              {@link String} value as keyword of datetime record
     *                              or {@link java.time.LocalDateTime} as value of datetime
     * @param exposure              {@link String} value as keyword of exposure record
     *                              or {@link Double} as value of exposure in seconds
     * @param rightAscension        {@link String} value as keyword of right ascension record,
     *                              {@link cz.muni.fi.fits.models.TimeObject} as right ascension value parameters
     *                              or {@link Double} or {@link java.math.BigDecimal} value of right ascension
     * @param declination           {@link String} value as keyword of declination record,
     *                              {@link cz.muni.fi.fits.models.DegreesObject} as declination value parameters
     *                              or {@link Double} or {@link java.math.BigDecimal} as value of declination
     * @param comment               comment of HJD record, insert
     *                              <code>null</code> when no comment to add
     * @param fitsFile              FITS file in which to chain records
     * @return                      {@link Result} object with results of this operation
     */
    Result computeHeliocentricJulianDate(Object datetime, Object exposure, Object rightAscension, Object declination, String comment, File fitsFile);
}
