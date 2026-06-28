package com.eternalcode.economy.format;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rafał "shitzuu" Chomczyk
 * @author vuxeim@pm.me <a href="https://vuxe.im/">vuxeim</a>
 **/
final class DecimalFormatterUtil {

    private static final long[] POWERS_OF_TEN = new long[19];
    static {
        long value = 1;
        for (int i = 0; i < POWERS_OF_TEN.length; i++) {
            POWERS_OF_TEN[i] = value;
            value *= 10;
        }
    }

    private static int countDigits(final long number) {
        int low = 0;
        int high = POWERS_OF_TEN.length - 1;
        while (low < high)
        {
            int mid = (low + high + 1) >>> 1;
            if (POWERS_OF_TEN[mid] <= number)
            {
                low = mid;
            }
            else
            {
                high = mid - 1;
            }
        }
        return low + 1;
    }

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
        final long integralPart = getIntegralPart(amount);
        final long absoluteIntegralPart =
            (integralPart == Long.MIN_VALUE)
            ? Long.MAX_VALUE
            : Math.abs(integralPart);

        return countDigits(absoluteIntegralPart);
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
        BigDecimal exact = BigDecimal.valueOf(Math.abs(amount))
            .remainder(BigDecimal.ONE)
            .movePointRight(2)
            .setScale(0, RoundingMode.HALF_UP);
        long hundredths = exact.longValueExact() % 100;
        return (hundredths < 10) ? 1 : 2;
    }

    public static int getLengthOfFractionalPart(final BigDecimal amount) {
        return getLengthOfFractionalPart(amount.doubleValue());
    }
}
