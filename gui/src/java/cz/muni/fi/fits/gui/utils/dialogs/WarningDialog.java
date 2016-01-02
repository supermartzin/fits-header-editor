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
public final class WarningDialog {

    /**
     *
     * @param title
     * @param header
     * @param content
     * @param mainApp
     */
    public static void show(String title, String header, String content, MainApp mainApp) {
        Alert warningDialog = new Alert(Alert.AlertType.WARNING);
        warningDialog.setTitle(title);
        warningDialog.setHeaderText(header);
        warningDialog.setContentText(content);

        if (mainApp != null) {
            warningDialog.initOwner(mainApp.getPrimaryStage());

            // set icon
            Stage stage = (Stage) warningDialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(mainApp.getApplicationIcon());
        }

        warningDialog.initModality(Modality.APPLICATION_MODAL);

        warningDialog.showAndWait();
    }
}
