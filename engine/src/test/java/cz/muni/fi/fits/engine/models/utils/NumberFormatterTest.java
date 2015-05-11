package cz.muni.fi.fits.engine.models.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for formatting methods of {@link NumberFormatter}
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class NumberFormatterTest {

    @Test
    public void testFormat_1() throws Exception {
        double number = 100.0 / 3.0;
        int minimumIntegerDigits = 4;
        int maximumFractionDigits = 5;

        String formatted = NumberFormatter.format(number, minimumIntegerDigits, maximumFractionDigits);

        assertNotNull(formatted);
        assertEquals("0033.33333", formatted);
    }

    @Test
    public void testFormat_2() throws Exception {
        double number = 2.45236;
        int minimumIntegerDigits = 2;
        int maximumFractionDigits = 3;

        String formatted = NumberFormatter.format(number, minimumIntegerDigits, maximumFractionDigits);

        assertNotNull(formatted);
        assertEquals("02.452", formatted);
    }

    @Test
    public void testFormat_3() throws Exception {
        double number = 0.000002;
        int minimumIntegerDigits = 5;
        int maximumFractionDigits = 3;

        String formatted = NumberFormatter.format(number, minimumIntegerDigits, maximumFractionDigits);

        assertNotNull(formatted);
        assertEquals("00000", formatted);
    }
}