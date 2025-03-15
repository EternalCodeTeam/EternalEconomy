package com.eternalcode.economy.account;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.UnaryOperator;

public record Account(UUID uuid, String name, BigDecimal balance) {

    public Account withBalance(BigDecimal balance) {
        return new Account(uuid, name, balance);
    }

    public Account withBalance(UnaryOperator<BigDecimal> operator) {
        return new Account(uuid, name, operator.apply(balance));
    }

}