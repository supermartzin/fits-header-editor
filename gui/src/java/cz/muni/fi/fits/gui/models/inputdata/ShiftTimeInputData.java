package cz.muni.fi.fits.gui.models.inputdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing input data for operation <code>Shift time of time record</code>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ShiftTimeInputData extends InputDataBase {

    private final String _keyword;
    private final int _years;
    private final int _months;
    private final int _days;
    private final int _hours;
    private final int _minutes;
    private final int _seconds;
    private final int _milliseconds;

    /**
     * TODO insert description
     *
     * @param keyword
     * @param years
     * @param months
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @param milliseconds
     */
    public ShiftTimeInputData(String keyword, int years, int months, int days,
                              int hours, int minutes, int seconds, int milliseconds) {
        _keyword = keyword;
        _years = years;
        _months = months;
        _days = days;
        _hours = hours;
        _minutes = minutes;
        _seconds = seconds;
        _milliseconds = milliseconds;

        _operation = Operation.SHIFT_TIME;
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
                || _inputFilePath == null
                || shiftValuesNotSet())
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(_keyword.toUpperCase());
        if (_years != Integer.MIN_VALUE)
            inputDataArguments.add("-y=" + _years);
        if (_months != Integer.MIN_VALUE)
            inputDataArguments.add("-m=" + _months);
        if (_days != Integer.MIN_VALUE)
            inputDataArguments.add("-d=" + _days);
        if (_hours != Integer.MIN_VALUE)
            inputDataArguments.add("-h=" + _hours);
        if (_minutes != Integer.MIN_VALUE)
            inputDataArguments.add("-min=" + _minutes);
        if (_seconds != Integer.MIN_VALUE)
            inputDataArguments.add("-s=" + _years);
        if (_milliseconds != Integer.MIN_VALUE)
            inputDataArguments.add("-ms=" + _milliseconds);

        return inputDataArguments;
    }

    private boolean shiftValuesNotSet() {
        return _years == Integer.MIN_VALUE
                && _months == Integer.MIN_VALUE
                && _days == Integer.MIN_VALUE
                && _hours == Integer.MIN_VALUE
                && _minutes == Integer.MIN_VALUE
                && _seconds == Integer.MIN_VALUE
                & _milliseconds == Integer.MIN_VALUE;
    }
}
