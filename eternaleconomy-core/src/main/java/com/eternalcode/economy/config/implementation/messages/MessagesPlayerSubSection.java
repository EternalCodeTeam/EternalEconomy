package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessagesPlayerSubSection extends OkaeriConfig {

    public Notice added = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Added <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to your account.</white>");
    public Notice removed = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Removed <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from your account.</white>");
    public Notice set = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Set your balance to <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");
    public Notice reset = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Resetted your balance.</white>");
    public Notice balance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Your balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice balanceOther =
        Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
            + " <white><gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice insufficientBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Insufficient funds,"
        + " you are missing <gradient:#00FFA2:#34AE00> {MISSING_BALANCE}</gradient>.</white>");
    public Notice transferSuccess = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray"
        + ">➤</dark_gray> <white>Successfully transferred <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferReceived = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Received <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferLimit = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Transaction limit is <gradient:#00FFA2:#34AE00>{LIMIT}</gradient>.</white>");
}
