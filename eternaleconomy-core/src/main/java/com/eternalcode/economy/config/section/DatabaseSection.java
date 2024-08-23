package com.eternalcode.economy.config.section;

import com.eternalcode.economy.database.DatabaseDriverType;
import com.eternalcode.economy.database.DatabaseSettings;
import eu.okaeri.configs.OkaeriConfig;

// TODO: Sprawdzić czy fieldy się normalnie generują oraz można je zmienić
public class DatabaseSection extends OkaeriConfig implements DatabaseSettings {

    @Override
    public DatabaseDriverType getDriverType() {
        return DatabaseDriverType.SQLITE;
    }

    @Override
    public String getHostname() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 3306;
    }

    @Override
    public String getDatabase() {
        return "eternal_economy";
    }

    @Override
    public String getUsername() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public boolean isSSL() {
        return false;
    }

    @Override
    public int poolSize() {
        return 16;
    }

    @Override
    public int timeout() {
        return 30000;
    }
}

