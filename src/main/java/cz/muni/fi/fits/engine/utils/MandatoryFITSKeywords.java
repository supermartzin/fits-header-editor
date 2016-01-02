package cz.muni.fi.fits.engine.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for working with mandatory FITS header keywords
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class MandatoryFITSKeywords {

    private static final Set<String> MANDATORY_KEYWORDS_REGEX = new HashSet<>();

    static {
        MANDATORY_KEYWORDS_REGEX.add("^NAXIS[0-9]{0,3}$");
        MANDATORY_KEYWORDS_REGEX.add("^SIMPLE$");
        MANDATORY_KEYWORDS_REGEX.add("^BITPIX$");
        MANDATORY_KEYWORDS_REGEX.add("^EXTEND$");
        MANDATORY_KEYWORDS_REGEX.add("^XTENSION$");
    }

    /**
     * Checks whether provided <code>keyword</code> does match
     * one of the mandatory keywords regex
     *
     * @param keyword   {@link String} value of keyword to check
     * @return          <code>true</code> when <code>keyword</code> matches some mandatory keyword regex,
     *                  <code>false</code> otherwise
     */
    public static boolean matchesMandatoryKeyword(String keyword) {
        if (keyword == null)
            return false;

        for (String keywordsRegex : MANDATORY_KEYWORDS_REGEX) {
            if (keyword.matches(keywordsRegex))
                return true;
        }

        return false;
    }
}
