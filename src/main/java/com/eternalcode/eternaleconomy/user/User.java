package com.eternalcode.eternaleconomy.user;

import java.math.BigDecimal;
import java.util.UUID;

public class User {

    public UUID uniqueId;
    public String name;
    public BigDecimal balance;

    public User(UUID uniqueId, String name, BigDecimal balance) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = balance;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addBalance(BigDecimal balance) {
        this.balance = this.balance.add(balance);
    }

    public void removeBalance(BigDecimal balance) {
        this.balance = this.balance.subtract(balance);
    }

    public void setName(String name) {
        this.name = name;
    }
}
