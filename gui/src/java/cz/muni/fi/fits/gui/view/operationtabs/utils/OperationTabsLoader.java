package cz.muni.fi.fits.gui.view.operationtabs.utils;

import cz.muni.fi.fits.gui.MainApp;
import cz.muni.fi.fits.gui.services.ResourceBundleService;
import cz.muni.fi.fits.gui.view.controllers.OperationTabsViewController;
import cz.muni.fi.fits.gui.view.operationtabs.controllers.OperationTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class OperationTabsLoader {

    /**
     *
     * @param resourcePath
     * @param parentController
     * @return
     * @throws IOException
     */
    public static Tab loadTab(String resourcePath, OperationTabsViewController parentController, MainApp mainApp)
            throws IOException {
        FXMLLoader tabFile = new FXMLLoader(MainApp.class.getResource(resourcePath));
        ResourceBundleService.setResourceBundle(tabFile);
        ScrollPane scrollPane = tabFile.load();

        // load controller
        OperationTabController controller = tabFile.getController();

        // add to parent controller
        if (parentController != null)
            parentController.addTabController(controller);

        // create Tab
        Tab tab = new Tab(controller.getTabName(), scrollPane);

        // add Tab to its controller
        controller.setTab(tab);

        // set Main app
        controller.setMainApp(mainApp);

        return tab;
    }
}
