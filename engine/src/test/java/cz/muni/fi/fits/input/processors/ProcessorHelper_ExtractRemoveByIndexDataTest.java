package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.models.RemoveFromIndexInputData;
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
 * Tests for extraction of input data for operation <b>Remove record from specific index</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractRemoveByIndexDataTest {

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
    public void testExtractRemoveByIndexData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "remove_ix", FILE_PATH.toString() };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractRemoveFromIndexData(args);
    }

    @Test
    public void testExtractRemoveByIndexData_Index_InvalidNumber() throws Exception {
        String[] args = new String[] { "remove_ix", FILE_PATH.toString(), "index" };

        exception.expect(IllegalInputDataException.class);
        CmdArgumentsProcessorHelper.extractRemoveFromIndexData(args);
    }

    @Test
    public void testExtractRemoveByIndexData_Index_Valid() throws Exception {
        String[] args = new String[] { "remove_ix", FILE_PATH.toString(), "25" };

        RemoveFromIndexInputData rfiid = CmdArgumentsProcessorHelper.extractRemoveFromIndexData(args);
        assertNotNull(rfiid);
        assertEquals(25, rfiid.getIndex());
    }
}
