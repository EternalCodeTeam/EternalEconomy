package com.eternalcode.economy.account.database;

import com.eternalcode.economy.account.Account;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigDecimal;
import java.util.UUID;

@DatabaseTable(tableName = "eternaleconomy_accounts")
class AccountTable {

    static final String UUID = "uuid";
    static final String NAME = "name";
    static final String BALANCE = "balance";

    @DatabaseField(id = true, unique = true, columnName = UUID)
    private UUID uuid;

    @DatabaseField(index = true, unique = true, columnName = NAME)
    private String name;

    @DatabaseField(dataType = DataType.BIG_DECIMAL_NUMERIC, columnName = BALANCE)
    private BigDecimal balance;

    public AccountTable() {
    }

    public AccountTable(UUID uuid, String name, BigDecimal balance) {
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
    }

    public static AccountTable fromAccount(Account account) {
        return new AccountTable(account.uuid(), account.name(), account.balance());
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
