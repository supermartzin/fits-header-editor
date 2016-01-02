package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.utils.Constants;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class OutputViewController extends Controller {

    public ScrollPane topContainer;
    public TextFlow outputTextFlow;

    @Override
    public void init() {
        outputTextFlow.getChildren().addListener(
                (ListChangeListener<? super Node>) change -> {
                    // for TextFlow autoscroll when new Text is appended
                    outputTextFlow.layout();
                    topContainer.layout();
                    topContainer.setVvalue(1.0);
                });
    }

    public void onInfoMessage(String infoMessage) {
        if (infoMessage != null) {
            // parse datetime
            Text datetimeText = prepareDateTimeText(infoMessage);

            // message type
            Text messageTypeText = prepareMessageTypeText("INFO ", Color.GREEN);

            // FITS file name (if any)
            Text fitsFileText = prepareFitsFileName(infoMessage);

            // message itself
            Text messageText = prepareMessageText(infoMessage, fitsFileText);

            // append to TextFlow
            appendToTextFlow(datetimeText, messageTypeText, fitsFileText, messageText);
        }
    }

    public void onErrorMessage(String errorMessage) {
        if (errorMessage != null) {
            // parse datetime
            Text datetimeText = prepareDateTimeText(errorMessage);

            // message type
            Text messageTypeText = prepareMessageTypeText("ERROR ", Color.DARKRED);

            // FITS file name (if any)
            Text fitsFileText = prepareFitsFileName(errorMessage);

            // message itself
            Text messageText = prepareMessageText(errorMessage, fitsFileText);

            // append to TextFlow
            appendToTextFlow(datetimeText, messageTypeText, fitsFileText, messageText);
        }
    }

    public void onExceptionMessage(String exceptionMessage) {
        if (exceptionMessage != null) {
            // parse datetime
            Text datetimeText = prepareDateTimeText(exceptionMessage);

            // message type
            Text messageTypeText = prepareMessageTypeText("EXCEPTION ", Color.RED);

            // FITS file name (if any)
            Text fitsFileText = prepareFitsFileName(exceptionMessage);

            // message itself
            Text messageText = prepareMessageText(exceptionMessage, fitsFileText);

            // append to TextFlow
            appendToTextFlow(datetimeText, messageTypeText, fitsFileText, messageText);
        }
    }


    private void appendToTextFlow(Text datetime, Text messageType, Text fitsFile, Text message) {
        // arrows
        Text arrows = prepareArrowsText();

        if (fitsFile != null)
            outputTextFlow.getChildren().addAll(datetime, messageType, arrows, new Text("["), fitsFile, new Text("]: "), message);
        else
            outputTextFlow.getChildren().addAll(datetime, messageType, arrows, message);
    }

    private Text prepareDateTimeText(String message) {
        String datetime = LocalDateTime.now().toString();
        Matcher matcher = Constants.OUTPUT_DATETIME_PATTERN.matcher(message);
        if (matcher.find()) {
            datetime = matcher.group(1);
        }
        Text datetimeText = new Text(datetime + " ");
        datetimeText.setFill(Color.GRAY);

        return datetimeText;
    }

    private Text prepareMessageTypeText(String messageType, Color color) {
        Text messageTypeText = new Text(messageType);
        messageTypeText.setFill(color);
        messageTypeText.setStyle("-fx-font-weight: bold");

        return messageTypeText;
    }

    private Text prepareArrowsText() {
        Text arrowsText = new Text(">> ");
        arrowsText.setFill(Color.GRAY);

        return arrowsText;
    }

    private Text prepareFitsFileName(String message) {
        Matcher matcher = Constants.OUTPUT_FITS_FILE_PATTERN.matcher(message);
        if (matcher.find()) {
            Text fitsFileNameText = new Text(matcher.group(1));
            fitsFileNameText.setStyle("-fx-font-weight: bold");

            return fitsFileNameText;
        }

        return null;
    }

    private Text prepareMessageText(String message, Text fitsFileText) {
        Text messageText = new Text(message + Constants.NEWLINE);

        if (fitsFileText != null) {
            String text = "[" + fitsFileText.getText() + "]: ";
            int index = message.indexOf(text);
            if (index > -1 && index + text.length() <= message.length()) {
                messageText = new Text(message.substring(index + text.length()) + Constants.NEWLINE);
            }
        } else {
            int index = message.indexOf(">> ");
            if (index > -1 && index + 3 <= message.length()) {
                String msg = message.substring(index + 3);
                messageText = new Text(msg + Constants.NEWLINE);
            }
        }

        return messageText;
    }
}
