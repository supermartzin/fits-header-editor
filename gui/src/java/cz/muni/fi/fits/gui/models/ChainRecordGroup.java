package cz.muni.fi.fits.gui.models;

import cz.muni.fi.fits.gui.listeners.RemoveListener;
import cz.muni.fi.fits.gui.models.operationenums.ChainValueType;
import cz.muni.fi.fits.gui.utils.Constants;
import cz.muni.fi.fits.gui.utils.Constrainer;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

import java.util.EventObject;
import java.util.ResourceBundle;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChainRecordGroup {

    private ComboBox<ComboBoxItem<ChainValueType>> _valueTypeField;
    private TextField _keywordValueField;
    private TextField _constantValueField;
    private Button _removeGroupButton;

    private static final Insets INSETS = new Insets(0, 5, 0, 5);
    private static final RowConstraints ROW_CONSTRAINTS = new RowConstraints();

    private final ResourceBundle _resources;
    private final boolean _unremovable;

    private RemoveListener _removeListener;
    private GridPane _parentPane;
    private int _rowIndex;

    public ChainRecordGroup(ResourceBundle resources, boolean unremovable) {
        if (resources == null)
            throw new IllegalArgumentException("resources are null");

        _resources = resources;
        _unremovable = unremovable;
        _removeListener = null;
        _rowIndex = -1;

        initializeFields();
    }

    public void addToGrid(final GridPane gridPane, final int rowIndex) {
        if (gridPane != null) {
            _parentPane = gridPane;
            _rowIndex = rowIndex;

            if (_unremovable) {
                _parentPane.addRow(rowIndex, _valueTypeField, _keywordValueField, _constantValueField);
            } else {
                _parentPane.addRow(rowIndex, _removeGroupButton, _valueTypeField, _keywordValueField, _constantValueField);

                GridPane.setColumnIndex(_removeGroupButton, 0);
                GridPane.setMargin(_removeGroupButton, INSETS);
            }

            _parentPane.getRowConstraints().add(ROW_CONSTRAINTS);

            GridPane.setColumnIndex(_valueTypeField, 1);
            GridPane.setMargin(_valueTypeField, INSETS);

            GridPane.setColumnIndex(_keywordValueField, 2);
            GridPane.setColumnIndex(_constantValueField, 2);
            GridPane.setMargin(_keywordValueField, INSETS);
            GridPane.setMargin(_constantValueField, INSETS);
        }
    }

    public ChainValueType getValueType() {
        if (_valueTypeField.getValue() == null)
            return null;

        return _valueTypeField.getValue().getType();
    }

    public String getValue() {
        ComboBoxItem<ChainValueType> chainValueTypeItem = _valueTypeField.getValue();

        if (chainValueTypeItem != null) {
            switch (chainValueTypeItem.getType()) {
                case KEYWORD:
                    return _keywordValueField.getText();
                case CONSTANT:
                    return _constantValueField.getText();
            }
        }

        return null;
    }

    public int getRowIndex() {
        return _rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        _rowIndex = rowIndex;

        GridPane.setRowIndex(_valueTypeField, rowIndex);
        GridPane.setRowIndex(_keywordValueField, rowIndex);
        GridPane.setRowIndex(_constantValueField, rowIndex);
        GridPane.setRowIndex(_removeGroupButton, rowIndex);
    }

    public void setRemoveListener(RemoveListener listener) {
        if (listener != null)
            _removeListener = listener;
    }


    private void initializeFields() {
        initValueTypeField();
        initValueFields();
        initRemoveGroupButton();
        initRowConstraints();
    }

    private void initValueTypeField() {
        _valueTypeField = new ComboBox();

        _valueTypeField.setPromptText(_resources.getString("choose.record"));

        _valueTypeField.setMinWidth(Region.USE_PREF_SIZE);

        _valueTypeField.getItems().add(new ComboBoxItem<>(ChainValueType.KEYWORD,
                _resources.getString(ChainValueType.KEYWORD.getPropertyName())));
        _valueTypeField.getItems().add(new ComboBoxItem<>(ChainValueType.CONSTANT,
                _resources.getString(ChainValueType.CONSTANT.getPropertyName())));

        _valueTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        _valueTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> onRecordTypeFieldSelectionChanged((ComboBoxItem) newValue));
    }

    private void initValueFields() {
        _keywordValueField = new TextField();
        _constantValueField = new TextField();

        _keywordValueField.setPrefWidth(150.0);
        _keywordValueField.setMinWidth(Region.USE_PREF_SIZE);
        _keywordValueField.setMaxWidth(Region.USE_PREF_SIZE);

        _keywordValueField.setPromptText(_resources.getString("field.required"));
        _constantValueField.setPromptText(_resources.getString("field.required"));

        _keywordValueField.setVisible(false);
        _constantValueField.setVisible(false);

        Constrainer.constrainTextFieldWithRegex(_keywordValueField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(_constantValueField, Constants.ONLY_ASCII_PATERN);
    }

    private void initRemoveGroupButton() {
        _removeGroupButton = new Button();

        //_removeGroupButton.setFont(new Font("System Bold", 12.0));
        _removeGroupButton.setGraphic(new ImageView(new Image("graphics/remove.png")));

        //_removeGroupButton.setText(_resources.getString("button.remove"));

        _removeGroupButton.setMinWidth(Region.USE_PREF_SIZE);
        _removeGroupButton.setMaxWidth(Region.USE_PREF_SIZE);

        if (_unremovable) {
            _removeGroupButton.setVisible(false);
        } else {
            _removeGroupButton.setOnAction(this::onRemove);
        }
    }

    private void initRowConstraints() {
        ROW_CONSTRAINTS.setMinHeight(Region.USE_PREF_SIZE);
        ROW_CONSTRAINTS.setPrefHeight(30.0);
        ROW_CONSTRAINTS.setMaxHeight(Region.USE_PREF_SIZE);
        ROW_CONSTRAINTS.setVgrow(Priority.NEVER);
    }

    private void onRecordTypeFieldSelectionChanged(ComboBoxItem comboBoxItem) {
        if (comboBoxItem != null) {
            switch ((ChainValueType) comboBoxItem.getType()) {
                case KEYWORD:
                    setRecordTypeDependentFieldsVisibility(true, false);
                    break;
                case CONSTANT:
                    setRecordTypeDependentFieldsVisibility(false, true);
                    break;
            }
        }
    }

    private void setRecordTypeDependentFieldsVisibility(
            boolean keywordValueField,
            boolean constantValueField) {
        this._keywordValueField.setVisible(keywordValueField);
        this._constantValueField.setVisible(constantValueField);
    }

    private void onRemove(ActionEvent actionEvent) {
        if (_parentPane != null) {
            _parentPane.getChildren().removeAll(_valueTypeField, _keywordValueField, _constantValueField, _removeGroupButton);
            _parentPane.getRowConstraints().remove(ROW_CONSTRAINTS);

            // raise onRemove event in registered listener
            if (_removeListener != null)
                _removeListener.onRemove(new EventObject(this));
        }
    }
}
