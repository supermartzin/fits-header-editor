package cz.muni.fi.fits.common.exceptions;

/**
 * Exception class used when error occurs during configuration phase
 * either with data or loading process
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class ConfigurationException extends Exception {

    /**
     * Creates new instance of {@link ConfigurationException} class
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Creates new instance of {@link ConfigurationException} class
     *
     * @param message   custom error message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates new instance of {@link ConfigurationException} class
     *
     * @param message   custom error message
     * @param cause     cause of this exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates new instance of {@link ConfigurationException} class
     *
     * @param cause cause of this exception
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
