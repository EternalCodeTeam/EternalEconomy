package com.eternalcode.economy.database;

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
    private HikariDataSource dataSource;
    private ConnectionSource connectionSource;

    public DatabaseManager(Logger logger, File dataFolder, DatabaseSettings databaseSettings) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.databaseSettings = databaseSettings;
    }

    public void connect() throws SQLException {
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
        switch (driverType) {
            case MY_SQL -> {
                this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                this.dataSource.setJdbcUrl("jdbc:mysql://" + settings.getHostname() + ":" + settings.getPort() + "/"
                        + settings.getDatabase());
            }

            case MARIA_DB -> {
                this.dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
                this.dataSource.setJdbcUrl("jdbc:mariadb://" + settings.getHostname() + ":" + settings.getPort() +
                        "/" + settings.getDatabase());
            }

            case H2 -> {
                this.dataSource.setDriverClassName("org.h2.Driver");
                this.dataSource.setJdbcUrl("jdbc:h2:./" + this.dataFolder + "/database");
            }

            case SQLITE -> {
                this.dataSource.setDriverClassName("org.sqlite.JDBC");
                this.dataSource.setJdbcUrl("jdbc:sqlite:" + this.dataFolder + "/database.db");
            }

            case POSTGRE_SQL -> {
                this.dataSource.setDriverClassName("org.postgresql.Driver");
                this.dataSource.setJdbcUrl("jdbc:postgresql://" + settings.getHostname() + ":" + settings.getPort() +
                        "/");
            }

            default -> throw new SQLException("SQL type '" + driverType + "' not found");
        }

        this.connectionSource = new DataSourceConnectionSource(this.dataSource, this.dataSource.getJdbcUrl());
        this.logger.info("Loaded database " + driverType + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }

    public void close() {
        try {
            this.dataSource.close();
            this.connectionSource.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
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
