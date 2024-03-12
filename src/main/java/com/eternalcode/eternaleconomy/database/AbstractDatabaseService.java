package com.eternalcode.eternaleconomy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.sql.DataSource;

public abstract class AbstractDatabaseService {

    protected final DataSource dataSource;

    protected AbstractDatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected CompletableFuture<Void> execute(String query, Consumer<PreparedStatement> consumer) {
        return this.queryAsync(query, preparedStatement -> {
            consumer.accept(preparedStatement);
            return null;
        });
    }

    protected <T> CompletableFuture<T> queryAsync(String query, Function<PreparedStatement, T> function) {
        return CompletableFuture.supplyAsync(() -> {
            try (
                Connection connection = this.dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
            ) {
                return function.apply(statement);
            }
            catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }).orTimeout(5, TimeUnit.SECONDS);
    }

    protected <T> T querySync(String query, Function<PreparedStatement, T> function) {
        try (
            Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            return function.apply(statement);
        }
        catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
