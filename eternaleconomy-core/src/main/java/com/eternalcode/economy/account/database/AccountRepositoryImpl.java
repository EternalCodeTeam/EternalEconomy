package com.eternalcode.economy.account.database;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.database.AbstractRepositoryOrmLite;
import com.eternalcode.economy.database.DatabaseException;
import com.eternalcode.economy.database.DatabaseManager;
import com.j256.ormlite.dao.Dao;
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
            TableUtils.createTableIfNotExists(databaseManager.connectionSource(), AccountTable.class);
            this.createIndexes();
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to create table for AccountTable.", exception);
        }
    }

    @Override
    public CompletableFuture<Void> save(Account account) {
        return this.save(AccountTable.class, AccountTable.fromAccount(account)).thenApply(status -> null);
    }

    @Override
    public CompletableFuture<Void> delete(Account account) {
        return this.delete(AccountTable.class, AccountTable.fromAccount(account)).thenApply(status -> null);
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllAccounts() {
        return this.selectAll(AccountTable.class)
                .thenApply(accountWrappers -> accountWrappers.stream()
                        .map(AccountTable::toAccount)
                        .toList());
    }

    @Override
    public CompletableFuture<List<Account>> getTopAccounts(int limit, int offset) {
        return this.<AccountTable, UUID, List<Account>>action(
                AccountTable.class, dao -> {
                    QueryBuilder<AccountTable, UUID> queryBuilder = dao.queryBuilder();
                    queryBuilder.orderBy(AccountTable.BALANCE, false);
                    queryBuilder.orderBy(AccountTable.UUID, true);

                    if (limit > 0) {
                        queryBuilder.limit((long) limit);
                    }

                    if (offset > 0) {
                        queryBuilder.offset((long) offset);
                    }

                    return queryBuilder.query().stream()
                            .map(AccountTable::toAccount)
                            .toList();
                });
    }

    @Override
    public CompletableFuture<Integer> getPosition(Account target) {
        return this.<AccountTable, UUID, Integer>action(
                AccountTable.class, dao -> {
                    QueryBuilder<AccountTable, UUID> qb = dao.queryBuilder();
                    Where<AccountTable, UUID> where = qb.where();

                    where.gt(AccountTable.BALANCE, target.balance());
                    where.or();
                    where.eq(AccountTable.BALANCE, target.balance());
                    where.and();
                    where.lt(AccountTable.UUID, target.uuid());

                    return (int) qb.countOf() + 1;
                });
    }

    @Override
    public CompletableFuture<Long> countAccounts() {
        return this.<AccountTable, UUID, Long>action(AccountTable.class, Dao::countOf);
    }

    /*
     * ORMLite annotations (@DatabaseField(index = true)) do not support creating
     * composite indexes with specific sort directions (ASC/DESC).
     * For the leaderboard, we need an index on (balance DESC, uuid ASC) to
     * efficiently query the top accounts without sorting the entire table.
     * This optimization significantly improves performance for large datasets.
     * 20.01.2026 - vlucky
     */
    private void createIndexes() {
        this.action(AccountTable.class, dao -> {
            dao.executeRaw(
                    "CREATE INDEX IF NOT EXISTS idx_leaderboard_composite ON eternaleconomy_accounts(balance DESC, uuid ASC)");
            return null;
        });
    }
}
