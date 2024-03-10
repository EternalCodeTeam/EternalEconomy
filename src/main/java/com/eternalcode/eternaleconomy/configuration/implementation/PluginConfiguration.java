package com.eternalcode.eternaleconomy.configuration.implementation;

import com.eternalcode.eternaleconomy.database.DatabaseType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;

import java.math.BigDecimal;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
@Header("# ")
@Header("# EternalEconomy configuration file")
@Header("# Permissions:")
@Header("# - eternaleconomy.admin - admin permissions")
@Header("# - eternaleconomy.player - player permissions")
@Header("# ")
public class PluginConfiguration extends OkaeriConfig {


    public Database database = new Database();


    @Comment("Starting balance")
    public BigDecimal starting_balance = BigDecimal.valueOf(250);

    @Comment("Minimal pay ammount")
    public BigDecimal minimal_pay_ammount = BigDecimal.valueOf(1);

    @Comment("Player doesnt have enought money message")
    public String not_enough_money_message = "<red>Error! You dont have enough money to do this!";


    @Comment("Pay sent message")
    public String pay_sent_message = "<green>You sent %ammount% to %player%";

    @Comment("Receive pay message")
    public String receive_pay_message = "<green> Your received %balance% from %player%";

    @Comment("Checking balance message")
    public String checking_balance_message = "<green>Your current balance: %balance%";

    @Comment("Minimal pay ammount message")
    public String minimal_pay_ammount_message = "<red>Error! The mimnal pay ammount is: %ammount%";

    @Comment("Checking another player balance message")
    public String checking_balance_other_message = "<green>%target%'s current balance: %balance%";

    @Comment("Add balance message")
    public String adding_balance_message = "<green>You sucesfully added %amount% to %player%";

    @Comment("Remove balance message")
    public String removing_balance_message = "<green>You sucesfully removed %amount% from %player%";

    @Comment("Set balance message")
    public String set_balance_message = "<green>You sucesfully set %players%'s balance to %player%";

    @Comment("Reset balance message")
    public String reset_balance_message = "<green>You sucesfully reset %player%'s balance";

    @Comment("Incorrect usage of economy command message")
    public String incorrect_economy_usage = "<red>Incorrect usage! Use /economt <set/add/remove/reset> <plyaer> <amount>";

    public static class Database extends OkaeriConfig {
        public DatabaseType type = DatabaseType.SQLITE;

        @Comment("For Remote databases.")
        public String host = "localhost";
        public String username = "root";
        public String password = "password";
        public int port = 3306;
        public String database = "eternaleconomy";
        public boolean useSSL = false;

        @Comment("For file based databases.")
        public String file = "economy.db";

    }
}
