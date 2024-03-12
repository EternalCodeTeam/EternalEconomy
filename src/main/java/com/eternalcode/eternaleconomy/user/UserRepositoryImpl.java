package com.eternalcode.eternaleconomy.user;

import com.eternalcode.eternaleconomy.database.AbstractDatabaseService;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.sql.DataSource;

public class UserRepositoryImpl extends AbstractDatabaseService implements UserRepository {

    private static final String INIT_TABLE_QUERY =
        "CREATE TABLE IF NOT EXISTS eternal_economy_users (uniqueId UUID PRIMARY KEY, name VARCHAR(255), balance NUMERIC(19,2));";
    private static final String SAVE_USER_QUERY =
        "INSERT INTO eternal_economy_users (uniqueId, name, balance) VALUES (?, ?, ?);";
    private static final String REMOVE_USER_QUERY = "DELETE FROM eternal_economy_users WHERE uniqueId = ?;";
    private static final String LOAD_USERS_QUERY = "SELECT * FROM eternal_economy_users;";

    public UserRepositoryImpl(DataSource dataSource) {
        super(dataSource);

        this.initTable();
    }

    private void initTable() {
        this.execute(INIT_TABLE_QUERY, preparedStatement -> {
            try {
                preparedStatement.execute();
            }
            catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        this.querySync(LOAD_USERS_QUERY, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));
                    String lastName = resultSet.getString("name");
                    BigDecimal balance = resultSet.getBigDecimal("balance");

                    users.add(new User(uniqueId, lastName, balance));
                }
                return null;
            }
            catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });

        return users;
    }

    @Override
    public CompletableFuture<Void> saveUser(User user) {
        return this.execute(SAVE_USER_QUERY, preparedStatement -> {
            try {
                preparedStatement.setString(1, user.getUniqueId().toString());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setBigDecimal(3, user.getBalance());

                preparedStatement.execute();
            }
            catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public CompletableFuture<Void> removeUser(User user) {
        return this.execute(REMOVE_USER_QUERY, preparedStatement -> {
            try {
                preparedStatement.setString(1, user.getUniqueId().toString());
                preparedStatement.execute();
            }
            catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
