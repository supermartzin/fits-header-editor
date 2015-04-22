package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.inputData.ShiftTimeInputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for extraction of input data for operation <b>Shift time of time record</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ProcessorHelper_ExtractShiftTimeDataTest {

    private static final Path FILE_PATH = Paths.get("test-files.in");

    private TypeConverter _converter;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Files.createFile(FILE_PATH);
        _converter = new DefaultTypeConverter();
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(FILE_PATH);
        _converter = null;
    }

    @Test
    public void testExtractShiftRecordData_WrongNumberOfParameters() throws Exception {
        String[] args = new String[] { "shift_time", "-jd", FILE_PATH.toString() };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
    }

    @Test
    public void testExtractShiftRecordData_WrongSwitchParameter() throws Exception {
        String[] args = new String[] { "shift_time", "-j", FILE_PATH.toString(), "-min=16", "-m=-2" };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
    }

    @Test
    public void testExtractShiftRecordData_NoKeywordParameter() throws Exception {
        String[] args = new String[] { "shift_time", FILE_PATH.toString(), "-y=-56", "-m=2" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("Keyword is not specified");
        CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
    }

    @Test
    public void testExtractShiftRecordData_UnknownShiftParameter() throws Exception {
        String[] args = new String[] { "shift_time", FILE_PATH.toString(), "KEYWORD", "-years=-56", "-months=2" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("is in invalid format");
        CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
    }

    @Test
    public void testExtractShiftRecordData_InvalidFormatOfShiftParameter() throws Exception {
        String[] args = new String[] { "shift_time", FILE_PATH.toString(), "KEYWORD", "-y=-56.0", "-min=2" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("argument is in invalid number format");
        CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
    }

    @Test
    public void testExtractShiftRecordData_CorrectParameters() throws Exception {
        String[] args = new String[] { "shift_time", FILE_PATH.toString(), "KEYWORD", "-y=-56", "-m=0", "-d=2", "-h=-23", "-min=2", "-s=0", "-ms=230" };

        ShiftTimeInputData stid = CmdArgumentsProcessorHelper.extractShiftTimeData(args, _converter);
        assertNotNull(stid);
        assertFalse(stid.updateJulianDate());
        assertEquals("KEYWORD", stid.getKeyword());
        assertEquals(-56, stid.getYearShift());
        assertEquals(0, stid.getMonthShift());
        assertEquals(2, stid.getDayShift());
        assertEquals(-23, stid.getHourShift());
        assertEquals(2, stid.getMinuteShift());
        assertEquals(0, stid.getSecondShift());
        assertEquals(230, stid.getMilisecondsShift());
    }
}
