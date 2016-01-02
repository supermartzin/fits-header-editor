package cz.muni.fi.fits.gui.view.controllers;

import cz.muni.fi.fits.gui.FITSHeaderEditor;
import cz.muni.fi.fits.gui.exceptions.InvalidEnginePathException;
import cz.muni.fi.fits.gui.models.FileItem;
import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.tasks.EditingTask;
import cz.muni.fi.fits.gui.utils.Constants;
import cz.muni.fi.fits.gui.utils.Parsers;
import cz.muni.fi.fits.gui.utils.ThreadUtils;
import cz.muni.fi.fits.gui.utils.dialogs.ErrorDialog;
import cz.muni.fi.fits.gui.utils.dialogs.ExceptionDialog;
import cz.muni.fi.fits.gui.utils.dialogs.InfoDialog;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import cz.muni.fi.fits.gui.view.operationtabs.controllers.TabController;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO description
 */
public class OperationTabsViewController extends Controller {

    public Button startButton;
    public ProgressBar progressBar;

    private final Collection<TabController> _tabs;

    public OperationTabsViewController() {
        _tabs = new HashSet<>();
    }

    @Override
    public void init() { }

    public void onStartEditor() {
        // get currently selected operation tab
        Optional<TabController> optional = _tabs.stream()
                .filter(TabController::selected)
                .findFirst();

        if (optional.isPresent()) {
            TabController tab = optional.get();

            // check correct java version
            if (!checkJavaVersion())
                return;

            // check input FITS files
            Collection<FileItem> selectedFiles = _mainApp.getSelectedFiles();
            if (!checkSelectedFiles(selectedFiles)) return;

            // input data
            InputData inputData = tab.getInputData();
            if (inputData != null) {
                    try {
                        // create input file with FITS filepaths
                        Path filesInFile = createInputFile(selectedFiles);

                        // add input file to input data
                        inputData.setInputFilePath(filesInFile.toAbsolutePath().toString());

                        EditingTask task = new EditingTask(new FITSHeaderEditor(_mainApp.getPreferences()),
                                                            inputData.getInputDataArguments(),
                                                            selectedFiles.size());

                        // send task to MainApp for adding listeners
                        _mainApp.setEditingTask(task);

                        progressBar.progressProperty().unbind();
                        progressBar.progressProperty().bind(task.progressProperty());
                        startButton.setDisable(true);

                        // on operation succeeded
                        task.setOnSucceeded(event -> {
                            // delete temp input file
                            deleteFileIfExists(filesInFile);

                            // get results
                            boolean endedWithErrors = task.endedWithErrors();

                            if (!endedWithErrors) {
                                // show success dialog
                                InfoDialog.show(
                                        _resources.getString("oper.success.dialog.title"),
                                        null,
                                        _resources.getString("oper.success.dialog.content"),
                                        _mainApp);
                            } else {
                                // show half-success dialog
                                InfoDialog.show(
                                        _resources.getString("oper.success.dialog.title"),
                                        null,
                                        _resources.getString("oper.success.half.dialog.content"),
                                        _mainApp);
                            }

                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0.0);
                            startButton.setDisable(false);
                        });

                        // on operation failed
                        task.setOnFailed(event -> {
                            // delete temp input file
                            deleteFileIfExists(filesInFile);

                            // show exception dialog
                            Throwable exception = ((Worker)event.getSource()).getException();
                            ExceptionDialog.show(
                                    _resources.getString("oper.fail.dialog.title"),
                                    _resources.getString("oper.fail.dialog.header"),
                                    _resources.getString("oper.fail.dialog.content")
                                            + Constants.NEWLINE + "   " + exception.getMessage(),
                                    exception,
                                    _mainApp);

                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0.0);
                            startButton.setDisable(false);
                        });

                        // on operation cancelled
                        task.setOnCancelled(event -> {
                            // delete temp input file
                            deleteFileIfExists(filesInFile);

                            // show error dialog
                            ErrorDialog.show(
                                    _resources.getString("app.error.dialog.title"),
                                    _resources.getString("app.error.dialog.header"),
                                    _resources.getString("oper.cancelled.dialog.content"),
                                    _mainApp);

                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0.0);
                            startButton.setDisable(false);
                        });

                        // START editing in background thread
                        ThreadUtils.executeInBackground(task);
                    } catch (InvalidEnginePathException iepEx) {
                        // invalid path to editor engine
                        ErrorDialog.show(
                                _resources.getString("app.error.dialog.title"),
                                _resources.getString("app.error.dialog.header"),
                                _resources.getString("app.error.dialog.content.engine.not_exist"),
                                _mainApp);
                    } catch (IOException ioEx) {
                        // error during execution of editor
                        ExceptionDialog.show(
                                _resources.getString("app.error.dialog.title"),
                                _resources.getString("app.error.dialog.header"),
                                _resources.getString("app.error.dialog.content.editor.start"),
                                ioEx,
                                _mainApp);
                    }

            }
        }
    }

    public void addTabController(TabController tabController) {
        if (tabController != null) {
            _tabs.add(tabController);
        }
    }

    private boolean checkJavaVersion() {
        String versionString = Runtime.class.getPackage().getSpecificationVersion();
        if (Parsers.Double.tryParse(versionString)) {
            Double version = Parsers.Double.parse(versionString);

            if (version < 1.8) {
                ErrorDialog.show(
                        _resources.getString("app.error.dialog.title"),
                        _resources.getString("app.error.dialog.header"),
                        _resources.getString("app.error.dialog.content.java.bad_version"),
                        _mainApp);
                return false;
            }
        }

        return true;
    }

    private boolean checkSelectedFiles(Collection<FileItem> files) {
        if (files == null || files.isEmpty()) {
            WarningDialog.show(
                    _resources.getString("oper.common.alert.title"),
                    _resources.getString("oper.common.alert.header"),
                    _resources.getString("oper.common.alert.content.files.empty"),
                    _mainApp);

            return false;
        } else {
            return true;
        }
    }

    private Path createInputFile(Collection<FileItem> files)
            throws IOException {
        List<String> filepaths = files.stream()
                                      .map(FileItem::getFilepath)
                                      .collect(Collectors.toList());

        Path filesIn = Files.createTempFile("files", "in");
        Files.write(filesIn, filepaths);

        return filesIn;
    }

    private void deleteFileIfExists(Path inputFile) {
        try {
            Files.deleteIfExists(inputFile);
        } catch (IOException ignored) { }
    }
}
