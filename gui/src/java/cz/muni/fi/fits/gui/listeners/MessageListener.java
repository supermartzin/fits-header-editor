package cz.muni.fi.fits.gui.listeners;

/**
 * TODO insert description
 *
 * @author Martin Vrábel - © 2015 FITS-HeaderVisualEditor
 * @version 1.0
 */
public interface MessageListener {

    /**
     *
     * @param infoMessage
     */
    void onInfo(String infoMessage);

    /**
     *
     * @param errorMessage
     */
    void onError(String errorMessage);

    /**
     *
     * @param exceptionMessage
     */
    void onException(String exceptionMessage);
}
