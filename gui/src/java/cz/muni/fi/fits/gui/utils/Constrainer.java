package cz.muni.fi.fits.gui.utils;

import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class Constrainer {

    /**
     *
     * @param field
     * @param regexPattern
     */
    public static void constrainTextFieldWithRegex(TextField field, Pattern regexPattern) {
        if (field == null)
            throw new IllegalArgumentException("TextField parameter is null");
        if (regexPattern == null)
            throw new IllegalArgumentException("Regex pattern is null");

        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                Matcher matcher = regexPattern.matcher(newValue);
                if (!matcher.matches())
                    field.setText(oldValue);
            }
        });
    }

    private Constrainer() { }
}
