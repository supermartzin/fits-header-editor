package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.MainApp;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class Controller implements Initializable {

    protected ResourceBundle _resources;
    protected MainApp _mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set resources
        if (resources != null)
            _resources = resources;

        init();
    }

    /**
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        if (mainApp != null)
            _mainApp = mainApp;
    }

    /**
     *
     */
    public abstract void init();
}
