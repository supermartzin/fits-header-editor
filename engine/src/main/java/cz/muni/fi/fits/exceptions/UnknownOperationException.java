package cz.muni.fi.fits.exceptions;

/**
 * Exception class used for case when operation
 * cannot be recognized from input data,
 * extends {@link IllegalInputDataException} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class UnknownOperationException extends IllegalInputDataException {

    private final String _operation;

    /**
     * Gets unrecognized operation
     *
     * @return  unrecognized operation as {@link String}
     */
    public String getOperation() {
        return _operation;
    }

    /**
     * Creates new instance of {@link UnknownOperationException} class
     *
     * @param operation specified unrecognized operation
     */
    public UnknownOperationException(String operation) {
        super();
        this._operation = operation;
    }

    /**
     * Creates new instance of {@link UnknownOperationException} class
     *
     * @param operation specified unrecognized operation
     * @param message   custom error message
     */
    public UnknownOperationException(String operation, String message) {
        super(message);
        this._operation = operation;
    }

    /**
     * Creates new instance of {@link UnknownOperationException} class
     *
     * @param operation specified unrecognized operation
     * @param message   custom error message
     * @param cause     cause of this exception
     */
    public UnknownOperationException(String operation, String message, Throwable cause) {
        super(message, cause);
        this._operation = operation;
    }

    /**
     * Creates new instance of {@link UnknownOperationException} class
     *
     * @param operation specified unrecognized operation
     * @param cause     cause of this exception
     */
    public UnknownOperationException(String operation, Throwable cause) {
        super(cause);
        this._operation = operation;
    }
}
