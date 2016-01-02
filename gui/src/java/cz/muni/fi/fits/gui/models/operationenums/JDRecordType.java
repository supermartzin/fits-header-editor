package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel - © 2015 FITS-HeaderVisualEditor
 * @version 1.0
 */
public enum JDRecordType implements PropertyBasedEnum {
    KEYWORD("record.type.keyword"),
    VALUE("record.type.value");

    private final String _propertyName;

    JDRecordType(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
