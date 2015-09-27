package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.common.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.common.exceptions.InvalidSwitchParameterException;
import cz.muni.fi.fits.common.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.models.ChainRecordsInputData;
import cz.muni.fi.fits.models.ChainValueType;
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
 * Tests for extraction of input data for operation <b>Chain multiple records</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractChainRecordsDataTest {

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
    public void testExtractChainRecordsData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "chain", "-u", FILE_PATH.toString() };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractChainRecordsData(args);
    }

    @Test
    public void testExtractChainRecordsData_SwitchParameters_False() throws Exception {
        String[] args = new String[] { "chain", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYWORD 1", "COMMENT" };

        ChainRecordsInputData crid = CmdArgumentsProcessorHelper.extractChainRecordsData(args);
        assertNotNull(crid);
        assertFalse(crid.updateIfExists());
        assertFalse(crid.skipIfChainKwNotExists());
    }

    @Test
    public void testExtractChainRecordsData_FirstSwitchParameter_WrongFormat() throws Exception {
        String[] args = new String[] { "chain", "-upd", "-s", FILE_PATH.toString() };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractChainRecordsData(args);
    }

    @Test
    public void testExtractChainRecordsData_SecondSwitchParameter_WrongFormat() throws Exception {
        String[] args = new String[] { "chain", "-s", "-upd", FILE_PATH.toString() };

        exception.expect(InvalidSwitchParameterException.class);
        CmdArgumentsProcessorHelper.extractChainRecordsData(args);
    }

    @Test
    public void testExtractChainRecordsData_SwitchParameters_Valid() throws Exception {
        String[] args = new String[] { "chain", "-s", "-u", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYWORD 1", "COMMENT" };

        ChainRecordsInputData crid = CmdArgumentsProcessorHelper.extractChainRecordsData(args);
        assertNotNull(crid);
        assertTrue(crid.updateIfExists());
        assertTrue(crid.skipIfChainKwNotExists());
    }

    @Test
    public void testExtractChainRecordsData_ChainParameters_Empty() throws Exception {
        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "KEYWORD", "COMMENT" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("does not contain any keyword or constant");
        CmdArgumentsProcessorHelper.extractChainRecordsData(args);
    }

    @Test
    public void testExtractChainRecordsData_Keyword_NotInData() throws Exception {
        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "-c=CONSTANT 1", "COMMENT" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("Keyword is not specified");
        CmdArgumentsProcessorHelper.extractChainRecordsData(args);
    }

    @Test
    public void testExtractChainRecordsData_Keyword_Valid() throws Exception {
        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "keyword", "-c=CONSTANT 1", "-k=KEYWORD 1", "COMMENT" };

        ChainRecordsInputData crid = CmdArgumentsProcessorHelper.extractChainRecordsData(args);
        assertNotNull(crid);
        assertEquals("KEYWORD", crid.getKeyword());
    }

    @Test
    public void testExtractChainRecordsData_Comment_PresentInData() throws Exception {
        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYWORD 1", "COMMENT" };

        ChainRecordsInputData crid = CmdArgumentsProcessorHelper.extractChainRecordsData(args);
        assertNotNull(crid);
        assertEquals("COMMENT", crid.getComment());
    }

    @Test
    public void testExtractChainRecordsData_Comment_NotInData() throws Exception {
        String[] args = new String[] { "chain", "-s", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYWORD 1" };

        ChainRecordsInputData crid = CmdArgumentsProcessorHelper.extractChainRecordsData(args);
        assertNotNull(crid);
        assertNull(crid.getComment());
    }

    @Test
    public void testExtractChainRecordsData_Parameters_Valid() throws Exception {
        String[] args1 = new String[] { "chain", "-s", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYWORD 1", "-k=KEYWORD 2", "-c=CONSTANT 2", "COMMENT" };

        ChainRecordsInputData crid1 = CmdArgumentsProcessorHelper.extractChainRecordsData(args1);
        assertNotNull(crid1);
        assertTrue(crid1.skipIfChainKwNotExists());
        assertFalse(crid1.updateIfExists());
        assertEquals("KEYWORD", crid1.getKeyword());
        assertEquals(4, crid1.getChainValues().size());
        assertEquals(ChainValueType.CONSTANT, crid1.getChainValues().get(0).getFirst());
        assertEquals("CONSTANT 1", crid1.getChainValues().get(0).getSecond());
        assertEquals(ChainValueType.KEYWORD, crid1.getChainValues().get(1).getFirst());
        assertEquals("KEYWORD 1", crid1.getChainValues().get(1).getSecond());
        assertEquals(ChainValueType.KEYWORD, crid1.getChainValues().get(2).getFirst());
        assertEquals("KEYWORD 2", crid1.getChainValues().get(2).getSecond());
        assertEquals(ChainValueType.CONSTANT, crid1.getChainValues().get(3).getFirst());
        assertEquals("CONSTANT 2", crid1.getChainValues().get(3).getSecond());
        assertEquals("COMMENT", crid1.getComment());


        String[] args2 = new String[] { "chain", "-u", FILE_PATH.toString(), "KEYWORD", "-c=CONSTANT 1", "-k=KEYword 1" };

        ChainRecordsInputData crid2 = CmdArgumentsProcessorHelper.extractChainRecordsData(args2);
        assertNotNull(crid2);
        assertFalse(crid2.skipIfChainKwNotExists());
        assertTrue(crid2.updateIfExists());
        assertEquals("KEYWORD", crid2.getKeyword());
        assertEquals(2, crid2.getChainValues().size());
        assertEquals(ChainValueType.CONSTANT, crid2.getChainValues().get(0).getFirst());
        assertEquals("CONSTANT 1", crid2.getChainValues().get(0).getSecond());
        assertEquals(ChainValueType.KEYWORD, crid2.getChainValues().get(1).getFirst());
        assertEquals("KEYWORD 1", crid2.getChainValues().get(1).getSecond());
        assertNull(crid2.getComment());
    }
}
