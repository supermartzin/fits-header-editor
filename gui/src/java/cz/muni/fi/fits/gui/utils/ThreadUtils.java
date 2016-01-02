package cz.muni.fi.fits.gui.utils;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class ThreadUtils {

    /**
     *
     * @param runnable
     */
    public static void executeInBackground(Runnable runnable) {
        if (runnable == null)
            throw new IllegalArgumentException("Provided runnable cannot be null");

        new Thread(runnable).start();
    }


    private ThreadUtils() { }
}
