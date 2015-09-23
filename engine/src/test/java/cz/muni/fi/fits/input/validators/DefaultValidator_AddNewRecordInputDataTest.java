package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.AddNewRecordInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link AddNewRecordInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_AddNewRecordInputDataTest {

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
    public void testValidate_AddNewRecordInputData_Null() throws Exception {
        AddNewRecordInputData anrid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_FitsFiles_Null() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMMENT", false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_FitsFiles_Empty() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Keyword_Null() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData(null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Keyword_Empty() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Keyword_WithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEY WORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Keyword_TooLong() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Value_Null() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", null, "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_StringValue_Empty() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "", "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_StringValue_TooLong() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_StringValue_WithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUEýššá", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_DoubleValue_NotANumber() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", Double.NaN, null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_DoubleValue_Infinite() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", Double.NEGATIVE_INFINITY, null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Comment_WithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMENTčšťščžťýž", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Comment_TooLong() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_CommentAndStringValue_TooLong() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TOO LONG - COMMENT TOO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_Valid() throws Exception {
        AddNewRecordInputData anrid1 = new AddNewRecordInputData("KEYWORD", 125.45, null, true, _fitsFiles);
        AddNewRecordInputData anrid2 = new AddNewRecordInputData("KEYWORD", "random value of testing record", "random comment", false, _fitsFiles);
        AddNewRecordInputData anrid3 = new AddNewRecordInputData("KEYWORD", true, "random comment", false, _fitsFiles);

        _validator.validate(anrid1);
        _validator.validate(anrid2);
        _validator.validate(anrid3);
    }
}
