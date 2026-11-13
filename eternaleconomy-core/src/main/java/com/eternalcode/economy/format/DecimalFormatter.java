package com.eternalcode.economy.format;

import java.math.BigDecimal;

public interface DecimalFormatter {

    String format(BigDecimal amount);

    String format(double amount);

    String formatBalance(BigDecimal amount);

    String formatExact(BigDecimal amount);
}
