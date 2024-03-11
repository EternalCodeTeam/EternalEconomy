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
    public BigDecimal startingBalance = BigDecimal.valueOf(250);

    @Comment("Minimal pay ammount")
    public BigDecimal minimalPayAmount = BigDecimal.valueOf(1);

    @Override
    public EconomyConfiguration economy() {
        return new EconomyConfigurationMessages();
    }

    public ConfigInterface getInterface() {
        return this;
    }


    public static class EconomyConfigurationMessages implements ConfigInterface.EconomyConfiguration {

        @Comment("Player doesnt have enough money message")
        public Notice notEnoughMoneyMessage = Notice.chat("<red>Error! You dont have enough money to do this!");

        @Comment("Pay sent message")
        public Notice paySentMessage = Notice.chat("<green>You sent %amount% to %player%");

        @Comment("Receive pay message")
        public Notice receivePayMessage = Notice.chat("<green>You received %amount% from %player%");

        @Comment("Checking balance message")
        public Notice checkBalanceMessage = Notice.chat("<green>Your current balance: %balance%");

        @Comment("Minimal pay ammount message")
        public Notice minimalPayAmountMessage = Notice.chat("<red>Error! The minimal pay amount is: %amount%");

        @Comment("Checking another player balance message")
        public Notice checkBalanceOtherMessage = Notice.chat("<green>%target%'s current balance: %balance%");

        @Comment("Add balance message")
        public Notice addBalanceMessage = Notice.chat("<green>You successfully added %amount% to %player%");

        @Comment("Remove balance message")
        public Notice removeBalanceMessage = Notice.chat("<green>You successfully removed %amount% from %player%");

        @Comment("Set balance message")
        public Notice setBalanceMessage = Notice.chat("<green>You successfully set %player%'s balance to %amount%");

        @Comment("Reset balance message")
        public Notice resetBalanceMessage = Notice.chat("<green>You successfully reset %player%'s balance");

        @Comment("Incorrect usage of economy command message")
        public Notice incorrectEconomyUsageMessage = Notice.chat("<red>Incorrect usage! Use /economy <set/add/remove/reset> <player> <amount>");

        @Override
        public Notice notEnoughMoneyMessage() {
            return notEnoughMoneyMessage;
        }

        @Override
        public Notice paySentMessage() {
            return paySentMessage;
        }

        @Override
        public Notice receivePayMessage() {
            return receivePayMessage;
        }

        @Override
        public Notice checkBalanceMessage() {
            return checkBalanceMessage;
        }

        @Override
        public Notice checkBalanceOtherMessage() {
            return checkBalanceOtherMessage;
        }

        @Override
        public Notice minimalPayAmmountMessage() {
            return minimalPayAmountMessage;
        }

        @Override
        public Notice addBalanceMessage() {
            return addBalanceMessage;
        }

        @Override
        public Notice removeBalanceMessage() {
            return removeBalanceMessage;
        }

        @Override
        public Notice setBalanceMessage() {
            return setBalanceMessage;
        }

        @Override
        public Notice resetBalanceMessage() {
            return resetBalanceMessage;
        }

        @Override
        public Notice incorrectEconomyUsageMessage() {
            return incorrectEconomyUsageMessage;
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