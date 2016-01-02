package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum ValueType implements PropertyBasedEnum {
    STRING("value.type.string"),
    NUMBER("value.type.number"),
    DATETIME("value.type.datetime"),
    DATE("value.type.date"),
    TIME("value.type.time"),
    BOOLEAN("value.type.boolean");

    private final String _propertyName;

    ValueType(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
