package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;

import static com.eternalcode.economy.config.implementation.messages.MessageConfig.*;

@Header("Messages for the admin section.")
public class MessageAdminSubSection extends OkaeriConfig {

    public Notice insufficientFunds =
        Notice.chat(messagesPrefix + "<white>Player {PLAYER} has insufficient funds,"
            + "they are missing <gradient:#00FFA2:#34AE00>{MISSING_BALANCE}</gradient>.</white>");


    public Notice added =
        Notice.chat(messagesPrefix + "<white>Added <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to "
            + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice removed =
        Notice.chat(messagesPrefix + " <white>Removed <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
            + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice set =
        Notice.chat(messagesPrefix + "<white>Set <gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance to "
            + "<gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");
    public Notice reset =
        Notice.chat(messagesPrefix + "<white>Reset <gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance.</white>");
    public Notice balance =
        Notice.chat(messagesPrefix + " <white><gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance is "
            + "<gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
}


