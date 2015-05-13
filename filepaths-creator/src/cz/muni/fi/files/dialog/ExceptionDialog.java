package cz.muni.fi.files.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
 * @version 1.0
 */
public class ExceptionDialog extends Alert {

    public ExceptionDialog() {
        super(AlertType.ERROR);
        this.setTitle("Neočakávaná chyba programu");
        this.setHeaderText("Nastala neočakávaná chyba programu");
    }

    public ExceptionDialog(String contentText, ButtonType... buttons) {
        super(AlertType.ERROR, contentText, buttons);
    }

    public void setException(Throwable exception) {
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

        // set content text
        this.setContentText("Program vyhodil neočakávanú výnimku so správou:\n     " +
                exception.getMessage());
    }
}
