package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.common.exceptions.ValidationException;
import cz.muni.fi.fits.input.models.ChainRecordsInputData;
import cz.muni.fi.fits.models.ChainValueType;
import cz.muni.fi.fits.common.utils.Tuple;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Tests for validation of {@link ChainRecordsInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public class DefaultValidator_ChainRecordsInputDataTest {

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
    public void testValidate_ChainRecordsInputData_Null() throws Exception {
        ChainRecordsInputData crid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_FitsFiles_Null() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_FitsFiles_Empty() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Keyword_Null() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData(null, chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Keyword_Empty() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>() ;
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Keyword_WithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD*", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Keyword_TooLong() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("TOO_LONG_KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValues_Null() throws Exception {
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", null, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValues_Empty() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Constant_Null() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, null));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Constants_WithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1 čľščč"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 2 žžťáýí"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Constants_TooLong_NotAllowedLongstrings() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "too long constant 1 - too long constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "too long constant 2 - too long constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("have exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Constants_TooLong_AllowedLongstrings() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "too long constant 1 - too long constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "too long constant 2 - too long constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, true, _fitsFiles);

        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeyword_Null() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, null));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeyword_Empty() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, ""));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeyword_WithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KE**ORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeyword_TooLong() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD_TOO_LONG"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, true, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Comment_WithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "commentýáíáí", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Comment_TooLong() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ConstantsAndComment_TooLong_NotAllowedLongstrings() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "long constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "long constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "TOO LONG COMMENT - TOO LONG COMENT - TOO lONG", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment value is too long");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ConstantsAndComment_TooLong_AllowedLongstrings() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "long constant 1"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainValueType.CONSTANT, "long constant 2"));
        chainValues.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "TOO LONG COMMENT - TOO LONG COMENT - TOO lONG", false, true, _fitsFiles);

        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_Valid() throws Exception {
        LinkedList<Tuple<ChainValueType, String>> chainValues1 = new LinkedList<>();
        chainValues1.push(new Tuple<>(ChainValueType.CONSTANT, "constant 1"));
        chainValues1.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues1.push(new Tuple<>(ChainValueType.CONSTANT, "constant 2"));
        ChainRecordsInputData crid1 = new ChainRecordsInputData("KEYWORD", chainValues1, "comment", false, true, _fitsFiles);

        LinkedList<Tuple<ChainValueType, String>> chainValues2 = new LinkedList<>();
        chainValues2.push(new Tuple<>(ChainValueType.KEYWORD, "NEW_KW"));
        chainValues2.push(new Tuple<>(ChainValueType.KEYWORD, "KEYWORD"));
        chainValues2.push(new Tuple<>(ChainValueType.CONSTANT, " "));
        chainValues2.push(new Tuple<>(ChainValueType.KEYWORD, "ANOTHER"));
        ChainRecordsInputData crid2 = new ChainRecordsInputData("KEYWORD", chainValues2, "comment", false, true, _fitsFiles);

        _validator.validate(crid1);
        _validator.validate(crid2);
    }
}
