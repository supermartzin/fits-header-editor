package cz.muni.fi.fits.engine.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for working with numbers in editing engine
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public final class NumberUtils {

    private static final int JULIAN_DATE_DECIMAL_SCALE = 6;

    /**
     * Wrappes provided {@link Double} Julian Date number into a {@link BigDecimal}
     * with predefined scale and rounding
     *
     * @param julianDate    value of Julian Date
     * @return              {@link BigDecimal} instance with wrapped Julian Date
     */
    public static BigDecimal createJDDecimal(double julianDate) {
        if (Double.isNaN(julianDate)
                || Double.isInfinite(julianDate))
            throw new IllegalArgumentException("Input Julian Date double is invalid");

        return new BigDecimal(julianDate).setScale(JULIAN_DATE_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    private NumberUtils() { }
}
