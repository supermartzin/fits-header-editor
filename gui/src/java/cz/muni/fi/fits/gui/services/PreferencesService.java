package cz.muni.fi.fits.gui.services;

import cz.muni.fi.fits.gui.models.Language;
import cz.muni.fi.fits.gui.utils.Constants;

import java.util.prefs.Preferences;

/**
 * TODO insert description
 */
public class PreferencesService {

    private static final String LANGUAGE_KEY = "lang";
    private static final String FILEPATH_KEY = "filepath";
    private static final String SAVE_OUTPUT_TO_FILE_SWITCH_KEY = "save_output_to_file_switch";
    private static final String OUTPUT_FILEPATH_KEY = "output_filepath";

    /**
     *
     * @param resourceClass
     * @return
     */
    public static Language getLanguage(Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // load language
        String lang = preferences.get(LANGUAGE_KEY, "");
        if (!lang.isEmpty()) {
            return Language.getLanguageByCode(lang);
        } else {
            // save and return default language
            preferences.put(LANGUAGE_KEY, Constants.DEFAULT_LANGUAGE.getCode());
            return Constants.DEFAULT_LANGUAGE;
        }
    }

    /**
     *
     * @param language
     * @param resourceClass
     */
    public static void saveLanguage(Language language, Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // save language
        preferences.put(LANGUAGE_KEY, language.getCode());
    }

    /**
     *
     * @param resourceClass
     * @return
     */
    public static String loadEngineFilepath(Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // load filepath
        return preferences.get(FILEPATH_KEY, null);
    }

    /**
     *
     * @param filepath
     * @param resourceClass
     */
    public static void saveEngineFilepath(String filepath, Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // save filepath
        if (filepath != null && !filepath.isEmpty())
            preferences.put(FILEPATH_KEY, filepath);
    }

    /**
     *
     * @param resourceClass
     * @return
     */
    public static boolean loadSaveOutputToFileSwitch(Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // load switch
        return preferences.getBoolean(SAVE_OUTPUT_TO_FILE_SWITCH_KEY, false);
    }

    /**
     *
     * @param value
     * @param resourceClass
     */
    public static void saveSaveOutputToFileSwitch(boolean value, Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // save switch
        preferences.putBoolean(SAVE_OUTPUT_TO_FILE_SWITCH_KEY, value);
    }

    /**
     *
     * @param resourceClass
     * @return
     */
    public static String loadOutputFilepath(Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // load output filepath
        return preferences.get(OUTPUT_FILEPATH_KEY, null);
    }

    /**
     *
     * @param filepath
     * @param resourceClass
     */
    public static void saveOutputFilepath(String filepath, Class<?> resourceClass) {
        // load preferences
        Preferences preferences = Preferences.userNodeForPackage(resourceClass.getClass());
        // save filepath
        if (filepath != null && !filepath.isEmpty())
            preferences.put(OUTPUT_FILEPATH_KEY, filepath);
    }
}
