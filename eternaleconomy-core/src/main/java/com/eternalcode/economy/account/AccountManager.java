package com.eternalcode.economy.account;

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
        this.accountRepository.save(account);

        return account;
    }

    public Collection<Account> accounts() {
        return this.accountByUniqueId.values();
    }
}
