package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.MainApp;
import cz.muni.fi.fits.gui.models.Language;
import cz.muni.fi.fits.gui.services.PreferencesService;
import cz.muni.fi.fits.gui.services.ResourceBundleService;
import cz.muni.fi.fits.gui.utils.dialogs.ExceptionDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * TODO insert description
 */
public class RootLayoutController extends Controller {

    public ToggleGroup languages;
    public Menu langMenu;

    private boolean _firstToggleEvent = true;

    @Override
    public void init() {
        // add listener for language changed
        languages.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !_firstToggleEvent) {
                RadioMenuItem langItem = (RadioMenuItem) newValue;
                // get new language
                Language newLanguage = Language.getLanguageByCode(langItem.getId());
                // save new language
                PreferencesService.saveLanguage(newLanguage, _mainApp.getClass());
                // alert user about restart
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(_resources.getString("restart.alert.title"));
                alert.setHeaderText(_resources.getString("restart.alert.header"));
                alert.setContentText(_resources.getString("restart.alert.text.lang"));
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(_mainApp.getPrimaryStage());
                alert.showAndWait();
            }

            if (_firstToggleEvent)
                _firstToggleEvent = false;
        });

        // set selected language menu item
        Language language = PreferencesService.getLanguage(MainApp.class);
        selectLanguageItem(language);
    }

    public void onPreferences() {
        try {
            FXMLLoader userPreferencesFile = new FXMLLoader(MainApp.class.getResource("view/UserPreferences.fxml"));
            ResourceBundleService.setResourceBundle(userPreferencesFile);
            AnchorPane anchorPane = userPreferencesFile.load();

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(_resources.getString("prefs"));
            stage.getIcons().add(_mainApp.getApplicationIcon());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(_mainApp.getPrimaryStage());

            stage.setScene(new Scene(anchorPane));

            UserPreferencesController controller = userPreferencesFile.getController();
            controller.setOwner(stage);
            controller.setMainApp(_mainApp);

            stage.showAndWait();
        } catch (IOException ioEx) {
            ExceptionDialog.show(
                    _resources.getString("app.error.dialog.title"),
                    _resources.getString("app.error.dialog.header"),
                    _resources.getString("app.error.dialog.content.preferences"),
                    ioEx,
                    _mainApp);
        }

    }

    public void onExit() {
        System.exit(0);
    }

    public void onAbout() {
        try {
            FXMLLoader aboutDialogFile = new FXMLLoader(MainApp.class.getResource("view/AboutDialog.fxml"));
            ResourceBundleService.setResourceBundle(aboutDialogFile);
            AnchorPane anchorPane = aboutDialogFile.load();

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(_resources.getString("app.about"));
            stage.getIcons().add(_mainApp.getApplicationIcon());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(_mainApp.getPrimaryStage());
            stage.setScene(new Scene(anchorPane));

            AboutDialogController controller = aboutDialogFile.getController();
            controller.setMainApp(_mainApp);

            stage.showAndWait();
        } catch (IOException ioEx) {
            ExceptionDialog.show(
                    _resources.getString("app.error.dialog.title"),
                    _resources.getString("app.error.dialog.header"),
                    _resources.getString("app.error.dialog.content.about"),
                    ioEx,
                    _mainApp);
        }
    }

    private void selectLanguageItem(Language language) {
        for (MenuItem menuItem : langMenu.getItems()) {
            RadioMenuItem item = (RadioMenuItem) menuItem;
            if (item.getId().equals(language.getCode())) {
                item.setSelected(true);
                return;
            }
        }
    }
}
