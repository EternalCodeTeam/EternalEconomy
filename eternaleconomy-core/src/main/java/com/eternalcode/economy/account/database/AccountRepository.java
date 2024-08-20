package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import java.util.concurrent.CompletableFuture;

public interface AccountRepository {

    CompletableFuture<Void> save(Account account);

    CompletableFuture<Void> delete(Account account);
}
