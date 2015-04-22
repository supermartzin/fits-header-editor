package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.inputData.ChangeKeywordInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link cz.muni.fi.fits.models.inputData.ChangeKeywordInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.0
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
    public void testValidate_ChangeKeywordInputData_NullFitsFiles() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW", false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NoFitsFiles() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NullOldKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData(null, "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_EmptyOldKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_OldKeywordWithInvalidChars() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW*", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_TooLongOldKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("TOO_LONG_OLD_KEYWORD", "NEW_KW", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NullNewKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_EmptyNewKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_NewKeywordWithInvalidChars() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "NEW_KW*", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(ckid);
    }

    @Test
    public void testValidate_ChangeKeywordInputData_TooLongNewKeyword() throws Exception {
        ChangeKeywordInputData ckid = new ChangeKeywordInputData("OLD_KW", "TOO_LONG_NEW_KEYWORD", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(ckid);
    }
}
