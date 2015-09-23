package cz.muni.fi.fits.input.validators;

import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.*;

/**
 * Interface for validating input data
 * if they are in correct form
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface InputDataValidator {

    /**
     * Validates input data for operation <b>Add new record</b>
     *
     * @param addNewRecordInputData input data to validate
     * @throws ValidationException  when input data are in invalid form
     */
    void validate(AddNewRecordInputData addNewRecordInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Add new record to specific index</b>
     *
     * @param addNewToIndexInputData    input data to validate
     * @throws ValidationException      when input data are in invalid form
     */
    void validate(AddNewToIndexInputData addNewToIndexInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Remove record by keyword</b>
     *
     * @param removeByKeywordInputData  input data to validate
     * @throws ValidationException      when input data are in invalid form
     */
    void validate(RemoveByKeywordInputData removeByKeywordInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Remove record from specific index</b>
     *
     * @param removeFromIndexInputData  input data to validate
     * @throws ValidationException      when input data are in invalid form
     */
    void validate(RemoveFromIndexInputData removeFromIndexInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Change keyword of record</b>
     *
     * @param changeKeywordInputData    input data to validate
     * @throws ValidationException      when input data are in invalid form
     */
    void validate(ChangeKeywordInputData changeKeywordInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Change value of record</b>
     *
     * @param changeValueByKeywordInputData input data to validate
     * @throws ValidationException          when input data are in invalid form
     */
    void validate(ChangeValueByKeywordInputData changeValueByKeywordInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Chain multiple records</b>
     *
     * @param chainRecordsInputData input data to validate
     * @throws ValidationException  when input data are in invalid form
     */
    void validate(ChainRecordsInputData chainRecordsInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Shift time of time record</b>
     *
     * @param shiftTimeInputData    input data to validate
     * @throws ValidationException  when input data are in invalid form
     */
    void validate(ShiftTimeInputData shiftTimeInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Compute Julian Date</b>
     *
     * @param computeJDInputData    input data to validate
     * @throws ValidationException  when input data are in invalid form
     */
    void validate(ComputeJDInputData computeJDInputData) throws ValidationException;

    /**
     * Validates input data for operation <b>Compute Heliocentric Julian Date</b>
     *
     * @param computeHJDInputData   input data to validate
     * @throws ValidationException  when input data are in invalid form
     */
    void validate(ComputeHJDInputData computeHJDInputData) throws ValidationException;
}
