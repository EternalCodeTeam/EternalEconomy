package com.eternalcode.economy.database;

public interface DatabaseSettings {

    DatabaseDriverType getDriverType();

    String getHostname();

    int getPort();

    String getDatabase();

    String getUsername();

    String getPassword();

    boolean isSSL();

    int poolSize();

    int timeout();
}