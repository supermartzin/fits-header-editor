package cz.muni.fi.fits.gui;

import java.io.Closeable;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface FITSFile extends Closeable {

    /**
     *
     * @param recordKey
     * @return
     */
    boolean containsRecord(String recordKey);

    /**
     *
     * @param recordKey
     * @param recordValue
     * @return
     */
    boolean containsRecordWithValue(String recordKey, String recordValue);
}
