package com.eternalcode.economy.account;

import java.math.BigDecimal;

public class AccountPaymentService {

    private final AccountManager accountManager;

    public AccountPaymentService(
            AccountManager accountManager
    ) {
        this.accountManager = accountManager;
    }

    public void payment(Account payer, Account receiver, BigDecimal amount) {
        payer = new Account(payer.uuid(), payer.name(), payer.balance().subtract(amount));
        receiver = new Account(receiver.uuid(), receiver.name(), receiver.balance().add(amount));

        this.accountManager.save(payer);
        this.accountManager.save(receiver);
    }

    public void setBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), amount);
        this.accountManager.save(account);
    }

    public void addBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().add(amount));
        this.accountManager.save(account);
    }

    public void removeBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().subtract(amount));
        this.accountManager.save(account);
    }

    public void resetBalance(Account account) {
        this.setBalance(account, BigDecimal.ZERO);
    }
}