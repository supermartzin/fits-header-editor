package cz.muni.fi.fits.engine.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for static methods of {@link NumberUtils} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class NumberUtilsTest {

    private static final int JULIAN_DATE_SCALE = 6;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCreateJDDecimal_Double_NaN() throws Exception {
        double julianDate = Double.NaN;

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("is invalid");
        NumberUtils.createJDDecimal(julianDate);
    }

    @Test
    public void testCreateJDDecimal_Double_Infinite() throws Exception {
        double julianDate = Double.POSITIVE_INFINITY;

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("is invalid");
        NumberUtils.createJDDecimal(julianDate);
    }

    @Test
    public void testCreateJDDecimal_Double_CorrectValue() throws Exception {
        double julianDateDouble = 145236.15478563259;

        BigDecimal julianDate = NumberUtils.createJDDecimal(julianDateDouble);

        assertNotNull(julianDate);
        assertEquals(JULIAN_DATE_SCALE, julianDate.scale());
        assertEquals(new BigDecimal("145236.154786"), julianDate);
        assertNotEquals(new BigDecimal("145236.154785"), julianDate);
    }

    @Test
    public void testCreateJDDecimal_Double_ThresholdValue_1() throws Exception {
        double julianDateDouble = 20.000000000000001;

        BigDecimal julianDate = NumberUtils.createJDDecimal(julianDateDouble);

        assertNotNull(julianDate);
        assertEquals(JULIAN_DATE_SCALE, julianDate.scale());
        assertEquals(new BigDecimal("20.000000"), julianDate);
        assertNotEquals(new BigDecimal("20.000001"), julianDate);
    }

    @Test
    public void testCreateJDDecimal_Double_ThresholdValue_2() throws Exception {
        double julianDateDouble = 96521.999999999;

        BigDecimal julianDate = NumberUtils.createJDDecimal(julianDateDouble);

        assertNotNull(julianDate);
        assertEquals(JULIAN_DATE_SCALE, julianDate.scale());
        assertEquals(new BigDecimal("96522.000000"), julianDate);
        assertNotEquals(new BigDecimal("96521.999999"), julianDate);
    }

    @Test
    public void testCreateJDDecimal_Double_CorrectValue_StringComparison() throws Exception {
        double julianDateDouble = 145236.15478563259;

        BigDecimal julianDate = NumberUtils.createJDDecimal(julianDateDouble);

        assertNotNull(julianDate);
        assertEquals("145236.154786", julianDate.toString());
        assertNotEquals("145236.154785", julianDate.toString());
    }
}