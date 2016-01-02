package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.models.inputdata.InputData;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

/**
 * TODO description
 */
public interface TabController extends Initializable {

    /**
     *
     * @param tab
     */
    void setTab(final Tab tab);

    /**
     *
     * @return
     */
    boolean selected();

    /**
     *
     * @return
     */
    InputData getInputData();

    /**
     *
     * @return
     */
    String getTabName();
}
