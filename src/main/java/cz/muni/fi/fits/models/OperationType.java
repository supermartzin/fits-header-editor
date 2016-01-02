package cz.muni.fi.fits.models;

/**
 * Enumeration of main editing operations
 *
 * @author Martin Vrábel
 * @version 1.2.1
 */
public enum OperationType {
    /**
     * Operation of adding new header record to the end of header
     */
    ADD_NEW_RECORD_TO_END,

    /**
     * Operation of adding new heade record to specified index in header
     */
    ADD_NEW_RECORD_TO_INDEX,

    /**
     * Operation of removing existing record from header by specific keyword
     */
    REMOVE_RECORD_BY_KEYWORD,

    /**
     * Operation of removing existing record from specified index in header
     */
    REMOVE_RECORD_FROM_INDEX,

    /**
     * Operation of changing existing header record's keyword to new keyword
     */
    CHANGE_KEYWORD,

    /**
     * Operation of changing existing header record's value (by specified keyword) to new value
     */
    CHANGE_VALUE_BY_KEYWORD,

    /**
     * Operation of chaining multiple existing header records to new single header record
     */
    CHAIN_RECORDS,

    /**
     * Operation of shifting time of time header record by specified shift
     */
    SHIFT_TIME,

    /**
     * Operation of computing and saving Julian date to header
     */
    COMPUTE_JD,

    /**
     * Operation of computing and saving Heliocentric Julian date to header
     */
    COMPUTE_HJD
}
