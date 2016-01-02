package cz.muni.fi.fits.gui.models.inputdata;

import cz.muni.fi.fits.gui.models.operationenums.ChainValueType;
import cz.muni.fi.fits.gui.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChainRecordsInputData extends InputDataBase {

    private final String _keyword;
    private final boolean _updateIfExists;
    private final boolean _longstringsSupport;
    private final List<ChainTuple> _valuesToChain;
    private final String _comment;

    public ChainRecordsInputData(String keyword, boolean updateIfExists, boolean longstringsSupport,
                                 List<ChainTuple> valuesToChain, String comment) {
        _keyword = keyword;
        _updateIfExists = updateIfExists;
        _longstringsSupport = longstringsSupport;
        _valuesToChain = valuesToChain;
        _comment = comment;

        _operation = Operation.CHAIN_RECORDS;
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
                || _valuesToChain == null
                || _valuesToChain.isEmpty()
                || _inputFilePath == null)
            return null;

        // created ordered list
        List<String> inputDataArguments = new LinkedList<>();
        inputDataArguments.add(_operation.getStringValue());
        if (_updateIfExists)
            inputDataArguments.add("-u");
        if (_longstringsSupport)
            inputDataArguments.add("-l");
        inputDataArguments.add(_inputFilePath);
        inputDataArguments.add(_keyword.toUpperCase());

        for (ChainTuple chainTuple : _valuesToChain) {
            // construct value
            String value = "";
            switch (chainTuple.getValueType()) {
                case KEYWORD:
                    value += "-k=";
                    break;
                case CONSTANT:
                    value += "-c=";
                    break;
            }
            value += chainTuple.getValue();

            // handle whitespaces
            value = StringUtils.wrapIfContainsWhitespace(value, "\"");

            inputDataArguments.add(value);
        }

        // handle whitespaces in comment
        String comment = StringUtils.wrapIfContainsWhitespace(_comment, "\"");
        if (comment != null)
            inputDataArguments.add(comment);

        return inputDataArguments;
    }


    /**
     * TODO insert description
     *
     * @author Martin Vrábel
     * @version 1.0
     */
    public static class ChainTuple {

        private final ChainValueType _valueType;
        private final String _value;

        public ChainTuple(final ChainValueType valueType, final String value) {
            _valueType = valueType;
            _value = value;
        }

        public ChainValueType getValueType() {
            return _valueType;
        }

        public String getValue() {
            return _value;
        }
    }
}
