package cz.muni.fi.fits.gui.models;

import cz.muni.fi.fits.gui.services.PreferencesService;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class Preferences {

    public static final String DEFAULT_ENGINE_FILEPATH = "engine.jar";
    public static final String DEFAULT_OUTPUT_FILEPATH = "output.txt";

    private String _engineFilepath;
    private boolean _saveOutputToFile;
    private String _outputFilepath;

    /**
     *
     */
    public Preferences() {
        // load default values
        _engineFilepath = PreferencesService.loadEngineFilepath(Preferences.class);
        if (_engineFilepath == null) {
            _engineFilepath = DEFAULT_ENGINE_FILEPATH;
            // save default value
            PreferencesService.saveEngineFilepath(_engineFilepath, Preferences.class);
        }

        // load output filepath switch
        _saveOutputToFile = PreferencesService.loadSaveOutputToFileSwitch(Preferences.class);

        // load output filepath
        _outputFilepath = PreferencesService.loadOutputFilepath(Preferences.class);
        if (_outputFilepath == null) {
            _outputFilepath = DEFAULT_OUTPUT_FILEPATH;
            // save default value
            PreferencesService.saveOutputFilepath(_outputFilepath, Preferences.class);
        }
    }

    /**
     *
     * @param engineFilepath
     */
    public void setEngineFilepath(String engineFilepath) {
        // set only if filepath changed
        if (!engineFilepath.equals(_engineFilepath)) {
            _engineFilepath = engineFilepath;
            // save to user preferences
            PreferencesService.saveEngineFilepath(engineFilepath, Preferences.class);
        }
    }

    /**
     *
     * @param saveOutputToFile
     */
    public void setSaveOutputToFile(boolean saveOutputToFile) {
        // set only if switch changed
        if (saveOutputToFile != _saveOutputToFile) {
            _saveOutputToFile = saveOutputToFile;
            // save to user preferences
            PreferencesService.saveSaveOutputToFileSwitch(saveOutputToFile, Preferences.class);
        }
    }

    /**
     *
     * @param outputFilepath
     */
    public void setOutputFilepath(String outputFilepath) {
        // set only if filepath changed
        if (!outputFilepath.equals(_outputFilepath)) {
            _outputFilepath = outputFilepath;
            // save to user preferences
            PreferencesService.saveOutputFilepath(outputFilepath, Preferences.class);
        }
    }

    public String getEngineFilepath() {
        return _engineFilepath;
    }

    public boolean saveOutputToFile() {
        return _saveOutputToFile;
    }

    public String getOutputFilepath() {
        return _outputFilepath;
    }
}
