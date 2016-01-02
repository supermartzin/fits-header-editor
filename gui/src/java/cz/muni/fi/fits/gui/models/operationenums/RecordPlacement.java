package cz.muni.fi.fits.gui.models.operationenums;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public enum RecordPlacement implements PropertyBasedEnum {
    END("place.end"),
    INDEX("place.index");

    private String _propertyName;

    RecordPlacement(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
