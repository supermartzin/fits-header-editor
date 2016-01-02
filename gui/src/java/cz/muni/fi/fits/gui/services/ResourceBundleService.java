package cz.muni.fi.fits.gui.services;

import cz.muni.fi.fits.gui.MainApp;
import cz.muni.fi.fits.gui.models.Language;
import cz.muni.fi.fits.gui.utils.UTF8Control;
import javafx.fxml.FXMLLoader;

import java.util.ResourceBundle;

/**
 * TODO insert description
 */
public class ResourceBundleService {

    private static final Language LANGUAGE = PreferencesService.getLanguage(MainApp.class);

    public static void setResourceBundle(FXMLLoader fxmlFile) {
        fxmlFile.setResources(ResourceBundle.getBundle("bundles.Language", LANGUAGE.getLocale(), new UTF8Control()));
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("bundles.Language", LANGUAGE.getLocale(), new UTF8Control());
    }
}
