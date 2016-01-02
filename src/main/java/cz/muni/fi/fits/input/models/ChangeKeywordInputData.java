package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Change keyword of record</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChangeKeywordInputData extends SwitchInputData {

    private final String _oldKeyword;
    private final String _newKeyword;

    /**
     * Creates new {@link ChangeKeywordInputData} object with specified change keyword data
     *
     * @param oldKeyword                old keyword of record in which to change keyword
     * @param newKeyword                new keyword that will replace old keyword in record
     * @param removeValueOfNewIfExists  value indicating whether to remove record with
     *                                  <code>new keyword</code> if already exists in header
     */
    public ChangeKeywordInputData(String oldKeyword, String newKeyword, boolean removeValueOfNewIfExists) {
        this(oldKeyword, newKeyword, removeValueOfNewIfExists, new HashSet<>());
    }

    /**
     * Creates new {@link ChangeKeywordInputData} object with specified change keyword data
     *
     * @param oldKeyword                old keyword of record in which to change keyword
     * @param newKeyword                new keyword that will replace old keyword in record
     * @param removeValueOfNewIfExists  value indicating whether to remove record with
     *                                  <code>newKeyword</code> if already exists in header
     * @param fitsFiles                 FITS files in which change keyword
     */
    public ChangeKeywordInputData(String oldKeyword, String newKeyword, boolean removeValueOfNewIfExists, Collection<File> fitsFiles) {
        super(OperationType.CHANGE_KEYWORD, fitsFiles);
        this._oldKeyword = oldKeyword != null ? oldKeyword.toUpperCase() : null;
        this._newKeyword = newKeyword != null ? newKeyword.toUpperCase() : null;
        this._switches.put("removeValueOfNewIfExists", removeValueOfNewIfExists);
    }

    public String getOldKeyword() {
        return _oldKeyword;
    }

    public String getNewKeyword() {
        return _newKeyword;
    }

    /**
     * Value indicating whether to remove record with new keyword if already exists in header
     *
     * @return  <code>true</code> when remove record with new keyword if exists
     *          <code>false</code> when do not remove record with new keyword if exists
     */
    public boolean removeValueOfNewIfExists() {
        return _switches.get("removeValueOfNewIfExists");
    }
}
