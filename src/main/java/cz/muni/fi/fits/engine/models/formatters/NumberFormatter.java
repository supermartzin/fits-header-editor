package cz.muni.fi.fits.engine.models.formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Helper class for formatting numbers info specific format
 *
 * @author Martin Vrábel
 * @version 1.0.1
 */
public class NumberFormatter {

    private NumberFormatter() {}

    /**
     * Formats inserted number into specified format usable in time or degree inscription,
     * e.g.
     * <pre>
     * {@code 123.5874 => "00123.58" with minimumIntegerDigits 5 and maximumFractionDigits 2
     *   2.0      => "02" with minimumIntegerDigits 2
     *   12.5     => "012.5" with minimumIntegerDigits 3 and maximumFractionDigits 5}
     * </pre>
     *
     * @param number                number number to format
     * @param minimumIntegerDigits  minimum number of digits in integral part of number,
     *                              filled with leadinf zeros if necessary
     * @param maximumFractionDigits maximum number of digits in fractional part of number,
     *                              no rounding is applied
     * @return                      formatted number in {@link String}
     */
    public static String format(double number, int minimumIntegerDigits, int maximumFractionDigits) {
        DecimalFormatSymbols separator = new DecimalFormatSymbols();
        separator.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(separator);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMinimumIntegerDigits(minimumIntegerDigits);
        decimalFormat.setMaximumFractionDigits(maximumFractionDigits);


        return decimalFormat.format(number);
    }
}
