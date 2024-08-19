package com.eternalcode.economy.account;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private final UUID uuid;
    private String name;
    private BigDecimal accountBalance;

    public Account(UUID uuid, String name, BigDecimal accountBalance) {
        this.uuid = uuid;
        this.name = name;
        this.accountBalance = accountBalance;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getAccountBalance() {
        return this.accountBalance;
    }
}
