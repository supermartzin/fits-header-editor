package cz.muni.fi.fits.gui.models.inputdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Remove record from index</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class RemoveFromIndexInputData extends InputDataBase {

    private final int _index;

    /**
     * TODO insert description
     * @param index
     */
    public RemoveFromIndexInputData(int index) {
        _index = index;

        _operation = Operation.REMOVE_FROM_INDEX;
    }

    /**
     * {@inheritDoc}
     *
     * @return  list of {@link String} arguments in required order
     *          or <code>null</code> if some of the required parameters is not set
     */
    @Override
    public List<String> getInputDataArguments() {
        if (_inputFilePath == null
                || _index < 1)
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(Integer.toString(_index, 10));

        return inputDataArguments;
    }
}
