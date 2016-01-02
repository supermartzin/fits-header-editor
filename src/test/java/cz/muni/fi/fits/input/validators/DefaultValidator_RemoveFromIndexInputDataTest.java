package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.common.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.RemoveFromIndexInputData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for validation of {@link RemoveFromIndexInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class DefaultValidator_RemoveFromIndexInputDataTest {

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
    public void testValidate_RemoveFromIndexInputData_Null() throws Exception {
        RemoveFromIndexInputData rfiid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveFromIndexInputData_FitsFiles_Null() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(2, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveFromIndexInputData_FitsFiles_Empty() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(2, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveFromIndexInputData_Index_InvalidNumber() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number bigger than 0");
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveFromIndexInputData_Valid() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(12, _fitsFiles);

        _validator.validate(rfiid);
    }
}
