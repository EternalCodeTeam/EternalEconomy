package com.eternalcode.economy.account.database;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.database.AbstractRepositoryOrmLite;
import com.eternalcode.economy.database.DatabaseException;
import com.eternalcode.economy.database.DatabaseManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountRepositoryImpl extends AbstractRepositoryOrmLite implements AccountRepository {

    public AccountRepositoryImpl(
            DatabaseManager databaseManager,
            Scheduler scheduler) {
        super(databaseManager, scheduler);

        try {
            TableUtils.createTableIfNotExists(databaseManager.connectionSource(), AccountWrapper.class);
        } catch (SQLException exception) {
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
    public CompletableFuture<Collection<Account>> getAllAccounts() {
        return this.selectAll(AccountWrapper.class)
                .thenApply(accountWrappers -> accountWrappers.stream()
                        .map(accountWrapper -> accountWrapper.toAccount())
                        .toList());
    }

    @Override
    public CompletableFuture<List<Account>> getTopAccounts(int limit, int offset) {
        return this.<AccountWrapper, UUID, List<Account>>action(AccountWrapper.class, dao -> {
            QueryBuilder<AccountWrapper, UUID> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy("balance", false); // false for DESC
            queryBuilder.orderBy("uuid", true); // true for ASC

            if (limit > 0) {
                queryBuilder.limit((long) limit);
            }

            if (offset > 0) {
                queryBuilder.offset((long) offset);
            }

            return queryBuilder.query().stream()
                    .map(AccountWrapper::toAccount)
                    .toList();
        });
    }

    @Override
    public CompletableFuture<Integer> getPosition(Account target) {
        return this.<AccountWrapper, UUID, Integer>action(AccountWrapper.class, dao -> {
            QueryBuilder<AccountWrapper, UUID> qb = dao.queryBuilder();
            Where<AccountWrapper, UUID> where = qb.where();

            // (balance > target) OR (balance == target AND uuid < target)
            where.gt("balance", target.balance());
            where.or();
            where.eq("balance", target.balance());
            where.and();
            where.lt("uuid", target.uuid());

            return (int) qb.countOf() + 1;
        });
    }

    @Override
    public CompletableFuture<Long> countAccounts() {
        return this.<AccountWrapper, UUID, Long>action(AccountWrapper.class, dao -> dao.countOf());
    }
}
