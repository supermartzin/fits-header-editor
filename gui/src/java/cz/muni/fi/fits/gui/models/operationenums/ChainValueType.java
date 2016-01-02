package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum ChainValueType implements PropertyBasedEnum {
    KEYWORD("record.type.keyword"),
    CONSTANT("record.type.constant");

    private final String _propertyName;

    ChainValueType(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
