package cz.muni.fi.fits.gui.models.inputdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Change keyword of existing record</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChangeKeywordInputData extends InputDataBase {

    private final String _oldKeyword;
    private final String _newKeyword;
    private final boolean _removeValueOfNewIfExists;

    /**
     * TODO insert description
     *
     * @param oldKeyword
     * @param newKeyword
     * @param removeValueOfNewIfExists
     */
    public ChangeKeywordInputData(String oldKeyword, String newKeyword, boolean removeValueOfNewIfExists) {
        _oldKeyword = oldKeyword;
        _newKeyword = newKeyword;
        _removeValueOfNewIfExists = removeValueOfNewIfExists;

        _operation = Operation.CHANGE_KEYWORD;
    }

    /**
     * {@inheritDoc}
     *
     * @return  {@link String} with ordered and formatted input data
     *          or <code>null</code> if some of the required parameters is not set
     */
    @Override
    public List<String> getInputDataArguments() {
        if (_oldKeyword == null
                || _newKeyword == null
                || _inputFilePath == null)
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        if (_removeValueOfNewIfExists)
            inputDataArguments.add("-rm");
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(_oldKeyword.toUpperCase());
        inputDataArguments.add(_newKeyword.toUpperCase());

        return inputDataArguments;
    }
}
