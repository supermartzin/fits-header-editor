package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.models.FileItem;
import cz.muni.fi.fits.gui.models.FilterType;
import cz.muni.fi.fits.gui.tasks.FilteringTask;
import cz.muni.fi.fits.gui.utils.Constants;
import cz.muni.fi.fits.gui.utils.Constrainer;
import cz.muni.fi.fits.gui.utils.ThreadUtils;
import cz.muni.fi.fits.gui.utils.combobox.ComboBoxItem;
import cz.muni.fi.fits.gui.utils.dialogs.ExceptionDialog;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Set;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FilterDialogController extends Controller {

    public ComboBox<ComboBoxItem<FilterType>> filterTypeField;
    public TextField keywordField;
    public CheckBox valueSwitchField;
    public TextField valueField;

    public ProgressIndicator progressIndicator;
    public Button okButton;
    public Button cancelButton;

    private Stage _ownerStage;

    @Override
    public void init() {
        loadFilterTypeField();
        loadValueSwitchField();
        loadValueField();
    }

    public void setOwner(Stage ownerStage) {
        _ownerStage = ownerStage;
    }

    public void onOK() {
        if (!checkKeywordValue())
            return;
        if (!checkFilterTypeField())
            return;

        final Collection<FileItem> files = _mainApp.getAllFiles();
        final FilterType filterType = filterTypeField.getValue().getType();
        final String keyword = keywordField.getText();
        final String value = valueSwitchField.isSelected()
                ? valueField.getText()
                : null;


        // create background service
        Task<Set<FileItem>> filteringTask = new FilteringTask(files, filterType, keyword, value);

        // set listeners
        filteringTask.setOnSucceeded(event ->
                Platform.runLater(() -> {
                    // remove all affected files
                    _mainApp.getAllFiles().removeAll(filteringTask.getValue());

                    showProgressIndicator(false);
                    closeStage();
                }));
        filteringTask.setOnFailed(event ->
                Platform.runLater(() -> {
                    showProgressIndicator(false);

                    ExceptionDialog.show(
                            _resources.getString("app.error.dialog.title"),
                            _resources.getString("app.error.dialog.header"),
                            _resources.getString("filter.error"),
                            ((Worker) event.getSource()).getException(),
                            _mainApp);

                    closeStage();
                }));
        filteringTask.setOnCancelled(event ->
                Platform.runLater(() -> {
                    showProgressIndicator(false);
                    closeStage();
                }));

        showProgressIndicator(true);
        disableButtons();
        disableFilterField();

        // START filtering in background thread
        ThreadUtils.executeInBackground(filteringTask);
    }

    public void onCancel() {
        closeStage();
    }


    private void closeStage() {
        Stage stage = (Stage) _ownerStage.getScene().getWindow();
        stage.close();
    }

    private void loadFilterTypeField() {
        filterTypeField.getItems().add(new ComboBoxItem<>(FilterType.REMOVE,
                _resources.getString(FilterType.REMOVE.getPropertyName())));
        filterTypeField.getItems().add(new ComboBoxItem<>(FilterType.KEEP,
                _resources.getString(FilterType.KEEP.getPropertyName())));

        filterTypeField.setCellFactory(param -> new ComboBoxListCell<>());

        filterTypeField.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null)
                        okButton.setDisable(false);
                });
    }

    private void loadValueSwitchField() {
        valueSwitchField.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        valueField.setDisable(false);
                    } else {
                        valueField.setDisable(true);
                    }
                }
        );
    }

    private void loadValueField() {
        Constrainer.constrainTextFieldWithRegex(keywordField, Constants.KEYWORD_PATTERN);
    }

    private boolean checkKeywordValue() {
        if (keywordField.getText().isEmpty()) {
            WarningDialog.show(
                    _resources.getString("oper.common.alert.title"),
                    _resources.getString("oper.common.alert.header"),
                    _resources.getString("filter.alert.content.keyword.empty"),
                    _mainApp);

            return false;
        }

        return true;
    }

    private boolean checkFilterTypeField() {
        if (filterTypeField.getValue() == null) {
            WarningDialog.show(
                    _resources.getString("oper.common.alert.title"),
                    _resources.getString("oper.common.alert.header"),
                    _resources.getString("filter.alert.content.type.not_set"),
                    _mainApp);

            return false;
        }

        return true;
    }

    private void showProgressIndicator(boolean showSwitch) {
        progressIndicator.setVisible(showSwitch);
    }

    private void disableButtons() {
        okButton.setDisable(true);
        cancelButton.setDisable(true);
    }

    private void disableFilterField() {
        filterTypeField.setDisable(true);
    }
}
