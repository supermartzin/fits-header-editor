package cz.muni.fi.fits.gui.models.inputdata;

/**
 * Abstract class implementing {@link InputData} interface
 * with properties common to all operations.
 * Used as a base class for all input data subclasses
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class InputDataBase implements InputData {

    protected String _inputFilePath;
    protected Operation _operation;

    /**
     * {@inheritDoc}
     *
     * @param filePath  path to existing file with FITS files paths
     */
    @Override
    public void setInputFilePath(String filePath) {
        if (filePath != null)
            _inputFilePath = filePath;
    }
}
