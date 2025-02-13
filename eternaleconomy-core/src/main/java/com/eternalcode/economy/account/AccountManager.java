package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public class AccountManager {

    private final Map<UUID, Account> accountByUniqueId = new HashMap<>();
    private final Map<String, Account> accountByName = new HashMap<>();
    private final TreeMap<String, Account> accountIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final NavigableMap<BigDecimal, Set<Account>> accountsByBalance = new TreeMap<>(Comparator.reverseOrder());

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
                accountManager.accountIndex.put(account.name(), account);
                accountManager.accountsByBalance.computeIfAbsent(account.balance(), k -> new HashSet<>()).add(account);
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
        this.accountsByBalance.computeIfAbsent(account.balance(), k -> new HashSet<>()).add(account);

        return account;
    }

    public Account create(Account account) {
        if (this.accountByUniqueId.containsKey(account.uuid())) {
            throw new IllegalArgumentException("Account already exists: " + account.uuid());
        }

        this.accountByUniqueId.put(account.uuid(), account);
        this.accountByName.put(account.name(), account);
        this.accountIndex.put(account.name(), account);
        this.accountsByBalance.computeIfAbsent(account.balance(), k -> new HashSet<>()).add(account);

        return account;
    }

    public void save(Account account) {
        Account previous = this.accountByUniqueId.put(account.uuid(), account);
        if (previous != null) {
            Set<Account> accountsWithPreviousBalance = this.accountsByBalance.get(previous.balance());
            if (accountsWithPreviousBalance != null) {
                accountsWithPreviousBalance.remove(previous);
                if (accountsWithPreviousBalance.isEmpty()) {
                    this.accountsByBalance.remove(previous.balance());
                }
            }
        }

        this.accountByName.put(account.name(), account);
        this.accountIndex.put(account.name(), account);
        this.accountsByBalance.computeIfAbsent(account.balance(), k -> new HashSet<>()).add(account);
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

    public NavigableMap<BigDecimal, Set<Account>> getAccountsByBalance() {
        return Collections.unmodifiableNavigableMap(this.accountsByBalance);
    }
}
