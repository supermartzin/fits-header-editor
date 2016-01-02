package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.ChainValueType;
import cz.muni.fi.fits.models.OperationType;
import cz.muni.fi.fits.common.utils.Tuple;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Class encapsulating input data for operation <b>Chain multiple records</b>
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public class ChainRecordsInputData extends SwitchInputData {

    private final String _keyword;
    private final LinkedList<Tuple<ChainValueType, String>> _chainValues;
    private final String _comment;

    /**
     * Creates new {@link ChainRecordsInputData} object with specified chain data
     *
     * @param keyword                   keyword of chained record
     * @param chainValues               list of constant or keyword tuples to chain into record
     * @param comment                   comment of chained record, insert
     *                                  <code>null</code> if no comment to add
     * @param updateIfExists            value indicating whether update record if it does already exists
     * @param allowLongstrings          value indicating whether allow longstring values in header
     *                                  if chained value is longer than basic limit
     */
    public ChainRecordsInputData(String keyword, LinkedList<Tuple<ChainValueType, String>> chainValues, String comment, boolean updateIfExists, boolean allowLongstrings) {
        this(keyword, chainValues, comment, updateIfExists, allowLongstrings, new HashSet<>());
    }

    /**
     * Creates new {@link ChainRecordsInputData} object with specified chain data
     *
     * @param keyword                   keyword of chained record
     * @param chainValues               list of constant or keyword tuples to chain into record
     * @param comment                   comment of chained record, insert
     *                                  <code>null</code> if no comment to add
     * @param updateIfExists            value indicating whether update record if it does already exist
     * @param allowLongstrings          value indicating whether allow longstring values in header
     *                                  if chained value is longer than basic limit
     * @param fitsFiles                 FITS files in which chain multiple records
     */
    public ChainRecordsInputData(String keyword, LinkedList<Tuple<ChainValueType, String>> chainValues, String comment, boolean updateIfExists, boolean allowLongstrings, Collection<File> fitsFiles) {
        super(OperationType.CHAIN_RECORDS, fitsFiles);
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
        this._chainValues = chainValues;
        this._comment = comment;
        this._switches.put("updateIfExists", updateIfExists);
        this._switches.put("allowLongstrings", allowLongstrings);
    }

    public String getKeyword() {
        return _keyword;
    }

    public LinkedList<Tuple<ChainValueType, String>> getChainValues() {
        return _chainValues;
    }

    public String getComment() {
        return _comment;
    }

    /**
     * Value indicating wheter update record if it does already exist
     *
     * @return  <code>true</code> when update record if already exists
     *          <code>false</code> when do not update record if already exists
     */
    public boolean updateIfExists() {
        return _switches.get("updateIfExists");
    }

    /**
     * Value indicating whether allow longstring values in header if chained value is longer than basic limit
     *
     * @return  <code>true</code> if longstring support is allowed
     *          <code>false</code> otherwise
     */
    public boolean longstringsAllowed() {
        return _switches.get("allowLongstrings");
    }
}
