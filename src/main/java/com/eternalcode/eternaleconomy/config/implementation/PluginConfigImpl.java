package com.eternalcode.eternaleconomy.config.implementation;

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
public class PluginConfigImpl extends OkaeriConfig implements PluginConfig {

    public Database database = new Database();

    public MessageSection messages = new MessageSection();

    public ArgumentSection argument = new ArgumentSection();

    @Comment("Starting balance")
    public BigDecimal startingBalance = BigDecimal.valueOf(250);

    @Comment("Minimal pay ammount")
    public BigDecimal minimalPayAmount = BigDecimal.valueOf(1);

    @Override
    public Messages messages() {
        return this.messages;
    }

    @Override
    public Argument argument() {
        return this.argument;
    }

    public static class ArgumentSection extends OkaeriConfig implements Argument {

        @Comment({ " ", "# {USAGE} - Correct usage" })
        public Notice usageMessage = Notice.chat("<dark_red>✘ <red>Correct usage: <gold>{USAGE}");
        public Notice usageMessageHead = Notice.chat("<green>► <white>Correct usage:");
        public Notice usageMessageEntry = Notice.chat("<green>► <white>{USAGE}");

        public Notice playerNotFound = Notice.chat("<dark_red>✘ <red>Player not found!");

        @Override
        public Notice playerNotFound() {
            return this.playerNotFound;
        }

        @Override
        public Notice usageMessage() {
            return this.usageMessage;
        }

        @Override
        public Notice usageMessageHead() {
            return this.usageMessageHead;
        }

        @Override
        public Notice usageMessageEntry() {
            return this.usageMessageEntry;
        }
    }

    public static class MessageSection extends OkaeriConfig implements Messages {

        @Comment("Player doesnt have enough money message")
        public Notice notEnoughMoneyMessage = Notice.chat("<red>You don't have enough money!");

        @Comment("Pay sent message")
        public Notice paySentMessage = Notice.actionbar("<green>You sent {AMOUNT} to {PLAYER}");

        @Comment("Receive pay message")
        public Notice receivePayMessage = Notice.chat("<green>You received {AMOUNT} from {PLAYER}");

        @Comment("Checking balance message")
        public Notice checkBalanceMessage = Notice.chat("<green>Your current balance: {BALANCE}");

        @Comment("Minimal pay ammount message")
        public Notice minimalPayAmountMessage = Notice.chat("<red>Error! The minimal pay amount is: {AMOUNT}");

        @Comment("Checking another player balance message")
        public Notice checkBalanceOtherMessage = Notice.chat("<green>{TARGET}'s current balance: {BALANCE}");

        @Comment("Add balance message")
        public Notice addBalanceMessage = Notice.chat("<green>You successfully added {AMOUNT} to {PLAYER}");

        @Comment("Remove balance message")
        public Notice removeBalanceMessage = Notice.chat("<green>You successfully removed {AMOUNT} from {PLAYER}");

        @Comment("Set balance message")
        public Notice setBalanceMessage = Notice.chat("<green>You successfully set {PLAYER}'s balance to {AMOUNT}");

        @Comment("Reset balance message")
        public Notice resetBalanceMessage = Notice.chat("<green>You successfully reset {PLAYER}'s balance");

        @Comment("Incorrect usage of economy command message")
        public Notice incorrectEconomyUsageMessage =
            Notice.chat("<red>Incorrect usage! Use /economy <set/add/remove/reset> <player> <amount>");

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
        public Notice minimalPayAmountMessage() {
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
