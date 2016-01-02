package cz.muni.fi.fits.gui.models.inputdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Remove record by keyword</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class RemoveByKeywordInputData extends InputDataBase {

    private final String _keyword;

    /**
     * TODO insert description
     *
     * @param keyword
     */
    public RemoveByKeywordInputData(String keyword) {
        _keyword = keyword;

        _operation = Operation.REMOVE_BY_KEYWORD;
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
                || _inputFilePath == null)
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(_keyword.toUpperCase());

        return inputDataArguments;
    }
}
