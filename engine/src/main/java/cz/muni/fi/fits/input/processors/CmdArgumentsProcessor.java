package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.common.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.common.exceptions.UnknownOperationException;
import cz.muni.fi.fits.common.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.input.models.*;

import javax.inject.Singleton;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * Processing class implementing {@link InputProcessor} interface
 * that takes input data from commandline arguments
 *
 * @author Martin Vrábel
 * @version 1.2.1
 */
@Singleton
public class CmdArgumentsProcessor implements InputProcessor {

    private final TypeConverter _converter;
    private final String[] _cmdArgs;

    /**
     * Creates new {@link CmdArgumentsProcessor} class with
     * commandline arguments as input data
     *
     * @param cmdArgs       command line arguments ontaining input data
     * @param typeConverter object of class implementing {@link TypeConverter} interface
     *                      that is used to convert values of input data
     */
    public CmdArgumentsProcessor(String[] cmdArgs, TypeConverter typeConverter) {
        this._cmdArgs = cmdArgs;
        this._converter = typeConverter;
    }

    /**
     * Process input data from commandline arguments as {@link InputData} object
     *
     * @return                              {@link InputData} object with processed input data
     * @throws IllegalInputDataException    {@inheritDoc}
     */
    @Override
    public InputData getProcessedInput() throws IllegalInputDataException {
        if (_cmdArgs == null)
            throw new IllegalInputDataException("Arguments are null");
        if (_cmdArgs.length == 0)
            throw new WrongNumberOfParametersException(0, "No arguments provided");
        if (_cmdArgs.length < 2)
            throw new WrongNumberOfParametersException(_cmdArgs.length, "Insufficient number of parameters");

        int fitsFilesArgIndex;
        InputData inputData;

        String operation = _cmdArgs[0].trim().toUpperCase();
        switch (operation) {
            case "ADD":
                inputData = CmdArgumentsProcessorHelper.extractAddNewRecordData(_cmdArgs, _converter);
                fitsFilesArgIndex = ((AddNewRecordInputData) inputData).updateIfExists() ? 2 : 1;
                break;

            case "ADD_IX":
                inputData = CmdArgumentsProcessorHelper.extractAddNewToIndexData(_cmdArgs, _converter);
                fitsFilesArgIndex = ((AddNewToIndexInputData) inputData).removeOldIfExists() ? 2 : 1;
                break;

            case "REMOVE":
                inputData = CmdArgumentsProcessorHelper.extractRemoveByKeywordData(_cmdArgs);
                fitsFilesArgIndex = 1;
                break;

            case "REMOVE_IX":
                inputData = CmdArgumentsProcessorHelper.extractRemoveFromIndexData(_cmdArgs);
                fitsFilesArgIndex = 1;
                break;

            case "CHANGE":
                inputData = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(_cmdArgs, _converter);
                fitsFilesArgIndex = ((ChangeValueByKeywordInputData) inputData).addNewIfNotExists() ? 2 : 1;
                break;

            case "CHANGE_KW":
                inputData = CmdArgumentsProcessorHelper.extractChangeKeywordData(_cmdArgs);
                fitsFilesArgIndex = ((ChangeKeywordInputData) inputData).removeValueOfNewIfExists() ? 2 : 1;
                break;

            case "CHAIN":
                inputData = CmdArgumentsProcessorHelper.extractChainRecordsData(_cmdArgs);
                ChainRecordsInputData crid = (ChainRecordsInputData) inputData;
                if (crid.updateIfExists() && crid.skipIfChainKwNotExists())
                    fitsFilesArgIndex = 3;
                else if (!crid.updateIfExists() && !crid.skipIfChainKwNotExists())
                    fitsFilesArgIndex = 1;
                else
                    fitsFilesArgIndex = 2;
                break;

            case "SHIFT_TIME":
                inputData = CmdArgumentsProcessorHelper.extractShiftTimeData(_cmdArgs, _converter);
                fitsFilesArgIndex = 1;
                break;

            case "JD":
                inputData = CmdArgumentsProcessorHelper.extractComputeJDData(_cmdArgs, _converter);
                fitsFilesArgIndex = 1;
                break;

            case "HJD":
                inputData = CmdArgumentsProcessorHelper.extractComputeHJDData(_cmdArgs, _converter);
                fitsFilesArgIndex = 1;
                break;

            default:
                throw new UnknownOperationException(operation, "Unknown operation '" + operation + "'");
        }

        // get input FITS files
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(_cmdArgs[fitsFilesArgIndex]);
        inputData.setFitsFiles(fitsFiles);

        return inputData;
    }


    /**
     * Gets commandline parameters entered to input console as stringified array of that parameters
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getInputParameters() {
        return Arrays.toString(_cmdArgs);
    }
}