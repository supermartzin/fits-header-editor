package cz.muni.fi.fits.common.utils;

/**
 * Static class contaning useful constants related to FITS files
 *
 * @author Martin Vrábel
 * @version 1.6
 */
public final class Constants {

    /**
     * Current version of this application
     */
    public static final String APP_VERSION = "1.0.1-RELEASE";


    /**
     * Maximum length of header record's keyword string
     */
    public static final int MAX_KEYWORD_LENGTH = 8;

    /**
     * Maximum length of comment string in header record
     */
    public static final int MAX_COMMENT_LENGTH = 47;

    /**
     * Maximum length of string value in single header record
     */
    public static final int MAX_STRING_VALUE_LENGTH = 68;

    /**
     * Maximum length of string value and comment in single header record
     */
    public static final int MAX_STRING_VALUE_COMMENT_LENGTH = 65;

    /**
     * Regular expression for keyword of header record
     */
    public static final String KEYWORD_REGEX = "[A-Z0-9_-]+";

    /**
     * Default keyword for Heliocentric Julian Date header record
     */
    public static final String DEFAULT_HJD_KEYWORD = "HJD";

    /**
     * Default comment for Heliocentric Julian Date header record
     */
    public static final String DEFAULT_HJD_COMMENT = "center of exposure";

    /**
     * Default keyword for Julian Date header record
     */
    public static final String DEFAULT_JD_KEYWORD = "JD";

    /**
     * Default comment for Julian Date header record
     */
    public static final String DEFAULT_JD_COMMENT = "center of exposure";

    /**
     * Default keyword for Right Ascension header record
     */
    public static final String DEFAULT_RA_KEYWORD = "RA";

    /**
     * Default comment for Right Ascension header record
     */
    public static final String DEFAULT_RA_COMMENT = "ra";

    /**
     * Default keyword for Declination header record
     */
    public static final String DEFAULT_DEC_KEYWORD = "DEC";

    /**
     * Default comment for Declination header record
     */
    public static final String DEFAULT_DEC_COMMENT = "dec";

    /**
     * Minimum allowed value for Right Ascension
     */
    public static final Double MIN_RA_VALUE = 0.0;

    /**
     * Maximum allowed value for Right Ascension
     */
    public static final Double MAX_RA_VALUE = 360.0;

    /**
     * Minimum allowed value for Declination
     */
    public static final Double MIN_DEC_VALUE = -90.0;

    /**
     * Maximum allowed value for Declination
     */
    public static final Double MAX_DEC_VALUE = 90.0;

    /**
     * Minumum number of hours for time value of Right Ascension
     */
    public static final Double RA_MIN_HOURS = 0.0;

    /**
     * Maximum number of hours for time value of Right Ascension
     */
    public static final Double RA_MAX_HOURS = 24.0;

    /**
     * Minumum number of minutes for time value of Right Ascension
     */
    public static final Double RA_MIN_MINUTES = 0.0;

    /**
     * Maximum number of minutes for time value of Right Ascension
     */
    public static final Double RA_MAX_MINUTES = 60.0;

    /**
     * Minumum number of seconds for time value of Right Ascension
     */
    public static final Double RA_MIN_SECONDS = 0.0;

    /**
     * Maximum number of seconds for time value of Right Ascension
     */
    public static final Double RA_MAX_SECONDS = 60.0;

    /**
     * Minumum number of degrees for degrees value of Declination
     */
    public static final Double DEC_MIN_DEGREES = -90.0;

    /**
     * Maximum number of degrees for degrees value of Declination
     */
    public static final Double DEC_MAX_DEGREES = 90.0;

    /**
     * Minumum number of minutes for degrees value of Declination
     */
    public static final Double DEC_MIN_MINUTES = 0.0;

    /**
     * Maximum number of minutes for degrees value of Declination
     */
    public static final Double DEC_MAX_MINUTES = 60.0;

    /**
     * Minumum number of seconds for degrees value of Declination
     */
    public static final Double DEC_MIN_SECONDS = 0.0;

    /**
     * Maximum number of seconds for degrees value of Declination
     */
    public static final Double DEC_MAX_SECONDS = 60.0;


    private Constants() { }
}
