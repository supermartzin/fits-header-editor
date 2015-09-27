package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.common.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.common.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.input.models.ChangeValueByKeywordInputData;
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
 * Tests for extraction of input data for operation <b>Change value of record</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractChangeValueByKeywordDataTest {

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
    public void testExtractChangeValueByKeywordData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "change", FILE_PATH.toString(), "KEYWORD", "VALUE", "COMMENT", "REDUNDANT_ARG" };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
    }

    @Test
    public void testExtractChangeValueByKeywordData_SwitchParameter_False() throws Exception {
        String[] args = new String[] { "change", FILE_PATH.toString(), "KEYWORD", "VALUE" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertFalse(cvbkid.addNewIfNotExists());
    }

    @Test
    public void testExtractChangeValueByKeywordData_SwitchParameter_WrongFormat() throws Exception {
        String[] args = new String[] { "change", "-add", FILE_PATH.toString(), "KEYWORD", "VALUE" };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
    }

    @Test
    public void testExtractChangeValueByKeywordData_SwitchParameter_Valid() throws Exception {
        String[] args = new String[] { "change", "-a", FILE_PATH.toString(), "KEYWORD", "VALUE" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertTrue(cvbkid.addNewIfNotExists());
    }

    @Test
    public void testExtractChangeValueByKeywordData_Keyword_Valid() throws Exception {
        String[] args = new String[] { "change", "-a", FILE_PATH.toString(), "KEYword", "VALUE" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertEquals("KEYWORD", cvbkid.getKeyword());
    }

    @Test
    public void testExtractChangeValueByKeywordData_Comment_PresentInData() throws Exception {
        String[] args = new String[] { "change", "-a", FILE_PATH.toString(), "KEYWORD", "VALUE", "comment" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertNotNull(cvbkid.getComment());
        assertEquals("comment", cvbkid.getComment());
    }

    @Test
    public void testExtractChangeValueByKeywordData_Comment_NotInData() throws Exception {
        String[] args = new String[] { "change", "-a", FILE_PATH.toString(), "KEYWORD", "VALUE" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertNull(cvbkid.getComment());
    }

    @Test
    public void testExtractChangeValueByKeywordData_Parameters_Valid() throws Exception {
        String[] args = new String[] { "change", FILE_PATH.toString(), "KEYWord", "VALUE", "COMMENT" };

        ChangeValueByKeywordInputData cvbkid = CmdArgumentsProcessorHelper.extractChangeValueByKeywordData(args, _converter);
        assertNotNull(cvbkid);
        assertEquals("KEYWORD", cvbkid.getKeyword());
        assertFalse(cvbkid.addNewIfNotExists());
        assertEquals("VALUE", cvbkid.getValue());
        assertEquals("COMMENT", cvbkid.getComment());
    }
}
