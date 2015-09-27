package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.OperationType;
import cz.muni.fi.fits.models.TimeObject;
import cz.muni.fi.fits.common.utils.Constants;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Compute Heliocentric Julian Date</b>
 *
 * @author Martin Vrábel
 * @version 1.0.1
 */
public class ComputeHJDInputData extends InputData {

    private final Object _datetime;
    private final Object _exposure;
    private final Object _rightAscension;
    private final Object _declination;
    private final String _comment;

    /**
     * Creates new {@link ComputeHJDInputData} object with provided parameters
     *
     * @param datetime          {@link String} keyword of record which contains DateTime value for HJD
     *                          or {@link LocalDateTime} value
     * @param exposure          {@link String} keyword of record which contains Exposure value for HJD
     *                          or {@link Double} value
     * @param rightAscension    {@link String} keyword of right ascension of observed object
     *                          or {@link TimeObject} value
     * @param declination       {@link String} keyword of declination of observed object
     *                          or {@link DegreesObject} value
     * @param comment           comment of HJD value, insert <code>null</code>
     *                          for default value <b>"center of exposure"</b>
     */
    public ComputeHJDInputData(Object datetime, Object exposure,
                               Object rightAscension, Object declination, String comment) {
        this(datetime, exposure, rightAscension, declination, comment, new HashSet<>());
    }

    /**
     * Creates new {@link ComputeHJDInputData} object with provided parameters
     *
     * @param datetime          {@link String} keyword of record which contains DateTime value for HJD
     *                          or {@link LocalDateTime} value
     * @param exposure          {@link String} keyword of record which contains Exposure value for HJD
     *                          or {@link Double} value
     * @param rightAscension    {@link String} keyword of right ascension of observed object
     *                          or {@link TimeObject} value
     * @param declination       {@link String} keyword of declination of observed object
     *                          or {@link DegreesObject} value
     * @param comment           comment of HJD value, insert <code>null</code>
     *                          for default value <b>"center of exposure"</b>
     * @param fitsFiles         FITS files in which to shift time of time record
     */
    public ComputeHJDInputData(Object datetime, Object exposure,
                               Object rightAscension, Object declination,
                               String comment, Collection<File> fitsFiles) {
        super(OperationType.COMPUTE_HJD, fitsFiles);
        if (datetime != null && datetime instanceof String)
            _datetime = ((String) datetime).toUpperCase();
        else
            _datetime = datetime;

        if (exposure != null && exposure instanceof String)
            _exposure = ((String) exposure).toUpperCase();
        else
            _exposure = exposure;

        if (rightAscension != null && rightAscension instanceof String)
            _rightAscension = ((String) rightAscension).toUpperCase();
        else
            _rightAscension = rightAscension;

        if (declination != null && declination instanceof String)
            _declination = ((String) declination).toUpperCase();
        else
            _declination = declination;

        _comment = comment != null ? comment : Constants.DEFAULT_HJD_COMMENT;
    }

    public Object getDatetime() {
        return _datetime;
    }

    public Object getExposure() {
        return _exposure;
    }

    public Object getRightAscension() {
        return _rightAscension;
    }

    public Object getDeclination() {
        return _declination;
    }

    public String getComment() {
        return _comment;
    }
}
