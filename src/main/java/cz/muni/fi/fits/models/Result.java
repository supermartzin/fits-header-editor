package cz.muni.fi.fits.models;

/**
 * Class representing result of some operation
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class Result {

    private final boolean _success;
    private final String _message;

    /**
     * Creates new {@link Result} object with success <code>status</code>
     * and specified <code>message</code>
     *
     * @param success   indicates if operation ended successfully or not
     * @param message   specified message describing result of operation
     */
    public Result(boolean success, String message) {
        this._success = success;
        this._message = message;
    }

    public boolean isSuccess() {
        return _success;
    }

    public String getMessage() {
        return _message;
    }
}
