package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.common.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.common.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.models.ChangeKeywordInputData;
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
 * Tests for extraction of input data for operation <b>Change keyword of record</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractChangeKeywordDataTest {

    private static final Path FILE_PATH = Paths.get("test-files.in");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Files.createFile(FILE_PATH);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(FILE_PATH);
    }

    @Test
    public void testExtractChangeKeywordData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "change_kw", FILE_PATH.toString(), "KEYWORD" };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
    }

    @Test
    public void testExtractChangeKeywordData_SwitchParameter_False() throws Exception {
        String[] args = new String[] { "change_kw", FILE_PATH.toString(), "OLD_KEYWORD", "NEW_KEYWORD" };

        ChangeKeywordInputData ckid = CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
        assertNotNull(ckid);
        assertFalse(ckid.removeValueOfNewIfExists());
    }

    @Test
    public void testExtractChangeKeywordData_SwitchParameter_WrongFormat() throws Exception {
        String[] args = new String[] { "change_kw", "-remove", FILE_PATH.toString(), "OLD_KEYWORD", "NEW_KEYWORD" };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
    }

    @Test
    public void testExtractChangeKeywordData_SwitchParameter_Valid() throws Exception {
        String[] args = new String[] { "change_kw", "-rm", FILE_PATH.toString(), "OLD_KEYWORD", "NEW_KEYWORD" };

        ChangeKeywordInputData ckid = CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
        assertNotNull(ckid);
        assertTrue(ckid.removeValueOfNewIfExists());
    }

    @Test
    public void testExtractChangeKeywordData_OldKeyword_Valid() throws Exception {
        String[] args = new String[] { "change_kw", "-rm", FILE_PATH.toString(), "OLD keyword", "NEW_KEYWORD" };

        ChangeKeywordInputData ckid = CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
        assertNotNull(ckid);
        assertEquals("OLD KEYWORD", ckid.getOldKeyword());
    }

    @Test
    public void testExtractChangeKeywordData_NewKeyword_Valid() throws Exception {
        String[] args = new String[] { "change_kw", "-rm", FILE_PATH.toString(), "OLD", "new KEYWORD" };

        ChangeKeywordInputData ckid = CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
        assertNotNull(ckid);
        assertEquals("NEW KEYWORD", ckid.getNewKeyword());
    }

    @Test
    public void testExtractChangeKeywordData_Parameters_Valid() throws Exception {
        String[] args = new String[] { "change_kw", "-rm", FILE_PATH.toString(), "olD_KEYWORD", "5963" };

        ChangeKeywordInputData ckid = CmdArgumentsProcessorHelper.extractChangeKeywordData(args);
        assertNotNull(ckid);
        assertEquals("OLD_KEYWORD", ckid.getOldKeyword());
        assertEquals("5963", ckid.getNewKeyword());
        assertTrue(ckid.removeValueOfNewIfExists());
    }
}
