package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.TimeObject;
import cz.muni.fi.fits.models.inputData.*;
import cz.muni.fi.fits.utils.Tuple;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Internal class used as helper for {@link CmdArgumentsProcessor} class
 * that helps to extract input data to specific operation
 *
 * @author Martin Vrábel
 * @version 1.3.5
 */
final class CmdArgumentsProcessorHelper {

    /**
     * Extracts files or paths to files for processing and return them as a collection of unique {@link File} objects.
     * If path does not exist it's skipped
     *
     * @param path                          path to input file or directory where the files are specified
     * @return                              unmodifiable collection of unique {@link File} objects representing extracted files
     * @throws IllegalInputDataException    when input file or directory is in invalid form
     */
    static Collection<File> extractFilesData(String path) throws IllegalInputDataException {
        if (path == null)
            throw new IllegalArgumentException("path is null");

        Collection<File> fitsFiles = new HashSet<>();

        boolean isFile = true;
        // check whether it is a file or directory
        File inputFile = new File(path);
        if (!inputFile.exists())
            throw new IllegalInputDataException("Input file or directory '" + path + "' does not exist");
        if (inputFile.isDirectory()) isFile = false;

        if (isFile) {
            // read paths to FITS files
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    // ignore commented lines
                    if (line.startsWith("#")) continue;

                    File fitsFile = new File(line);
                    // check if file or directory
                    if (Files.isRegularFile(fitsFile.toPath())) {
                        // add file directly
                        fitsFiles.add(fitsFile);
                    } else if (Files.isDirectory(fitsFile.toPath())) {
                        // load all files in directory
                        Files.walk(fitsFile.toPath(), 1).forEach(file -> {
                            if (Files.isRegularFile(file))
                                fitsFiles.add(new File(file.toUri()));
                        });
                    }
                }
            } catch (FileNotFoundException fnfEx) {
                throw new IllegalInputDataException("Input file '" + path + "' does not exist", fnfEx);
            } catch (IOException ioEx) {
                throw new IllegalInputDataException("Error reading input file '" + path + "'", ioEx);
            }
        } else {
            // read files in directory
            try {
                Files.walk(Paths.get(path), 1).forEach(filePath -> {
                    if (Files.isRegularFile(filePath))
                        fitsFiles.add(new File(filePath.toUri()));
                });
            } catch (IOException ioEx) {
                throw new IllegalInputDataException("Error reading input directory with files", ioEx);
            } catch (InvalidPathException ipEx) {
                throw new IllegalInputDataException("Path to input directory is invalid", ipEx);
            } catch (SecurityException sEx) {
                throw new IllegalInputDataException("Cannot access input directory - insufficient permissions", sEx);
            }
        }

        return Collections.unmodifiableCollection(fitsFiles);
    }

    /**
     * Extracts input data for operation <b>Add new record</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @param converter                     {@link TypeConverter} object used to convert type of value in new record
     * @return                              {@link AddNewRecordInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static AddNewRecordInputData extractAddNewRecordData(String[] cmdArgs, TypeConverter converter) throws IllegalInputDataException {
        // get switch (optional)
        boolean updateIfExists = false;
        String switchParam = cmdArgs[1].toLowerCase().trim();
        if (switchParam.startsWith("-")) {
            if (switchParam.equals("-u"))
                updateIfExists = true;
            else
                throw new InvalidSwitchParameterException(switchParam, "Switch parameter is in invalid format: '" + switchParam + "'. Correct format is '-u'");
        }

        if (!updateIfExists) {
            if (cmdArgs.length < 4 || cmdArgs.length > 5)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'ADD'");
        } else {
            if (cmdArgs.length < 5 || cmdArgs.length > 6)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'ADD'");
        }

        // get keyword of new record (required)
        String keyword = !updateIfExists ? cmdArgs[2].trim() : cmdArgs[3].trim();

        // get value of new record (required)
        String strValue = !updateIfExists ? cmdArgs[3].trim() : cmdArgs[4].trim();
        Object value;
        // integer
        if (converter.tryParseInt(strValue))
            value = converter.parseInt(strValue);
        // long
        else if (converter.tryParseLong(strValue))
            value = converter.parseLong(strValue);
        // BigInteger
        else if (converter.tryParseBigInteger(strValue))
            value = converter.parseBigInteger(strValue);
        // double
        else if (converter.tryParseDouble(strValue))
            value = converter.parseDouble(strValue);
        // BigDecimal
        else if (converter.tryParseBigDecimal(strValue))
            value = converter.parseBigDecimal(strValue);
        // boolean
        else if (converter.tryParseBoolean(strValue))
            value = converter.parseBoolean(strValue);
        // String
        else
            value = strValue;

        // get comment of new record (optional)
        String comment = null;
        if (!updateIfExists && cmdArgs.length == 5)
            comment = cmdArgs[4].trim();
        else if (updateIfExists && cmdArgs.length == 6)
            comment = cmdArgs[5].trim();

        return new AddNewRecordInputData(keyword, value, comment, updateIfExists);
    }

    /**
     * Extracts input data for operation <b>Add new record to specific index</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @param converter                     {@link TypeConverter} object used to convert type of value in new record
     * @return                              {@link AddNewToIndexInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static AddNewToIndexInputData extractAddNewToIndexData(String[] cmdArgs, TypeConverter converter) throws IllegalInputDataException {
        // get switch (optional)
        boolean removeOldIfExists = false;
        String switchParam  = cmdArgs[1].toLowerCase().trim();
        if (switchParam.startsWith("-")) {
            if (switchParam.equals("-rm"))
                removeOldIfExists = true;
            else
                throw new InvalidSwitchParameterException(switchParam, "Switch parameter is in invalid format: '" + switchParam + "'. Correct format is '-rm'");
        }

        if (!removeOldIfExists) {
            if (cmdArgs.length < 5 || cmdArgs.length > 6)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'ADD_IX'");
        } else {
            if (cmdArgs.length < 6 || cmdArgs.length > 7)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'ADD_IX'");
        }

        // get index where to add new record (required)
        int index;
        String indexString = !removeOldIfExists ? cmdArgs[2].trim() : cmdArgs[3].trim();
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException nfEx) {
            throw new IllegalInputDataException("Index is in invalid format: " + indexString, nfEx);
        }

        // get keyword of new record (required)
        String keyword = !removeOldIfExists ? cmdArgs[3].trim() : cmdArgs[4].trim();

        // get value of new record (required)
        String strValue = !removeOldIfExists ? cmdArgs[4].trim() : cmdArgs[5].trim();
        Object value;
        // integer
        if (converter.tryParseInt(strValue))
            value = converter.parseInt(strValue);
        // long
        else if (converter.tryParseLong(strValue))
            value = converter.parseLong(strValue);
        // BigInteger
        else if (converter.tryParseBigInteger(strValue))
            value = converter.parseBigInteger(strValue);
        // double
        else if (converter.tryParseDouble(strValue))
            value = converter.parseDouble(strValue);
        // BigDecimal
        else if (converter.tryParseBigDecimal(strValue))
            value = converter.parseBigDecimal(strValue);
        // boolean
        else if (converter.tryParseBoolean(strValue))
            value = converter.parseBoolean(strValue);
        // String
        else
            value = strValue;

        // get comment of new record (optional)
        String comment = null;
        if (!removeOldIfExists && cmdArgs.length == 6)
            comment = cmdArgs[5].trim();
        else if (removeOldIfExists & cmdArgs.length == 7)
            comment = cmdArgs[6].trim();

        return new AddNewToIndexInputData(index, keyword, value, comment, removeOldIfExists);
    }

    /**
     * Extracts input data for operation <b>Remove record by keyword</b>
     *
     * @param cmdArgs                           commandline arguments containing specific input data
     * @return                                  {@link RemoveByKeywordInputData} object with input data
     * @throws WrongNumberOfParametersException when input data are in invalid form
     */
    static RemoveByKeywordInputData extractRemoveByKeywordData(String[] cmdArgs) throws WrongNumberOfParametersException {
        if (cmdArgs.length != 3)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'REMOVE'");

        // get keyword of record to remove (required)
        String keyword = cmdArgs[2].trim();

        return new RemoveByKeywordInputData(keyword);
    }

    /**
     * Extracts input data for operation <b>Remove record from specific index</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @return                              {@link RemoveFromIndexInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static RemoveFromIndexInputData extractRemoveFromIndexData(String[] cmdArgs) throws IllegalInputDataException {
        if (cmdArgs.length != 3)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'REMOVE_IX'");

        // get index where to add new record (required)
        int index;
        String indexString = cmdArgs[2].trim();
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException nfEx) {
            throw new IllegalInputDataException("Index is in invalid format: " + indexString, nfEx);
        }

        return new RemoveFromIndexInputData(index);
    }

    /**
     * Extracts input data for operation <b>Change keyword of record</b>
     *
     * @param cmdArgs                           commandline arguments containing specific input data
     * @return                                  {@link ChangeKeywordInputData} object with input data
     * @throws WrongNumberOfParametersException when number of provided arguments is not sufficient
     * @throws InvalidSwitchParameterException  when switch argument is in invalid form
     */
    static ChangeKeywordInputData extractChangeKeywordData(String[] cmdArgs) throws WrongNumberOfParametersException, InvalidSwitchParameterException {
        // get switch (optional)
        boolean removeValueOfNewIfExists = false;
        String switchParam = cmdArgs[1].toLowerCase().trim();
        if (switchParam.startsWith("-")) {
            if (switchParam.equals("-rm"))
                removeValueOfNewIfExists = true;
            else
                throw new InvalidSwitchParameterException(switchParam, "Switch parameter is in invalid format: '" + switchParam + "'. Correct format is '-rm'");
        }

        if (!removeValueOfNewIfExists) {
            if (cmdArgs.length != 4)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHANGE_KW'");
        } else {
            if (cmdArgs.length != 5)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHANGE_KW'");
        }

        // get old keyword (required)
        String oldKeyword = !removeValueOfNewIfExists ? cmdArgs[2].trim() : cmdArgs[3].trim();

        // get new keyword (required)
        String newKeyword = !removeValueOfNewIfExists ? cmdArgs[3].trim() : cmdArgs[4].trim();

        return new ChangeKeywordInputData(oldKeyword, newKeyword, removeValueOfNewIfExists);
    }

    /**
     * Extracts input data for operation <b>Change keyword of record</b>
     *
     * @param cmdArgs                           commandline arguments containing specific input data
     * @param converter                         {@link TypeConverter} object used to convert type of new value in record to change
     * @return                                  {@link ChangeValueByKeywordInputData} object with input data
     * @throws WrongNumberOfParametersException when number of provided arguments is not sufficient
     * @throws InvalidSwitchParameterException  when switch argument is in invalid form
     */
    static ChangeValueByKeywordInputData extractChangeValueByKeywordData(String[] cmdArgs, TypeConverter converter) throws WrongNumberOfParametersException, InvalidSwitchParameterException {
        // get switch (optional)
        boolean addNewIfNotExists = false;
        String switchParam = cmdArgs[1].toLowerCase().trim();
        if (switchParam.startsWith("-")) {
            if (switchParam.equals("-a"))
                addNewIfNotExists = true;
            else
                throw new InvalidSwitchParameterException(switchParam, "Switch parameter is in invalid format: '" + switchParam + "'. Correct format is '-a'");
        }

        if (!addNewIfNotExists) {
            if (cmdArgs.length < 4 || cmdArgs.length > 5)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHANGE'");
        } else {
            if (cmdArgs.length < 5 || cmdArgs.length > 6)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHANGE'");
        }

        // get keyword of changing record (required)
        String keyword = !addNewIfNotExists ? cmdArgs[2].trim() : cmdArgs[3].trim();

        // get value of changing record (required)
        String strValue = !addNewIfNotExists ? cmdArgs[3].trim() : cmdArgs[4].trim();
        Object value;
        // integer
        if (converter.tryParseInt(strValue))
            value = converter.parseInt(strValue);
        // long
        else if (converter.tryParseLong(strValue))
            value = converter.parseLong(strValue);
        // BigInteger
        else if (converter.tryParseBigInteger(strValue))
            value = converter.parseBigInteger(strValue);
        // double
        else if (converter.tryParseDouble(strValue))
            value = converter.parseDouble(strValue);
        // BigDecimal
        else if (converter.tryParseBigDecimal(strValue))
            value = converter.parseBigDecimal(strValue);
        // boolean
        else if (converter.tryParseBoolean(strValue))
            value = converter.parseBoolean(strValue);
        // String
        else
            value = strValue;

        // get comment of changing record (optional)
        String comment = null;
        if (!addNewIfNotExists && cmdArgs.length == 5)
            comment = cmdArgs[4].trim();
        else if (addNewIfNotExists && cmdArgs.length == 6)
            comment = cmdArgs[5].trim();

        return new ChangeValueByKeywordInputData(keyword, value, comment,addNewIfNotExists);
    }

    /**
     * Extracts input data for operation <b>Chain multiple records</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @return                              {@link ChainRecordsInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static ChainRecordsInputData extractChainRecordsData(String[] cmdArgs) throws IllegalInputDataException {
        // get switches (optional)
        boolean updateIfExists = false;
        boolean skipIfChainKwNotExists = false;
        String firstSwitchParam = cmdArgs[1].toLowerCase().trim();
        // some parameter on first place
        if (firstSwitchParam.startsWith("-")) {
            switch (firstSwitchParam) {
                case "-u":
                    updateIfExists = true;
                    break;
                case "-s":
                    skipIfChainKwNotExists = true;
                    break;
                default:
                    throw new InvalidSwitchParameterException(firstSwitchParam, "First switch parameter is in invalid format: '" + firstSwitchParam + "'. Correct format is '-u' or '-s");
            }

            if (cmdArgs.length < 3)
                throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHAIN'");

            String secondSwitchParam = cmdArgs[2].toLowerCase().trim();
            // some parameter on second place
            if (secondSwitchParam.startsWith("-")) {
                // if first param is '-u' second must be '-s'
                if (updateIfExists) {
                    if (secondSwitchParam.equals("-s"))
                        skipIfChainKwNotExists = true;
                    else
                        throw new InvalidSwitchParameterException(secondSwitchParam, "Second switch parameter is in invalid format: '" + secondSwitchParam + "'. Correct format is '-s");
                    // if first param is '-s' second must be '-u'
                } else {
                    if (secondSwitchParam.equals("-u"))
                        updateIfExists = true;
                    else
                        throw new InvalidSwitchParameterException(secondSwitchParam, "Second switch parameter is in invalid format: '" + secondSwitchParam + "'. Correct format is '-u");
                }
            }
        }

        // set keyword of chained record (required)
        int keywordIndex = updateIfExists && skipIfChainKwNotExists
                ? 4
                : !updateIfExists && !skipIfChainKwNotExists ? 2 : 3;
        // check for correct number of parameters
        if (cmdArgs.length < keywordIndex + 1)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHAIN'");

        String keyword = cmdArgs[keywordIndex].trim();

        if (keyword.startsWith("-c=") || keyword.startsWith("-k="))
            throw new IllegalInputDataException("Keyword is not specified");

        // set parameters to chain (required) and comment (optional)
        int startIndex = keywordIndex + 1;
        // check for correct number of parameters
        if (cmdArgs.length < startIndex + 1)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'CHAIN'");

        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        String comment = null;
        for (int i = startIndex; i < cmdArgs.length; i++) {
            String argument = cmdArgs[i];

            // constant
            if (argument.startsWith("-c=") || argument.startsWith("-C=")) {
                argument = argument.substring(3);
                if (!argument.isEmpty())
                    chainValues.add(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, argument));
                continue;
            }

            // keyword
            if (argument.startsWith("-k=") || argument.startsWith("-K=")) {
                argument = argument.substring(3).trim().toUpperCase();
                if (!argument.isEmpty())
                    chainValues.add(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, argument));
                continue;
            }

            // comment (optional)
            if (i == cmdArgs.length - 1) {
                comment = argument;
                continue;
            }

            throw new IllegalInputDataException("Invalid parameter to chain: '" + argument + "'");
        }

        if (chainValues.isEmpty())
            throw new IllegalInputDataException("Parameters does not contain any keyword or constant to chain");

        return new ChainRecordsInputData(keyword, chainValues, comment, updateIfExists, skipIfChainKwNotExists);
    }

    /**
     * Extracts input data for operation <b>Shift time of time record</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @param converter                     {@link TypeConverter} object used to convert {@link String} time value to {@link Integer} value
     * @return                              {@link ShiftTimeInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static ShiftTimeInputData extractShiftTimeData(String[] cmdArgs, TypeConverter converter) throws IllegalInputDataException {
        if (cmdArgs.length < 4 || cmdArgs.length > 10)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'SHIFT_TIME'");

        // get keyword (required)
        String keyword = cmdArgs[2].trim();
        if (keyword.toLowerCase().startsWith("-y=")
                || keyword.toLowerCase().startsWith("-m=")
                || keyword.toLowerCase().startsWith("-d=")
                || keyword.toLowerCase().startsWith("-h=")
                || keyword.toLowerCase().startsWith("-min=")
                || keyword.toLowerCase().startsWith("-s=")
                || keyword.toLowerCase().startsWith("-ms="))
            throw new WrongNumberOfParametersException(cmdArgs.length, "Keyword is not specified");

        // load time shifts
        int yearShift = 0;
        int monthShift = 0;
        int dayShift = 0;
        int hourShift = 0;
        int minuteShift = 0;
        int secondShift = 0;
        int milisecondShift = 0;
        for (int i = 3; i < cmdArgs.length; i++) {
            String argument = cmdArgs[i].trim();

            // parse the number from argument
            if (argument.toLowerCase().startsWith("-y=")) {
                argument = argument.substring(3).trim();
                if (converter.tryParseInt(argument)) {
                    yearShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Years time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-m=")) {
                argument = argument.substring(3).trim();
                if (converter.tryParseInt(argument)) {
                    monthShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Months time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-d=")) {
                argument = argument.substring(3).trim();
                if (converter.tryParseInt(argument)) {
                    dayShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Days time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-h=")) {
                argument = argument.substring(3).trim();
                if (converter.tryParseInt(argument)) {
                    hourShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Hours time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-min=")) {
                argument = argument.substring(5).trim();
                if (converter.tryParseInt(argument)) {
                    minuteShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Minutes time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-s=")) {
                argument = argument.substring(3).trim();
                if (converter.tryParseInt(argument)) {
                    secondShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Seconds time shift argument is in invalid number format");
                }
            } else if (argument.toLowerCase().startsWith("-ms=")) {
                argument = argument.substring(4).trim();
                if (converter.tryParseInt(argument)) {
                    milisecondShift = converter.parseInt(argument);
                } else {
                    throw new IllegalInputDataException("Miliseconds time shift argument is in invalid number format");
                }
            } else {
                throw new IllegalInputDataException("Parameter '" + argument + "' is in invalid format. Must start with one of: " +
                        "'-y=', '-m=', '-d=', '-h=', '-min=', '-s=', '-ms='");
            }
        }

        return new ShiftTimeInputData(keyword, yearShift, monthShift, dayShift, hourShift, minuteShift, secondShift, milisecondShift);
    }

    /**
     * Extracts input data for operation <b>Compute Julian Date</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @param converter                     {@link TypeConverter} object used to convert {@link String} input parameters to specific value types
     * @return                              {@link ComputeJDInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static ComputeJDInputData extractComputeJDData(String[] cmdArgs, TypeConverter converter) throws IllegalInputDataException {
        if (cmdArgs.length < 4 || cmdArgs.length > 5)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'JD'");

        String parameter;

        // get datetime parameter (required)
        String datetimeKeyword = null;
        LocalDateTime datetimeValue = null;
        parameter = cmdArgs[2].trim();

        if (converter.tryParseLocalDateTime(parameter))
            datetimeValue = converter.parseLocalDateTime(parameter);
        else
            datetimeKeyword = parameter;

        // get exposure time parameter (required)
        String exposureKeyword = null;
        double exposureValue = Double.NaN;
        parameter = cmdArgs[3].trim();

        if (converter.tryParseDouble(parameter))
            exposureValue = converter.parseDouble(parameter);
        else
            exposureKeyword = parameter;

        // get comment (optional)
        String comment = null;
        if (cmdArgs.length == 5)
            comment = cmdArgs[4].trim();

        // datetime == keyword && exposure = keyword
        if (datetimeKeyword != null && exposureKeyword != null)
            return new ComputeJDInputData(datetimeKeyword, exposureKeyword, comment);
        // datetime == value && exposure == keyword
        else if (datetimeValue != null && exposureKeyword != null)
            return new ComputeJDInputData(datetimeValue, exposureKeyword, comment);
        // datetime == keyword && exposure == value
        else if (datetimeKeyword != null && !Double.isNaN(exposureValue))
            return new ComputeJDInputData(datetimeKeyword, exposureValue, comment);
        // datetime == value && exposure == value
        else
            return new ComputeJDInputData(datetimeValue, exposureValue, comment);
    }

    /**
     * Extracts input data for operation <b>Compute Heliocentric Julian Date</b>
     *
     * @param cmdArgs                       commandline arguments containing specific input data
     * @param converter                     {@link TypeConverter} object used to convert {@link String} input parameters to specific value types
     * @return                              {@link ComputeHJDInputData} object with input data
     * @throws IllegalInputDataException    when input data are in invalid form
     */
    static ComputeHJDInputData extractComputeHJDData(String[] cmdArgs, TypeConverter converter) throws IllegalInputDataException {
        if (cmdArgs.length < 6 || cmdArgs.length > 7)
            throw new WrongNumberOfParametersException(cmdArgs.length, "Wrong number of parameters for operation 'HJD'");

        String parameter;

        // get datetime parameter (requred)
        Object datetime;
        parameter = cmdArgs[2].trim();

        if (converter.tryParseLocalDateTime(parameter))
            datetime = converter.parseLocalDateTime(parameter);
        else
            datetime = parameter;

        // get exposure time parameter (required)
        Object exposure;
        parameter = cmdArgs[3].trim();

        if (converter.tryParseDouble(parameter))
            exposure = converter.parseDouble(parameter);
        else
            exposure = parameter;

        // get right ascension parameters (required)
        Object rightAscension;
        parameter = cmdArgs[4].trim();

        if (converter.tryParseDouble(parameter)) {
            // double value
            rightAscension = converter.parseDouble(parameter);
        } else if (converter.tryParseBigDecimal(parameter)) {
            // BigDecimal value
            rightAscension = converter.parseBigDecimal(parameter);
        } else if (parameter.split(":").length == 3) {
            // time object value
            String[] raValues = parameter.split(":");
            double hours;
            double minutes;
            double seconds;

            // parse hours
            if (converter.tryParseDouble(raValues[0].trim()))
                hours = converter.parseDouble(raValues[0].trim());
            else throw new IllegalInputDataException("Right ascension's hours parameter is in invalid format");

            // parse minutes
            if (converter.tryParseDouble(raValues[1].trim()))
                minutes = converter.parseDouble(raValues[1].trim());
            else throw new IllegalInputDataException("Right ascension's minutes parameter is in invalid format");

            // parse seconds
            if (converter.tryParseDouble(raValues[2].trim()))
                seconds = converter.parseDouble(raValues[2].trim());
            else throw new IllegalInputDataException("Right ascension's seconds parameter is in invalid format");

            rightAscension = new TimeObject(hours, minutes, seconds);
        } else {
            // keyword value
            rightAscension = parameter;
        }

        // get declination parameters (required)
        Object declination;
        parameter = cmdArgs[5].trim();

        if (converter.tryParseDouble(parameter)) {
            // double value
            declination = converter.parseDouble(parameter);
        } else if (converter.tryParseBigDecimal(parameter)) {
            // BigDecimal value
            declination = converter.parseBigDecimal(parameter);
        } else if (parameter.split(":").length == 3) {
            // degrees object value
            String[] decValues = parameter.split(":");
            double degrees;
            double minutes;
            double seconds;

            // parse degrees
            if (converter.tryParseDouble(decValues[0].trim()))
                degrees = converter.parseDouble(decValues[0].trim());
            else throw new IllegalInputDataException("Declination's degrees parameter is in invalid format");

            // parse minutes
            if (converter.tryParseDouble(decValues[1].trim()))
                minutes = converter.parseDouble(decValues[1].trim());
            else throw new IllegalInputDataException("Declination's minutes parameter is in invalid format");

            // parse seconds
            if (converter.tryParseDouble(decValues[2].trim()))
                seconds = converter.parseDouble(decValues[2].trim());
            else throw new IllegalInputDataException("Declination's seconds parameter is in invalid format");

            declination = new DegreesObject(degrees, minutes, seconds);
        } else {
            // keyword value
            declination = parameter;
        }

        // get comment (optional)
        String comment = null;
        if (cmdArgs.length == 7)
            comment = cmdArgs[6].trim();

        return new ComputeHJDInputData(datetime, exposure, rightAscension, declination, comment);
    }
}
