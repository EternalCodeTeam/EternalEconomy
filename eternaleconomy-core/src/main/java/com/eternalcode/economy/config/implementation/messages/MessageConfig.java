package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {
    @Comment("Messages prefix")
    public static String messagesPrefix =
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> ";
    public Notice invalidAmount = Notice.chat(
        messagesPrefix + "<white>Invalid amount, please provide a valid number.</white>");
    public Notice invalidPlayer = Notice.chat(
        messagesPrefix + "<white>Invalid player, please provide a valid player.</white>");
    public Notice notSender = Notice.chat(
        messagesPrefix + "<white>You cannot perform this action on yourself.</white>");

    public Notice correctUsage =
        Notice.chat(messagesPrefix + "Correct usage:");
    public Notice correctUsageHeader = Notice.chat("<dark_gray>➤</dark_gray> &fCorrect usage:");
    public Notice correctUsageEntry = Notice.chat("<dark_gray>➤</dark_gray> &f{USAGE}");

    public Notice missingPermission =
        Notice.chat(messagesPrefix + "<white>Missing permission: <gradient:#00FFA2:#34AE00>{PERMISSION}</gradient>.</white>");

    public MessageAdminSubSection admin = new MessageAdminSubSection();
    public MessagesPlayerSubSection player = new MessagesPlayerSubSection();
}
