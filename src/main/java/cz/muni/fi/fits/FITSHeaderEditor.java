package cz.muni.fi.fits;

import cz.muni.fi.fits.engine.HeaderEditingEngine;
import cz.muni.fi.fits.common.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.common.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.*;
import cz.muni.fi.fits.input.processors.InputProcessor;
import cz.muni.fi.fits.input.validators.InputDataValidator;
import cz.muni.fi.fits.engine.models.Result;
import cz.muni.fi.fits.output.writers.OutputWriter;

import javax.inject.Inject;
import java.io.File;

/**
 * Core editing class used as a central point of FITS Header Editor Tool
 *
 * @author Martin Vrábel
 * @version 1.0.1
 */
public class FITSHeaderEditor {

    private final HeaderEditingEngine _headerEditingEngine;
    private final InputProcessor _inputProcessor;
    private final InputDataValidator _inputDataValidator;
    private final OutputWriter _outputWriter;

    /**
     * Creates new {@link FITSHeaderEditor} object with specified external dependencies
     *
     * @param headerEditingEngine   object with core editing engine
     * @param inputProcessor        process input data
     * @param inputDataValidator    validates input data
     * @param outputWriter          writer object that takes care of writing to output
     */
    @Inject
    public FITSHeaderEditor(HeaderEditingEngine headerEditingEngine,
                            InputProcessor inputProcessor,
                            InputDataValidator inputDataValidator,
                            OutputWriter outputWriter) {
        this._headerEditingEngine = headerEditingEngine;
        this._inputProcessor = inputProcessor;
        this._inputDataValidator = inputDataValidator;
        this._outputWriter = outputWriter;

        // in case of uncaught exception
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> _outputWriter.writeException(e));
    }

    /**
     * Main method which starts FITS header editing with constructor-provided input data and dependencies
     */
    public void start() {
        try {
            _outputWriter.writeInfo("Entered parameters: " + _inputProcessor.getInputParameters());

            // process input parameters
            InputData inputData = _inputProcessor.getProcessedInput();

            switch (inputData.getOperationType()) {
                case ADD_NEW_RECORD_TO_END:
                    AddNewRecordInputData anrid = (AddNewRecordInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(anrid);

                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // insert into FITS files
                    for (File fitsFile : anrid.getFitsFiles()) {
                        Result result = _headerEditingEngine.addNewRecord(
                                anrid.getKeyword(),
                                anrid.getValue(),
                                anrid.getComment(),
                                anrid.updateIfExists(),
                                fitsFile);

                        // write result to output
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case ADD_NEW_RECORD_TO_INDEX:
                    AddNewToIndexInputData antiid = (AddNewToIndexInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(antiid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // insert into FITS files
                    for (File fitsFile : antiid.getFitsFiles()) {
                        Result result = _headerEditingEngine.addNewRecordToIndex(
                                antiid.getIndex(),
                                antiid.getKeyword(),
                                antiid.getValue(),
                                antiid.getComment(),
                                antiid.removeOldIfExists(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case REMOVE_RECORD_BY_KEYWORD:
                    RemoveByKeywordInputData rbkid = (RemoveByKeywordInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(rbkid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // remove record from FITS files
                    for (File fitsFile : rbkid.getFitsFiles()) {
                        Result result = _headerEditingEngine.removeRecordByKeyword(
                                rbkid.getKeyword(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case REMOVE_RECORD_FROM_INDEX:
                    RemoveFromIndexInputData rfiid = (RemoveFromIndexInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(rfiid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // remove record from specified index in FITS files
                    for (File fitsFile : rfiid.getFitsFiles()) {
                        Result result = _headerEditingEngine.removeRecordFromIndex(
                                rfiid.getIndex(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case CHANGE_KEYWORD:
                    ChangeKeywordInputData ckid = (ChangeKeywordInputData) inputData;
                    // validate input data
                    _inputDataValidator.validate(ckid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // change keyword for specific record in FITS files
                    for (File fitsFile : ckid.getFitsFiles()) {
                        Result result = _headerEditingEngine.changeKeywordOfRecord(
                                ckid.getOldKeyword(),
                                ckid.getNewKeyword(),
                                ckid.removeValueOfNewIfExists(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case CHANGE_VALUE_BY_KEYWORD:
                    ChangeValueByKeywordInputData cvbkid = (ChangeValueByKeywordInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(cvbkid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // change value of specified record in FITS files
                    for (File fitsFile : cvbkid.getFitsFiles()) {
                        Result result = _headerEditingEngine.changeValueOfRecord(
                                cvbkid.getKeyword(),
                                cvbkid.getValue(),
                                cvbkid.getComment(),
                                cvbkid.addNewIfNotExists(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case CHAIN_RECORDS:
                    ChainRecordsInputData crid = (ChainRecordsInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(crid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // chain multiple records to new record in FITS files
                    for (File fitsFile : crid.getFitsFiles()) {
                        Result result = _headerEditingEngine.chainMultipleRecords(
                                crid.getKeyword(),
                                crid.getChainValues(),
                                crid.getComment(),
                                crid.updateIfExists(),
                                crid.longstringsAllowed(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case SHIFT_TIME:
                    ShiftTimeInputData stid = (ShiftTimeInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(stid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // shift time of time record in FITS files
                    for (File fitsFile : stid.getFitsFiles()) {
                        Result result = _headerEditingEngine.shiftTimeOfTimeRecord(
                                stid.getKeyword(),
                                stid.getYearShift(),
                                stid.getMonthShift(),
                                stid.getDayShift(),
                                stid.getHourShift(),
                                stid.getMinuteShift(),
                                stid.getSecondShift(),
                                stid.getNanosecondShift(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case COMPUTE_JD:
                    ComputeJDInputData cjdid = (ComputeJDInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(cjdid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // compute JD in FITS files
                    for (File fitsFile : cjdid.getFitsFiles()) {
                        Result result = _headerEditingEngine.computeJulianDate(
                                cjdid.getDatetime(),
                                cjdid.getExposure(),
                                cjdid.getComment(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;

                case COMPUTE_HJD:
                    ComputeHJDInputData chjdid = (ComputeHJDInputData)inputData;
                    // validate input data
                    _inputDataValidator.validate(chjdid);
                    _outputWriter.writeInfo("Provided parameters are in correct format");

                    // compute HJD in FITS files
                    for (File fitsFile : chjdid.getFitsFiles()) {
                        Result result = _headerEditingEngine.computeHeliocentricJulianDate(
                                chjdid.getDatetime(),
                                chjdid.getExposure(),
                                chjdid.getRightAscension(),
                                chjdid.getDeclination(),
                                chjdid.getComment(),
                                fitsFile);

                        // write result
                        if (result.isSuccess())
                            _outputWriter.writeInfo(fitsFile, result.getMessage());
                        else
                            _outputWriter.writeError(fitsFile, result.getMessage());
                    }
                    break;
            }
        } catch (IllegalInputDataException | ValidationException iidEx) {
            _outputWriter.writeException(iidEx);
        }
    }
}
