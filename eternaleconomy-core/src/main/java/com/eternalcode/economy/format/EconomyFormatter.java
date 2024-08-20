package com.eternalcode.economy.format;

import java.math.BigDecimal;

public interface EconomyFormatter {

    String format(BigDecimal amount);

    String format(double amount);

}
