package com.eternalcode.economy.withdraw;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WithdrawDecayCalculator {

    private static final int DECAY_SCALE = 2;
    private static final double MILLIS_PER_HOUR = 3_600_000.0;
    private static final double MAX_HOURLY_RATE = 0.999;
    private static final long MIN_SCHEDULE_DELAY_MILLIS = 1_000L;

    public BigDecimal computeDecayedValue(
        BigDecimal nominal, long createdAtMillis, long nowMillis, double hourlyRatePercent, BigDecimal floor
    ) {
        double hourlyRate = this.clampRate(hourlyRatePercent / 100.0);
        long elapsedMillis = nowMillis - createdAtMillis;

        if (elapsedMillis <= 0 || hourlyRate <= 0) {
            return nominal.max(floor).setScale(DECAY_SCALE, RoundingMode.HALF_UP);
        }

        double elapsedHours = elapsedMillis / MILLIS_PER_HOUR;
        double retention = Math.pow(1.0 - hourlyRate, elapsedHours);

        if (Double.isNaN(retention) || retention <= 0) {
            return floor.setScale(DECAY_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal decayed = nominal.multiply(BigDecimal.valueOf(retention));
        return decayed.max(floor).min(nominal).setScale(DECAY_SCALE, RoundingMode.HALF_UP);
    }

    public long computeNextUpdateAtMillis(
        BigDecimal nominal, long createdAtMillis, BigDecimal lastDisplayedValue,
        BigDecimal floor, double hourlyRatePercent, double thresholdRatioPercent
    ) {
        double hourlyRate = this.clampRate(hourlyRatePercent / 100.0);
        double thresholdRatio = thresholdRatioPercent / 100.0;

        if (hourlyRate <= 0 || lastDisplayedValue.compareTo(floor) <= 0) {
            return Long.MAX_VALUE;
        }

        double nominalD = nominal.doubleValue();
        double floorD = floor.doubleValue();
        double target = lastDisplayedValue.doubleValue() * (1.0 - thresholdRatio);

        if (target <= floorD) {
            target = floorD;
        }
        if (target <= 0 || target >= nominalD) {
            return Long.MAX_VALUE;
        }

        double hoursFromCreation = Math.log(target / nominalD) / Math.log(1.0 - hourlyRate);
        long targetMillis = createdAtMillis + (long) Math.ceil(hoursFromCreation * MILLIS_PER_HOUR);

        return Math.max(targetMillis, System.currentTimeMillis() + MIN_SCHEDULE_DELAY_MILLIS);
    }

    private double clampRate(double rate) {
        if (rate < 0) {
            return 0;
        }
        return Math.min(rate, MAX_HOURLY_RATE);
    }
}
