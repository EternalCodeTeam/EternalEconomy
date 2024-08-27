package com.eternalcode.economy.account;

import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;

public class AccountPaymentService {

    private final AccountManager accountManager;
    private final PluginConfig pluginConfig;

    public AccountPaymentService(
            AccountManager accountManager,
            PluginConfig pluginConfig
    ) {
        this.accountManager = accountManager;
        this.pluginConfig = pluginConfig;
    }

    public boolean payment(Account payer, Account receiver, BigDecimal amount) {
        payer = new Account(payer.uuid(), payer.name(), payer.balance().subtract(amount));
        receiver = new Account(receiver.uuid(), receiver.name(), receiver.balance().add(amount));

        this.accountManager.save(payer);
        this.accountManager.save(receiver);

        return true;
    }

    public boolean setBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), amount);
        this.accountManager.save(account);

        return true;
    }

    public boolean addBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().add(amount));
        this.accountManager.save(account);

        return true;
    }

    public boolean removeBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().subtract(amount));
        this.accountManager.save(account);

        return true;
    }

    public boolean resetBalance(Account account) {
        this.setBalance(account, this.pluginConfig.defaultBalance);
        return true;
    }
}