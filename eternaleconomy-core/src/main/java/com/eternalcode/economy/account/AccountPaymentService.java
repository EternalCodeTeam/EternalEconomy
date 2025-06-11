package com.eternalcode.economy.account;

import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;

public class AccountPaymentService {

    private final AccountManager accountManager;
    private final PluginConfig config;

    public AccountPaymentService(
        AccountManager accountManager, PluginConfig config
    ) {
        this.accountManager = accountManager;
        this.config = config;
    }

    public void payment(Account payer, Account receiver, BigDecimal amount) {
        this.accountManager.save(payer.withBalance(balance -> balance.subtract(amount)));
        this.accountManager.save(receiver.withBalance(balance -> balance.add(amount)));
    }

    public void setBalance(Account account, BigDecimal amount) {
        this.accountManager.save(account.withBalance(amount));
    }

    public void addBalance(Account account, BigDecimal amount) {
        this.accountManager.save(account.withBalance(balance -> balance.add(amount)));
    }

    public void removeBalance(Account account, BigDecimal amount) {
        this.accountManager.save(account.withBalance(balance -> balance.subtract(amount)));
    }

    public void resetBalance(Account account) {
        this.setBalance(account, this.config.defaultBalance);
    }
}
