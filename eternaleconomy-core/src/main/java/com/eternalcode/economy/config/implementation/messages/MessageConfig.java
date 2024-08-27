package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {

    public Notice invalidAmount = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Invalid amount, please provide a valid number.</white>");
    public Notice invalidPlayer = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>Invalid player, please provide a valid player.</white>");
    public Notice notYourself = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <white>You cannot perform this action on yourself.</white>");

    public MessageAdminSubSection admin = new MessageAdminSubSection();
    public MessagesPlayerSubSection player = new MessagesPlayerSubSection();
}
