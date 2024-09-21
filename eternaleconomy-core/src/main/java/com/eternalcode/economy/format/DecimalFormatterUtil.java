package com.eternalcode.economy.format;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

import java.math.BigDecimal;

/**
 * @author Rafał "shitzuu" Chomczyk
 **/
final class DecimalFormatterUtil {

    private DecimalFormatterUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static long getIntegralPart(final double amount) {
        final double fractionalPart = getFractionalPart(amount);
        return (long) (amount - fractionalPart);
    }

    public static long getIntegralPart(final BigDecimal amount) {
        return getIntegralPart(amount.doubleValue());
    }

    public static int getLengthOfIntegralPart(final double amount) {
        return Long.toString(getIntegralPart(amount)).length();
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
