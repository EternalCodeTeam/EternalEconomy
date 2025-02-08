package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.config.implementation.PluginConfig;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
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
        this.lastUpdated = Instant.now();
        this.loadLeaderboard();
    }

    private void loadLeaderboard() {
        this.accountRepository.getAllAccounts().thenAccept(accounts -> {
            this.topAccounts.clear();
            this.topAccounts.putAll(accounts.stream()
                .sorted(Comparator.comparing(Account::balance).reversed())
                .collect(Collectors.groupingBy(
                    Account::balance,
                    Collectors.toList()
                )));
        });
    }

    public Collection<Account> getLeaderboard() {
        return this.topAccounts.values().stream()
            .flatMap(List::stream)
            .limit(this.pluginConfig.leaderboardSize)
            .toList();
    }

    public void updateLeaderboard() {
        this.loadLeaderboard();
        this.lastUpdated = Instant.now();
    }

    public Instant lastUpdated() {
        return this.lastUpdated;
    }

    public CompletableFuture<LeaderboardPosition> getLeaderboardPosition(Account targetAccount) {
        return this.accountRepository.getAllAccounts().thenApply(accounts -> {
            List<Account> sorted = accounts.stream()
                .sorted(Comparator.comparing(Account::balance).reversed())
                .toList();
            int position = sorted.indexOf(targetAccount) + 1;

            return new LeaderboardPosition(targetAccount, position);
        });
    }
}
