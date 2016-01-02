package cz.muni.fi.fits.gui.utils.dialogs;

import cz.muni.fi.fits.gui.MainApp;
import cz.muni.fi.fits.gui.services.ResourceBundleService;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class ExceptionDialog {

    /**
     * @param title
     * @param header
     * @param content
     * @param exception
     * @param mainApp
     */
    public static void show(String title, String header, String content, Throwable exception, MainApp mainApp) {
        Dialog exceptionDialog = new Dialog(ResourceBundleService.getBundle());
        exceptionDialog.setTitle(title);
        exceptionDialog.setHeaderText(header);
        exceptionDialog.setContentText(content);
        exceptionDialog.setException(exception);

        if (mainApp != null) {
            exceptionDialog.initOwner(mainApp.getPrimaryStage());

            // set icon
            Stage stage = (Stage) exceptionDialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(mainApp.getApplicationIcon());
        }

        exceptionDialog.initModality(Modality.APPLICATION_MODAL);

        exceptionDialog.showAndWait();
    }

    /**
     * Exception dialog class - extension of classic {@link Alert} class
     * with {@link javafx.scene.control.Alert.AlertType} set to <code>ERROR</code>
     * used for showing details of exception in Alert dialog
     *
     * @author Martin Vrábel
     * @version 1.1
     */
    static class Dialog extends Alert {

        private final ResourceBundle _resources;

        /**
         * Creates new instance of basic {@link Dialog} class
         *
         * @param resources {@link ResourceBundle} obect for internationalization
         */
        public Dialog(ResourceBundle resources) {
            super(AlertType.ERROR);

            _resources = resources;
        }

        /**
         * Creates new instance of {@link Dialog} class with specified <code>contentText</code>
         * and <code>buttonTypes</code>
         *
         * @param contentText   content text of dialog
         * @param resources     {@link ResourceBundle} obect for internationalization
         * @param buttons       buttons of this dialog
         */
        public Dialog(String contentText, ResourceBundle resources, ButtonType... buttons) {
            super(AlertType.ERROR, contentText, buttons);

            _resources = resources;
        }

        /**
         * Sets instance of {@link Throwable} that will be displayed in this dialog
         *
         * @param exception {@link Throwable} instance of exception to display
         */
        public void setException(Throwable exception) {
            if (exception != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label(_resources.getString("dialog.exception.trace"));

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                this.getDialogPane().setExpandableContent(expContent);

                Platform.runLater(() -> {
                    Hyperlink detailsButton = (Hyperlink) this.getDialogPane().lookup(".details-button");
                    this.getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) -> {
                        detailsButton.setText(newValue
                                ? _resources.getString("dialog.exception.button.details.hide")
                                : _resources.getString("dialog.exception.button.details.show"));
                    });

                    this.getDialogPane().setExpanded(true);
                    this.getDialogPane().setExpanded(false);
                });
            }
        }
    }
}
