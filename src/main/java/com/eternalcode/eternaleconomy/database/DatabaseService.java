package com.eternalcode.eternaleconomy.database;

import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class DatabaseService {

    private final PluginConfiguration configuration;

    public DatabaseService(PluginConfiguration pluginConfiguration) {
        this.configuration = pluginConfiguration;
    }

    public HikariDataSource connect(File dataFolder) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);

        DatabaseType type = configuration.database.type;

        switch (type) {
            case MYSQL -> {
                hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                hikariConfig.setJdbcUrl(
                    "jdbc:mysql://"
                        + configuration.database.host
                        + ":"
                        + configuration.database.port
                        + "/"
                        + configuration.database.database
                        + "?useSSL="
                        + configuration.database.useSSL
                );
                hikariConfig.setUsername(configuration.database.username);
                hikariConfig.setPassword(configuration.database.password);
            }

            case SQLITE -> {
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl("jdbc:sqlite:" + dataFolder + File.separator + "database.db");
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

        return new HikariDataSource(hikariConfig);
    }
}
