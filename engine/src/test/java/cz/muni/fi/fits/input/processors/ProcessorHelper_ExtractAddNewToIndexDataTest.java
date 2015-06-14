package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.inputData.AddNewToIndexInputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Tests for extraction of input data for operation <b>Add new record to specific index</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractAddNewToIndexDataTest {

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
    public void testExtractAddNewToIndexData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString() };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
    }

    @Test
    public void testExtractAddNewToIndexData_Index_InvalidFormat() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString(), "one","KEYWORD", "VALUE" };

        exception.expect(IllegalInputDataException.class);
        CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
    }

    @Test
    public void testExtractAddNewToIndexData_Index_Valid() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString(), "152","KEYWORD", "VALUE" };

        AddNewToIndexInputData antiid = CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
        assertNotNull(antiid);
        assertEquals(152, antiid.getIndex());
    }

    @Test
    public void testExtractAddNewToIndexData_SwitchParameter_True() throws Exception {
        String[] args = new String[] { "add_ix", "-rm", FILE_PATH.toString(), "1", "KEYWORD", "VALUE" };

        AddNewToIndexInputData antiid = CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
        assertNotNull(antiid);
        assertTrue(antiid.removeOldIfExists());
    }

    @Test
    public void testExtractAddNewToIndexData_SwitchParameter_False() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString(), "12", "KEYWORD", "VALUE" };

        AddNewToIndexInputData antiid = CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
        assertNotNull(antiid);
        assertFalse(antiid.removeOldIfExists());
    }

    @Test
    public void testExtractAddNewToIndexData_SwitchParameter_WrongFormat() throws Exception {
        String[] args = new String[] { "add_ix", "-remove", FILE_PATH.toString(), "15", "KEYWORD", "VALUE" };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
    }

    @Test
    public void testExtractAddNewToIndexData_Keyword_Valid() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString(), "25", "keyword", "123456789745" };

        AddNewToIndexInputData antiid = CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
        assertNotNull(antiid);
        assertEquals("KEYWORD", antiid.getKeyword());
    }

    @Test
    public void testExtractAddNewToIndexData_Parameters_Valid() throws Exception {
        String[] args = new String[] { "add_ix", FILE_PATH.toString(), "25", "KEYWORD", "123456789745" };

        AddNewToIndexInputData antiid = CmdArgumentsProcessorHelper.extractAddNewToIndexData(args, _converter);
        assertNotNull(antiid);
        assertEquals(25, antiid.getIndex());
        assertEquals("KEYWORD", antiid.getKeyword());
        assertEquals(123456789745L, antiid.getValue());
        assertNull(antiid.getComment());
        assertFalse(antiid.removeOldIfExists());
    }
}
