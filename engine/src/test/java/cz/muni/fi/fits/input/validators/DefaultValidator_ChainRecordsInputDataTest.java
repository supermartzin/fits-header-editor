package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.inputData.ChainRecordsInputData;
import cz.muni.fi.fits.utils.Tuple;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Tests for validation of {@link cz.muni.fi.fits.models.inputData.ChainRecordsInputData} input data
 * in {@link DefaultInputDataValidator} class
 *
 * @author Martin Vrábel
 * @version 1.1
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
    public void testValidate_ChainRecordsInputData_NullFitsFiles() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, null);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NoFitsFiles() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData(null, chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_EmptyKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>() ;
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_KeywordWithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD*", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("TOO_LONG_KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullChainValues() throws Exception {
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", null, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_EmptyChainValues() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullConstant() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, null));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ConstantsWithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1 čľščč"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 2 žžťáýí"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongConstants() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "too long constant 1 - too long constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "too long constant 2 - too long constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("have exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullChainValuesKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, null));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_EmptyChainValuesKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, ""));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeywordWithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KE**ORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongChainValuesKeyword() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD_TOO_LONG"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_CommentWithInvalidChars() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "commentýáíáí", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongComment() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongConstantsAndComment() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues = new LinkedList<>();
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "long constant 1"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        chainValues.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "long constant 2"));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "TOO LONG COMMENT - TOO LONG COMENT - TOO lONG", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ValidInputData() throws Exception {
        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues1 = new LinkedList<>();
        chainValues1.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 1"));
        chainValues1.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        chainValues1.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, "constant 2"));
        ChainRecordsInputData crid1 = new ChainRecordsInputData("KEYWORD", chainValues1, "comment", false, true, _fitsFiles);

        LinkedList<Tuple<ChainRecordsInputData.ChainValueType, String>> chainValues2 = new LinkedList<>();
        chainValues2.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "NEW_KW"));
        chainValues2.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "KEYWORD"));
        chainValues2.push(new Tuple<>(ChainRecordsInputData.ChainValueType.CONSTANT, " "));
        chainValues2.push(new Tuple<>(ChainRecordsInputData.ChainValueType.KEYWORD, "ANOTHER"));
        ChainRecordsInputData crid2 = new ChainRecordsInputData("KEYWORD", chainValues2, "comment", false, true, _fitsFiles);

        _validator.validate(crid1);
        _validator.validate(crid2);
    }
}
