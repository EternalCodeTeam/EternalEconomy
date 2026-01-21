package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardServiceImpl implements LeaderboardService {

    private static final int SNAPSHOT_SIZE = 10_000;

    private final AccountRepository repository;
    private final AtomicReference<LeaderboardSnapshot> snapshotRef = new AtomicReference<>();

    public LeaderboardServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<LeaderboardPage> getLeaderboardPage(int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;

        if (startIndex < SNAPSHOT_SIZE) {
            return getPageFromSnapshot(page, pageSize, startIndex);
        }

        return getPageFromDatabaseKeyset(page, pageSize, startIndex);
    }

    @Override
    public CompletableFuture<LeaderboardEntry> getLeaderboardPosition(Account target) {
        LeaderboardSnapshot snapshot = snapshotRef.get();

        if (snapshot != null) {
            Integer position = snapshot.getPosition(target.uuid());
            if (position != null) {
                return CompletableFuture.completedFuture(new LeaderboardEntry(target, position));
            }
        }

        return repository.getPosition(target).thenApply(position -> new LeaderboardEntry(target, position));
    }

    public void invalidateCache() {
        this.snapshotRef.set(null);
    }

    public CompletableFuture<Void> refreshSnapshot() {
        return repository.getTopAccounts(SNAPSHOT_SIZE, 0).thenCombine(
            repository.countAccounts(), (accounts, totalCount) -> {
                LeaderboardSnapshot newSnapshot = new LeaderboardSnapshot(accounts, totalCount);
                snapshotRef.set(newSnapshot);
                return null;
            });
    }

    private CompletableFuture<LeaderboardSnapshot> getOrRefreshSnapshot() {
        LeaderboardSnapshot current = snapshotRef.get();
        if (current != null) {
            return CompletableFuture.completedFuture(current);
        }

        return repository.getTopAccounts(SNAPSHOT_SIZE, 0).thenCombine(
            repository.countAccounts(), (accounts, totalCount) -> {
                LeaderboardSnapshot newSnapshot = new LeaderboardSnapshot(accounts, totalCount);
                snapshotRef.set(newSnapshot);
                return newSnapshot;
            });
    }

    private CompletableFuture<LeaderboardPage> getPageFromSnapshot(int page, int pageSize, int startIndex) {
        return getOrRefreshSnapshot().thenApply(snapshot -> {
            int endIndex = Math.min(startIndex + pageSize, snapshot.size());
            List<Account> accounts = snapshot.accounts().subList(startIndex, endIndex);

            return createPage(accounts, page, pageSize, startIndex, snapshot.totalCount());
        });
    }

    private CompletableFuture<LeaderboardPage> getPageFromDatabaseKeyset(int page, int pageSize, int startIndex) {
        return getOrRefreshSnapshot().thenCompose(snapshot -> {
            int offset = startIndex - SNAPSHOT_SIZE;
            return repository.getTopAccounts(pageSize, SNAPSHOT_SIZE + offset)
                .thenApply(accounts -> createPage(accounts, page, pageSize, startIndex, snapshot.totalCount()));
        });
    }

    private LeaderboardPage createPage(
        List<Account> accounts, int page, int pageSize, int startIndex,
        long totalCount) {
        List<LeaderboardEntry> entries = new ArrayList<>(accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            entries.add(new LeaderboardEntry(accounts.get(i), startIndex + i + 1));
        }

        int maxPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));
        int nextPage = page + 1 < maxPages ? page + 1 : -1;

        return new LeaderboardPage(entries, page, maxPages, nextPage);
    }
}
