package cz.muni.fi.fits.gui.services;

import cz.muni.fi.fits.gui.models.FilterType;
import cz.muni.fi.fits.gui.models.FitsFile;
import javafx.concurrent.Task;
import nom.tam.fits.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FilteringTask extends Task<Set<FitsFile>> {

    private final Collection<FitsFile> _fitsFiles;
    private final FilterType _filterType;
    private final String _keyword;
    private final String _value;

    public FilteringTask(Collection<FitsFile> fitsFiles, FilterType filterType,
                         String keyword, String value) {
        if (fitsFiles == null)
            throw new IllegalArgumentException("FitsFiles collection is null");
        if (filterType == null)
            throw new IllegalArgumentException("Filter type is null");
        if (keyword == null)
            throw new IllegalArgumentException("Keyword is null");

        _fitsFiles = new HashSet<>(fitsFiles);
        _filterType = filterType;
        _keyword = keyword.toUpperCase();
        _value = value;
    }

    @Override
    protected Set<FitsFile> call() throws Exception {
        // allow longstrings
        FitsFactory.setLongStringsEnabled(true);

        Set<FitsFile> filesToRemove = new HashSet<>();

        _fitsFiles.forEach(
                fitsFile -> {
                    try (Fits fits = new Fits(fitsFile.getFilepath())) {
                        boolean matchesFilter = false;

                        // get header of first HDU unit
                        BasicHDU hdu = fits.getHDU(0);
                        Header header = hdu.getHeader();

                        // find out if matches filter
                        if (_value != null) {
                            matchesFilter = header.containsKey(_keyword)
                                    && header.findCard(_keyword).getValue().equals(_value);
                        } else {
                            matchesFilter = header.containsKey(_keyword);
                        }

                        // remove or keep files with matching keyword/value
                        switch (_filterType) {
                            case KEEP:
                                if (!matchesFilter)
                                    filesToRemove.add(fitsFile);
                                break;

                            case REMOVE:
                                if (matchesFilter)
                                    filesToRemove.add(fitsFile);
                                break;
                        }
                    } catch (IOException | FitsException ignored) { }
                });

        // return files to remove
        return filesToRemove;
    }
}
