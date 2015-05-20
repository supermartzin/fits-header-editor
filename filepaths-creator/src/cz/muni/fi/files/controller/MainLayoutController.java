package cz.muni.fi.files.controller;

import cz.muni.fi.files.MainApp;
import cz.muni.fi.files.dialog.ExceptionDialog;
import cz.muni.fi.files.service.FileCreationService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainLayoutController {

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
            final Task fileCreationTask = createFileCreationTask();

            FileCreationService service = new FileCreationService(fileCreationTask);
            service.setOnFailed(this::onTaskFailed);
            service.setOnSucceeded(this::onTaskSucceeded);

            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(fileCreationTask.progressProperty());

            createOutputFileButton.setDisable(true);
            progressBar.setVisible(true);

            service.start();
        }
    }

    private Task createFileCreationTask() {
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

                String outputDirectory = _outputDirectory != null
                        ? _outputDirectory.getAbsolutePath() + File.separator
                        : "";
                String outputFilename = outputFileNameTextField.getText();
                Path outputPath = Paths.get(outputDirectory + outputFilename);

                Files.write(outputPath, files);

                return true;
            }
        };
    }

    private void onTaskSucceeded(Event event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(_mainApp.getPrimaryStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Informácia");
        alert.setHeaderText("Súbor úspešne vytvorený!");
        alert.setContentText("Súbor s cestami k súborom bol úspešne vytvorený.");

        alert.showAndWait();

        progressBar.setVisible(false);
        createOutputFileButton.setDisable(false);
    }

    private void onTaskFailed(Event event) {
        Worker worker = (Worker)event.getSource();

        ExceptionDialog dialog = new ExceptionDialog();
        dialog.initOwner(_mainApp.getPrimaryStage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nastala chyba");
        dialog.setHeaderText("Súbor nebolo možné vytvoriť!");
        dialog.setException(worker.getException());

        // set content text according to exception type
        if (worker.getException() instanceof AccessDeniedException) {
            dialog.setContentText("V zadanom priečinku nie sú dostatočné oprávnenia na zápis súboru." +
                    "\r\nSkúste vybrať iný priečinok.");
        } else {
            dialog.setContentText("Vyskytla sa chyba počas vytvárania súboru s cestami.");
        }

        dialog.showAndWait();

        progressBar.setVisible(false);
        createOutputFileButton.setDisable(false);
    }

    private boolean mandatoryFieldsFilled() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(_mainApp.getPrimaryStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Nevyplnené povinné pole");
        alert.setHeaderText("Nie je vyplnené povinné pole!");

        if (_inputDirectory == null) {
            alert.setContentText("Musíte vybrať vstupný priečinok so súbormi!");
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

    private boolean satisfiesFilesFilter(File file) {
        if (file == null)
            return false;

        Set<String> filters = getFileFilters();
        if (filters.isEmpty())
            return true;

        for (String filter : filters) {
            if (file.getName().endsWith(filter))
                return true;
        }

        return false;
    }
}
