package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.inputData.AddNewToIndexInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link cz.muni.fi.fits.models.inputData.AddNewToIndexInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_AddNewToIndexInputDataTest {

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
    public void testValidate_AddNewToIndexInputData_Null() throws Exception {
        AddNewToIndexInputData antiid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_FitsFiles_Null() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENT", false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_FitsFiles_Empty() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Index_InvalidNumber() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(0, "KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number bigger than 0");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Keyword_Null() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Keyword_Empty() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Keyword_WithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Keyword_TooLong() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Value_Null() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", null, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_StringValue_Empty() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_StringValue_TooLong() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_StringValue_WithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUEčšľť", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_DoubleValue_NotANumber() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", Double.NaN, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_DoubleValue_Infinite() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", Double.POSITIVE_INFINITY, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Comment_WithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENTčšľť", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Comment_TooLong() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_CommentAndStringValue_TooLong() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TO LONG - COMMENT TO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_Valid() throws Exception {
        AddNewToIndexInputData antiid1 = new AddNewToIndexInputData(2, "KEYWORD", 125.45, null, true, _fitsFiles);
        AddNewToIndexInputData antiid2 = new AddNewToIndexInputData(15, "KEYWORD", "random value of testing record", "random comment", false, _fitsFiles);
        AddNewToIndexInputData antiid3 = new AddNewToIndexInputData(43975, "KEYWORD", true, "random comment", false, _fitsFiles);

        _validator.validate(antiid1);
        _validator.validate(antiid2);
        _validator.validate(antiid3);
    }
}
