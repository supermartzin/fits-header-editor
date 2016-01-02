package cz.muni.fi.fits.gui;

import cz.muni.fi.fits.gui.exceptions.ExternalEditorException;
import cz.muni.fi.fits.gui.listeners.OutputListener;

import java.util.Collection;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface ExternalProcess {

    /**
     *
     * @param arguments
     * @throws ExternalEditorException
     */
    void run(Collection<String> arguments) throws ExternalEditorException;

    /**
     *
     * @param outputListener
     */
    void addOutputListener(OutputListener outputListener);
}
