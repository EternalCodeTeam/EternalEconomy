package com.eternalcode.economy.database;

import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.H2_DRIVER;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.H2_JDBC_URL;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.MARIADB_DRIVER;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.MARIADB_JDBC_URL;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.MYSQL_DRIVER;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.MYSQL_JDBC_URL;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.POSTGRESQL_DRIVER;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.POSTGRESQL_JDBC_URL;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.SQLITE_DRIVER;
import static com.eternalcode.economy.database.DatabaseConnectionDriverConstant.SQLITE_JDBC_URL;

import com.google.common.base.Stopwatch;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseManager {

    private final Logger logger;
    private final File dataFolder;
    private final DatabaseSettings databaseSettings;
    private final Map<Class<?>, Dao<?, ?>> cachedDao = new ConcurrentHashMap<>();
    private final boolean useSSL;
    private final boolean requireSSL;
    private HikariDataSource dataSource;
    private ConnectionSource connectionSource;

    public DatabaseManager(Logger logger, File dataFolder, DatabaseSettings databaseSettings) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.databaseSettings = databaseSettings;
        this.useSSL = databaseSettings.isSSL();
        this.requireSSL = databaseSettings.isSSL();
    }

    public void connect() throws DatabaseException {
        Stopwatch stopwatch = Stopwatch.createStarted();

        this.dataSource = new HikariDataSource();

        DatabaseSettings settings = this.databaseSettings;

        this.dataSource.addDataSourceProperty("cachePrepStmts", "true");
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource.addDataSourceProperty("useServerPrepStmts", "true");

        this.dataSource.setMaximumPoolSize(settings.poolSize());
        this.dataSource.setConnectionTimeout(settings.timeout());
        this.dataSource.setUsername(settings.getUsername());
        this.dataSource.setPassword(settings.getPassword());

        DatabaseDriverType driverType = settings.getDriverType();
        try {
            switch (driverType) {
                case MY_SQL -> {
                    this.dataSource.setDriverClassName(MYSQL_DRIVER);
                    this.dataSource.setJdbcUrl(String.format(
                            MYSQL_JDBC_URL,
                            settings.getHostname(), settings.getPort(), settings.getDatabase(), useSSL, requireSSL)
                    );
                }

                case MARIA_DB -> {
                    this.dataSource.setDriverClassName(MARIADB_DRIVER);
                    this.dataSource.setJdbcUrl(String.format(
                            MARIADB_JDBC_URL,
                            settings.getHostname(), settings.getPort(), settings.getDatabase(), useSSL, requireSSL)
                    );
                }

                case H2 -> {
                    this.dataSource.setDriverClassName(H2_DRIVER);
                    this.dataSource.setJdbcUrl(String.format(
                            H2_JDBC_URL,
                            this.dataFolder)
                    );
                }

                case SQLITE -> {
                    this.dataSource.setDriverClassName(SQLITE_DRIVER);
                    this.dataSource.setJdbcUrl(String.format(
                            SQLITE_JDBC_URL,
                            this.dataFolder)
                    );
                }

                case POSTGRE_SQL -> {
                    this.dataSource.setDriverClassName(POSTGRESQL_DRIVER);
                    this.dataSource.setJdbcUrl(String.format(
                            POSTGRESQL_JDBC_URL,
                            settings.getHostname(), settings.getPort(), useSSL)
                    );
                }

                default -> throw new DatabaseException("SQL type '" + driverType + "' not found");
            }

            this.connectionSource = new DataSourceConnectionSource(this.dataSource, this.dataSource.getJdbcUrl());
            this.logger.info(
                    "Loaded database " + driverType + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
        catch (SQLException exception) {
            throw new DatabaseException("Failed to connect to the database", exception);
        }
    }

    public void close() {
        try {
            this.dataSource.close();
            this.connectionSource.close();
        }
        catch (Exception exception) {
            logger.severe("Failed to close database connection: " + exception.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T, ID> Dao<T, ID> getDao(Class<T> type) {
        try {
            Dao<?, ?> dao = this.cachedDao.get(type);

            if (dao == null) {
                dao = DaoManager.createDao(this.connectionSource, type);
                this.cachedDao.put(type, dao);
            }

            return (Dao<T, ID>) dao;
        }
        catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ConnectionSource connectionSource() {
        return this.connectionSource;
    }
}