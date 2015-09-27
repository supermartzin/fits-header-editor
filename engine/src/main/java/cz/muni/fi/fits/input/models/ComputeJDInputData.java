package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;
import cz.muni.fi.fits.common.utils.Constants;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Compute Julian Date</b>
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ComputeJDInputData extends InputData {

    private final Object _datetime;
    private final Object _exposure;
    private final String _comment;

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  keyword of record which contains DateTime value for JD
     * @param exposure  keyword of record which contains Exposure value for JD
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     */
    public ComputeJDInputData(String datetime, String exposure, String comment) {
        this(datetime, exposure, comment, new HashSet<>());
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  keyword of record which contains DateTime value for JD
     * @param exposure  value of Exposure in seconds
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     */
    public ComputeJDInputData(String datetime, double exposure, String comment) {
        this(datetime, exposure, comment, new HashSet<>());
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  {@link LocalDateTime} value
     * @param exposure  keyword of record which contains Exposure value for JD
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     */
    public ComputeJDInputData(LocalDateTime datetime, String exposure, String comment) {
        this(datetime, exposure, comment, new HashSet<>());
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  {@link LocalDateTime} value
     * @param exposure  value of Exposure in seconds
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     */
    public ComputeJDInputData(LocalDateTime datetime, double exposure, String comment) {
        this(datetime, exposure, comment, new HashSet<>());
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  keyword of record which contains DateTime value for JD
     * @param exposure  keyword of record which contains Exposure value for JD
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     * @param fitsFiles FITS files in which to shift time of time record
     */
    public ComputeJDInputData(String datetime, String exposure, String comment, Collection<File> fitsFiles) {
        super(OperationType.COMPUTE_JD, fitsFiles);
        _datetime = datetime != null ? datetime.toUpperCase() : null;
        _exposure = exposure != null ? exposure.toUpperCase() : null;
        _comment = comment == null ? Constants.DEFAULT_JD_COMMENT : comment;
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  keyword of record which contains DateTime value for JD
     * @param exposure  value of Exposure in seconds
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     * @param fitsFiles FITS files in which to shift time of time record
     */
    public ComputeJDInputData(String datetime, double exposure, String comment, Collection<File> fitsFiles) {
        super(OperationType.COMPUTE_JD, fitsFiles);
        _datetime = datetime != null ? datetime.toUpperCase() : null;
        _exposure = exposure;
        _comment = comment == null ? Constants.DEFAULT_JD_COMMENT : comment;
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  {@link LocalDateTime} value
     * @param exposure  keyword of record which contains Exposure value for JD
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     * @param fitsFiles FITS files in which to shift time of time record
     */
    public ComputeJDInputData(LocalDateTime datetime, String exposure, String comment, Collection<File> fitsFiles) {
        super(OperationType.COMPUTE_JD, fitsFiles);
        _datetime = datetime;
        _exposure = exposure != null ? exposure.toUpperCase() : null;
        _comment = comment == null ? Constants.DEFAULT_JD_COMMENT : comment;
    }

    /**
     * Creates new {@link ComputeJDInputData} object with provided parameters
     *
     * @param datetime  {@link LocalDateTime} value
     * @param exposure  value of Exposure in seconds
     * @param comment   comment of JD value, insert <code>null</code>
     *                  for default value <b>"center of exposure"</b>
     * @param fitsFiles FITS files in which to shift time of time record
     */
    public ComputeJDInputData(LocalDateTime datetime, double exposure, String comment, Collection<File> fitsFiles) {
        super(OperationType.COMPUTE_JD, fitsFiles);
        _datetime = datetime;
        _exposure = exposure;
        _comment = comment == null ? Constants.DEFAULT_JD_COMMENT : comment;
    }

    public Object getDatetime() {
        return _datetime;
    }

    public Object getExposure() {
        return _exposure;
    }

    public String getComment() {
        return _comment;
    }
}
