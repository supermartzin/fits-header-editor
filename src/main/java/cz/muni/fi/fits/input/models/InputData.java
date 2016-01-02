package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Abstract class representing input data got from input datasource
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class InputData {

    private final OperationType _operationType;
    private Collection<File> _fitsFiles;

    public InputData(OperationType operationType) {
        this(operationType, new HashSet<>());
    }

    public InputData(OperationType operationType, Collection<File> fitsFiles) {
        this._operationType = operationType;
        this._fitsFiles = fitsFiles;
    }

    public OperationType getOperationType() {
        return _operationType;
    }

    public Collection<File> getFitsFiles() {
        return _fitsFiles;
    }

    public void setFitsFiles(Collection<File> fitsFiles) {
        this._fitsFiles = fitsFiles;
    }
}