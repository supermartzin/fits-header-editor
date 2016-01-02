package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Remove record by keyword</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class RemoveByKeywordInputData extends InputData {

    private final String _keyword;

    /**
     * Creates new {@link RemoveByKeywordInputData} object with specified remove record data
     *
     * @param keyword   keyword of record to remove
     */
    public RemoveByKeywordInputData(String keyword) {
        this(keyword, new HashSet<>());
    }

    /**
     * Creates new {@link RemoveByKeywordInputData} object with specified remove record data
     *
     * @param keyword   keyword of record to remove
     * @param fitsFiles FITS files from which remove record
     */
    public RemoveByKeywordInputData(String keyword, Collection<File> fitsFiles) {
        super(OperationType.REMOVE_RECORD_BY_KEYWORD, fitsFiles);
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
    }

    public String getKeyword() {
        return _keyword;
    }
}
