package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum ShiftDirection implements PropertyBasedEnum {
    FORWARD("direction.forward"),
    BACKWARD("direction.backward");

    private final String _propertyName;

    ShiftDirection(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
