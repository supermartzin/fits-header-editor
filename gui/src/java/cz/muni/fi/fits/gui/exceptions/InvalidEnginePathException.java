package cz.muni.fi.fits.gui.exceptions;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class InvalidEnginePathException extends Exception {

    public InvalidEnginePathException() {
        super();
    }

    public InvalidEnginePathException(String message) {
        super(message);
    }

    public InvalidEnginePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEnginePathException(Throwable cause) {
        super(cause);
    }
}
