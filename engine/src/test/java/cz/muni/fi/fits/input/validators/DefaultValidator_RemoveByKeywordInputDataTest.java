package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.RemoveByKeywordInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link RemoveByKeywordInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_RemoveByKeywordInputDataTest {

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
    public void testValidate_RemoveByKeywordInputData_Null() throws Exception {
        RemoveByKeywordInputData rbkid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_FitsFiles_Null() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD", null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_FitsFiles_Empty() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD", new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_Keyword_Null() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData(null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_Keyword_Empty() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_Keyword_WithInvalidChars() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD*", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_Keyword_TooLong() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("TOO_LONG_KEYWORD", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_Valid() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD", _fitsFiles);

        _validator.validate(rbkid);
    }
}
