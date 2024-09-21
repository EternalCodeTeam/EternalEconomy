package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;

@Header("Messages for the admin section.")
public class MessageAdminSubSection extends OkaeriConfig {

    public Notice insufficientFunds =
        Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
            + "<white>Player {PLAYER} has insufficient funds, they are missing <gradient:#00FFA2:#34AE00>{MISSING_BALANCE}</gradient>.</white>");

    public Notice added = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Added <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice removed = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Removed <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice set = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Set <gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance to "
        + "<gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");
    public Notice reset = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Reset <gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance.</white>");
    public Notice balance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white><gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance is "
        + "<gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
}


