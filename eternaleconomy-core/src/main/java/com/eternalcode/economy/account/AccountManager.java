package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepository;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class AccountManager {

    private final Map<UUID, Account> accountByUniqueId = new HashMap<>();
    private final Map<String, Account> accountByName = new HashMap<>();
    private final TreeMap<String, Account> accountIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final AccountRepository accountRepository;

    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static AccountManager create(AccountRepository accountRepository) {
        AccountManager accountManager = new AccountManager(accountRepository);

        accountRepository.getAllAccounts().thenAccept(accounts -> {
            for (Account account : accounts) {
                accountManager.accountByUniqueId.put(account.uuid(), account);
                accountManager.accountByName.put(account.name(), account);
            }
        });

        return accountManager;
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
        this.accountIndex.put(name, account);

        return account;
    }

    public void save(Account account) {
        this.accountByUniqueId.put(account.uuid(), account);
        this.accountByName.put(account.name(), account);
        this.accountRepository.save(account);
    }

    public Collection<Account> getAccountStartingWith(String prefix) {
        return Collections.unmodifiableCollection(
            this.accountIndex.subMap(prefix, true, prefix + Character.MAX_VALUE, true).values()
        );
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(this.accountByUniqueId.values());
    }

    /**
     * Gets top accounts sorted by balance.
     * Using PriorityQueue instead of standard sorting because:
     * 1. PriorityQueue maintains only 'limit' elements in memory, reducing memory usage
     * 2. More efficient when we need only top X elements from large dataset
     */
    public Collection<Account> getSortedTopAccounts(int limit) {
        return this.accountByUniqueId.values().stream()
            .collect(Collectors.toCollection(() ->
                new PriorityQueue<>((a1, a2) -> a2.balance().compareTo(a1.balance()))
            ))
            .stream()
            .limit(limit)
            .collect(Collectors.toList());
    }

    public AccountPosition getAccountPosition(Account targetAccount) {
        int position = (int) this.accountByUniqueId.values().stream()
            .filter(account -> account.balance().compareTo(targetAccount.balance()) > 0)
            .count() + 1;

        return new AccountPosition(targetAccount, position);
    }

}
