package cz.muni.fi.files.utils;

/**
 * Interface for listener of thread completeness
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface ThreadCompleteListener {

    /**
     * Method is supposed to be called when thread with this listener registered is ending
     *
     * @param runnable  thread when this listener is registered
     */
    void notifyWhenThreadComplete(final Runnable runnable);
}
