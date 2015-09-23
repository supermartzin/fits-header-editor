package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.ChangeKeywordInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link ChangeKeywordInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_ChangeKeywordInputDataTest {

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
    public void testValidate_ChangeKeywordInputData_Null() throws Exception {
        ChangeKeywordInputData ckid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_FitsFiles_Null() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW", false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_FitsFiles_Empty() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_OldKeyword_Null() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData(null, "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_OldKeyword_Empty() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_OldKeyword_WithInvalidChars() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW*", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_OldKeyword_TooLong() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("TOO_LONG_OLD_KEYWORD", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NewKeyword_Null() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NewKeyword_Empty() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NewKeyword_WithInvalidChars() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW*", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NewKeyword_TooLong() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "TOO_LONG_NEW_KEYWORD", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_Valid() throws Exception {
        ChangeKeywordInputData ckid1 = new ChangeKeywordInputData("OLD_KW", "NEW_KW", false, _fitsFiles);
        ChangeKeywordInputData ckid2 = new ChangeKeywordInputData("OLD", "NEW", true, _fitsFiles);

        _validator.validate(ckid1);
        _validator.validate(ckid2);
    }
}
