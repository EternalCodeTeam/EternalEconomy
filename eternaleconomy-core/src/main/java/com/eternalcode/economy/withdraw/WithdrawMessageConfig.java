package com.eternalcode.economy.withdraw;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class WithdrawMessageConfig extends OkaeriConfig {
    public Notice noItemInHand = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold an item to create a banknote!"
    );

    public Notice noBanknoteInHand = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You must hold a banknote in your hand to redeem it!"
    );

    public Notice itemSetSuccess = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have set the banknote item to <white>{ITEM}<green>!"
    );

    public Notice banknoteWithdrawn = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have withdrawn a banknote worth <white>{VALUE}<green>!"
    );

    public Notice banknoteRedeemed = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <green>You have redeemed your banknote worth <white>{VALUE}<green>!"
    );

    public Notice invalidInteraction = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You cannot use the banknote item in an anvil or crafting table!"
    );

    public Notice noInventorySpace = Notice.chat(
        "<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> <red>You do not have enough space in your inventory to withdraw a banknote!"
    );
}
