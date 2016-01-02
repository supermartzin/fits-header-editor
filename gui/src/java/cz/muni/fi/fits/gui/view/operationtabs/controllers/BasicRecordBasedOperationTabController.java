package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.exceptions.ValidationException;
import cz.muni.fi.fits.gui.models.operationenums.ValueType;
import cz.muni.fi.fits.gui.utils.*;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class BasicRecordBasedOperationTabController extends OperationTabController {

    public ComboBox<ComboBoxItem<ValueType>> valueTypeField;

    public TextField keywordField;
    public TextField commentField;

    // STRING
    public TextField valueStringField;
    // NUMBER
    public TextField valueNumberField;
    // DATETIME
    public HBox valueDateTimeContainer;
    public DatePicker valueDateTimeDateField;
    public TextField valueDateTimeTimeField;
    // DATE
    public DatePicker valueDateField;
    // TIME
    public TextField valueTimeField;
    // BOOLEAN
    public CheckBox valueBooleanField;


    @Override
    public void init() {
        loadValueTypeField();
    }

    protected void setFieldsConstraints() {
        Constrainer.constrainTextFieldWithRegex(valueTimeField, Constants.TIME_PATTERN);
        Constrainer.constrainTextFieldWithRegex(valueDateTimeTimeField, Constants.TIME_PATTERN);
        Constrainer.constrainTextFieldWithRegex(valueNumberField, Constants.DECIMAL_NUMBER_PATTERN);
        Constrainer.constrainTextFieldWithRegex(keywordField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(valueStringField, Constants.ONLY_ASCII_PATERN);
        Constrainer.constrainTextFieldWithRegex(valueStringField, Constants.STRING_VALUE_MAX_LENGTH_PATERN);
        Constrainer.constrainTextFieldWithRegex(commentField, Constants.ONLY_ASCII_PATERN);
        Constrainer.constrainTextFieldWithRegex(commentField, Constants.COMMENT_MAX_LENGTH_PATERN);
    }

    protected void loadValueTypeField() {
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.STRING,
                _resources.getString(ValueType.STRING.getPropertyName())));
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.NUMBER,
                _resources.getString(ValueType.NUMBER.getPropertyName())));
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.DATETIME,
                _resources.getString(ValueType.DATETIME.getPropertyName())));
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.DATE,
                _resources.getString(ValueType.DATE.getPropertyName())));
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.TIME,
                _resources.getString(ValueType.TIME.getPropertyName())));
        valueTypeField.getItems().add(new ComboBoxItem<>(ValueType.BOOLEAN,
                _resources.getString(ValueType.BOOLEAN.getPropertyName())));

        valueTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        valueTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> onValueTypeFieldSelectionChanged((ComboBoxItem) newValue));
    }

    protected void onValueTypeFieldSelectionChanged(ComboBoxItem comboBoxItem) {
        if (comboBoxItem != null) {
            switch ((ValueType) comboBoxItem.getType()) {
                case STRING:
                    setValueTypeDependentFieldsVisibility(true, false, false, false, false, false);
                    break;
                case NUMBER:
                    setValueTypeDependentFieldsVisibility(false, true, false, false, false, false);
                    break;
                case DATETIME:
                    setValueTypeDependentFieldsVisibility(false, false, true, false, false, false);
                    break;
                case DATE:
                    setValueTypeDependentFieldsVisibility(false, false, false, true, false, false);
                    break;
                case TIME:
                    setValueTypeDependentFieldsVisibility(false, false, false, false, true, false);
                    break;
                case BOOLEAN:
                    setValueTypeDependentFieldsVisibility(false, false, false, false, false, true);
                    break;
            }
        }
    }

    protected void setValueTypeDependentFieldsVisibility(
            boolean stringField,
            boolean numberField,
            boolean datetimeField,
            boolean dateField,
            boolean timeField,
            boolean booleanField) {
        valueStringField.setVisible(stringField);
        valueNumberField.setVisible(numberField);
        valueDateTimeContainer.setVisible(datetimeField);
        valueDateField.setVisible(dateField);
        valueTimeField.setVisible(timeField);
        valueBooleanField.setVisible(booleanField);
    }

    protected String getRecordValue() {
        ComboBoxItem<ValueType> valueTypeItem = valueTypeField.getValue();

        if (valueTypeItem != null) {
            switch (valueTypeItem.getType()) {
                case STRING:
                    return valueStringField.getText();
                case NUMBER:
                    return valueNumberField.getText();
                case BOOLEAN:
                    return Boolean.toString(valueBooleanField.isSelected());
                case DATE:
                    return valueDateField.getValue().toString();
                case TIME:
                    LocalTime valueTime = Parsers.Time.parse(valueTimeField.getText());
                    if (valueTime != null)
                        return valueTime.toString();
                case DATETIME:
                    LocalDate date = valueDateTimeDateField.getValue();
                    LocalTime time = Parsers.Time.parse(valueDateTimeTimeField.getText());

                    if (date == null)
                        date = LocalDate.of(0, 1, 1);
                    if (time == null)
                        time = LocalTime.of(0, 0, 0);

                    return LocalDateTime.of(date, time).toString();
            }
        }

        return null;
    }


    /**
     * TODO insert description
     */
    abstract class Validator {

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
         * @throws ValidationException
         */
        void validateValueTypeField()
                throws ValidationException {
            if (valueTypeField.getValue() == null) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.common.alert.content.value.type"),
                        _mainApp);

                throw new ValidationException("Value type is not selected");
            }
        }

        /**
         * @throws ValidationException
         */
        void validateValueField()
                throws ValidationException {
            ValueType valueType = valueTypeField.getValue().getType();

            switch (valueType) {
                case DATE:
                    if (valueDateField.getValue() == null)
                        valueEmpty();

                    break;

                case TIME:
                    String timeString = valueTimeField.getText();
                    if (timeString.isEmpty())
                        valueEmpty();
                    if (!Parsers.Time.tryParse(timeString))
                        valueInvalid();

                    break;

                case DATETIME:
                    String timeText = valueDateTimeTimeField.getText();

                    if (valueDateTimeDateField.getValue() == null
                            && timeText.isEmpty()) {
                        valueEmpty();
                    }
                    if (!timeText.isEmpty()
                            && !Parsers.Time.tryParse(timeText)) {
                        valueInvalid();
                    }

                    break;

                case NUMBER:
                    String numberText = valueNumberField.getText();
                    if (numberText.isEmpty())
                        valueEmpty();
                    if (!Parsers.Integer.tryParse(numberText)
                            && !Parsers.Long.tryParse(numberText)
                            && !Parsers.Double.tryParse(numberText))
                        valueInvalid();

                    break;

                case STRING:
                    if (valueStringField.getText().isEmpty())
                        valueEmpty();

                    if (!commentField.getText().isEmpty()) {
                        int length = commentField.getText().length() + valueStringField.getText().length();
                        if (length > Constants.MAX_STRING_VALUE_AND_COMMENT_LENGTH) {
                            WarningDialog.show(
                                    _resources.getString("oper.common.alert.title"),
                                    _resources.getString("oper.common.alert.header"),
                                    _resources.getString("oper.common.alert.content.value_comment.too_long"),
                                    _mainApp);

                            throw new ValidationException("Value of the record along with comment is too long");
                        }
                    }
                    break;

                default:
                    break;
            }
        }


        private void valueEmpty()
                throws ValidationException {
            WarningDialog.show(
                    _resources.getString("oper.common.alert.title"),
                    _resources.getString("oper.common.alert.header"),
                    _resources.getString("oper.common.alert.content.value.empty"),
                    _mainApp);

            throw new ValidationException("Value of the record is empty");
        }

        private void valueInvalid()
                throws ValidationException {
            WarningDialog.show(
                    _resources.getString("oper.common.alert.title"),
                    _resources.getString("oper.common.alert.header"),
                    _resources.getString("oper.common.alert.content.value.invalid"),
                    _mainApp);

            throw new ValidationException("Value of the record is in invalid format");
        }
    }
}
