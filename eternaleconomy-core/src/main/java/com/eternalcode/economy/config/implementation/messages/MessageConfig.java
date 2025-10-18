package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.economy.withdraw.WithdrawMessageConfig;
import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {

    public Notice positiveNumberRequired = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Invalid '{AMOUNT}' value, positive number required!</white>");
    public Notice invalidPlayer = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Invalid player, please provide a valid player.</white>");
    public Notice notSender = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>You cannot perform this action on yourself.</white>");

    public Notice correctUsage =
        Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> Correct usage:");
    public Notice correctUsageHeader = Notice.chat("<dark_gray>➤</dark_gray> &fCorrect usage:");
    public Notice correctUsageEntry = Notice.chat("<dark_gray>➤</dark_gray> &f{USAGE}");

    public Notice missingPermission = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Missing permission: <gradient:#00FFA2:#34AE00>{PERMISSION}</gradient>.</white>");

    public Notice invalidMoney =
        Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>Invalid money value format! Use: 1000, 1k, 1.5k, 1m, etc.");
    public Notice incorrectMoneyArgument = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>Price must be greater than or equal 0.1!");

    public MessageAdminSubSection admin = new MessageAdminSubSection();
    public MessagesPlayerSubSection player = new MessagesPlayerSubSection();
    public WithdrawMessageConfig withdraw = new WithdrawMessageConfig();
}
