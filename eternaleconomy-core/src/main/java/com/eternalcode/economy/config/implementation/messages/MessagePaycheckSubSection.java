package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessagePaycheckSubSection extends OkaeriConfig {
    public Notice noItem = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold item to create paycheck!"
    );

    public Notice noCheck = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold an paycheck to redeem it!"
    );

    public Notice setItem = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have set paycheck item to <white>{ITEM}<green>!"
    );

    public Notice withdraw = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have withdrawn a paycheck of <white>{VALUE}<green>!"
    );

    public Notice redeem = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have redeemed your paycheck of <white>{VALUE}<green>!"
    );
}
