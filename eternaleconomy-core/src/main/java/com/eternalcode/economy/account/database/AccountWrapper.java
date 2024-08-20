package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigDecimal;
import java.util.UUID;

@DatabaseTable(tableName = "eternaleconomy_accounts")
public class AccountWrapper {

    @DatabaseField(id = true)
    private UUID uuid;

    @DatabaseField
    private String name;

    @DatabaseField
    private BigDecimal balance;

    public AccountWrapper() {
    }

    public AccountWrapper(UUID uuid, String name, BigDecimal balance) {
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
    }

    public static AccountWrapper fromAccount(Account account) {
        return new AccountWrapper(account.uuid(), account.name(), account.balance());
    }

    public Account toAccount() {
        return new Account(this.uuid, this.name, this.balance);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }
}
