package cz.muni.fi.fits.gui.models.inputdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Add new record to index</code>
 *
 * @author Martin Vrábel - © 2015 FITS-HeaderVisualEditor
 * @version 1.0
 */
public class AddNewToIndexInputData extends InputDataBase {

    private final String _keyword;
    private final String _value;
    private final String _comment;
    private final int _index;
    private final boolean _removeOldIfExists;

    /**
     * TODO insert description
     *
     * @param keyword
     * @param value
     * @param comment
     * @param index
     * @param removeOldIfExists
     */
    public AddNewToIndexInputData(String keyword, String value, String comment, int index, boolean removeOldIfExists) {
        _keyword = keyword;
        _value = value;
        _comment = comment;
        _index = index;
        _removeOldIfExists = removeOldIfExists;

        _operation = Operation.ADD_TO_INDEX;
    }

    /**
     * {@inheritDoc}
     *
     * @return  list of {@link String} arguments in required order
     *          or <code>null</code> if some of the required parameters is not set
     */
    @Override
    public List<String> getInputDataArguments() {
        if (_keyword == null
                || _value == null
                || _index < 1
                || _inputFilePath == null)
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        if (_removeOldIfExists)
            inputDataArguments.add("-rm");
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(Integer.toString(_index, 10));
        inputDataArguments.add(_keyword.toUpperCase());
        inputDataArguments.add(_value);
        if (_comment != null)
            inputDataArguments.add(_comment);

        return inputDataArguments;
    }
}
