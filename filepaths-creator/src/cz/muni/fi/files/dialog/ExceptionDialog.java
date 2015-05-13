package cz.muni.fi.files.dialog;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception dialog class - extension of classic {@link Alert} class
 * with {@link javafx.scene.control.Alert.AlertType} set to <code>ERROR</code>
 * used for showing details of exception in Alert dialog
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class ExceptionDialog extends Alert {

    /**
     * Creates new instance of basic {@link ExceptionDialog}
     */
    public ExceptionDialog() {
        super(AlertType.ERROR);
    }

    /**
     * Creates new instance of {@link ExceptionDialog} with specified <code>contentText</code>
     * and <code>buttonTypes</code>
     *
     * @param contentText   content text of dialog
     * @param buttons       buttons of this dialog
     */
    public ExceptionDialog(String contentText, ButtonType... buttons) {
        super(AlertType.ERROR, contentText, buttons);
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

            Label label = new Label("Výpis trasovania výnimky:");

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
                    detailsButton.setText(newValue ? "Menej detailov" : "Viac detailov");
                });

                this.getDialogPane().setExpanded(true);
                this.getDialogPane().setExpanded(false);
            });
        }
    }
}
