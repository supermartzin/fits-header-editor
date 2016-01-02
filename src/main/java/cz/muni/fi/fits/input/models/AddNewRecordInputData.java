package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Add new record</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class AddNewRecordInputData extends SwitchInputData {

    private final String _keyword;
    private final Object _value;
    private final String _comment;

    /**
     * Creates new {@link AddNewRecordInputData} object with specified new record data
     *
     * @param keyword           keyword of new record to add
     * @param value             value of new record to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> if no comment to add
     * @param updateIfExists    value indicating whether update record
     *                          if one with the same keyword already exists
     */
    public AddNewRecordInputData(String keyword, Object value, String comment, boolean updateIfExists) {
        this(keyword, value, comment, updateIfExists, new HashSet<>());
    }

    /**
     * Creates new {@link AddNewRecordInputData} object with specified new record data
     *
     * @param keyword           keyword of new record to add
     * @param value             value of new record to add
     * @param comment           comment of new record to add, insert
     *                          <code>null</code> if no comment to add
     * @param updateIfExists    value indicating whether update record
     *                          if one with the same keyword already exists
     * @param fitsFiles         FITS files to which add new record
     */
    public AddNewRecordInputData(String keyword, Object value, String comment, boolean updateIfExists, Collection<File> fitsFiles) {
        super(OperationType.ADD_NEW_RECORD_TO_END, fitsFiles);
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
        this._value = value;
        this._comment = comment;
        this._switches.put("updateIfExists", updateIfExists);
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
     * Value indicating whether update record if one with the same keyword already exists
     *
     * @return  <code>true</code> when update record if already exists
     *          <code>false</code> when do not update record if already exists
     */
    public boolean updateIfExists() {
        return _switches.get("updateIfExists");
    }
}
