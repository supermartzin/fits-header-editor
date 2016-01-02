package cz.muni.fi.fits.gui;

import cz.muni.fi.fits.gui.exceptions.ExternalEditorException;
import cz.muni.fi.fits.gui.exceptions.InvalidEnginePathException;
import cz.muni.fi.fits.gui.listeners.OutputListener;
import cz.muni.fi.fits.gui.models.Preferences;
import cz.muni.fi.fits.gui.utils.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FITSHeaderEditor implements ExternalProcess {

    private final Preferences _preferences;

    private OutputListener _outputListener;

    public FITSHeaderEditor(Preferences preferences)
            throws InvalidEnginePathException {
        _preferences = preferences;

        checkEnginePath();
    }

    @Override
    public void run(Collection<String> arguments)
            throws ExternalEditorException {
        if (arguments == null || arguments.isEmpty())
            throw new IllegalArgumentException("Arguments are not set!");

        Path enginePropertiesFile = null;

        try {
            // prepare arguments
            List<String> args = prepareArguments(arguments);

            // create engine properties file
            enginePropertiesFile = buildEnginePropertiesFile();

            // set working directory
            Path workingDirectory = Paths.get(_preferences.getEngineFilepath()).getParent();

            // set up and start engine process
            Process process = new ProcessBuilder(args)
                    .directory(workingDirectory.toFile())
                    .redirectErrorStream(true)
                    .start();

            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(process.getInputStream()), Constants.DEFAULT_CHARSET))) {
                while ((line = br.readLine()) != null) {
                    if (_outputListener != null)
                        _outputListener.onOutputMessage(line);
                }
            }

            // wait for process to end
            process.waitFor();
        } catch (IOException
                | InterruptedException ex) {
            throw new ExternalEditorException(ex);
        } finally {
            if (enginePropertiesFile != null)
                deleteFileIfExists(enginePropertiesFile);
        }
    }

    @Override
    public void addOutputListener(OutputListener outputListener) {
        if (outputListener == null)
            throw new IllegalArgumentException("output listener is null");

        _outputListener = outputListener;
    }


    private void checkEnginePath()
            throws InvalidEnginePathException {
        if (_preferences.getEngineFilepath() == null || _preferences.getEngineFilepath().isEmpty())
            throw new InvalidEnginePathException();

        if (!new File(_preferences.getEngineFilepath()).exists())
            throw new InvalidEnginePathException();
    }

    private List<String> prepareArguments(Collection<String> arguments) {
        return new LinkedList<String>() {
            {
                add("java");
                add("-jar");
                add(_preferences.getEngineFilepath());
                addAll(arguments);
            }
        };
    }

    private Path buildEnginePropertiesFile() throws IOException {
        File engineFile = new File(_preferences.getEngineFilepath());

        String engineDirectory = engineFile.getParent();
        if (engineDirectory == null)
            engineDirectory = ".";

        Path fitsProperties = Paths.get(engineDirectory + Constants.FILE_SEPARATOR + Constants.ENGINE_PROPERTIES_FILENAME);

        // create properties file if it does not exist
        if (!Files.exists(fitsProperties)) {
            Files.createFile(fitsProperties);
        }

        // construct properties
        List<String> properties = new LinkedList<>();
        // output.writer
        String outputWriter = "console";
        if (_preferences.saveOutputToFile())
            outputWriter += ", file";
        properties.add("output.writer=" + outputWriter);
        // output.file
        if (_preferences.saveOutputToFile()) {
            String outputFilepath = _preferences.getOutputFilepath();
            // escape Windows path backslashes if present
            outputFilepath = outputFilepath.replaceAll("\\\\", "\\\\\\\\");
            properties.add("output.file=" + outputFilepath);
        }

        // write properties
        Files.write(fitsProperties, properties);

        return fitsProperties;
    }

    private void deleteFileIfExists(Path inputFile) {
        try {
            Files.deleteIfExists(inputFile);
        } catch (IOException ignored) { }
    }
}
