package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.input.models.InputData;

/**
 * Interface fo processing input data for editing engine
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public interface InputProcessor {

    /**
     * Process input data ad return them as {@link InputData} object
     *
     * @return                              processed input data as {@link InputData} object
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    InputData getProcessedInput() throws IllegalInputDataException;

    /**
     * Gets parameters entered on input as stringified array of that parameters
     *
     * @return {@link String} value containing list of input parameters
     */
    String getInputParameters();
}
