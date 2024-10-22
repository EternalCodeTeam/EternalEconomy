package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

import static com.eternalcode.economy.config.implementation.messages.MessageConfig.*;

public class MessagesPlayerSubSection extends OkaeriConfig {

    public Notice added =
        Notice.chat(messagesPrefix + "<white>Added <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>"
            + "to your account.</white>");
    public Notice removed =
        Notice.chat(messagesPrefix + " <white>Removed <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>"
            + "from your account.</white>");
    public Notice set =
        Notice.chat(messagesPrefix + "<white>Set your balance to <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");
    public Notice reset =
        Notice.chat(messagesPrefix + "<white>Your balance was reset.</white>");
    public Notice balance =
        Notice.chat(messagesPrefix + " <white>Your balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice balanceOther =
        Notice.chat(messagesPrefix + " <white><gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance is"
            + "<gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice insufficientBalance = Notice.chat(messagesPrefix + "<white>Insufficient funds,"
        + " you are missing <gradient:#00FFA2:#34AE00> {MISSING_BALANCE}</gradient>.</white>");
    public Notice transferSuccess =
        Notice.chat(messagesPrefix + "<white>Successfully transferred <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to "
            + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferReceived =
        Notice.chat(messagesPrefix + "<white>Received <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
            + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferLimit =
        Notice.chat(messagesPrefix + "<white>Transaction limit is <gradient:#00FFA2:#34AE00>{LIMIT}</gradient>.</white>");
}
