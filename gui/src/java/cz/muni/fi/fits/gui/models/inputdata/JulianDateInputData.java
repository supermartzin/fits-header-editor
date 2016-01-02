package cz.muni.fi.fits.gui.models.inputdata;

import cz.muni.fi.fits.gui.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Compute Julian Date</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class JulianDateInputData extends InputDataBase {

    private final String _datetime;
    private final String _exposure;
    private final String _comment;

    /**
     * TODO insert description
     *
     * @param datetime
     * @param exposure
     * @param comment
     */
    public JulianDateInputData(String datetime, String exposure, String comment) {
        _datetime = datetime;
        _exposure = exposure;
        _comment = comment;

        _operation = Operation.JD;
    }

    /**
     * {@inheritDoc}
     *
     * @return  list of {@link String} arguments in required order
     *          or <code>null</code> if some of the required parameters is not set
     */
    @Override
    public List<String> getInputDataArguments() {
        if (_datetime == null
                || _exposure == null
                || _inputFilePath == null)
            return null;

        // handle whitespaces
        String comment = StringUtils.wrapIfContainsWhitespace(_comment, "\"");

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(_datetime);
        inputDataArguments.add(_exposure);
        if (comment != null)
            inputDataArguments.add(comment);

        return inputDataArguments;
    }
}
