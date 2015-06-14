package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.inputData.ShiftTimeInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link cz.muni.fi.fits.models.inputData.ChainRecordsInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_ShiftTimeInputDataTest {

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
    public void testValidate_ShiftTimeInputData_Null() throws Exception {
        ShiftTimeInputData stid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_FitsFiles_Null() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("KEYWORD", 0, 0, 0, 0, 1, 0, 0, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_FitsFiles_Empty() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("KEYWORD", 0, 1, 0, 0, 10, 5, 0, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_Keyword_Null() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData(null, 0, -9, 0, 0, 0, 8, 12, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_Keyword_Empty() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("", 0, 3, 0, -2, 0, 4, 0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_Keyword_WithInvalidChars() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("KEYWORD*", 0, 0, 0, 0, 4, 0, 0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_Keyword_TooLong() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("KEYWORD_TOO_LONG", 0, -5, 0, 0, 0, 0, 0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_TimeShiftArguments_AllZero() throws Exception {
        ShiftTimeInputData stid = new ShiftTimeInputData("KEYWORD", 0, 0, 0, 0, 0, 0, 0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("No time shift is specified");
        _validator.validate(stid);
    }

    @Test
    public void testValidate_ShiftTimeInputData_Valid() throws Exception {
        ShiftTimeInputData stid1 = new ShiftTimeInputData("KEYWORD", 12, -5, 0, 0, 0, 0, 0, _fitsFiles);
        ShiftTimeInputData stid2 = new ShiftTimeInputData("KEYWORD", 0, 0, 12, 45, -3, 2, 0, _fitsFiles);

        _validator.validate(stid1);
        _validator.validate(stid2);
    }
}
