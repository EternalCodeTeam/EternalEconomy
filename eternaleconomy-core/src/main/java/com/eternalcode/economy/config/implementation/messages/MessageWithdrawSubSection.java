package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessageWithdrawSubSection extends OkaeriConfig {
    public Notice noItem = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold item to create paycheck!"
    );

    public Notice noCheck = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold an paycheck to redeem it!"
    );

    public Notice itemSet = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have set the banknote item to <white>{ITEM}<green>!"
    );

    public Notice banknoteWithdrawn = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have withdrawn a banknote worth <white>{VALUE}<green>!"
    );

    public Notice banknoteRedeemed = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have redeemed your banknote worth <white>{VALUE}<green>!"
    );

    public Notice anvilInteract = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You cannot rename the "
            + "banknote item in an anvil!"
    );
}
