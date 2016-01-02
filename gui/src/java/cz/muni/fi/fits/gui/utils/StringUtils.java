package cz.muni.fi.fits.gui.utils;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class StringUtils {

    /**
     *
     * @param stringToWrap
     * @param wrappingSymbol
     * @return
     */
    public static String wrapIfContainsWhitespace(String stringToWrap, String wrappingSymbol) {
        if (stringToWrap != null && stringToWrap.contains(" ")) {
            if (wrappingSymbol != null) {
                // return wrapped String
                return wrappingSymbol + stringToWrap + wrappingSymbol;
            }
        }

        // return the original String
        return stringToWrap;
    }
}
