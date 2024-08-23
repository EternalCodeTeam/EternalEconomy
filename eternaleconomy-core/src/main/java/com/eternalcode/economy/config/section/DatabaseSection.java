package com.eternalcode.economy.config.section;

import com.eternalcode.economy.database.DatabaseDriverType;
import com.eternalcode.economy.database.DatabaseSettings;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class DatabaseSection extends OkaeriConfig implements DatabaseSettings {

    @Comment({"Type of the database driver (e.g., SQLITE, H2, MY_SQL, MARIA_DB, POSTGRE_SQL).", "Determines the "
            + "database type "
            + "to be used."})
    private DatabaseDriverType driverType = DatabaseDriverType.SQLITE;

    @Comment({"Hostname of the database server.", "For local databases, this is usually 'localhost'."})
    private String hostname = "localhost";

    @Comment({"Port number of the database server. Common ports:",
             " - MySQL: 3306",
             " - PostgreSQL: 5432",
             " - H2: Not applicable (file-based)",
             " - SQLite: Not applicable (file-based)"})
    private int port = 3306;

    @Comment("Name of the database to connect to. This is the name of the specific database instance.")
    private String database = "eternal_economy";

    @Comment("Username for the database connection. This is the user account used to authenticate with the database.")
    private String username = "root";

    @Comment("Password for the database connection. This is the password for the specified user account.")
    private String password = "password";

    @Comment("Enable SSL for the database connection. Set to true to use SSL/TLS for secure connections.")
    private boolean ssl = false;

    @Comment("Connection pool size. This determines the maximum number of connections in the pool.")
    private int poolSize = 16;

    @Comment("Connection timeout in milliseconds. This is the maximum time to wait for a connection from the pool.")
    private int timeout = 30000;

    @Override
    public DatabaseDriverType getDriverType() {
        return this.driverType;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isSSL() {
        return this.ssl;
    }

    @Override
    public int poolSize() {
        return this.poolSize;
    }

    @Override
    public int timeout() {
        return this.timeout;
    }
}