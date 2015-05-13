package cz.muni.fi.files.controller;

import cz.muni.fi.files.MainApp;
import cz.muni.fi.files.utils.NotifyingRunnable;
import cz.muni.fi.files.utils.ThreadCompleteListener;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainLayoutController implements ThreadCompleteListener {

    private MainApp _mainApp;

    private File _inputDirectory = null;
    private File _outputDirectory = null;

    public ProgressBar progressBar;
    public Button createOutputFileButton;
    public TextField inputDirTextField;
    public TextField filterTextField;
    public TextField outputDirTextField;
    public TextField outputFileNameTextField;

    public void setMainApp(MainApp mainApp) {
        _mainApp = mainApp;
    }

    public void onBrowseForInputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File inputDir = directoryChooser.showDialog(_mainApp.getPrimaryStage());
        if (inputDir != null) {
            inputDirTextField.setText(inputDir.getAbsolutePath());
            _inputDirectory = inputDir;
        }
    }

    public void onBrowseForOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File outputDir = directoryChooser.showDialog(_mainApp.getPrimaryStage());
        if (outputDir != null) {
            outputDirTextField.setText(outputDir.getAbsolutePath());
            _outputDirectory = outputDir;
        }
    }

    public void onCreateFile() {
        if (mandatoryFieldsFilled()) {
            Task createOutputFileTask = getCreateOutputFileTask();

            createOutputFileButton.setDisable(true);
            progressBar.setVisible(true);
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(createOutputFileTask.progressProperty());

            NotifyingRunnable runnable = new NotifyingRunnable(createOutputFileTask);
            runnable.addListener(this);
            new Thread(runnable).start();
        }
    }

    private boolean mandatoryFieldsFilled() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(_mainApp.getPrimaryStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Nevyplnené povinné pole");
        alert.setHeaderText("Nie je vyplnené povinné pole!");

        if (_inputDirectory == null) {
            alert.setContentText("Musíte vybrať vstupný priečinok so súbormi!");
        } else if (_outputDirectory == null) {
            alert.setContentText("Musíte vybrať priečinok, kde uložiť výstupný súbor!");
        } else if (outputFileNameTextField.getText().isEmpty()) {
            alert.setContentText("Musíte vyplniť názov výstupného súboru!");
        } else {
            return true;
        }

        alert.showAndWait();
        return false;
    }

    private Set<String> getFileFilters() {
        Set<String> filters = new HashSet<>();

        String text = filterTextField.getText().trim();

        String[] _filters = text.split(",");
        for (String _filter : _filters) {
            _filter = _filter.trim();

            if (!_filter.isEmpty())
                filters.add(_filter);
        }

        return filters;
    }

    private Task getCreateOutputFileTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int itemsCount = _inputDirectory.list().length;
                int counter = 0;

                // list all satisfying files
                List<String> files = new ArrayList<>();
                for (File file : _inputDirectory.listFiles()) {
                    updateProgress(++counter, itemsCount);
                    if (file.isFile() && satisfiesFilesFilter(file)) {
                        files.add(file.getAbsolutePath());
                    }
                }

                String outputFilename = outputFileNameTextField.getText();
                Files.write(Paths.get(_outputDirectory.getAbsolutePath() + "\\" + outputFilename), files);

                return true;
            }
        };
    }

    private boolean satisfiesFilesFilter(File file) {
        if (file == null)
            return false;

        Set<String> filters = getFileFilters();
        for (String filter : filters) {
            if (file.getName().endsWith(filter))
                return true;
        }

        return false;
    }

    @Override
    public void notifyOfThreadComplete(Runnable runnable) {
        if (runnable instanceof  NotifyingRunnable) {
            NotifyingRunnable notifyingRunnable = (NotifyingRunnable) runnable;

            progressBar.setVisible(false);
            createOutputFileButton.setDisable(false);

            if (notifyingRunnable.hasEndedWithNoError()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(_mainApp.getPrimaryStage());
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle("Informácia");
                    alert.setHeaderText("Súbor úspešne vytvorený!");
                    alert.setContentText("Súbor s cestami k súborom bol úspešne vytvorený.");

                    alert.showAndWait();
                });
            } else {
                // TODO throw error
            }
        }
    }
}
