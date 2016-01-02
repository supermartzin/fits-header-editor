package cz.muni.fi.fits.common;

import cz.muni.fi.fits.output.writers.OutputWriter;

/**
 * Class holding configuration properties used to configure
 * instance of {@link cz.muni.fi.fits.FITSHeaderEditor} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class Configuration {

    private final OutputWriter.Type _outputWriterType;
    private final String _outputFilePath;

    /**
     * Creates new {@link Configuration} object and initializes
     * properties with entered parameters
     *
     * @param outputWriterType  type of output writer
     * @param outputFilePath    path to file if output writer writes to file
     */
    public Configuration(OutputWriter.Type outputWriterType, String outputFilePath) {
        _outputWriterType = outputWriterType;
        _outputFilePath = outputFilePath;
    }

    public OutputWriter.Type getOutputWriterType() {
        return _outputWriterType;
    }

    public String getOutputFilePath() {
        return _outputFilePath;
    }
}
