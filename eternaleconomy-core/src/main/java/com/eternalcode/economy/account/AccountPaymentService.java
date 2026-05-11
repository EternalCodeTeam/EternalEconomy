package com.eternalcode.economy.account;

import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;

public class AccountPaymentService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final AccountManager accountManager;
    private final PluginConfig config;

    public AccountPaymentService(
        AccountManager accountManager, PluginConfig config
    ) {
        this.accountManager = accountManager;
        this.config = config;
    }

    public void payment(Account payer, Account receiver, BigDecimal amount) {
        this.requirePositiveAmount(amount);
        this.requireEnoughBalance(payer, amount);

        this.accountManager.save(payer.withBalance(balance -> balance.subtract(amount)));
        this.accountManager.save(receiver.withBalance(balance -> balance.add(amount)));
    }

    public void setBalance(Account account, BigDecimal amount) {
        if (amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative, got: " + amount);
        }

        this.accountManager.save(account.withBalance(amount));
    }

    public void addBalance(Account account, BigDecimal amount) {
        this.requirePositiveAmount(amount);

        this.accountManager.save(account.withBalance(balance -> balance.add(amount)));
    }

    public void removeBalance(Account account, BigDecimal amount) {
        this.requirePositiveAmount(amount);
        this.requireEnoughBalance(account, amount);

        this.accountManager.save(account.withBalance(balance -> balance.subtract(amount)));
    }

    public void resetBalance(Account account) {
        this.setBalance(account, this.config.defaultBalance);
    }

    public boolean hasEnoughBalance(Account account, BigDecimal amount) {
        this.requirePositiveAmount(amount);

        return account.balance().compareTo(amount) >= 0;
    }

    private void requirePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive, got: " + amount);
        }
    }

    private void requireEnoughBalance(Account account, BigDecimal amount) {
        if (!this.hasEnoughBalance(account, amount)) {
            throw new IllegalArgumentException(
                "Account " + account.uuid() + " has insufficient balance for amount: " + amount
            );
        }
    }
}
