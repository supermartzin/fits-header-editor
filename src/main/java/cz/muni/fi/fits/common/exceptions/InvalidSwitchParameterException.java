package cz.muni.fi.fits.common.exceptions;

/**
 * Exception class used for cases when switch parameter
 * in input data is incorrect or is in incorrect format,
 * extends {@link IllegalInputDataException} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class InvalidSwitchParameterException extends IllegalInputDataException {

    private final String _invalidSwitch;

    /**
     * Gets invalid switch that caused this exception
     *
     * @return invalid switch as {@link String} value
     */
    public String getInvalidSwitch() {
        return _invalidSwitch;
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     */
    public InvalidSwitchParameterException(String invalidSwitch) {
        super();
        _invalidSwitch = invalidSwitch;
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param invalidSwitch specific invalid switch
     * @param message       custom error mesage
     */
    public InvalidSwitchParameterException(String invalidSwitch, String message) {
        super(message);
        _invalidSwitch = invalidSwitch;
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param invalidSwitch specific invalid switch
     * @param message       custom error mesage
     * @param cause         cause of this exception
     */
    public InvalidSwitchParameterException(String invalidSwitch, String message, Throwable cause) {
        super(message, cause);
        _invalidSwitch = invalidSwitch;
    }

    /**
     * Creates new instance of {@link IllegalInputDataException} class
     *
     * @param invalidSwitch specific invalid switch
     * @param cause         cause of this exception
     */
    public InvalidSwitchParameterException(String invalidSwitch, Throwable cause) {
        super(cause);
        _invalidSwitch = invalidSwitch;
    }
}
