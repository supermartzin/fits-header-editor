package cz.muni.fi.fits.input.models;

import cz.muni.fi.fits.models.OperationType;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class encapsulating input data for operation <b>Shift time of time record</b>
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ShiftTimeInputData extends InputData {

    private final String _keyword;
    private final int _yearShift;
    private final int _monthShift;
    private final int _dayShift;
    private final int _hourShift;
    private final int _minuteShift;
    private final int _secondShift;
    private final int _nanosecondsShift;

    /**
     * Creates new {@link ShiftTimeInputData} object with specified shift parameters
     *
     * @param keyword           keyword of time record in which to shift time
     * @param yearShift         time shift for years
     * @param monthShift        time shift for months
     * @param dayShift          time shift for days
     * @param hourShift         time shift for hours
     * @param minuteShift       time shift for minutes
     * @param secondShift       time shift for seconds
     * @param milisecondShift   time shift for nanoseconds
     */
    public ShiftTimeInputData(String keyword,
                              int yearShift,
                              int monthShift,
                              int dayShift,
                              int hourShift,
                              int minuteShift,
                              int secondShift,
                              int milisecondShift) {
        this(keyword, yearShift, monthShift, dayShift, hourShift, minuteShift, secondShift, milisecondShift, new HashSet<>());
    }

    /**
     * Creates new {@link ShiftTimeInputData} object with specified shift parameters
     *
     * @param keyword           keyword of time record in which to shift time
     * @param yearShift         time shift for years
     * @param monthShift        time shift for months
     * @param dayShift          time shift for days
     * @param hourShift         time shift for hours
     * @param minuteShift       time shift for minutes
     * @param secondShift       time shift for seconds
     * @param milisecondShift   time shift for nanoseconds
     * @param fitsFiles         FITS files in which to shift time of time record
     */
    public ShiftTimeInputData(String keyword,
                              int yearShift,
                              int monthShift,
                              int dayShift,
                              int hourShift,
                              int minuteShift,
                              int secondShift,
                              int milisecondShift,
                              Collection<File> fitsFiles) {
        super(OperationType.SHIFT_TIME, fitsFiles);
        _keyword = keyword != null ? keyword.toUpperCase() : null;
        _yearShift = yearShift;
        _monthShift = monthShift;
        _dayShift = dayShift;
        _hourShift = hourShift;
        _minuteShift = minuteShift;
        _secondShift = secondShift;
        _nanosecondsShift = milisecondShift * 1000 * 1000; // convert to nanoseconds
    }

    public String getKeyword() {
        return _keyword;
    }

    public int getYearShift() {
        return _yearShift;
    }

    public int getMonthShift() {
        return _monthShift;
    }

    public int getDayShift() {
        return _dayShift;
    }

    public int getHourShift() {
        return _hourShift;
    }

    public int getMinuteShift() {
        return _minuteShift;
    }

    public int getSecondShift() {
        return _secondShift;
    }

    public int getMilisecondsShift() {
        return _nanosecondsShift / 1000000;
    }

    public int getNanosecondShift() {
        return _nanosecondsShift;
    }
}
