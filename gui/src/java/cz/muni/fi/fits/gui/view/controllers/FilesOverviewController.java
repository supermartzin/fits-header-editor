package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.MainApp;
import cz.muni.fi.fits.gui.models.FileItem;
import cz.muni.fi.fits.gui.services.ResourceBundleService;
import cz.muni.fi.fits.gui.utils.dialogs.ExceptionDialog;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * TODO description
 */
public class FilesOverviewController extends Controller {

    private static final Object LOCK = new Object();

    public TableView<FileItem> tableView;
    public TableColumn<FileItem, Boolean> selectColumn;
    public TableColumn<FileItem, String> filenameColumn;
    public TableColumn<FileItem, String> filepathColumn;

    public MenuItem deleteMenuItem;
    public MenuItem selectMenuItem;
    public MenuItem invertMenuItem;
    public MenuItem deselectMenuItem;

    public Button invertSelectionButton;
    public Button filterFilesButton;

    private CheckBox selectFilesCheckBox;
    private IntegerProperty _numberOfSelectedFiles;

    @Override
    public void init() {
        // allow multiple rows selection
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // listener for items selection change
        tableView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() > 0) {
                setContextMenuState(true, true, true, true);
            } else {
                setContextMenuState(false, false, false, false);
            }
        });

        // prevent table columns reordering
        tableView.getColumns().addListener((ListChangeListener<TableColumn<FileItem, ?>>) change -> {
            change.next();
            if (change.wasReplaced()) {
                tableView.getColumns().clear();
                tableView.getColumns().addAll(selectColumn, filenameColumn, filepathColumn);
            }
        });

        // set column as a CheckBox column
        selectColumn.setCellFactory(cellData -> {
            CheckBoxTableCell<FileItem, Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
            // set value changed listener
            checkBoxTableCell.setSelectedStateCallback(index -> tableView.getItems().get(index).selectedProperty());
            return checkBoxTableCell;
        });

        // add checkbox to column heading
        selectFilesCheckBox = new CheckBox();
        selectFilesCheckBox.setDisable(true);
        selectColumn.setGraphic(selectFilesCheckBox);
        selectFilesCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            synchronized (LOCK) {
                tableView.getItems().forEach(fileItem -> fileItem.setSelected(newValue));
            }
        });

        // set value of cells in column
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        filenameColumn.setCellValueFactory(cellData -> cellData.getValue().filenameProperty());
        filepathColumn.setCellValueFactory(cellData -> cellData.getValue().filepathProperty());
    }

    public void setTableItemsCollection(ObservableList<FileItem> observableList) {
        if (observableList == null)
            throw new IllegalArgumentException("observableList is null");

        // set table items
        tableView.setItems(observableList);

        // listener for number of items in table
        tableView.getItems().addListener((ListChangeListener<FileItem>) change ->
                Platform.runLater(() -> {
                    if (change.getList().size() > 0) {
                        setSelectButtonsState(true, true, true);
                    } else {
                        // unselect check box in table header
                        selectFilesCheckBox.setSelected(false);

                        setSelectButtonsState(false, false, false);
                    }
                }));
    }

    //region Buttons methods

    public void onInvertSelection() {
        tableView.getItems().forEach(fileItem -> fileItem.setSelected(!fileItem.isSelected()));
    }

    public void onFilterActivated() {
        try {
            FXMLLoader filterDialogFile = new FXMLLoader(MainApp.class.getResource("view/FilterDialog.fxml"));
            ResourceBundleService.setResourceBundle(filterDialogFile);
            AnchorPane anchorPane = filterDialogFile.load();

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(_resources.getString("filter.dialog.title"));
            stage.getIcons().add(_mainApp.getApplicationIcon());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(_mainApp.getPrimaryStage());

            stage.setScene(new Scene(anchorPane));

            FilterDialogController controller = filterDialogFile.getController();
            controller.setMainApp(_mainApp);
            controller.setOwner(stage);

            stage.showAndWait();
        } catch (IOException ioEx) {
            ExceptionDialog.show(
                    _resources.getString("app.error.dialog.title"),
                    _resources.getString("app.error.dialog.header"),
                    _resources.getString("app.error.dialog.content.filter"),
                    ioEx,
                    _mainApp);
        }
    }

    public void onAddFiles() {
        // set Extension filter for FITS files
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("FITS files (*.fit, *.fits, *.fts)", "*.fit", "*.fits", "*.fts");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(extensionFilter);
        List<File> files = fileChooser.showOpenMultipleDialog(_mainApp.getPrimaryStage());
        if (files != null) {
            files.forEach(file -> {
                FileItem newFile = new FileItem(file.getName(), file.getAbsolutePath());
                // check for file uniqueness
                if (!tableView.getItems().contains(newFile))
                    tableView.getItems().add(newFile);
            });
        }
    }

    private void setSelectButtonsState(boolean selectAllState, boolean invertSelectionState, boolean filterState) {
        selectFilesCheckBox.setDisable(!selectAllState);
        invertSelectionButton.setDisable(!invertSelectionState);
        filterFilesButton.setDisable(!filterState);
    }

    //endregion

    //region Context Menu methods

    public void onContextMenuShowing() {
        ObservableList<FileItem> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (areAllItemsSelected(selectedItems)) {
            selectMenuItem.setDisable(true);
        }
        if (areAllItemsDeselected(selectedItems)) {
            deselectMenuItem.setDisable(true);
        }
    }

    public void onContextMenuHidden() {
        selectMenuItem.setDisable(false);
        deselectMenuItem.setDisable(false);
    }

    public void onDeleteContextMenu() {
        ObservableList<FileItem> selected = tableView.getSelectionModel().getSelectedItems();
        if (selected.size() > 0) tableView.getItems().removeAll(selected);
    }

    public void onSelectContextMenu() {
        ObservableList<FileItem> selected = tableView.getSelectionModel().getSelectedItems();
        selected.forEach(fileItem -> fileItem.setSelected(true));
    }

    public void onDeselectContextMenu() {
        ObservableList<FileItem> selected = tableView.getSelectionModel().getSelectedItems();
        selected.forEach(fileItem -> fileItem.setSelected(false));
    }

    public void onInvertContextMenu() {
        ObservableList<FileItem> selected = tableView.getSelectionModel().getSelectedItems();
        selected.forEach(fileItem -> fileItem.setSelected(!fileItem.isSelected()));
    }

    private void setContextMenuState(boolean deleteState, boolean selectState, boolean deselectState, boolean invertState) {
        deleteMenuItem.setDisable(!deleteState);
        selectMenuItem.setDisable(!selectState);
        deselectMenuItem.setDisable(!deselectState);
        invertMenuItem.setDisable(!invertState);
    }

    private boolean areAllItemsSelected(Collection<FileItem> fileItems) {
        if (fileItems == null)
            return false;

        // if at least one FitsFile is deselected > return false
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isSelected())
                return false;
        }

        return true;
    }

    private boolean areAllItemsDeselected(Collection<FileItem> fileItems) {
        if (fileItems == null)
            return false;

        // if at least one FitsFile is selected > return false
        for (FileItem fileItem : fileItems) {
            if (fileItem.isSelected())
                return false;
        }

        return true;
    }

    //endregion
}
