package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import cz.muni.fi.fits.exceptions.WrongNumberOfParametersException;
import cz.muni.fi.fits.input.converters.DefaultTypeConverter;
import cz.muni.fi.fits.input.converters.TypeConverter;
import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.TimeObject;
import cz.muni.fi.fits.models.inputData.ComputeHJDInputData;
import cz.muni.fi.fits.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests for extraction of input data for operation <b>Compute Heliocentric Julian Date</b>
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public class ProcessorHelper_ExtractComputeHJDDataTest {

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
    public void testExtractComputeHJDData_Parameters_WrongNumber() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME" };

        exception.expect(WrongNumberOfParametersException.class);
        CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
    }

    @Test
    public void testExtractComputeHJDData_DatetimeParameter_LocalDateTime() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "2015-05-08T12:34:56", "EXPOSURE", "14:22:30", "-15:-16:-17" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDatetime() instanceof LocalDateTime);
        assertEquals(LocalDateTime.of(2015, 5, 8, 12, 34, 56), chjdid.getDatetime());
    }

    @Test
    public void testExtractComputeHJDData_DatetimeKeyword_String() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATE tImE", "EXPOSURE", "14:22:30", "-15:-16:-17" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDatetime() instanceof String);
        assertEquals("DATE TIME", chjdid.getDatetime());
    }

    @Test
    public void testExtractComputeHJDData_ExposureParameter_Double() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "2015-05-08T12:34:56", "25.45", "14:22:30", "-15:-16:-17" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getExposure() instanceof Double);
        assertEquals(25.45, (double) chjdid.getExposure(), 0.0);
    }

    @Test
    public void testExtractComputeHJDData_ExposureKeyword_String() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "ExposurE", "14:22:30", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getExposure() instanceof String);
        assertEquals("EXPOSURE", chjdid.getExposure());
    }

    @Test
    public void testExtractComputeHJDData_RightAscensionKeyword_String() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "rghtAsc", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getRightAscension() instanceof String);
        assertEquals("RGHTASC", chjdid.getRightAscension());
    }

    @Test
    public void testExtractComputeHJDData_RightAscensionParameter_TimeObject_Valid() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getRightAscension() instanceof TimeObject);
        TimeObject rightAscension = (TimeObject) chjdid.getRightAscension();
        assertEquals(14.0, rightAscension.getHours(), 0.0);
        assertEquals(22.0, rightAscension.getMinutes(), 0.0);
        assertEquals(30.0, rightAscension.getSeconds(), 0.0);
    }

    @Test
    public void testExtractComputeHJDData_RightAscensionParameter_TimeObject_InvalidFormat() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:abc:30", "-15:-16:-17", "comment" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("is in invalid format");
        CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
    }

    @Test
    public void testExtractComputeHJDData_RightAscensionParameter_Double() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14.5689", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getRightAscension() instanceof Double);
        double rightAscension = (double) chjdid.getRightAscension();
        assertEquals(14.5689, rightAscension, 0.0);
    }

    @Test
    public void testExtractComputeHJDData_RightAscensionParameter_Decimal() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "2514.5689E385", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getRightAscension() instanceof BigDecimal);
        BigDecimal rightAscension = (BigDecimal) chjdid.getRightAscension();
        assertEquals(new BigDecimal("2514.5689E385"), rightAscension);
    }

    @Test
    public void testExtractComputeHJDData_DeclinationKeyword_String() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "dec" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDeclination() instanceof String);
        assertEquals("DEC", chjdid.getDeclination());
    }

    @Test
    public void testExtractComputeHJDData_DeclinationParameter_DegreesObject_Valid() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "-15:-16:-17" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDeclination() instanceof DegreesObject);
        DegreesObject declination = (DegreesObject) chjdid.getDeclination();
        assertEquals(-15.0, declination.getDegrees(), 0.0);
        assertEquals(-16.0, declination.getMinutes(), 0.0);
        assertEquals(-17.0, declination.getSeconds(), 0.0);
    }

    @Test
    public void testExtractComputeHJDData_DeclinationParameter_DegreesObject_InvalidFormat() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "decln:-16:-17" };

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("is in invalid format");
        CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
    }

    @Test
    public void testExtractComputeHJDData_DeclinationParameter_Double() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "226.2481" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDeclination() instanceof Double);
        double declination = (double) chjdid.getDeclination();
        assertEquals(226.2481, declination, 0.0);
    }

    @Test
    public void testExtractComputeHJDData_DeclinationParameter_Decimal() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "2261248964582316452.2481E320" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertTrue(chjdid.getDeclination() instanceof BigDecimal);
        BigDecimal declination = (BigDecimal) chjdid.getDeclination();
        assertEquals(new BigDecimal("2261248964582316452.2481E320"), declination);
    }

    @Test
    public void testExtractComputeHJDData_Comment_PresentInData() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "12:-16:-17", "comment" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertNotNull(chjdid.getComment());
        assertEquals("comment", chjdid.getComment());
    }

    @Test
    public void testExtractComputeHJDData_Comment_Default() throws Exception {
        String[] args = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "EXPOSURE", "14:22:30", "12:-16:-17" };

        ComputeHJDInputData chjdid = CmdArgumentsProcessorHelper.extractComputeHJDData(args, _converter);
        assertNotNull(chjdid);
        assertNotNull(chjdid.getComment());
        assertEquals(Constants.DEFAULT_HJD_COMMENT, chjdid.getComment());
    }

    @Test
    public void testExtractComputeHJDData_CorrectParameters() throws Exception {
        String[] args1 = new String[] { "hjd", FILE_PATH.toString(), "DATETIME", "12.48", "RA", "-15:-16:-17", "comment" };

        ComputeHJDInputData chjdid1 = CmdArgumentsProcessorHelper.extractComputeHJDData(args1, _converter);
        assertNotNull(chjdid1);
        assertEquals("DATETIME", chjdid1.getDatetime());
        assertEquals(12.48, (double) chjdid1.getExposure(), 0.0);
        assertEquals("RA", chjdid1.getRightAscension());
        assertNotNull(chjdid1.getDeclination());
        assertTrue(chjdid1.getDeclination() instanceof DegreesObject);
        DegreesObject declination = (DegreesObject) chjdid1.getDeclination();
        assertEquals(-15.0, declination.getDegrees(), 0.0);
        assertEquals(-16.0, declination.getMinutes(), 0.0);
        assertEquals(-17.0, declination.getSeconds(), 0.0);
        assertEquals("comment", chjdid1.getComment());


        String[] args2 = new String[] { "hjd", FILE_PATH.toString(), "1970-01-01T00:00:00.0", "EXPTIME", "14:-12:-30", "DEC" };

        ComputeHJDInputData chjdid2 = CmdArgumentsProcessorHelper.extractComputeHJDData(args2, _converter);
        assertNotNull(chjdid2);
        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0), chjdid2.getDatetime());
        assertEquals("EXPTIME", chjdid2.getExposure());
        assertNotNull(chjdid2.getRightAscension());
        assertTrue(chjdid2.getRightAscension() instanceof TimeObject);
        TimeObject rightAscension = (TimeObject) chjdid2.getRightAscension();
        assertEquals(14.0, rightAscension.getHours(), 0.0);
        assertEquals(-12.0, rightAscension.getMinutes(), 0.0);
        assertEquals(-30.0, rightAscension.getSeconds(), 0.0);
        assertEquals("DEC", chjdid2.getDeclination());
        assertEquals(Constants.DEFAULT_JD_COMMENT, chjdid2.getComment());
    }
}
