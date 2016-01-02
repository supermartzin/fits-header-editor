package cz.muni.fi.fits.common.utils;

/**
 * Utility class for various operation with strings
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class StringUtils {

    /**
     * Extracts class name of provided {@link Throwable} object without
     * package information
     *
     * @param exception {@link Throwable} object from which to extract a name
     * @return          {@link String} value containing the class name of exception
     *                  or <b>empty</b> {@link String} if provided exception is <code>null</code>
     */
    public static String getExceptionType(Throwable exception) {
        if (exception == null)
            return "";

        String type = exception.getClass().getTypeName();

        int lastIndex = type.lastIndexOf('.');
        if (lastIndex > -1)
            return type.substring(lastIndex + 1);
        else
            return type;
    }


    public StringUtils() { }
}
