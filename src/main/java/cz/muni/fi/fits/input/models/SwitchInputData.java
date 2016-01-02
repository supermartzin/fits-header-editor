package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Abstract class representing input data with one or more switches,
 * extends {@link InputData} model class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public abstract class SwitchInputData extends InputData {

    protected final Map<String, Boolean> _switches;

    public SwitchInputData(OperationType operationType) {
        this(operationType, new HashSet<>());
    }

    public SwitchInputData(OperationType operationType, Collection<File> fitsFiles) {
        super(operationType, fitsFiles);
        this._switches = new HashMap<>();
    }
}
