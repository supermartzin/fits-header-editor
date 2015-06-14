package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.models.inputData.RemoveByKeywordInputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for extraction of input data for operation <b>Remove record by keyword</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractRemoveByKeywordDataTest {

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
    public void testExtractRemoveByKeywordData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "remove", FILE_PATH.toString(), "KEYWORD", "ARG1", "ARG2" };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractRemoveByKeywordData(args);
    }

    @Test
    public void testExtractRemoveByKeywordData_Keyword_Valid() throws Exception {
        String[] args = new String[] { "remove", FILE_PATH.toString(), "remove this keyWORD" };

        RemoveByKeywordInputData rbkid = CmdArgumentsProcessorHelper.extractRemoveByKeywordData(args);
        assertNotNull(rbkid);
        assertEquals("REMOVE THIS KEYWORD", rbkid.getKeyword());
    }
}
