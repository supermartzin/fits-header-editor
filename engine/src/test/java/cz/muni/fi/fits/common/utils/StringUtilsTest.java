package cz.muni.fi.fits.common.utils;

import cz.muni.fi.fits.common.exceptions.ConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for methods in {@link StringUtils} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class StringUtilsTest {

    @Test
    public void testGetExceptionType_Null() throws Exception {
        String type = StringUtils.getExceptionType(null);

        assertNotNull(type);
        assertEquals("", type);
    }

    @Test
    public void testGetExceptionType_Correct() throws Exception {
        Throwable exception1 = new IllegalArgumentException();
        Throwable exception2 = new ConfigurationException();

        String type1 = StringUtils.getExceptionType(exception1);
        assertNotNull(type1);
        assertEquals("IllegalArgumentException", type1);

        String type2 = StringUtils.getExceptionType(exception2);
        assertNotNull(type2);
        assertEquals("ConfigurationException", type2);
    }
}