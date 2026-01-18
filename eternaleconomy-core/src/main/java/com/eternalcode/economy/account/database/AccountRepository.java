package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AccountRepository {

    CompletableFuture<Void> save(Account account);

    CompletableFuture<Void> delete(Account account);

    CompletableFuture<Collection<Account>> getAllAccounts();

    CompletableFuture<List<Account>> getTopAccounts(int limit, int offset);

    CompletableFuture<Integer> getPosition(Account target);

    CompletableFuture<Long> countAccounts();
}
