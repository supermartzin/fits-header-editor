package cz.muni.fi.fits.exceptions;

/**
 * Exception class used when input data are
 * either not in correct format or contains illegal data
 * or there is some
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class IllegalInputDataException extends Exception {

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     */
    public IllegalInputDataException() {
        super();
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param message   custom error message
     */
    public IllegalInputDataException(String message) {
        super(message);
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param message   custom error message
     * @param cause     cause of this exception
     */
    public IllegalInputDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param cause cause of this exception
     */
    public IllegalInputDataException(Throwable cause) {
        super(cause);
    }
}
