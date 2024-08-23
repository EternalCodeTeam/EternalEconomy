package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.database.DatabaseDriverType;
import com.eternalcode.economy.database.DatabaseSettings;
import com.eternalcode.economy.format.EconomyUnit;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.Arrays;
import java.util.List;

public class PluginConfig extends OkaeriConfig {

    @Comment("Units settings")
    public Units units = new Units();

    @Comment("Database settings")
    public Database database = new Database();

    public static class Database extends OkaeriConfig implements DatabaseSettings {
        @Comment("Database driver type, available: SQLITE, H2, MY_SQL, POSTGRE_SQL, MARIA_DB")
        private final DatabaseDriverType driverType = DatabaseDriverType.SQLITE;

        @Comment("Database hostname")
        private final String hostname = "localhost";

        @Comment({
                "Database port",
                "Default: 3306",
                "For SQLite and H2 set to anyone you want, because flat databases don't uses this",
                "For PostgreSQL set to 5432",
                "For MariaDB and MySQL set to 3306"
        })
        private final int port = 3306;

        @Comment("Database name")
        private final String database = "eternal_economy";

        @Comment("Database username")
        private final String username = "root";

        @Comment("Database password")
        private final String password = "password";

        @Comment("Use SSL connection")
        private final boolean ssl = false;

        @Comment("Connection pool size")
        private final int poolSize = 16;

        @Comment("Connection timeout")
        private final int timeout = 30000;

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

    public static class Units extends OkaeriConfig {
        public List<EconomyUnit> units = Arrays.asList(
                new EconomyUnit(1_000, 'k'),
                new EconomyUnit(1_000_000, 'm'),
                new EconomyUnit(1_000_000_000, 'g'),
                new EconomyUnit(1_000_000_000_000L, 't'),
                new EconomyUnit(1_000_000_000_000_000L, 'p'),
                new EconomyUnit(1_000_000_000_000_000_000L, 'e')
        );
    }
}
