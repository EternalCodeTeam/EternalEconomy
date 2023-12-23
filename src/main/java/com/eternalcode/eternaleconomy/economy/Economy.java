package com.eternalcode.eternaleconomy.economy;

import java.math.BigDecimal;
import java.util.UUID;

public interface Economy {

    void createAccount(UUID uuid);

    boolean hasAccount(UUID uuid);

    void withdraw(UUID uuid, BigDecimal balance);

    void deposit(UUID uuid, BigDecimal balance);

    void set(UUID uuid, BigDecimal balance);

    boolean has(UUID uuid, BigDecimal balance);

    PlayerBalance getBalance(UUID uuid);
}
