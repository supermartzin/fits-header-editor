package cz.muni.fi.fits.exceptions;

/**
 * Exception class used when input data are not in valid form
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class ValidationException extends Exception {

    /**
     * Creates new instance of {@link ValidationException} class
     */
    public ValidationException() {
        super();
    }

    /**
     * Creates new instance of {@link ValidationException} class
     *
     * @param message   custom error message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Creates new instance of {@link ValidationException} class
     *
     * @param message   custom error message
     * @param cause     cause of this exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates new instance of {@link ValidationException} class
     *
     * @param cause cause of this exception
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }
}
