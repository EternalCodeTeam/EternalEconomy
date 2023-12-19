package com.eternalcode.eternaleconomy.eco;

import java.util.UUID;
public interface Economy {

    boolean createAccount(UUID uuid);
    boolean hasAccount(UUID uuid);
    boolean withdraw(UUID uuid, double balance);
    boolean deposit(UUID uuid, double balance);
    boolean set(UUID uuid, double balance);
    boolean has(UUID uuid, double balance);
    PlayerBalance getBalance(UUID uuid);

}
