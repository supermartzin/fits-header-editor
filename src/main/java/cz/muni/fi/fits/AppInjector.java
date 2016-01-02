package cz.muni.fi.fits;

import com.google.inject.AbstractModule;
import cz.muni.fi.fits.common.Configuration;
import cz.muni.fi.fits.engine.HeaderEditingEngine;
import cz.muni.fi.fits.engine.NomTamFitsEditingEngine;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.processors.CmdArgumentsProcessor;
import cz.muni.fi.fits.input.processors.InputProcessor;
import cz.muni.fi.fits.input.validators.DefaultInputDataValidator;
import cz.muni.fi.fits.input.validators.InputDataValidator;
import cz.muni.fi.fits.output.writers.ConsoleOutputWriter;
import cz.muni.fi.fits.output.writers.FileConsoleOutputWriter;
import cz.muni.fi.fits.output.writers.FileOutputWriter;
import cz.muni.fi.fits.output.writers.OutputWriter;

/**
 * Class to inject all defined dependencies with Google Guice
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class AppInjector extends AbstractModule {

    private final Object _inputData;
    private final Configuration _configuration;

    /**
     * Creates new {@link AppInjector} object for injecting dependencies in program
     *
     * @param inputData     input data
     * @param configuration {@link Configuration} object with program configuration
     */
    public AppInjector(Object inputData, Configuration configuration) {
        this._inputData = inputData;
        this._configuration = configuration;
    }

    @Override
    protected void configure() {
        OutputWriter.Type outputWriterType = _configuration.getOutputWriterType();
        switch (outputWriterType) {
            case CONSOLE:
                bind(OutputWriter.class).to(ConsoleOutputWriter.class);
                break;

            case FILE:
                String outputFilepath = _configuration.getOutputFilePath();
                bind(OutputWriter.class).toInstance(new FileOutputWriter(outputFilepath));
                break;

            case FILE_AND_CONSOLE:
                outputFilepath = _configuration.getOutputFilePath();
                bind(OutputWriter.class).toInstance(new FileConsoleOutputWriter(outputFilepath));
                break;

            // use console output writer as default
            default:
                bind(OutputWriter.class).to(ConsoleOutputWriter.class);
        }

        bind(HeaderEditingEngine.class).to(NomTamFitsEditingEngine.class);
        bind(InputDataValidator.class).to(DefaultInputDataValidator.class);
        bind(InputProcessor.class).toInstance(new CmdArgumentsProcessor((String[]) _inputData, new DefaultTypeConverter()));
    }
}
