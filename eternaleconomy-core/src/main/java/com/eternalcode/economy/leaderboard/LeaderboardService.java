package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LeaderboardService {

    private final AccountRepository accountRepository;
    private final PluginConfig pluginConfig;
    private final Map<BigDecimal, List<Account>> topAccounts = new TreeMap<>(Comparator.reverseOrder());
    private final Object lock = new Object();

    private Instant lastUpdated;

    public LeaderboardService(AccountRepository accountRepository, PluginConfig pluginConfig) {
        this.accountRepository = accountRepository;
        this.pluginConfig = pluginConfig;
        this.lastUpdated = Instant.now();
        this.initializeLeaderboard();
    }

    private void initializeLeaderboard() {
        this.accountRepository.getAllAccounts().thenAccept(this::processAccounts);
    }

    private void processAccounts(Collection<Account> accounts) {
        synchronized (this.lock) {
            Map<BigDecimal, List<Account>> grouped = accounts.stream()
                .sorted(Comparator.comparing(Account::balance).reversed())
                .collect(Collectors.groupingBy(
                    Account::balance,
                    () -> new TreeMap<>(Comparator.reverseOrder()),
                    Collectors.toList()
                ));

            this.topAccounts.clear();
            this.topAccounts.putAll(grouped);
            this.lastUpdated = Instant.now();
        }
    }

    public Collection<Account> getLeaderboard() {
        synchronized (this.lock) {
            return this.topAccounts.values().stream()
                .flatMap(List::stream)
                .limit(this.pluginConfig.leaderboardSize)
                .collect(Collectors.toList());
        }
    }

    public void updateLeaderboard() {
        this.accountRepository.getAllAccounts().thenAccept(this::processAccounts);
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public CompletableFuture<LeaderboardPosition> getLeaderboardPosition(Account targetAccount) {
        return CompletableFuture.supplyAsync(() -> {
            synchronized (this.lock) {
                BigDecimal targetBalance = targetAccount.balance();
                int cumulative = 0;

                for (Map.Entry<BigDecimal, List<Account>> entry : this.topAccounts.entrySet()) {
                    BigDecimal currentBalance = entry.getKey();
                    List<Account> group = entry.getValue();

                    int comparison = currentBalance.compareTo(targetBalance);

                    if (comparison > 0) {
                        cumulative += group.size();
                        continue;
                    }

                    if (comparison == 0) {
                        for (Account account : group) {
                            if (account.equals(targetAccount)) {
                                return new LeaderboardPosition(targetAccount, cumulative + 1);
                            }
                        }
                        return new LeaderboardPosition(targetAccount, -1);
                    }

                    break;
                }

                return new LeaderboardPosition(targetAccount, -1);
            }
        });
    }
}
