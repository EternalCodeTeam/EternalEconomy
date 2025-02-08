package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {
    @Comment("Messages prefix")
    public String messagesPrefix =
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> ";

    public Notice invalidAmount = Notice.chat(
        "{PREFIX}<white>Invalid amount, please provide a valid number.</white>"
    );
    public Notice invalidPlayer = Notice.chat(
        "{PREFIX}<white>Invalid player, please provide a valid player.</white>"
    );
    public Notice notSender = Notice.chat(
        "{PREFIX}<white>You cannot perform this action on yourself.</white>"
    );
    public Notice correctUsage = Notice.chat(
        "{PREFIX}Correct usage:"
    );
    public Notice correctUsageHeader = Notice.chat(
        "{PREFIX}<dark_gray>➤</dark_gray> &fCorrect usage:"
    );
    public Notice correctUsageEntry = Notice.chat(
        "{PREFIX}<dark_gray>➤</dark_gray> &f{USAGE}"
    );
    public Notice missingPermission = Notice.chat(
        "{PREFIX}<white>Missing permission: <gradient:#00FFA2:#34AE00>{PERMISSION}</gradient>.</white>"
    );

    public MessageAdminSubSection admin = new MessageAdminSubSection();
    public MessagesPlayerSubSection player = new MessagesPlayerSubSection();
}
