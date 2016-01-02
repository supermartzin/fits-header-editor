package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Remove record from index</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class RemoveFromIndexInputData extends InputData {

    private final int _index;

    /**
     * Creates new {@link RemoveFromIndexInputData} object with specified remove record data
     *
     * @param index index of record to remove
     */
    public RemoveFromIndexInputData(int index) {
        this(index, new HashSet<>());
    }

    /**
     * Creates new {@link RemoveFromIndexInputData} object with specified remove record data
     *
     * @param index     index of record to remove
     * @param fitsFiles FITS files from which remove record on index
     */
    public RemoveFromIndexInputData(int index, Collection<File> fitsFiles) {
        super(OperationType.REMOVE_RECORD_FROM_INDEX, fitsFiles);
        this._index = index;
    }

    public int getIndex() {
        return _index;
    }
}
