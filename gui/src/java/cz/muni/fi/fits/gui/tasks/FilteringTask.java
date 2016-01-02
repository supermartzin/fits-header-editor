package cz.muni.fi.fits.gui.tasks;

import cz.muni.fi.fits.gui.FITSFile;
import cz.muni.fi.fits.gui.models.FilterType;
import cz.muni.fi.fits.gui.models.FileItem;
import cz.muni.fi.fits.gui.NomTamFitsFile;
import javafx.concurrent.Task;
import nom.tam.fits.FitsFactory;

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
public class FilteringTask extends Task<Set<FileItem>> {

    private final Collection<FileItem> _files;
    private final FilterType _filterType;
    private final String _keyword;
    private final String _value;

    public FilteringTask(Collection<FileItem> files, FilterType filterType,
                         String keyword, String value) {
        if (files == null)
            throw new IllegalArgumentException("Files collection is null");
        if (filterType == null)
            throw new IllegalArgumentException("Filter type is null");
        if (keyword == null)
            throw new IllegalArgumentException("Keyword is null");

        _files = new HashSet<>(files);
        _filterType = filterType;
        _keyword = keyword.toUpperCase();
        _value = value;
    }

    @Override
    protected Set<FileItem> call() throws Exception {
        // allow longstrings
        FitsFactory.setLongStringsEnabled(true);

        Set<FileItem> filesToRemove = new HashSet<>();

        _files.forEach(
                fileItem -> {
                    try (FITSFile fits = new NomTamFitsFile(fileItem.getFilepath())) {
                        boolean matchesFilter = false;

                        // find out if matches filter
                        if (_value != null)
                            matchesFilter = fits.containsRecordWithValue(_keyword, _value);
                        else
                            matchesFilter = fits.containsRecord(_keyword);

                        // remove or keep files with matching keyword/value
                        switch (_filterType) {
                            case KEEP:
                                if (!matchesFilter)
                                    filesToRemove.add(fileItem);
                                break;

                            case REMOVE:
                                if (matchesFilter)
                                    filesToRemove.add(fileItem);
                                break;
                        }
                    } catch (IOException ignored) { }
                });

        // return files to remove
        return filesToRemove;
    }
}
