package com.eternalcode.eternaleconomy.eco;

import java.util.UUID;
public interface Economy {

    boolean createAccount(UUID var1);
    boolean hasAccount(UUID var1);
    boolean withdraw(UUID var1, double var2);
    boolean deposit(UUID var1, double var2);
    boolean set(UUID var1, double var2);
    boolean has(UUID var1, double var2);
    PlayerBalance getBalance(UUID var1);

}
