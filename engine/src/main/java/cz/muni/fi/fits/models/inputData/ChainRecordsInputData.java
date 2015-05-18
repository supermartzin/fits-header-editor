package cz.muni.fi.fits.models.inputData;

import cz.muni.fi.fits.models.OperationType;
import cz.muni.fi.fits.utils.Tuple;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Class encapsulating input data for operation <b>Chain multiple records</b>
 *
 * @author Martin Vrábel
 * @version 1.2
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
     * @param skipIfChainKwNotExists    value indicating whether skip specific keyword in chain values
     *                                  if it does not already exist
     */
    public ChainRecordsInputData(String keyword, LinkedList<Tuple<ChainValueType, String>> chainValues, String comment, boolean updateIfExists, boolean skipIfChainKwNotExists) {
        this(keyword, chainValues, comment, updateIfExists, skipIfChainKwNotExists, new HashSet<>());
    }

    /**
     * Creates new {@link ChainRecordsInputData} object with specified chain data
     *
     * @param keyword                   keyword of chained record
     * @param chainValues               list of constant or keyword tuples to chain into record
     * @param comment                   comment of chained record, insert
     *                                  <code>null</code> if no comment to add
     * @param updateIfExists            value indicating whether update record if it does already exist
     * @param skipIfChainKwNotExists    value indicating whether skip specific keyword in chain values
     *                                  if it does not exist
     * @param fitsFiles                 FITS files in which chain multiple records
     */
    public ChainRecordsInputData(String keyword, LinkedList<Tuple<ChainValueType, String>> chainValues, String comment, boolean updateIfExists, boolean skipIfChainKwNotExists, Collection<File> fitsFiles) {
        super(OperationType.CHAIN_RECORDS, fitsFiles);
        this._keyword = keyword != null ? keyword.toUpperCase() : null;
        this._chainValues = chainValues;
        this._comment = comment;
        this._switches.put("updateIfExists", updateIfExists);
        this._switches.put("skipIfChainKwNotExists", skipIfChainKwNotExists);
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
     * Value indicating whether skip specific keyword in chain values if it does not exist in header
     *
     * @return  <code>true</code> when skip specific keyword if it does not exist
     *          <code>false</code> when do not skip specific keyword if it does not exist
     */
    public boolean skipIfChainKwNotExists() {
        return _switches.get("skipIfChainKwNotExists");
    }

    /**
     * Enumeration of types of chain values
     */
    public enum ChainValueType {
        CONSTANT,
        KEYWORD
    }
}
