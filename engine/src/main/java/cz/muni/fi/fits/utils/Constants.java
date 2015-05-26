package cz.muni.fi.fits.utils;

/**
 * Static class contaning useful constants related to FITS files
 *
 * @author Martin Vrábel
 * @version 1.4
 */
public final class Constants {

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
    public static final double MIN_RA_VALUE = 0;

    /**
     * Maximum allowed value for Right Ascension
     */
    public static final double MAX_RA_VALUE = 360;

    /**
     * Minimum allowed value for Declination
     */
    public static final double MIN_DEC_VALUE = -90;

    /**
     * Maximum allowed value for Declination
     */
    public static final double MAX_DEC_VALUE = 90;
}
