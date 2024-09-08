package com.eternalcode.economy.format;

import com.eternalcode.economy.config.implementation.PluginConfig;

import java.math.BigDecimal;
import java.util.List;

import static com.eternalcode.economy.format.DecimalFormatterUtil.getFractionalPart;
import static com.eternalcode.economy.format.DecimalFormatterUtil.getIntegralPart;
import static com.eternalcode.economy.format.DecimalFormatterUtil.getLengthOfIntegralPart;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;

/**
 * @author Rafał "shitzuu" Chomczyk
 **/
public class DecimalFormatterImpl implements DecimalFormatter {

    public static final String TRUNCATED_AMOUNT_DELIMITER = ".";
    private static final int INTEGRAL_PART_INIT_OFFSET = 1;
    private static final long SMALLEST_SCALE_FACTOR = 1_000;

    private final PluginConfig pluginConfig;

    public DecimalFormatterImpl(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    private static String getFormattedAmountWithSuffix(double amount, double divisor, char suffix) {
        return getTruncatedAmount(amount / divisor) + suffix;
    }

    // This optimization had to be done as in microbenchmark it was
    // that use of this method was approximately 15x faster than use
    // of built-in DecimalFormat.
    private static String getTruncatedAmount(double amount) {
        double fractionalPart = getFractionalPart(amount);
        if (fractionalPart < 0.01) {
            return Long.toString((long) amount);
        }

        fractionalPart *= 100;
        fractionalPart =
                (fractionalPart < 99 && fractionalPart % 1 >= 0.5) ? ceil(fractionalPart) : floor(fractionalPart);
        return (long) amount + TRUNCATED_AMOUNT_DELIMITER + (long) fractionalPart;
    }

    public String getFormattedDecimal(BigDecimal amount) {
        return this.getFormattedDecimal(amount.doubleValue());
    }

    public String getFormattedDecimal(double amount) {
        if (amount < SMALLEST_SCALE_FACTOR) {
            return getTruncatedAmount(amount);
        }

        final long integralPart = getIntegralPart(amount);
        final int integralPartLength = getLengthOfIntegralPart(integralPart) - INTEGRAL_PART_INIT_OFFSET;
        final int nearestScaleDivider = integralPartLength / 3 - 1;

        List<DecimalUnit> units = this.pluginConfig.units.format;
        DecimalUnit decimalUnit = units.get(nearestScaleDivider);

        return getFormattedAmountWithSuffix(
                amount,
                decimalUnit.getFactor(),
                decimalUnit.getSuffix());
    }

    @Override
    public String format(BigDecimal amount) {
        return this.getFormattedDecimal(amount);
    }

    @Override
    public String format(double amount) {
        return this.getFormattedDecimal(amount);
    }
}