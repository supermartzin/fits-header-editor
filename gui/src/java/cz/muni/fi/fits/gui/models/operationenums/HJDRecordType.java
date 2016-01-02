package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum HJDRecordType implements PropertyBasedEnum {
    KEYWORD("record.type.keyword"),
    DECIMAL_VALUE("record.type.decimal_value"),
    FULL_VALUE("record.type.full_value");

    private final String _propertyName;

    HJDRecordType(String propertyName) {
        this._propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
