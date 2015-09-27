package cz.muni.fi.fits.common.exceptions;

/**
 * Exception class used for cases when there was
 * wrong number of parameters entered in input data,
 * extends {@link IllegalInputDataException} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class WrongNumberOfParametersException extends IllegalInputDataException {

    private final int _numberOfParams;

    /**
     * Gets actual number of parameters that was cause for this exception
     *
     * @return  actual number of parameters
     */
    public int getNumberOfParameters() {
        return _numberOfParams;
    }

    /**
     * Creates new instance of {@link WrongNumberOfParametersException} class
     *
     * @param numberOfParams    actual number of entered parameters
     */
    public WrongNumberOfParametersException(int numberOfParams) {
        super();
        this._numberOfParams = numberOfParams;
    }

    /**
     * Creates new instance of {@link WrongNumberOfParametersException} class
     *
     * @param numberOfParams    actual number of entered parameters
     * @param message           custom error message
     */
    public WrongNumberOfParametersException(int numberOfParams, String message) {
        super(message);
        this._numberOfParams = numberOfParams;
    }

    /**
     * Creates new instance of {@link WrongNumberOfParametersException} class
     *
     * @param numberOfParams    actual number of entered parameters
     * @param message           custom error message
     * @param cause             cause of this exception
     */
    public WrongNumberOfParametersException(int numberOfParams, String message, Throwable cause) {
        super(message, cause);
        this._numberOfParams = numberOfParams;
    }

    /**
     * Creates new instance of {@link WrongNumberOfParametersException} class
     *
     * @param numberOfParams    actual number of entered parameters
     * @param cause             cause of this exception
     */
    public WrongNumberOfParametersException(int numberOfParams, Throwable cause) {
        super(cause);
        this._numberOfParams = numberOfParams;
    }
}
