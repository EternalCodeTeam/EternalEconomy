package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.NavigableMap;
import java.util.concurrent.CompletableFuture;

public class LeaderboardService {

    private final AccountManager accountManager;

    public LeaderboardService(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public CompletableFuture<List<LeaderboardEntry>> getLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        int position = 1;

        NavigableMap<?, Set<Account>> balanceTiers = accountManager.getAccountsByBalance();
        for (Set<Account> accounts : balanceTiers.values()) {
            for (Account account : accounts) {
                entries.add(new LeaderboardEntry(account, position));
            }
            position += accounts.size();
        }
        return CompletableFuture.completedFuture(entries);
    }

    public CompletableFuture<LeaderboardPage> getLeaderboardPage(int page, int entriesPerPage) {
        return getLeaderboard().thenApply(entries -> {
            int totalEntries = entries.size();
            int maxPages = (int) Math.ceil((double) totalEntries / entriesPerPage);
            int currentPage = Math.max(1, Math.min(page, maxPages));
            int startIndex = (currentPage - 1) * entriesPerPage;
            int endIndex = Math.min(startIndex + entriesPerPage, totalEntries);
            List<LeaderboardEntry> pageEntries = entries.subList(startIndex, endIndex);
            int nextPage = currentPage < maxPages ? currentPage + 1 : -1;
            return new LeaderboardPage(pageEntries, currentPage, maxPages, nextPage);
        });
    }

    public CompletableFuture<LeaderboardEntry> getLeaderboardPosition(Account target) {
        int position = 1;
        NavigableMap<?, Set<Account>> balanceTiers = accountManager.getAccountsByBalance();
        for (Set<Account> accounts : balanceTiers.values()) {
            if (accounts.contains(target)) {
                return CompletableFuture.completedFuture(new LeaderboardEntry(target, position));
            }
            position += accounts.size();
        }
        return CompletableFuture.completedFuture(new LeaderboardEntry(target, -1));
    }
}
