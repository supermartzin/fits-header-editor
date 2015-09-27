package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.common.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.ComputeJDInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Tests for validation of {@link ComputeJDInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_ComputeJDInputDataTest {

    private static InputDataValidator _validator;
    private static Collection<File> _fitsFiles;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() throws Exception {
        _validator = new DefaultInputDataValidator();
        _fitsFiles = Sets.newHashSet(new File("sample1.fits"), new File("sample2.fits"));
    }


    @Test
    public void testValidate_ComputeJDInputData_Null() throws Exception {
        ComputeJDInputData cjdid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_FitsFiles_Null() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", 40.0, "comment", null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_FitsFiles_Empty() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", 40.0, "comment");

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_DatetimeParameter_Null() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData((String)null, 40.0, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_DatetimeKeyword_Empty() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("", 40.0, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_DatetimeKeyword_WithInvalidChars() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATE TIME", 40.0, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_DatetimeKeyword_TooLong() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("TOO_LONG_KEYWORD", 40.0, null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureParameter_Null() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", null, null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureDoubleValue_NotANumber() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", Double.NaN, null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureDoubleValue_Infinite() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", Double.NEGATIVE_INFINITY, null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureKeyword_Empty() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", "", null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureKeyword_WithInvalidChars() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", "EXPOS**E", null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_ExposureKeyword_TooLong() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", "EXPOSURE_TOO_LONG", null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_Comment_WithInvalidChars() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", 40.0, "commentčšľľšč", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_Comment_TooLong() throws Exception {
        ComputeJDInputData cjdid = new ComputeJDInputData("DATETIME", 40.0, "comment too long - comment too long - comment too long", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cjdid);
    }

    @Test
    public void testValidate_ComputeJDInputData_Valid() throws Exception {
        ComputeJDInputData cjdid1 = new ComputeJDInputData("DATETIME", 12.45, "comment", _fitsFiles);
        ComputeJDInputData cjdid2 = new ComputeJDInputData(LocalDateTime.of(2014, 5, 5, 12, 34, 56), "EXPOSURE", null, _fitsFiles);
        ComputeJDInputData cjdid3 = new ComputeJDInputData("DATETIME", "EXPOSURE", "comment", _fitsFiles);
        ComputeJDInputData cjdid4 = new ComputeJDInputData(LocalDateTime.of(2014, 5, 5, 12, 34, 56), 1000.56, null, _fitsFiles);

        _validator.validate(cjdid1);
        _validator.validate(cjdid2);
        _validator.validate(cjdid3);
        _validator.validate(cjdid4);
    }
}
