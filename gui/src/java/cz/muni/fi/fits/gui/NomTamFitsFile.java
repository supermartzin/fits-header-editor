package cz.muni.fi.fits.gui;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;

import java.io.IOException;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class NomTamFitsFile implements FITSFile {

    private final Fits _fitsFile;
    private final Header _fitsHeader;

    public NomTamFitsFile(String fitsFilename)
            throws IOException {
        try {
            _fitsFile = new Fits(fitsFilename);

            // get header of first HDU unit
            _fitsHeader = _fitsFile.getHDU(0).getHeader();
        } catch (FitsException fEx) {
            throw new IOException(fEx);
        }
    }

    @Override
    public boolean containsRecord(String recordKey) {
        if (recordKey != null)
            return _fitsHeader.containsKey(recordKey);
        else
            return false;
    }

    @Override
    public boolean containsRecordWithValue(String recordKey, String recordValue) {
        return recordKey != null
                && recordValue != null
                && _fitsHeader.containsKey(recordKey)
                && _fitsHeader.findCard(recordKey).getValue().contains(recordValue);
    }

    @Override
    public void close() throws IOException {
        _fitsFile.close();
    }
}
