package cz.muni.fi.fits.common.loaders;

import cz.muni.fi.fits.common.Configuration;
import cz.muni.fi.fits.common.exceptions.ConfigurationException;
import cz.muni.fi.fits.common.utils.Placeholders;
import cz.muni.fi.fits.output.writers.OutputWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class for loading configuration of FITS Header Editor program
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public final class ConfigurationLoader {

    /**
     * Loads configuration of FITS Header Editor from provided <code>properties</code>
     *
     * @param properties                {@link Properties} object with user defined properties
     * @return                          {@link Configuration} object
     * @throws ConfigurationException   when there some error occurs during configuration loading
     */
    public static Configuration loadConfiguration(Properties properties)
            throws ConfigurationException {
        if (properties == null)
            throw new IllegalArgumentException("properties are null");

        if (properties.isEmpty())
            throw new ConfigurationException("properties are empty");

        String outputWriter = properties.getProperty(Placeholders.OUTPUT_WRITER_PROPERTY);
        if (outputWriter == null || outputWriter.isEmpty())
            throw new ConfigurationException("Cannot find property 'output.writer' in properties file");

        Configuration configuration = null;

        List<String> writers = Arrays.asList(splitAndTrimString(outputWriter, ","));
        if (writers.size() == 1) {
            // load console output writer
            if (writers.contains(Placeholders.CONSOLE_WRITER_OPTION)) {
                configuration = new Configuration(OutputWriter.Type.CONSOLE, null);
            }

            // load file output writer
            if (writers.contains(Placeholders.FILE_WRITER_OPTION)) {
                String outputFilepath = properties.getProperty(Placeholders.OUTPUT_FILE_PROPERTY);
                if (outputFilepath == null || outputFilepath.isEmpty())
                    throw new ConfigurationException("Cannot find property " + Placeholders.OUTPUT_FILE_PROPERTY + " in properties file");

                configuration = new Configuration(OutputWriter.Type.FILE, outputFilepath);
            }
        }
        if (writers.size() == 2) {
            // load file and console output writer
            if (writers.contains(Placeholders.CONSOLE_WRITER_OPTION)
                    && writers.contains(Placeholders.FILE_WRITER_OPTION)) {
                String outputFilepath = properties.getProperty(Placeholders.OUTPUT_FILE_PROPERTY);
                if (outputFilepath == null || outputFilepath.isEmpty())
                    throw new ConfigurationException("Cannot find property " + Placeholders.OUTPUT_FILE_PROPERTY + " in properties file");

                configuration = new Configuration(OutputWriter.Type.FILE_AND_CONSOLE, outputFilepath);
            }
        }

        if (configuration != null)
            return configuration;
        else
            throw new ConfigurationException("Unknown value for property '" + Placeholders.OUTPUT_WRITER_PROPERTY + "'");
    }

    private static String[] splitAndTrimString(String string, String delimiter) {
        String[] splittedArray = string.split(delimiter);
        String[] trimmedArray = new String[splittedArray.length];

        // trim items in split
        for (int i = 0; i < splittedArray.length; i++) {
            trimmedArray[i] = splittedArray[i].trim();
        }

        return trimmedArray;
    }
}
