package cz.muni.fi.fits.utils;

/**
 * Static class contaning useful constants related to FITS files
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public final class Constants {

    public static final int MAX_KEYWORD_LENGTH = 8;
    public static final int MAX_COMMENT_LENGTH = 47;
    public static final int MAX_STRING_VALUE_LENGTH = 68;
    public static final int MAX_STRING_VALUE_COMMENT_LENGTH = 65;
    public static final String KEYWORD_REGEX = "[A-Z0-9_-]+";
    public static final String DEFAULT_HJD_KEYWORD = "HJD";
    public static final String DEFAULT_HJD_COMMENT = "center of exposure";
    public static final String DEFAULT_JD_KEYWORD = "JD";
    public static final String DEFAULT_JD_COMMENT = "center of exposure";
    public static final String DEFAULT_RA_KEYWORD = "RA";
    public static final String DEFAULT_RA_COMMENT = "ra";
    public static final String DEFAULT_DEC_KEYWORD = "DEC";
    public static final String DEFAULT_DEC_COMMENT = "dec";
    public static final double MIN_RA_VALUE = 0;
    public static final double MAX_RA_VALUE = 360;
    public static final double MIN_DEC_VALUE = -90;
    public static final double MAX_DEC_VALUE = 90;
}
