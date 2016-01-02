package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.exceptions.ValidationException;
import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.models.inputdata.ShiftTimeInputData;
import cz.muni.fi.fits.gui.models.operationenums.ShiftDirection;
import cz.muni.fi.fits.gui.utils.*;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ShiftTimeTabController extends OperationTabController {

    public TextField keywordField;

    // shift value fields
    public TextField yearsField;
    public TextField monthsField;
    public TextField daysField;
    public TextField hoursField;
    public TextField minutesField;
    public TextField secondsField;
    public TextField millisecondsField;

    // shift direction fields
    public ComboBox<ComboBoxItem<ShiftDirection>> yearsDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> monthsDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> daysDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> hoursDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> minutesDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> secondsDirectionField;
    public ComboBox<ComboBoxItem<ShiftDirection>> millisecondsDirectionField;

    private Validator _validator;

    @Override
    public void init() {
        _tabName = _resources.getString("tab.shift");
        _validator = new Validator();

        setFieldsConstraints();
        loadShiftDirectionFields();
    }

    @Override
    public InputData getInputData() {
        try {
            // validate fields
            _validator.validateKeywordField();
            _validator.validateTimeFields();
            _validator.validateYearsField();
            _validator.validateMonthsField();
            _validator.validateDaysField();
            _validator.validateHoursField();
            _validator.validateMinutesField();
            _validator.validateSecondsField();
            _validator.validateMillisecondsField();

            // get keyword
            String keyword = keywordField.getText();

            // get time shifts
            int yearsShift = parseShift(yearsField.getText(), yearsDirectionField.getValue());
            int monthsShift = parseShift(monthsField.getText(), monthsDirectionField.getValue());
            int daysShift = parseShift(daysField.getText(), daysDirectionField.getValue());
            int hoursShift = parseShift(hoursField.getText(), hoursDirectionField.getValue());
            int minutesShift = parseShift(minutesField.getText(), minutesDirectionField.getValue());
            int secondsShift = parseShift(secondsField.getText(), secondsDirectionField.getValue());
            int millisecondsShift = parseShift(millisecondsField.getText(), millisecondsDirectionField.getValue());

            return new ShiftTimeInputData(keyword, yearsShift, monthsShift, daysShift, hoursShift,
                    minutesShift, secondsShift, millisecondsShift);
        } catch (ValidationException vEx) {
            // validation errors
            return null;
        }
    }

    private void setFieldsConstraints() {
        Constrainer.constrainTextFieldWithRegex(keywordField, Constants.KEYWORD_PATTERN);

        Constrainer.constrainTextFieldWithRegex(yearsField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(monthsField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(daysField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(hoursField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(minutesField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(secondsField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(millisecondsField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
    }

    private void loadShiftDirectionFields() {
        ComboBoxItem<ShiftDirection> forwardDirection = new ComboBoxItem<>(
                ShiftDirection.FORWARD, _resources.getString(ShiftDirection.FORWARD.getPropertyName()));
        ComboBoxItem<ShiftDirection> backwardDirection = new ComboBoxItem<>(
                ShiftDirection.BACKWARD, _resources.getString(ShiftDirection.BACKWARD.getPropertyName()));

        yearsDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        monthsDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        daysDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        hoursDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        minutesDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        secondsDirectionField.getItems().addAll(forwardDirection, backwardDirection);
        millisecondsDirectionField.getItems().addAll(forwardDirection, backwardDirection);

        yearsDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        monthsDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        daysDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        hoursDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        minutesDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        secondsDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
        millisecondsDirectionField.setCellFactory(param -> new ComboBoxListCell<>());
    }

    private int parseShift(String text, ComboBoxItem<ShiftDirection> direction) {
        if (!text.isEmpty() && direction != null) {
            if (direction.getType().equals(ShiftDirection.BACKWARD))
                return -1 * Parsers.Integer.parse(text);
            if (direction.getType().equals(ShiftDirection.FORWARD))
                return Parsers.Integer.parse(text);
        }

        return Integer.MIN_VALUE;
    }


    /**
     * TODO insert description
     */
    class Validator {

        /**
         * @throws ValidationException
         */
        void validateKeywordField()
                throws ValidationException {
            if (keywordField.getText().isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.common.alert.content.keyword.empty"),
                        _mainApp);

                throw new ValidationException("Keyword field is not set");
            }
        }

        /**
         *
         */
        void validateTimeFields() throws ValidationException {
            if (yearsField.getText().isEmpty()
                    && monthsField.getText().isEmpty()
                    && daysField.getText().isEmpty()
                    && hoursField.getText().isEmpty()
                    && minutesField.getText().isEmpty()
                    && secondsField.getText().isEmpty()
                    && millisecondsField.getText().isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.shift.alert.content.value.empty"),
                        _mainApp);

                throw new ValidationException("None of the time shift fields is set");
            }
        }

        /**
         *
         */
        void validateYearsField()
                throws ValidationException {
            String yearsText = yearsField.getText();
            if (!yearsText.isEmpty()) {
                if (!Parsers.Integer.tryParse(yearsText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.years.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of years shift is in invalid format");
                }
                if (yearsDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.years.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of years shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateMonthsField()
                throws ValidationException {
            String monthsText = monthsField.getText();
            if (!monthsText.isEmpty()) {
                if (!Parsers.Integer.tryParse(monthsText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.months.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of months shift is in invalid format");
                }
                if (monthsDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.months.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of months shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateDaysField()
                throws ValidationException {
            String daysText = daysField.getText();
            if (!daysText.isEmpty()) {
                if (!Parsers.Integer.tryParse(daysText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.days.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of days shift is in invalid format");
                }
                if (daysDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.days.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of days shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateHoursField()
                throws ValidationException {
            String hoursText = hoursField.getText();
            if (!hoursText.isEmpty()) {
                if (!Parsers.Integer.tryParse(hoursText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.hours.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of hours shift is in invalid format");
                }
                if (hoursDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.hours.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of hours shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateMinutesField()
                throws ValidationException {
            String minutesText = minutesField.getText();
            if (!minutesText.isEmpty()) {
                if (!Parsers.Integer.tryParse(minutesText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.minutes.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of minutes shift is in invalid format");
                }
                if (minutesDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.minutes.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of minutes shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateSecondsField()
                throws ValidationException {
            String secondsText = secondsField.getText();
            if (!secondsText.isEmpty()) {
                if (!Parsers.Integer.tryParse(secondsText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.seconds.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of seconds shift is in invalid format");
                }
                if (secondsDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.seconds.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of seconds shift is not selected");
                }
            }
        }

        /**
         *
         */
        void validateMillisecondsField()
                throws ValidationException {
            String millisecondsText = millisecondsField.getText();
            if (!millisecondsText.isEmpty()) {
                if (!Parsers.Integer.tryParse(millisecondsText)) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.milliseconds.invalid"),
                            _mainApp);

                    throw new ValidationException("Value of milliseconds shift is in invalid format");
                }
                if (millisecondsDirectionField.getValue() == null) {
                    WarningDialog.show(
                            _resources.getString("oper.common.alert.title"),
                            _resources.getString("oper.common.alert.header"),
                            _resources.getString("oper.shift.alert.content.value.milliseconds.direction.empty"),
                            _mainApp);

                    throw new ValidationException("Direction of milliseconds shift is not selected");
                }
            }
        }
    }
}
