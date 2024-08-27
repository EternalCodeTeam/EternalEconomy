package com.eternalcode.economy.account.database;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.database.AbstractRepositoryOrmLite;
import com.eternalcode.economy.database.DatabaseException;
import com.eternalcode.economy.database.DatabaseManager;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AccountRepositoryImpl extends AbstractRepositoryOrmLite implements AccountRepository {

    public AccountRepositoryImpl(
            DatabaseManager databaseManager,
            Scheduler scheduler
    ) {
        super(databaseManager, scheduler);

        try {
            TableUtils.createTableIfNotExists(databaseManager.connectionSource(), AccountWrapper.class);
        }
        catch (SQLException exception) {
            throw new DatabaseException("Failed to create table for AccountWrapper.", exception);
        }
    }

    @Override
    public CompletableFuture<Void> save(Account account) {
        return this.save(AccountWrapper.class, AccountWrapper.fromAccount(account)).thenApply(status -> null);
    }

    @Override
    public CompletableFuture<Void> delete(Account account) {
        return this.delete(AccountWrapper.class, AccountWrapper.fromAccount(account)).thenApply(status -> null);
    }

    @Override
    public CompletableFuture<List<Account>> getAllAccounts() {
        return this.selectAll(AccountWrapper.class)
                .thenApply(accountWrappers -> accountWrappers.stream()
                        .map(AccountWrapper::toAccount)
                        .toList());
    }
}

