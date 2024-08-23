package com.eternalcode.economy.config.implementation;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class MessageConfig extends OkaeriConfig {

    public Notice invalidAmount = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Invalid amount, please provide a valid number.</white>");
    public Notice invalidPlayer = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Invalid player, please provide a valid player.</white>");

    public Notice insufficientBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Insufficient funds, you are missing <gradient:#00FFA2:#34AE00>"
            + "{MISSING_BALANCE}</gradient>.</white>");

    public Notice addBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Added <gradient:#00FFA2:#34AE00>{ADDED}</gradient> to your "
            + "account.</white>");

    public Notice setBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Set your balance to <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");

    public Notice removeBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Removed <gradient:#00FFA2:#34AE00>{REMOVED}</gradient> from your "
            + "account.</white>");

    public Notice transferSuccess = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Successfully transferred <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> "
            + "to <gradient:#00FFA2:#34AE00>{RECEIVER}</gradient>.</white>");

    public Notice transferReceived = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Received <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
            + "<gradient:#00FFA2:#34AE00>{PAYER}</gradient>.</white>");

    public Notice balance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY/gradient></b> "
            + "<dark_gray>➤</dark_gray> <white>Your balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");


}
