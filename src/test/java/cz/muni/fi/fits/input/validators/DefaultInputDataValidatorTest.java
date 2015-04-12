package cz.muni.fi.fits.input.validators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cz.muni.fi.fits.exceptions.ValidationException;
import cz.muni.fi.fits.models.inputData.*;
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
 * TODO description
 */
public class DefaultInputDataValidatorTest {

    private static InputDataValidator _validator;
    private static Collection<File> _fitsFiles;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() throws Exception {
        _validator = new DefaultInputDataValidator();
        _fitsFiles = Sets.newHashSet(new File("sample1.fits"), new File("sample2.fits"));
    }


    // validate 'AddNewRecordInputData'
    @Test
    public void testValidate_AddNewRecordInputData_Null() throws Exception {
        AddNewRecordInputData anrid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_NoFitsFiles() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_NullKeyword() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData(null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_EmptyKeyword() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_KeywordWithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEY WORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_TooLongKeyword() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_NullValue() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", null, "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_EmptyStringValue() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "", "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_TooLongStringValue() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_StringValueWithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUEýššá", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_CommentWithInvalidChars() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMENTčšťščžťýž", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_TooLongComment() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(anrid);
    }

    @Test
    public void testValidate_AddNewRecordInputData_TooLongCommentAndStringValue() throws Exception {
        AddNewRecordInputData anrid = new AddNewRecordInputData("KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TO LONG - COMMENT TO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(anrid);
    }


    // validate 'AddNewToIndexInputData'
    @Test
    public void testValidate_AddNewToIndexInputData_Null() throws Exception {
        AddNewToIndexInputData antiid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_NoFitsFiles() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_InvalidIndex() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(0, "KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number bigger than 0");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_NullKeyword() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_EmptyKeyword() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_KeywordWithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_TooLongKeyword() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_NullValue() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", null, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_EmptyStringValue() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_TooLongStringValue() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                        "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_StringValueWithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUEčšľť", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_CommentWithInvalidChars() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENTčšľť", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_TooLongComment() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(antiid);
    }

    @Test
    public void testValidate_AddNewToIndexInputData_TooLongCommentAndStringValue() throws Exception {
        AddNewToIndexInputData antiid = new AddNewToIndexInputData(2, "KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TO LONG - COMMENT TO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(antiid);
    }


    // validate 'RemoveByKeywordInputData'
    @Test
    public void testValidate_RemoveByKeywordInputData_Null() throws Exception {
        RemoveByKeywordInputData rbkid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_NoFitsFiles() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD", new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_NullKeyword() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData(null, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_EmptyKeyword() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_KeywordWithInvalidChars() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("KEYWORD*", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(rbkid);
    }

    @Test
    public void testValidate_RemoveByKeywordInputData_TooLongKeyword() throws Exception {
        RemoveByKeywordInputData rbkid = new RemoveByKeywordInputData("TOO_LONG_KEYWORD", _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(rbkid);
    }


    // validate 'RemoveByIndexInputData'
    @Test
    public void testValidate_RemoveByIndexInputData_Null() throws Exception {
        RemoveFromIndexInputData rfiid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveByIndexInputData_NoFitsFiles() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(2, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(rfiid);
    }

    @Test
    public void testValidate_RemoveByIndexInputData_InvalidIndex() throws Exception {
        RemoveFromIndexInputData rfiid = new RemoveFromIndexInputData(0, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("must be number bigger than 0");
        _validator.validate(rfiid);
    }


    // validate 'ChangeKeywordInputData'
    @Test
    public void testValidate_ChangeKeywordInputData_Null() throws Exception {
        ChangeKeywordInputData ckid = null;

        exception.expect(IllegalArgumentException.class);
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


    // validate 'ChangeValueByKeywordInputData'
    @Test
    public void testValidate_ChangeValueByKeywordInputData_Null() throws Exception {
        ChangeValueByKeywordInputData cvbkid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_NoFitsFiles() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENT", false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_NullKeyword() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData(null, "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_EmptyKeyword() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_KeywordWithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD*", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_TooLongKeyword() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("TOO_LONG_KEYWORD", "VALUE", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_NullValue() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", null, "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_EmptyStringValue() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "", "COMMENT", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_TooLongStringValue() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE TOO LONG - VALUE",
                                "", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_StringValueWithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUEščľščščšľ", null, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_CommentWithInvalidChars() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENTščľščščšľ", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_TooLongComment() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE", "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(cvbkid);
    }

    @Test
    public void testValidate_ChangeValueByKeywordInputData_TooLongCommentAndStringValue() throws Exception {
        ChangeValueByKeywordInputData cvbkid = new ChangeValueByKeywordInputData("KEYWORD", "VALUE VALUE VALUE VALUE VALUE VALUE", "COMMENT TO LONG - COMMENT TO LONG", false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(cvbkid);
    }


    // validate 'ChainRecordsInputData'
    @Test
    public void testValidate_ChainRecordsInputData_Null() throws Exception {
        ChainRecordsInputData crid = null;

        exception.expect(IllegalArgumentException.class);
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NoFitsFiles() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, new HashSet<>());

        exception.expect(ValidationException.class);
        exception.expectMessage("No FITS files provided");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData(null, chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_EmptyKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_KeywordWithInvalidChars() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD*", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
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
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", ""), new Tuple<>("keyword", "")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullConstant() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(
                new Tuple<>("constant", "constant 1"),
                new Tuple<>("keyword", "KEYWORD"),
                new Tuple<>("constant", null)));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ConstantsWithInvalidChars() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(
                new Tuple<>("constant", "constant 1 čľščč"),
                new Tuple<>("keyword", "KEYWORD"),
                new Tuple<>("constant", "constant 2 žžťáýí")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongConstants() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(
                new Tuple<>("constant", "too long constant 1 - too long constant 1"),
                new Tuple<>("constant", "too long constant 2 - too long constant 2")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("have exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_NullChainValuesKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(
                new Tuple<>("constant", "constant 1"),
                new Tuple<>("keyword", null),
                new Tuple<>("constant", "constant 2")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be null");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_EmptyChainValuesKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant"), new Tuple<>("keyword", "")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("cannot be empty");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_ChainValuesKeywordWithInvalidChars() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant"), new Tuple<>("keyword", "KE**ORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongChainValuesKeyword() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant"), new Tuple<>("keyword", "KEYWORD_TOO_LONG")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, null, false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_CommentWithInvalidChars() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant 1"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "commentýáíáí", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("contains invalid non-ASCII characters");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongComment() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(new Tuple<>("constant", "constant"), new Tuple<>("keyword", "KEYWORD")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "COMMENT TO LONG - COMMENT TO LONG - COMMENT TO LONG - COMMENT TOO", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("has exceeded maximum allowed length");
        _validator.validate(crid);
    }

    @Test
    public void testValidate_ChainRecordsInputData_TooLongConstantsAndComment() throws Exception {
        LinkedList<Tuple> chainValues = new LinkedList<>(Lists.<Tuple>newArrayList(
                new Tuple<>("constant", "long constant 1"),
                new Tuple<>("keyword", "KEYWORD"),
                new Tuple<>("constant", "long constant 2")));
        ChainRecordsInputData crid = new ChainRecordsInputData("KEYWORD", chainValues, "TOO LONG COMMENT - TOO LONG COMENT - TOO lONG", false, false, _fitsFiles);

        exception.expect(ValidationException.class);
        exception.expectMessage("Comment is too long");
        _validator.validate(crid);
    }
}