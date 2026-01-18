package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountRepositoryInMemory implements AccountRepository {

    private final HashMap<UUID, Account> accounts = new HashMap<>();

    @Override
    public CompletableFuture<Void> save(Account account) {
        accounts.put(account.uuid(), account);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> delete(Account account) {
        accounts.remove(account.uuid());
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllAccounts() {
        return CompletableFuture.completedFuture(Collections.unmodifiableCollection(accounts.values()));
    }

    @Override
    public CompletableFuture<List<Account>> getTopAccounts(int limit, int offset) {
        List<Account> sorted = accounts.values().stream()
                .sorted(Comparator.comparing(Account::balance).reversed()
                        .thenComparing(Account::uuid))
                .skip(offset)
                .limit(limit)
                .toList();

        return CompletableFuture.completedFuture(sorted);
    }

    @Override
    public CompletableFuture<Integer> getPosition(Account target) {
        long position = accounts.values().stream()
                .sorted(Comparator.comparing(Account::balance).reversed()
                        .thenComparing(Account::uuid))
                .takeWhile(account -> !account.uuid().equals(target.uuid()))
                .count() + 1;

        return CompletableFuture.completedFuture((int) position);
    }

    @Override
    public CompletableFuture<Long> countAccounts() {
        return CompletableFuture.completedFuture((long) accounts.size());
    }
}
