package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Add new record to specific index</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class AddNewToIndexInputData extends SwitchInputData {

    private final int _index;
    private final String _keyword;
    private final Object _value;
    private final String _comment;

    /**
     * Creates new {@link AddNewToIndexInputData} object with specified new record data
     *
     * @param index             specific index where to add new record
     * @param keyword           keyword of new record to add
     * @param value             value of new record to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> if no comment to add
     * @param removeOldIfExists value indicating whether remove old record
     *                          if one with the same keyword already exists
     */
    public AddNewToIndexInputData(int index, String keyword, Object value, String comment, boolean removeOldIfExists) {
        this(index, keyword, value, comment, removeOldIfExists, new HashSet<>());
    }

    /**
     * Creates new {@link AddNewToIndexInputData} object with specified new record data
     *
     * @param index             specific index where to add new record
     * @param keyword           keyword of new record to add
     * @param value             value of new record to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> if no comment to add
     * @param removeOldIfExists value indicating whether remove old record
     *                          if one with the same keyword already exists
     * @param fitsFiles         FITS files to which add new record
     */
    public AddNewToIndexInputData(int index, String keyword, Object value, String comment, boolean removeOldIfExists, Collection<File> fitsFiles) {
        super(OperationType.ADD_NEW_RECORD_TO_INDEX, fitsFiles);
        this._index = index;
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
        this._value = value;
        this._comment = comment;
        this._switches.put("removeOldIfExists", removeOldIfExists);
    }

    public int getIndex() {
        return this._index;
    }

    public String getKeyword() {
        return _keyword;
    }

    public Object getValue() {
        return _value;
    }

    public String getComment() {
        return _comment;
    }

    /**
     * Value indicating whether remove old record if one with the same keyword already exists
     *
     * @return  <code>true</code> when remove old record if already exists
     *          <code>false</code> when do not remove old record if already exists
     */
    public boolean removeOldIfExists() {
        return _switches.get("removeOldIfExists");
    }
}