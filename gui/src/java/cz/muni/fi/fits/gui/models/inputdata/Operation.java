package cz.muni.fi.fits.gui.models.inputdata;

/**
 * TODO insert description
 *
 * @author Martin Vrábel - © 2015 FITS-HeaderVisualEditor
 * @version 1.0
 */
public enum Operation {
    ADD_TO_END("ADD"),
    ADD_TO_INDEX("ADD_IX"),
    CHANGE_VALUE("CHANGE"),
    CHANGE_KEYWORD("CHANGE_KW"),
    REMOVE_BY_KEYWORD("REMOVE"),
    REMOVE_FROM_INDEX("REMOVE_IX"),
    SHIFT_TIME("SHIFT_TIME"),
    CHAIN_RECORDS("CHAIN"),
    HJD("HJD"),
    JD("JD");

    private final String _stringValue;

    Operation(String stringValue) {
        _stringValue = stringValue;
    }

    public String getStringValue() {
        return _stringValue;
    }
}
