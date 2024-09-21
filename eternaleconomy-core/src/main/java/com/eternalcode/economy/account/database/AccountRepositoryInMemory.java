package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
}
