package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Change value of record by keyword</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChangeValueByKeywordInputData extends SwitchInputData {

    private final String _keyword;
    private final Object _value;
    private final String _comment;

    /**
     * Creates new {@link ChangeKeywordInputData} object with specified change value data
     *
     * @param keyword           keyword of record in which to change the value
     * @param value             value to change in record
     * @param comment           comment of changed record, insert <code>null</code>
     *                          if want to use original comment
     * @param addNewIfNotExists value indicating whether add new record to header
     *                          if one with keyword does not exists
     */
    public ChangeValueByKeywordInputData(String keyword, Object value, String comment, boolean addNewIfNotExists) {
        this(keyword, value, comment, addNewIfNotExists, new HashSet<>());
    }

    /**
     * Creates new {@link ChangeKeywordInputData} object with specified change value data
     *
     * @param keyword           keyword of record in which to change the value
     * @param value             value to change in record
     * @param comment           comment of changed record, insert <code>null</code>
     *                          if want to use original comment
     * @param addNewIfNotExists value indicating whether add new record to header
     *                          if one with keyword does not exists
     * @param fitsFiles         FITS files in which change value of record
     */
    public ChangeValueByKeywordInputData(String keyword, Object value, String comment, boolean addNewIfNotExists, Collection<File> fitsFiles) {
        super(OperationType.CHANGE_VALUE_BY_KEYWORD, fitsFiles);
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
        this._value = value;
        this._comment = comment;
        this._switches.put("addNewIfNotExists", addNewIfNotExists);
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
     * Value indicating whether to add new record if the one with keyword does not exists
     *
     * @return  <code>true</code> when add new record if it does not exist
     *          <code>false</code> when do not add new record if it does not exist
     */
    public boolean addNewIfNotExists() {
        return _switches.get("addNewIfNotExists");
    }
}
