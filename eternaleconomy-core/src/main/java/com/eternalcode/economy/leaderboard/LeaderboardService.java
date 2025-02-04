package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPosition;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.config.implementation.PluginConfig;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LeaderboardService {

    private final AccountRepository accountRepository;
    private final PluginConfig pluginConfig;
    private final Map<BigDecimal, List<Account>> topAccounts = new TreeMap<>(Comparator.reverseOrder());
    private Instant lastUpdated;

    public LeaderboardService(AccountRepository accountRepository, PluginConfig pluginConfig) {
        this.accountRepository = accountRepository;
        this.pluginConfig = pluginConfig;
        this.loadTopAccounts();
        this.lastUpdated = Instant.now();
    }

    private void loadTopAccounts() {
        this.accountRepository.getAllAccounts().thenAccept(accounts -> {
            this.topAccounts.clear();
            accounts.stream()
                .collect(Collectors.toCollection(() ->
                    new PriorityQueue<>((a1, a2) -> a2.balance().compareTo(a1.balance()))
                ))
                .stream()
                .limit(this.pluginConfig.leaderboardSize)
                .forEach(account -> {
                    this.topAccounts.computeIfAbsent(account.balance(), (k) -> new ArrayList<>()).add(account);
                });
        });
    }

    public Collection<Account> getTopAccounts() {
        return this.topAccounts.values().stream()
            .flatMap(List::stream)
            .limit(this.pluginConfig.leaderboardSize)
            .toList();
    }

    public void updateTopAccounts() {
        loadTopAccounts();
        this.lastUpdated = Instant.now();
    }

    public Instant lastUpdated() {
        return this.lastUpdated;
    }

    public CompletableFuture<AccountPosition> getAccountPosition(Account targetAccount) {
        return this.accountRepository.getAllAccounts().thenApply(accounts -> {
            int position = (int) accounts.stream()
                .filter(account -> account.balance().compareTo(targetAccount.balance()) > 0)
                .count() + 1;

            return new AccountPosition(targetAccount, position);
        });
    }
}
