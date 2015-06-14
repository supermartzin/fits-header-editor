package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.inputData.ComputeJDInputData;
import cz.muni.fi.fits.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests for extraction of input data for operation <b>Compute Julian Date</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class ProcessorHelper_ExtractComputeJDDataTest {

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
    public void testExtractComputeJDData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "DATETIME" };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
    }

    @Test
    public void testExtractComputeJDData_DatetimeParameter_LocalDateTime() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "2015-05-08T12:34:56", "EXPOSURE", "comment" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertTrue(cjdid.getDatetime() instanceof LocalDateTime);
        assertEquals(LocalDateTime.of(2015, 5, 8, 12, 34, 56), cjdid.getDatetime());
    }

    @Test
    public void testExtractComputeJDData_DatetimeKeyword_String() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "DATEtime", "exposure" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertTrue(cjdid.getDatetime() instanceof String);
        assertEquals("DATETIME", cjdid.getDatetime());
    }

    @Test
    public void testExtractComputeJDData_ExposureParameter_Double() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "2015-05-08T12:34:56", "25.45", "comment" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertTrue(cjdid.getExposure() instanceof Double);
        assertEquals(25.45, (double) cjdid.getExposure(), 0.0);
    }

    @Test
    public void testExtractComputeJDData_ExposureKeyword_String() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "DATETIME", "expoSURE" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertTrue(cjdid.getExposure() instanceof String);
        assertEquals("EXPOSURE", cjdid.getExposure());
    }

    @Test
    public void testExtractComputeJDData_Comment_PresentInData() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "DATETIME", "expoSURE", "comment" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertNotNull(cjdid);
        assertNotNull(cjdid.getComment());
        assertEquals("comment", cjdid.getComment());
    }

    @Test
    public void testExtractComputeJDData_Comment_Default() throws Exception {
        String[] args = new String[] { "jd", FILE_PATH.toString(), "DATETIME", "expoSURE" };

        ComputeJDInputData cjdid = CmdArgumentsProcessorHelper.extractComputeJDData(args, _converter);
        assertNotNull(cjdid);
        assertNotNull(cjdid.getComment());
        assertEquals(Constants.DEFAULT_JD_COMMENT, cjdid.getComment());
    }

    @Test
    public void testExtractComputeJDData_CorrectParameters() throws Exception {
        String[] args1 = new String[] { "jd", FILE_PATH.toString(), "2015-02-12T14:22:36.5", "EXPTIME", "comment" };

        ComputeJDInputData cjdid1 = CmdArgumentsProcessorHelper.extractComputeJDData(args1, _converter);
        assertNotNull(cjdid1);
        assertEquals(LocalDateTime.of(2015, 2, 12, 14, 22, 36, 500 * 1000 * 1000), cjdid1.getDatetime());
        assertEquals("EXPTIME", cjdid1.getExposure());
        assertEquals("comment", cjdid1.getComment());


        String[] args2 = new String[] { "jd", FILE_PATH.toString(), "DATE_OBS", "40.125" };

        ComputeJDInputData cjdid2 = CmdArgumentsProcessorHelper.extractComputeJDData(args2, _converter);
        assertNotNull(cjdid2);
        assertEquals("DATE_OBS", cjdid2.getDatetime());
        assertEquals(40.125, (double) cjdid2.getExposure(), 0.0);
        assertEquals(Constants.DEFAULT_JD_COMMENT, cjdid2.getComment());
    }
}
