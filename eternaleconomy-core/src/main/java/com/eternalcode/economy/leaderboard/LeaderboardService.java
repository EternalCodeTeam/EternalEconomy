package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.google.common.collect.TreeMultimap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public class LeaderboardService {

    private final AccountManager accountManager;

    public LeaderboardService(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public CompletableFuture<LeaderboardPage> getLeaderboardPage(int page, int entriesPerPage) {
        return CompletableFuture.supplyAsync(() -> {
            TreeMultimap<BigDecimal, Account> balanceTiers = accountManager.getAccountsByBalance();
            List<LeaderboardEntry> entries = new ArrayList<>(entriesPerPage);

            int currentPage = Math.max(1, page);
            int toSkip = (currentPage - 1) * entriesPerPage;
            int count = 0;
            int globalPosition = 1;

            pageLoop:
            for (Collection<Account> accounts : balanceTiers.asMap().values()) {
                for (Account account : accounts) {
                    if (count >= toSkip && entries.size() < entriesPerPage) {
                        entries.add(new LeaderboardEntry(account, globalPosition));
                    }

                    if (entries.size() >= entriesPerPage) {
                        break pageLoop;
                    }

                    count++;
                    globalPosition++;
                }
            }

            int totalEntries = accountManager.getAccountsCount();
            int maxPages = (int) Math.ceil((double) totalEntries / entriesPerPage);
            int nextPage = currentPage < maxPages ? currentPage + 1 : -1;

            return new LeaderboardPage(entries, currentPage, maxPages, nextPage);
        });
    }

    public CompletableFuture<LeaderboardEntry> getLeaderboardPosition(Account target) {
        return CompletableFuture.supplyAsync(() -> {
            TreeSet<Account> accounts = accountManager.getAccountsByBalanceSet();

            if (!accounts.contains(target)) {
                return new LeaderboardEntry(target, -1);
            }

            int position = accounts.headSet(target, false).size() + 1;
            return new LeaderboardEntry(target, position);
        });
    }
}
