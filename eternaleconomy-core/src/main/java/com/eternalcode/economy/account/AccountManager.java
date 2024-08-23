package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager {

    private final Map<UUID, Account> accountByUniqueId = new HashMap<>();
    private final Map<String, Account> accountByName = new HashMap<>();

    private final AccountRepository accountRepository;

    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(UUID uuid) {
        return this.accountByUniqueId.get(uuid);
    }

    public void loadAccounts() {
        this.accountRepository.getAllAccounts().thenAccept(accounts -> {
            accounts.forEach(account -> {
                this.accountByUniqueId.put(account.uuid(), account);
                this.accountByName.put(account.name(), account);
            });
        });
    }

    public Account getAccount(String name) {
        return this.accountByName.get(name);
    }

    public Account getOrCreate(UUID uuid, String name) {
        Account byUniqueId = this.accountByUniqueId.get(uuid);

        if (byUniqueId != null) {
            return byUniqueId;
        }

        Account byName = this.accountByName.get(name);

        if (byName != null) {
            return byName;
        }

        return this.create(uuid, name);
    }

    public Account create(UUID uuid, String name) {
        if (this.accountByUniqueId.containsKey(uuid) || this.accountByName.containsKey(name)) {
            throw new IllegalArgumentException("Account with UUID " + uuid + " already exists");
        }

        Account account = new Account(uuid, name, BigDecimal.ZERO);
        this.accountByUniqueId.put(uuid, account);
        this.accountByName.put(name, account);

        return account;
    }

    public void save(Account account) {
        this.accountByUniqueId.put(account.uuid(), account);
        this.accountByName.put(account.name(), account);
        this.accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return this.accountByUniqueId.values();
    }
}