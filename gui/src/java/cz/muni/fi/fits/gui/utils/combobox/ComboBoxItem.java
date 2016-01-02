package cz.muni.fi.fits.gui.utils.combobox;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ComboBoxItem<T> {

    private T _type;
    private String _value;

    public ComboBoxItem(T type, String value) {
        _type = type;
        _value = value;
    }

    public T getType() {
        return _type;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value;
    }
}
