package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LeaderboardService {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardService.class.getName());

    private final AccountRepository accountRepository;
    private final PluginConfig pluginConfig;

    public LeaderboardService(AccountRepository accountRepository, PluginConfig pluginConfig) {
        this.accountRepository = accountRepository;
        this.pluginConfig = pluginConfig;
    }

    public CompletableFuture<Collection<Account>> getLeaderboard() {
        return this.accountRepository.getAllAccounts().thenApply(accounts -> {
            Map<BigDecimal, List<Account>> grouped = accounts.stream()
                .sorted(Comparator.comparing(Account::balance).reversed())
                .collect(Collectors.groupingBy(
                    Account::balance,
                    () -> new TreeMap<>(Comparator.reverseOrder()),
                    Collectors.toList()
                ));

            return grouped.values().stream()
                .flatMap(List::stream)
                .limit(this.pluginConfig.leaderboardSize)
                .collect(Collectors.toList());
        });
    }

    public CompletableFuture<LeaderboardPosition> getLeaderboardPosition(Account targetAccount) {
        return this.accountRepository.getAllAccounts().thenApply(accounts -> {
            Map<BigDecimal, List<Account>> grouped = accounts.stream()
                .sorted(Comparator.comparing(Account::balance).reversed())
                .collect(Collectors.groupingBy(
                    Account::balance,
                    () -> new TreeMap<>(Comparator.reverseOrder()),
                    Collectors.toList()
                ));

            BigDecimal targetBalance = targetAccount.balance();
            int cumulative = 0;

            for (Map.Entry<BigDecimal, List<Account>> entry : grouped.entrySet()) {
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
        });
    }
}
