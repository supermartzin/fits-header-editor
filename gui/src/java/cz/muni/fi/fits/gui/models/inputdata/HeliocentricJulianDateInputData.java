package cz.muni.fi.fits.gui.models.inputdata;

import cz.muni.fi.fits.gui.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Compute Heliocentric Julian Date</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class HeliocentricJulianDateInputData extends InputDataBase {

    private final String _datetime;
    private final String _exposure;
    private final String _rightAscension;
    private final String _declination;
    private final String _comment;

    /**
     * TODO insert description
     *
     * @param datetime
     * @param exposure
     * @param rightAscension
     * @param declination
     * @param comment
     */
    public HeliocentricJulianDateInputData(String datetime, String exposure, String rightAscension, String declination, String comment) {
        _datetime = datetime;
        _exposure = exposure;
        _rightAscension = rightAscension;
        _declination = declination;
        _comment = comment;

        _operation = Operation.HJD;
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
                || _rightAscension == null
                || _declination == null
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
        inputDataArguments.add(_rightAscension);
        inputDataArguments.add(_declination);
        if (comment != null)
            inputDataArguments.add(comment);

        return inputDataArguments;
    }
}
