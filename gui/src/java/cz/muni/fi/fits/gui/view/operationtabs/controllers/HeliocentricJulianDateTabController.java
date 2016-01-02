package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.exceptions.ValidationException;
import cz.muni.fi.fits.gui.models.inputdata.HeliocentricJulianDateInputData;
import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.models.operationenums.HJDRecordType;
import cz.muni.fi.fits.gui.utils.*;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class HeliocentricJulianDateTabController extends JulianDateBasedOperationTabController {

    // RIGHT ASCENSION
    public ComboBox<ComboBoxItem<HJDRecordType>> rightAscensionRecordTypeField;
    public TextField rightAscensionKeywordField;
    public TextField rightAscensionDecimalValueField;
    public HBox rightAscensionFullValueContainer;
    public TextField rightAscensionHoursField;
    public TextField rightAscensionMinutesField;
    public TextField rightAscensionSecondsField;

    // DECLINATION
    public ComboBox<ComboBoxItem<HJDRecordType>> declinationRecordTypeField;
    public TextField declinationKeywordField;
    public TextField declinationDecimalValueField;
    public HBox declinationFullValueContainer;
    public TextField declinationDegreesField;
    public TextField declinationMinutesField;
    public TextField declinationSecondsField;

    private Validator _validator;

    @Override
    public void init() {
        super.init();

        _tabName = _resources.getString("tab.hjd");
        _validator = new Validator();

        loadRightAscensionRecordTypeField();
        loadDeclinationRecordTypeField();
    }

    @Override
    public InputData getInputData() {
        try {
            // validate fields
            _validator.validateDatetimeRecordTypeField();
            _validator.validateDatetimeValueField();
            _validator.validateExposureRecordTypeField();
            _validator.validateExposureValueField();
            _validator.validateRightAscensionRecordType();
            _validator.validateRightAscensionValueField();
            _validator.validateDeclinationRecordType();
            _validator.validateDeclinationValueField();

            // get datetime
            String datetime = getDatetimeFieldValue();

            // get exposure
            String exposure = getExposureFieldValue();

            // get Right Ascension
            String rightAscension = getRightAscensionFieldValue();

            // get Declination
            String declination = getDeclinationFieldValue();

            // get comment
            String comment = commentField.getText();

            return new HeliocentricJulianDateInputData(datetime, exposure, rightAscension, declination, comment);
        } catch (ValidationException vEx) {
            // validation errors
            return null;
        }
    }

    @Override
    protected void setFieldsConstraints() {
        super.setFieldsConstraints();

        Constrainer.constrainTextFieldWithRegex(rightAscensionKeywordField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(rightAscensionDecimalValueField, Constants.RIGHT_ASCENSION_PATTERN);
        Constrainer.constrainTextFieldWithRegex(rightAscensionHoursField, Constants.HOURS_PATTERN);
        Constrainer.constrainTextFieldWithRegex(rightAscensionMinutesField, Constants.MINUTES_PATTERN);
        Constrainer.constrainTextFieldWithRegex(rightAscensionSecondsField, Constants.SECONDS_PATTERN);

        Constrainer.constrainTextFieldWithRegex(declinationKeywordField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(declinationDecimalValueField, Constants.DECLINATION_PATTERN);
        Constrainer.constrainTextFieldWithRegex(declinationDegreesField, Constants.DEGREES_PATTERN);
        Constrainer.constrainTextFieldWithRegex(declinationMinutesField, Constants.MINUTES_PATTERN);
        Constrainer.constrainTextFieldWithRegex(declinationSecondsField, Constants.SECONDS_PATTERN);
    }

    private void loadRightAscensionRecordTypeField() {
        loadHJDRecordTypeItems(rightAscensionRecordTypeField);

        rightAscensionRecordTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        rightAscensionRecordTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> onRightAscensionRecordTypeFieldSelectionChanged((ComboBoxItem) newValue));
    }

    private void loadDeclinationRecordTypeField() {
        loadHJDRecordTypeItems(declinationRecordTypeField);

        declinationRecordTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        declinationRecordTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> onDeclinationRecordTypeFieldSelectionChanged((ComboBoxItem) newValue));
    }

    private void loadHJDRecordTypeItems(ComboBox comboBox) {
        if (comboBox != null) {
            comboBox.getItems().add(new ComboBoxItem<>(HJDRecordType.KEYWORD,
                    _resources.getString(HJDRecordType.KEYWORD.getPropertyName())));
            comboBox.getItems().add(new ComboBoxItem<>(HJDRecordType.DECIMAL_VALUE,
                    _resources.getString(HJDRecordType.DECIMAL_VALUE.getPropertyName())));
            comboBox.getItems().add(new ComboBoxItem<>(HJDRecordType.FULL_VALUE,
                    _resources.getString(HJDRecordType.FULL_VALUE.getPropertyName())));
        }
    }

    private void onRightAscensionRecordTypeFieldSelectionChanged(ComboBoxItem comboBoxItem) {
        if (comboBoxItem != null) {
            switch ((HJDRecordType) comboBoxItem.getType()) {
                case KEYWORD:
                    setRightAscensionRecordTypeDependentFieldsVisibility(true, false, false);
                    break;
                case DECIMAL_VALUE:
                    setRightAscensionRecordTypeDependentFieldsVisibility(false, true, false);
                    break;
                case FULL_VALUE:
                    setRightAscensionRecordTypeDependentFieldsVisibility(false, false, true);
                    break;
            }
        }
    }

    private void onDeclinationRecordTypeFieldSelectionChanged(ComboBoxItem comboBoxItem) {
        if (comboBoxItem != null) {
            switch ((HJDRecordType) comboBoxItem.getType()) {
                case KEYWORD:
                    setDeclinationRecordTypeDependentFieldsVisibility(true, false, false);
                    break;
                case DECIMAL_VALUE:
                    setDeclinationRecordTypeDependentFieldsVisibility(false, true, false);
                    break;
                case FULL_VALUE:
                    setDeclinationRecordTypeDependentFieldsVisibility(false, false, true);
                    break;
            }
        }
    }

    private void setRightAscensionRecordTypeDependentFieldsVisibility(
            boolean rightAscenionKeywordField,
            boolean rightAscensionDecimalValueField,
            boolean rightAscensionFullValueContainer) {
        this.rightAscensionKeywordField.setVisible(rightAscenionKeywordField);
        this.rightAscensionDecimalValueField.setVisible(rightAscensionDecimalValueField);
        this.rightAscensionFullValueContainer.setVisible(rightAscensionFullValueContainer);
    }

    private void setDeclinationRecordTypeDependentFieldsVisibility(
            boolean declinationKeywordField,
            boolean declinationDecimalValueField,
            boolean declinationFullValueContainer) {
        this.declinationKeywordField.setVisible(declinationKeywordField);
        this.declinationDecimalValueField.setVisible(declinationDecimalValueField);
        this.declinationFullValueContainer.setVisible(declinationFullValueContainer);
    }

    private String getRightAscensionFieldValue() {
        ComboBoxItem<HJDRecordType> recordTypeItem = rightAscensionRecordTypeField.getValue();

        if (recordTypeItem != null) {
            switch (recordTypeItem.getType()) {
                case KEYWORD:
                    return rightAscensionKeywordField.getText();

                case DECIMAL_VALUE:
                    return rightAscensionDecimalValueField.getText();

                case FULL_VALUE:
                    String raHoursText = rightAscensionHoursField.getText();
                    String hours = raHoursText.isEmpty() ? "00" : raHoursText;

                    String raMinutesText = rightAscensionMinutesField.getText();
                    String minutes = raMinutesText.isEmpty() ? "00" : raMinutesText;

                    String raSecondsText = rightAscensionSecondsField.getText();
                    String seconds = raSecondsText.isEmpty() ? "00" : raSecondsText;

                    return hours + ":" + minutes + ":" + seconds;
            }
        }

        return null;
    }

    private String getDeclinationFieldValue() {
        ComboBoxItem<HJDRecordType> recordTypeItem = declinationRecordTypeField.getValue();

        if (recordTypeItem != null) {
            switch (recordTypeItem.getType()) {
                case KEYWORD:
                    return declinationKeywordField.getText();

                case DECIMAL_VALUE:
                    return declinationDecimalValueField.getText();

                case FULL_VALUE:
                    String decDegreesText = declinationDegreesField.getText();
                    String degrees = decDegreesText.isEmpty() ? "00" : decDegreesText;

                    String decMinutesText = declinationMinutesField.getText();
                    String minutes = decMinutesText.isEmpty() ? "00" : decMinutesText;

                    String decSecondsText = declinationSecondsField.getText();
                    String seconds = decSecondsText.isEmpty() ? "00" : decSecondsText;

                    return degrees + ":" + minutes + ":" + seconds;
            }
        }

        return null;
    }


    /**
     * TODO insert description
     */
    class Validator extends JulianDateBasedOperationTabController.Validator {

        /**
         *
         * @throws ValidationException
         */
        void validateRightAscensionRecordType()
                throws ValidationException {
            if (rightAscensionRecordTypeField.getValue() == null) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.hjd.alert.content.ra.type"),
                        _mainApp);

                throw new ValidationException("Right ascension value type is not selected");
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateRightAscensionValueField()
                throws ValidationException {
            HJDRecordType rightAscensionRecordType = rightAscensionRecordTypeField.getValue().getType();

            switch (rightAscensionRecordType) {
                case KEYWORD:
                    if (rightAscensionKeywordField.getText().isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.ra.keyword.empty"),
                                _mainApp);

                        throw new ValidationException("Keyword of the right ascension record is not set");
                    }
                    break;

                case DECIMAL_VALUE:
                    String raDecimalValueText = rightAscensionDecimalValueField.getText();

                    if (raDecimalValueText.isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.ra.value.empty"),
                                _mainApp);

                        throw new ValidationException("Right ascension value is empty");
                    }
                    if (!Parsers.Double.tryParse(raDecimalValueText)) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.ra.value.invalid"),
                                _mainApp);

                        throw new ValidationException("Right ascension value is in invalid format");
                    }
                    break;

                case FULL_VALUE:
                    String raHours = rightAscensionHoursField.getText();
                    String raMinutes = rightAscensionMinutesField.getText();
                    String raSeconds = rightAscensionSecondsField.getText();

                    if (raHours.isEmpty() && raMinutes.isEmpty() && raSeconds.isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.ra.value.empty"),
                                _mainApp);

                        throw new ValidationException("Right ascension value is empty");
                    }
                    if (!raHours.isEmpty() && !Parsers.Integer.tryParse(raHours)
                            || !raMinutes.isEmpty() && !Parsers.Integer.tryParse(raMinutes)
                            || !raSeconds.isEmpty() && !Parsers.Integer.tryParse(raSeconds)) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.ra.value.invalid"),
                                _mainApp);

                        throw new ValidationException("Right ascension value is in invalid format");
                    }
                    break;
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateDeclinationRecordType()
                throws ValidationException {
            if (declinationRecordTypeField.getValue() == null) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.hjd.alert.content.dec.type"),
                        _mainApp);

                throw new ValidationException("Declination value type is not selected");
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateDeclinationValueField()
                throws ValidationException {
            HJDRecordType declinationRecordType = declinationRecordTypeField.getValue().getType();

            switch (declinationRecordType) {
                case KEYWORD:
                    if (declinationKeywordField.getText().isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.dec.keyword.empty"),
                                _mainApp);

                        throw new ValidationException("Keyword of the declination record is not set");
                    }
                    break;

                case DECIMAL_VALUE:
                    String decDecimalValueText = declinationDecimalValueField.getText();

                    if (decDecimalValueText.isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.dec.value.empty"),
                                _mainApp);

                        throw new ValidationException("Declination value is empty");
                    }
                    if (!Parsers.Double.tryParse(decDecimalValueText)) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.dec.value.invalid"),
                                _mainApp);

                        throw new ValidationException("Declination value is in invalid format");
                    }
                    break;

                case FULL_VALUE:
                    String decDegrees = declinationDegreesField.getText();
                    String decMinutes = declinationMinutesField.getText();
                    String decSeconds = declinationSecondsField.getText();

                    if (decDegrees.isEmpty() && decMinutes.isEmpty() && decSeconds.isEmpty()) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.dec.value.empty"),
                                _mainApp);

                        throw new ValidationException("Declination value is empty");
                    }
                    if (!decDegrees.isEmpty() && !Parsers.Integer.tryParse(decDegrees)
                            || !decMinutes.isEmpty() && !Parsers.Integer.tryParse(decMinutes)
                            || !decSeconds.isEmpty() && !Parsers.Integer.tryParse(decSeconds)) {
                        WarningDialog.show(
                                _resources.getString("oper.common.alert.title"),
                                _resources.getString("oper.common.alert.header"),
                                _resources.getString("oper.hjd.alert.content.dec.value.invalid"),
                                _mainApp);

                        throw new ValidationException("Declination value is in invalid format");
                    }
                    break;
            }
        }
    }
}
