package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.view.controllers.Controller;
import javafx.scene.control.Tab;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class OperationTabController extends Controller implements TabController {

    protected String _tabName;

    private boolean _selected;

    @Override
    public void setTab(final Tab tab) {
        if (tab != null) {
            // add listener for Tab selection
            tab.selectedProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        _selected = newValue;
                    });
        }
    }

    @Override
    public boolean selected() {
        return _selected;
    }

    @Override
    public String getTabName() {
        return _tabName;
    }
}
