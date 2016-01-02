package cz.muni.fi.fits.gui.models.inputdata;

import java.util.List;

/**
 * Interface for creating classes storing input data
 * for various FITS Header editing operations
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface InputData {

    /**
     * Creates list of {@link String} containing all input data
     * as ordered arguments for use in command line engine program
     *
     * @return  list of {@link String} arguments in required order
     */
    List<String> getInputDataArguments();

    /**
     * Sets a path to file which contains paths to FITS files
     * used for editing operation
     *
     * @param filePath  path to existing file with FITS files paths
     */
    void setInputFilePath(String filePath);
}
