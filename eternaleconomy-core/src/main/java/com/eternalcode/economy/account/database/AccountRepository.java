package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AccountRepository {

    CompletableFuture<Void> save(Account account);

    CompletableFuture<Void> delete(Account account);

    CompletableFuture<List<Account>> getAllAccounts();
}
