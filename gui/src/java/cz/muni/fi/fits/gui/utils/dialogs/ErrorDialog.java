package cz.muni.fi.fits.gui.utils.dialogs;

import cz.muni.fi.fits.gui.MainApp;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class ErrorDialog {

    /**
     *
     * @param title
     * @param header
     * @param content
     * @param mainApp
     */
    public static void show(String title, String header, String content, MainApp mainApp) {
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle(title);
        errorDialog.setHeaderText(header);
        errorDialog.setContentText(content);

        if (mainApp != null) {
            errorDialog.initOwner(mainApp.getPrimaryStage());

            // set icon
            Stage stage = (Stage) errorDialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(mainApp.getApplicationIcon());
        }

        errorDialog.initModality(Modality.APPLICATION_MODAL);

        errorDialog.showAndWait();
    }
}
