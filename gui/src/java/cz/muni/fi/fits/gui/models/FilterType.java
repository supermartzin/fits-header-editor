package cz.muni.fi.fits.gui.models;

import cz.muni.fi.fits.gui.models.operationenums.PropertyBasedEnum;

/**
 * TODO insert description
 *
 * @author Martin Vrábel - © 2015 FITS Header Visual Editor
 * @version 1.0
 */
public enum FilterType implements PropertyBasedEnum {
    REMOVE("filter.type.remove"),
    KEEP("filter.type.keep");

    private final String _propertyName;

    FilterType(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return _propertyName;
    }
}
