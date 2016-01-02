package cz.muni.fi.fits.engine.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for methods of {@link MandatoryFITSKeywords} class
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class MandatoryFITSKeywordsTest {

    @Test
    public void testMatchesMandatoryKeyword_Keyword_Null() throws Exception {
        String keyword = null;

        boolean result = MandatoryFITSKeywords.matchesMandatoryKeyword(keyword);

        assertFalse(result);
    }

    @Test
    public void testMatchesMandatoryKeyword_Keyword_IsMandatory() throws Exception {
        String keyword = "NAXIS2";

        boolean result = MandatoryFITSKeywords.matchesMandatoryKeyword(keyword);

        assertTrue(result);
    }

    @Test
    public void testMatchesMandatoryKeyword_Keyword_IsNotMandatory() throws Exception {
        String keyword = "EXPOSURE";

        boolean result = MandatoryFITSKeywords.matchesMandatoryKeyword(keyword);

        assertFalse(result);
    }
}