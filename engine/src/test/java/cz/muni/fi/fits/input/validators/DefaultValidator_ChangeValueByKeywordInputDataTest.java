package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.common.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.ChangeValueByKeywordInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link ChangeValueByKeywordInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_ChangeValueByKeywordInputDataTest {

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
    public void testValidate_ChangeValueByKeywordInputData_Null() throws Exception {
        ChangeValueByKeywordInputData cvbkid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_FitsFiles_Null() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENT", false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_FitsFiles_Empty() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Keyword_Null() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData(null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Keyword_Empty() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Keyword_WithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Keyword_TooLong() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Value_Null() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", null, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_StringValue_Empty() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_StringValue_TooLong() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_StringValue_WithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUEščľščščšľ", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_DoubleValue_NotANumber() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", Double.NaN, null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a correct number");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_DoubleValue_Infinite() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", Double.NEGATIVE_INFINITY, null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be a finite number");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Comment_WithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENTščľščščšľ", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Comment_TooLong() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_CommentAndStringValue_TooLong() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TO LONG - COMMENT TO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_Valid() throws Exception {
        ChangeValueByKeywordInputData cvbkid1 = new ChangeValueByKeywordInputData("KEYWORD", "random value for testing purposes", "comment", false, _fitsFiles);
        ChangeValueByKeywordInputData cvbkid2 = new ChangeValueByKeywordInputData("KEYWORD", false, "comment", true, _fitsFiles);
        ChangeValueByKeywordInputData cvbkid3 = new ChangeValueByKeywordInputData("KEYWORD", LocalDateTime.of(2014, 5, 5, 12, 34, 56), null, true, _fitsFiles);

        _validator.validate(cvbkid1);
        _validator.validate(cvbkid2);
        _validator.validate(cvbkid3);
    }
}
