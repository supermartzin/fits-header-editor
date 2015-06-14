package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.DegreesObject;
import cz.muni.fi.fits.models.TimeObject;
import cz.muni.fi.fits.models.inputData.ComputeHJDInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Tests for validation of {@link ComputeHJDInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.2
 */
public class DefaultValidator_ComputeHJDInputDataTest {

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
    public void testValidate_ComputeHJDInputData_Null() throws Exception {
        ComputeHJDInputData chjdid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_FitsFiles_Null() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment", null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_FitsFiles_Empty() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment");

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DatetimeParameter_Null() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData(null, 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DatetimeParameter_InvalidType() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData(true, "exptime",
                new TimeObject(12, 25.3, 6), "DEC", "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be either String keyword or LocalDateTime value");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DatetimeKeyword_Empty() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DatetimeKeyword_WithInvalidChars() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATE TIME", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DatetimeKeyword_TooLong() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("TOO_LONG_KEYWORD", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureParameter_Null() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", null,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureParameter_InvalidType() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData(LocalDateTime.now(), true,
                new TimeObject(12, 25.3, 6), "DEC", "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be either String keyword or Double value");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureKeyword_Empty() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "",
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureKeyword_WithInvalidChars() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOS**E",
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureKeyword_TooLong() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE_TOO_LONG",
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureDoubleValue_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", Double.NaN,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_ExposureDoubleValue_Infinite() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", Double.POSITIVE_INFINITY,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscension_Null() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                null, new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionParameter_InvalidType() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData(LocalDateTime.now(), 10.0,
                false, "DEC", "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be either String keyword, TimeObject value or decimal number value");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionHours_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(Double.NaN, 24.0, 5), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionMinutes_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, Double.NaN, 5), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionSeconds_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, Double.NaN), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionKeyword_Empty() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPTIME",
                "", new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionKeyword_WithInvalidChars() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                "RA/*/", new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionKeyword_TooLong() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                "RIGHT_ASCENSION", new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionDouble_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                Double.NaN, new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_RightAscensionDoubleValue_Infinite() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                Double.POSITIVE_INFINITY, new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfRightAscensionParameter_1() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                372.256, new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfRightAscensionParameter_2() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new BigDecimal(372.256), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfRightAscensionParameter_3() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(24, 15, 68), new DegreesObject(-87, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_Declination_Null() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 25.3, 6), null, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationParameter_InvalidType() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 25.3, 6), true, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be either String keyword, DegreesObject value or decimal number value");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationDegrees_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 24.0, 5), new DegreesObject(Double.NaN, 25, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationMinutes_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 23, 5), new DegreesObject(-87, Double.NaN, 14.256), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationSeconds_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), new DegreesObject(-87, 25, Double.NaN), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfDeclinationParameter_1() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), -91.36, null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfDeclinationParameter_2() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), new BigDecimal(-91.36), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_InvalidRangeOfDeclinationParameter_3() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), new DegreesObject(89.8, 25.16, 99.99), null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("is not in range");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationKeyword_Empty() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPTIME",
                new TimeObject(12, 14, 23), "", null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationKeyword_WithInvalidChars() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), "DEC*/", null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationKeyword_TooLong() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), "DECLINATION_TOO_LONG", "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationDoubleValue_NotANumber() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), Double.NaN, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_DeclinationDoubleValue_Infinite() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(12, 14, 23), Double.NEGATIVE_INFINITY, "comment", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_Comment_WithInvalidChars() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "commentčšľľšč", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_Comment_TooLong() throws Exception {
        ComputeHJDInputData chjdid = new ComputeHJDInputData("DATETIME", 40.0,
                new TimeObject(12, 25.3, 6), new DegreesObject(-87, 25, 14.256), "comment too long - comment too long - comment too long", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(chjdid);
    }

    @Test
    public void testValidate_ComputeHJDInputData_Valid() throws Exception {
        ComputeHJDInputData chjdid1 = new ComputeHJDInputData("DATETIME", 12.45,
                new TimeObject(12, 14.36, 23), new DegreesObject(0, 25, 89.12), "comment", _fitsFiles);
        ComputeHJDInputData chjdid2 = new ComputeHJDInputData(LocalDateTime.of(2014, 5, 5, 12, 34, 56), "EXPOSURE",
                new TimeObject(12, 14.36, 23), new DegreesObject(0, 0, 0), null, _fitsFiles);
        ComputeHJDInputData chjdid3 = new ComputeHJDInputData("DATETIME", "EXPOSURE",
                new TimeObject(0, 0.0, 0.0), new DegreesObject(-87.2, 25, 89.12), "comment", _fitsFiles);
        ComputeHJDInputData chjdid4 = new ComputeHJDInputData(LocalDateTime.of(2014, 5, 5, 12, 34, 56), 1000.56,
                new TimeObject(0, 0.0, 0.0), new DegreesObject(0, 0, 0.0), null, _fitsFiles);

        _validator.validate(chjdid1);
        _validator.validate(chjdid2);
        _validator.validate(chjdid3);
        _validator.validate(chjdid4);
    }
}