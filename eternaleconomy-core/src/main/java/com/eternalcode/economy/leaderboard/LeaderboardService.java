package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LeaderboardService {

    private final AccountRepository accountRepository;
    private final Object lock = new Object();
    private final TreeMap<BigDecimal, List<Account>> leaderboard = new TreeMap<>(Comparator.reverseOrder());

    public LeaderboardService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletableFuture<Collection<Account>> getLeaderboard() {
        return this.accountRepository.getAllAccounts().thenApply(accounts -> {
            synchronized (lock) {
                leaderboard.clear();
                leaderboard.putAll(accounts.stream()
                    .collect(Collectors.groupingBy(
                        Account::balance,
                        () -> new TreeMap<>(Comparator.reverseOrder()),
                        Collectors.toList()
                    )));
    
                return leaderboard.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            }
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
