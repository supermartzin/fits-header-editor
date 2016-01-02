package cz.muni.fi.fits.common.utils;

import com.google.common.base.Objects;

/**
 * Class defining simple object pair
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class Tuple<Type1, Type2> {

    private final Type1 _first;
    private final Type2 _second;

    /**
     * Creates new Tuple object with specified types
     *
     * @param first     object on first place of tuple
     * @param second    object on second place of tuple
     */
    public Tuple(Type1 first, Type2 second) {
        this._first = first;
        this._second = second;
    }

    public Type1 getFirst() {
        return _first;
    }

    public Type2 getSecond() {
        return _second;
    }

    @Override
    public String toString() {
        return "<" + _first.toString() + " - " + _second.toString() + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equal(_first, tuple._first) &&
                Objects.equal(_second, tuple._second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_first, _second);
    }
}
