package com.eternalcode.eternaleconomy.configuration.implementation;

import com.eternalcode.eternaleconomy.configuration.ConfigInterface;
import com.eternalcode.eternaleconomy.database.DatabaseType;
import com.eternalcode.multification.notice.Notice;
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
public class PluginConfiguration extends OkaeriConfig implements ConfigInterface {

    public Database database = new Database();
    public BalanceTop balanceTop = new BalanceTop();


    @Comment("Starting balance")
    public BigDecimal starting_balance = BigDecimal.valueOf(250);

    @Comment("Minimal pay ammount")
    public BigDecimal minimal_pay_ammount = BigDecimal.valueOf(1);

    @Override
    public EconomyConfiguration economy() {
        return new EconomyConfigurationMessages();
    }

    public ConfigInterface getInterface() {
        return this;
    }


    public static class EconomyConfigurationMessages implements ConfigInterface.EconomyConfiguration {

        @Comment("Player doesnt have enough money message")
        public Notice not_enough_money_message = Notice.chat("<red>Error! You dont have enough money to do this!");

        @Comment("Pay sent message")
        public Notice pay_sent_message = Notice.chat("<green>You sent %amount% to %player%");

        @Comment("Receive pay message")
        public Notice receive_pay_message = Notice.chat("<green>You received %amount% from %player%");

        @Comment("Checking balance message")
        public Notice checking_balance_message = Notice.chat("<green>Your current balance: %balance%");

        @Comment("Minimal pay ammount message")
        public Notice minimal_pay_ammount_message = Notice.chat("<red>Error! The mimnal pay ammount is: %ammount%");

        @Comment("Checking another player balance message")
        public Notice checking_balance_other_message = Notice.chat("<green>%target%'s current balance: %balance%");

        @Comment("Add balance message")
        public Notice adding_balance_message = Notice.chat("<green>You sucesfully added %amount% to %player%");

        @Comment("Remove balance message")
        public Notice removing_balance_message = Notice.chat("<green>You sucesfully removed %amount% from %player%");

        @Comment("Set balance message")
        public Notice set_balance_message = Notice.chat("<green>You successfully set %player%'s balance to %amount%");

        @Comment("Reset balance message")
        public Notice reset_balance_message = Notice.chat("<green>You sucesfully reset %player%'s balance");

        @Comment("Incorrect usage of economy command message")
        public Notice incorrect_economy_usage = Notice.chat("<red>Incorrect usage! Use /economt <set/add/remove/reset> <plyaer> <amount>");

        @Override
        public Notice not_enough_money_message() {
            return not_enough_money_message;
        }

        @Override
        public Notice pay_sent_message() {
            return pay_sent_message;
        }

        @Override
        public Notice pay_received_message() {
            return receive_pay_message;
        }

        @Override
        public Notice checking_balance_message() {
            return checking_balance_message;
        }

        @Override
        public Notice checking_balance_other_message() {
            return checking_balance_other_message;
        }

        @Override
        public Notice minimal_pay_ammount_message() {
            return minimal_pay_ammount_message;
        }

        @Override
        public Notice adding_balance_message() {
            return adding_balance_message;
        }

        @Override
        public Notice removing_balance_message() {
            return removing_balance_message;
        }

        @Override
        public Notice set_balance_message() {
            return set_balance_message;
        }

        @Override
        public Notice reset_balance_message() {
            return reset_balance_message;
        }

        @Override
        public Notice incorrect_economy_usage() {
            return incorrect_economy_usage;
        }

    }

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

    public static class BalanceTop extends OkaeriConfig {
        @Comment("Inventory title")
        public String inventory_title = "<yellow>Balance Top";
    }
}
