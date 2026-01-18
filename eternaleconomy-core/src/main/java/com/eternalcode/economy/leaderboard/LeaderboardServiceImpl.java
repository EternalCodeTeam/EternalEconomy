package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LeaderboardServiceImpl implements LeaderboardService {

    private static final String CACHE_KEY = "top_leaderboard";
    private static final int TOP_CACHE_SIZE = 100;

    private final AccountRepository repository;
    private final Cache<String, List<Account>> topCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(30))
            .build();

    public LeaderboardServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<LeaderboardPage> getLeaderboardPage(int page, int pageSize) {
        int startIndex = page * pageSize;

        if (startIndex < TOP_CACHE_SIZE) {
            return getPageFromCache(page, pageSize);
        }

        return getPageFromDatabase(page, pageSize);
    }

    @Override
    public CompletableFuture<LeaderboardEntry> getLeaderboardPosition(Account target) {
        return getOrRefreshTopCache().thenCompose(topAccounts -> {
            int position = topAccounts.indexOf(target);

            if (position != -1) {
                return CompletableFuture.completedFuture(new LeaderboardEntry(target, position + 1));
            }

            return calculatePositionFromAll(target);
        });
    }

    public void invalidateCache() {
        this.topCache.invalidate(CACHE_KEY);
    }

    private CompletableFuture<LeaderboardPage> getPageFromCache(int page, int pageSize) {
        return getOrRefreshTopCache().thenApply(topAccounts -> {
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, topAccounts.size());

            List<LeaderboardEntry> entries = new ArrayList<>();
            for (int i = startIndex; i < endIndex; i++) {
                entries.add(new LeaderboardEntry(topAccounts.get(i), i + 1));
            }

            int totalEntries = topAccounts.size();
            int maxPages = Math.max(1, (int) Math.ceil((double) totalEntries / pageSize));
            int nextPage = page + 1 < maxPages ? page + 1 : -1;

            return new LeaderboardPage(entries, page, maxPages, nextPage);
        });
    }

    private CompletableFuture<LeaderboardPage> getPageFromDatabase(int page, int pageSize) {
        return repository.getTopAccounts(pageSize, page * pageSize)
                .thenCompose(accounts -> repository.countAccounts().thenApply(totalEntries -> {
                    List<LeaderboardEntry> entries = new ArrayList<>();
                    int startPosition = page * pageSize;

                    for (int i = 0; i < accounts.size(); i++) {
                        entries.add(new LeaderboardEntry(accounts.get(i), startPosition + i + 1));
                    }

                    int maxPages = Math.max(1, (int) Math.ceil((double) totalEntries / pageSize));
                    int nextPage = page + 1 < maxPages ? page + 1 : -1;

                    return new LeaderboardPage(entries, page, maxPages, nextPage);
                }));
    }

    private CompletableFuture<List<Account>> getOrRefreshTopCache() {
        List<Account> cached = topCache.getIfPresent(CACHE_KEY);

        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        return repository.getTopAccounts(TOP_CACHE_SIZE, 0)
                .thenApply(topAccounts -> {
                    topCache.put(CACHE_KEY, topAccounts);
                    return topAccounts;
                });
    }

    private CompletableFuture<LeaderboardEntry> calculatePositionFromAll(Account target) {
        return repository.getPosition(target)
                .thenApply(position -> new LeaderboardEntry(target, position));
    }
}
