package com.eternalcode.economy.format;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log10;

import java.math.BigDecimal;

/**
 * @author Rafał "shitzuu" Chomczyk
 * @author vuxeim@pm.me <a href="https://vuxe.im/">vuxeim</a>
 **/
final class DecimalFormatterUtil {

    private DecimalFormatterUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static long getIntegralPart(final double amount) {
        return (long) amount;
    }

    public static long getIntegralPart(final BigDecimal amount) {
        return getIntegralPart(amount.doubleValue());
    }

    public static int getLengthOfIntegralPart(final double amount) {
        // unbelievably faster than converting to string and getting length
        /*
        in Java:
            log10(0) == -Infinity
            log10(negative number) == NaN
        in real arithmetic:
            log10(0) is undefined
            log10(negative number) is undefined
        */
        final long integralPart = getIntegralPart(amount);
        if (integralPart == 0) { return 1; }
        if (integralPart < 0) { return getLengthOfIntegralPart(-integralPart); } // add +1 if minus sign has to be taken into account
        return 1 + (int) getIntegralPart(log10(integralPart));
    }

    public static int getLengthOfIntegralPart(final BigDecimal amount) {
        return getLengthOfIntegralPart(amount.doubleValue());
    }

    public static double getFractionalPart(final double amount) {
        return amount % 1;
    }

    public static double getFractionalPart(final BigDecimal amount) {
        return getFractionalPart(amount.doubleValue());
    }

    public static int getLengthOfFractionalPart(final double amount) {
        double fractionalPart = getFractionalPart(amount);
        fractionalPart *= 100;
        fractionalPart =
            (fractionalPart < 99 && fractionalPart % 1 >= 0.5) ? ceil(fractionalPart) : floor(fractionalPart);
        return Long.toString((long) fractionalPart).length();
    }

    public static int getLengthOfFractionalPart(final BigDecimal amount) {
        return getLengthOfFractionalPart(amount.doubleValue());
    }
}
