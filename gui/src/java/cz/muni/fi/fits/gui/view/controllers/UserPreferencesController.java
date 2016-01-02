package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.models.Preferences;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class UserPreferencesController extends Controller {

    private static final Preferences PREFERENCES = new Preferences();

    private Stage _ownerStage;

    public TextField enginePathField;

    public CheckBox saveOutputToFileSwitch;
    public Label outputFileLocationLabel;
    public TextField outputFileLocationField;
    public Button outputFileChooseLocationField;

    @Override
    public void init() {
        saveOutputToFileSwitch.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        outputFileLocationLabel.setDisable(false);
                        outputFileLocationField.setDisable(false);
                        outputFileChooseLocationField.setDisable(false);
                    } else {
                        outputFileLocationLabel.setDisable(true);
                        outputFileLocationField.setDisable(true);
                        outputFileChooseLocationField.setDisable(true);
                    }
                });

        loadPreferences();
    }

    public void setOwner(Stage ownerStage) {
        _ownerStage = ownerStage;
    }

    public void onChooseEngineFile() {
        // set extension filter for .jar file
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(_resources.getString("extension.file.jar"), "*.jar");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(extensionFilter);
        File jarFile = fileChooser.showOpenDialog(_ownerStage);
        if (jarFile != null) {
            enginePathField.setText(jarFile.getAbsolutePath());
        }
    }

    public void onChooseOutputFileLocation() {
        // set extension filter for .jar file
        FileChooser.ExtensionFilter txtExtension = new FileChooser.ExtensionFilter(_resources.getString("extension.file.txt"), "*.txt");
        FileChooser.ExtensionFilter outExtension = new FileChooser.ExtensionFilter(_resources.getString("extension.file.out"), "*.out");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(txtExtension, outExtension);
        File outputFile = fileChooser.showSaveDialog(_ownerStage);
        if (outputFile != null) {
            String outputFilepath = outputFile.getPath();

            // edit filename if bad extension has been written
            if (!outputFilepath.endsWith(".txt")
                    && !outputFilepath.endsWith(".out")) {
                FileChooser.ExtensionFilter selectedExtensionFilter = fileChooser.getSelectedExtensionFilter();
                outputFilepath += selectedExtensionFilter.getExtensions().get(0).substring(1);
            }

            outputFileLocationField.setText(outputFilepath);
        }
    }

    public void onOK() {
        // save engine filepath
        PREFERENCES.setEngineFilepath(enginePathField.getText());
        // save output file writing switch
        PREFERENCES.setSaveOutputToFile(saveOutputToFileSwitch.isSelected());
        // save output file
        PREFERENCES.setOutputFilepath(outputFileLocationField.getText());

        _mainApp.setPreferences(PREFERENCES);

        closeStage();
    }

    public void onCancel() {
        closeStage();
    }

    private void loadPreferences() {
        enginePathField.setText(PREFERENCES.getEngineFilepath());

        boolean saveOutputToFile = PREFERENCES.saveOutputToFile();
        saveOutputToFileSwitch.setSelected(saveOutputToFile);

        outputFileLocationField.setText(PREFERENCES.getOutputFilepath());
    }

    private void closeStage() {
        Stage stage = (Stage) _ownerStage.getScene().getWindow();
        stage.close();
    }
}
