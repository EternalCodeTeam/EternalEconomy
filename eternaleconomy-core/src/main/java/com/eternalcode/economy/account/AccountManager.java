package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.leaderboard.LeaderboardServiceImpl;
import com.eternalcode.economy.leaderboard.LeaderboardService;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {

    private final Map<UUID, Account> accountByUniqueId = new ConcurrentHashMap<>();
    private final Map<String, Account> accountByName = new ConcurrentHashMap<>();

    private final AccountRepository accountRepository;
    private final LeaderboardServiceImpl leaderboardService;

    public AccountManager(AccountRepository accountRepository, PluginConfig config) {
        this.accountRepository = accountRepository;
        this.leaderboardService = new LeaderboardServiceImpl(accountRepository);
    }

    public static AccountManager create(AccountRepository accountRepository, PluginConfig config) {
        AccountManager accountManager = new AccountManager(accountRepository, config);

        accountRepository.getAllAccounts()
                .thenAccept(accounts -> {
                    for (Account account : accounts) {
                        accountManager.save(account);
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("Failed to load accounts: " + throwable.getMessage());
                    throwable.printStackTrace();
                    return null;
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
        Account existing = this.accountByUniqueId.get(uuid);

        if (existing != null) {
            if (!existing.name().equals(name)) {
                Account updated = new Account(uuid, name, existing.balance());
                this.save(updated);
                return updated;
            }
            return existing;
        }

        Account newAccount = new Account(uuid, name, BigDecimal.ZERO);
        this.save(newAccount);
        return newAccount;
    }

    public void save(Account account) {
        Account old = this.accountByUniqueId.get(account.uuid());
        if (old != null && !old.name().equals(account.name())) {
            this.accountByName.remove(old.name());
        }

        this.accountByUniqueId.put(account.uuid(), account);
        this.accountByName.put(account.name(), account);

        this.leaderboardService.invalidateCache();

        this.accountRepository.save(account).exceptionally(throwable -> {
            System.err.println("Failed to save account " + account.uuid() + ": " + throwable.getMessage());
            return null;
        });
    }

    public Collection<Account> getAccountStartingWith(String prefix) {
        return this.accountByName.values().stream()
                .filter(account -> account.name().toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(this.accountByUniqueId.values());
    }

    public LeaderboardService getLeaderboardService() {
        return this.leaderboardService;
    }

}
