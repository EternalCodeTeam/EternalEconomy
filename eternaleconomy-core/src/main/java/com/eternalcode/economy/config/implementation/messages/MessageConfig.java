package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {

    public Notice invalidAmount = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Invalid amount, please provide a valid number.</white>");
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

    public MessageAdminSubSection admin = new MessageAdminSubSection();
    public MessagesPlayerSubSection player = new MessagesPlayerSubSection();
}
