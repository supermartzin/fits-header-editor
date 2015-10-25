package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.models.inputdata.RemoveByKeywordInputData;
import cz.muni.fi.fits.gui.models.inputdata.RemoveFromIndexInputData;
import cz.muni.fi.fits.gui.models.operationenums.RemoveType;
import cz.muni.fi.fits.gui.utils.*;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class RemoveRecordController extends OperationTabController {

    public ComboBox<ComboBoxItem<RemoveType>> removeTypeField;

    // remove BY KEYWORD fields
    public Label keywordLabel;
    public TextField keywordField;
    // remove BY INDEX fields
    public Label indexLabel;
    public TextField indexField;

    private Validator _validator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        _tabName = resources.getString("tab.remove");
        _validator = new Validator();

        setFieldsConstraints();
        loadRemoveTypeField(resources);
    }

    @Override
    public InputData getInputData() {
        try {
            // validate fields
            _validator.validateRemoveTypeField();

            InputData inputData = null;

            // create InputData based on remove type
            switch (removeTypeField.getValue().getType()) {
                case BY_KEYWORD:
                    // validate
                    _validator.validateKeywordField();

                    // get keyword
                    String keyword = keywordField.getText();

                    inputData = new RemoveByKeywordInputData(keyword);
                    break;

                case FROM_INDEX:
                    // validate
                    _validator.validateIndexField();

                    // get index
                    int index = Parsers.Integer.parse(indexField.getText());

                    inputData = new RemoveFromIndexInputData(index);
                    break;
            }

            return inputData;
        } catch (ValidationException vEx) {
            // validation errors
            return null;
        }
    }

    private void setFieldsConstraints() {
        Constrainer.constrainTextFieldWithRegex(keywordField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(indexField, Constants.NONNEG_INTEGRAL_NUMBER_PATTERN);
    }

    private void loadRemoveTypeField(ResourceBundle resources) {
        if (resources != null) {
            removeTypeField.getItems().add(new ComboBoxItem<>(RemoveType.BY_KEYWORD,
                    resources.getString(RemoveType.BY_KEYWORD.getPropertyName())));
            removeTypeField.getItems().add(new ComboBoxItem<>(RemoveType.FROM_INDEX,
                    resources.getString(RemoveType.FROM_INDEX.getPropertyName())));
        }

        removeTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        removeTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> onRemoveTypeFieldSelectionChanged((ComboBoxItem) newValue));
    }

    private void onRemoveTypeFieldSelectionChanged(ComboBoxItem comboBoxItem) {
        if (comboBoxItem != null) {
            switch ((RemoveType) comboBoxItem.getType()) {
                case BY_KEYWORD:
                    setRemoveTypeDependentFieldsVisibility(true, true, false, false);
                    break;
                case FROM_INDEX:
                    setRemoveTypeDependentFieldsVisibility(false, false, true, true);
            }
        }
    }

    private void setRemoveTypeDependentFieldsVisibility(boolean keywordLabel,
                                                        boolean keywordField,
                                                        boolean indexLabel,
                                                        boolean indexField) {
        this.keywordLabel.setVisible(keywordLabel);
        this.keywordField.setVisible(keywordField);
        this.indexLabel.setVisible(indexLabel);
        this.indexField.setVisible(indexField);
    }


    /**
     *
     */
    class Validator {

        /**
         *
         * @throws ValidationException
         */
        void validateRemoveTypeField()
                throws ValidationException {
            if (removeTypeField.getValue() == null) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.remove.alert.content.type"));

                throw new ValidationException("Remove type is not selected");
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateKeywordField()
                throws ValidationException {
            if (keywordField.getText().isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.common.alert.content.keyword.empty"));

                throw new ValidationException("Keyword field is not set");
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateIndexField()
                throws ValidationException {
            String indexText = indexField.getText();

            if (indexText.isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.common.alert.content.index.empty"));

                throw new ValidationException("Index field is not set");
            }
            if (!Parsers.Integer.tryParse(indexText)) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.common.alert.content.index.invalid"));

                throw new ValidationException("Index field value is in invalid format");
            }
        }
    }
}
