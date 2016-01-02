package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum RemoveType implements PropertyBasedEnum {
    BY_KEYWORD("remove.by.keyword"),
    FROM_INDEX("remove.by.index");

    private final String _propertyName;

    RemoveType(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
