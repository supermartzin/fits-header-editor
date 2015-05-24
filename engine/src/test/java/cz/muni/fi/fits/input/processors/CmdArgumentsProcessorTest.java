package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.UnknownOperationException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.OperationType;
import cz.muni.fi.fits.models.inputData.*;
import cz.muni.fi.fits.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Tests for {@link CmdArgumentsProcessor} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class CmdArgumentsProcessorTest {

    private static final Path SAMPLE1 = Paths.get("sample1.fits");
    private static final Path SAMPLE2 = Paths.get("sample2.fits");
    private static final Path SAMPLE3 = Paths.get("sample3.fits");

    private static final Path FILE_PATH = Paths.get("test-files.in");
    private TypeConverter _converter;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Files.createFile(FILE_PATH);
        Files.createFile(SAMPLE1);
        Files.createFile(SAMPLE2);
        Files.createFile(SAMPLE3);
        _converter = new DefaultTypeConverter();
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(FILE_PATH);
        Files.deleteIfExists(SAMPLE1);
        Files.deleteIfExists(SAMPLE2);
        Files.deleteIfExists(SAMPLE3);
        _converter = null;
    }

    @Test
    public void testGetProcessedInput_NullArguments() throws Exception {
        InputProcessor inputProcessor = new CmdArgumentsProcessor(null, _converter);

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("Arguments are null");
        inputProcessor.getProcessedInput();
    }

    @Test
    public void testGetProcessedInput_NoArguments() throws Exception {
        InputProcessor inputProcessor = new CmdArgumentsProcessor(new String[]{}, _converter);

        exception.expect(WrongNumberOfParametersException.class);
        inputProcessor.getProcessedInput();
    }

    @Test
    public void testGetProcessedInput_WrongNumberOfArguments() throws Exception {
        String[] args = new String[] { "add" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        exception.expect(WrongNumberOfParametersException.class);
        inputProcessor.getProcessedInput();
    }

    @Test
    public void testGetProcessedInput_BadOperation() throws Exception {
        String[] args = new String[] { "add_bad", "files.in" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        exception.expect(UnknownOperationException.class);
        inputProcessor.getProcessedInput();
    }

    // AddNewRecordInputData test
    @Test
    public void testGetProcessedInput_ValidAddNewRecordInputData() throws Exception {
        Files.write(FILE_PATH, Collections.singletonList(SAMPLE1.toString()));

        String[] args = new String[] { "add", FILE_PATH.toString(), "KEYWORD", "true", "comment" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.ADD_NEW_RECORD_TO_END);
        assertTrue(inputData instanceof AddNewRecordInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(1, inputData.getFitsFiles().size());

        AddNewRecordInputData anrid = (AddNewRecordInputData)inputData;
        assertFalse(anrid.updateIfExists());
        assertEquals("KEYWORD", anrid.getKeyword());
        assertEquals(Boolean.TRUE, anrid.getValue());
        assertEquals("comment", anrid.getComment());
    }

    // AddNewToIndexInputData test
    @Test
    public void testGetProcessedInput_ValidAddNewToIndexInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                SAMPLE3.toString()));

        String[] args = new String[] { "add_ix", "-rm", FILE_PATH.toString(), "9", "KEYWORD", "256321456987456321477" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.ADD_NEW_RECORD_TO_INDEX);
        assertTrue(inputData instanceof AddNewToIndexInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(3, inputData.getFitsFiles().size());

        AddNewToIndexInputData antiid = (AddNewToIndexInputData)inputData;
        assertTrue(antiid.removeOldIfExists());
        assertEquals(9, antiid.getIndex());
        assertEquals("KEYWORD", antiid.getKeyword());
        assertEquals(new BigInteger("256321456987456321477"), antiid.getValue());
        assertNull(antiid.getComment());
    }

    // RemoveByKeywordInputData test
    @Test
    public void testGetProcessedInput_ValidRemoveByKeywordInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE3.toString()));

        String[] args = new String[] { "remove", FILE_PATH.toString(), "KEYWORD" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.REMOVE_RECORD_BY_KEYWORD);
        assertTrue(inputData instanceof RemoveByKeywordInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(2, inputData.getFitsFiles().size());

        RemoveByKeywordInputData rbkid = (RemoveByKeywordInputData)inputData;
        assertEquals("KEYWORD", rbkid.getKeyword());
    }

    // RemoveFromIndexInputData test
    @Test
    public void testGetProcessedInput_ValidRemoveFromIndexInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString()));

        String[] args = new String[] { "remove_ix", FILE_PATH.toString(), "12" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.REMOVE_RECORD_FROM_INDEX);
        assertTrue(inputData instanceof RemoveFromIndexInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(2, inputData.getFitsFiles().size());

        RemoveFromIndexInputData rfiid = (RemoveFromIndexInputData)inputData;
        assertEquals(12, rfiid.getIndex());
    }

    // ChangeKeywordInputData test
    @Test
    public void testGetProcessedInput_ValidChangeKeywordInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                SAMPLE3.toString()));

        String[] args = new String[] { "change_kw", "-rm", FILE_PATH.toString(), "OLD_KEYWORD", "NEW KEYWORD" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.CHANGE_KEYWORD);
        assertTrue(inputData instanceof ChangeKeywordInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(3, inputData.getFitsFiles().size());

        ChangeKeywordInputData ckid = (ChangeKeywordInputData)inputData;
        assertEquals("OLD_KEYWORD", ckid.getOldKeyword());
        assertEquals("NEW KEYWORD", ckid.getNewKeyword());
        assertTrue(ckid.removeValueOfNewIfExists());
    }

    // ChangeKeywordInputData test
    @Test
    public void testGetProcessedInput_ValidChangeValueByKeywordInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString()));

        String[] args = new String[] { "change", "-a", FILE_PATH.toString(), "KEYWORD", "12.45E120", "comment" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.CHANGE_VALUE_BY_KEYWORD);
        assertTrue(inputData instanceof ChangeValueByKeywordInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(2, inputData.getFitsFiles().size());

        ChangeValueByKeywordInputData cvbkid = (ChangeValueByKeywordInputData)inputData;
        assertTrue(cvbkid.addNewIfNotExists());
        assertEquals("KEYWORD", cvbkid.getKeyword());
        assertEquals(12.45E120, cvbkid.getValue());
        assertEquals("comment", cvbkid.getComment());
    }

    // ChainRecordsInputData test
    @Test
    public void testGetProcessedInput_ValidChainRecordsInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                SAMPLE3.toString(),
                "sample4.fts"));

        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "KEYWORD", "-c=constant 1", "-k=key_word", "-c=Constant 2" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.CHAIN_RECORDS);
        assertTrue(inputData instanceof ChainRecordsInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(3, inputData.getFitsFiles().size());

        ChainRecordsInputData crid = (ChainRecordsInputData)inputData;
        assertTrue(crid.skipIfChainKwNotExists());
        assertFalse(crid.updateIfExists());
        assertEquals("KEYWORD", crid.getKeyword());
        assertEquals(3, crid.getChainValues().size());
        assertNull(crid.getComment());
    }

    // ShiftTimeInputData test
    @Test
    public void testGetProcessedInput_ValidShiftTimeInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                "sample3.fit"));
        String[] args = new String[] { "shift_time", FILE_PATH.toString(), "KEYWORD", "-y=-23", "-min=12", "-d=-123" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.SHIFT_TIME);
        assertTrue(inputData instanceof ShiftTimeInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(2, inputData.getFitsFiles().size());

        ShiftTimeInputData stid = (ShiftTimeInputData)inputData;
        assertEquals("KEYWORD", stid.getKeyword());
        assertEquals(-23, stid.getYearShift());
        assertEquals(0, stid.getMonthShift());
        assertEquals(-123, stid.getDayShift());
        assertEquals(0, stid.getHourShift());
        assertEquals(12, stid.getMinuteShift());
        assertEquals(0, stid.getSecondShift());
        assertEquals(0, stid.getMilisecondsShift());
        assertEquals(0, stid.getNanosecondShift());
    }

    // ComputeHJDInputData test
    @Test
    public void testGetProcessedInput_ValidComputeHJDInputData() throws Exception {
        Files.write(FILE_PATH, Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                SAMPLE3.toString(),
                "sample4.fit",
                "sample5.fit",
                "directory/sample6.fit"));
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATE-OBS", "10.50", "RGHT_ASC", "-45:26:13.5" };
        InputProcessor inputProcessor = new CmdArgumentsProcessor(args, _converter);

        InputData inputData = inputProcessor.getProcessedInput();

        assertTrue(inputData != null);
        assertTrue(inputData.getOperationType() == OperationType.COMPUTE_HJD);
        assertTrue(inputData instanceof ComputeHJDInputData);
        assertNotNull(inputData.getFitsFiles());
        assertEquals(3, inputData.getFitsFiles().size());

        ComputeHJDInputData chjdid = (ComputeHJDInputData)inputData;
        assertTrue(chjdid.getDatetime() instanceof String);
        assertEquals("DATE-OBS", chjdid.getDatetime());
        assertTrue(chjdid.getExposure() instanceof Double);
        assertEquals(10.5, (double) chjdid.getExposure(), 0.0);
        assertTrue(chjdid.getRightAscension() instanceof String);
        assertEquals("RGHT_ASC", chjdid.getRightAscension());
        assertTrue(chjdid.getDeclination() instanceof DegreesObject);
        DegreesObject declination = (DegreesObject) chjdid.getDeclination();
        assertEquals(-45.0, declination.getDegrees(), 0.0);
        assertEquals(26.0, declination.getMinutes(), 0.0);
        assertEquals(13.5, declination.getSeconds(), 0.0);
        assertEquals(Constants.DEFAULT_HJD_COMMENT, chjdid.getComment());
    }
}