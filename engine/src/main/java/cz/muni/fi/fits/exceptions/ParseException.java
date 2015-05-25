package cz.muni.fi.fits.exceptions;

/**
 * Exception class used in cases when
 * some specific value type cannot be parsed
 * from another value,
 * extends {@link RuntimeException} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class ParseException extends RuntimeException {

    /**
     * Creates new instance of {@link ParseException} class
     */
    public ParseException() {
        super();
    }

    /**
     * Creates new instance of {@link ParseException} class
     *
     * @param message   custom error message
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Creates new instance of {@link ParseException} class
     *
     * @param message   custom error message
     * @param cause     cause of this exception
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates new instance of {@link ParseException} class
     *
     * @param cause cause of this exception
     */
    public ParseException(Throwable cause) {
        super(cause);
    }
}
