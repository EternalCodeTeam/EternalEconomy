package com.eternalcode.economy.account;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AccountRepository {

    CompletableFuture<Void> save(Account account);

    CompletableFuture<Void> delete(Account account);

    CompletableFuture<Account> save(UUID uuid);

    CompletableFuture<Account> delete(UUID uuid);
}
