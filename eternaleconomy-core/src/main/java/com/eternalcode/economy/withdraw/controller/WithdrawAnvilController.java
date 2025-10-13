package com.eternalcode.economy.withdraw.controller;

import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.economy.withdraw.WithdrawItemService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WithdrawAnvilController implements Listener {

    private final WithdrawItemService withdrawItemService;
    private final NoticeService noticeService;

    public WithdrawAnvilController(WithdrawItemService withdrawItemService, NoticeService noticeService) {
        this.withdrawItemService = withdrawItemService;
        this.noticeService = noticeService;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAnvilUse(InventoryClickEvent event) {
        Inventory topInventory = event.getInventory();

        if (!(topInventory instanceof AnvilInventory) && !(topInventory instanceof CraftingInventory)) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        if (event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            return;
        }

        if (this.withdrawItemService.isBanknote(item)) {
            event.getView().close();

            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.inventoryInteract)
                .viewer(event.getWhoClicked())
                .send();
        }
    }

    // @EventHandler
    // public void onPlayerDrag(InventoryDragEvent event) {
    //     Inventory topInventory = event.getView().getTopInventory();
    //
    //     if (!(topInventory instanceof AnvilInventory) && !(topInventory instanceof CraftingInventory)) {
    //         return;
    //     }
    //     event.get
    //     int topSize = topInventory.getSize();
    //     for (int rawSlot : event.getRawSlots()) {
    //         if (rawSlot < topSize) {
    //             event.setCancelled(true);
    //         }
    //     }
    // }
}
