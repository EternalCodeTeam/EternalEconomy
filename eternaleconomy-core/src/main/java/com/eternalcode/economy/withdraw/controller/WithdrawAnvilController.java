package com.eternalcode.economy.withdraw.controller;

import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.economy.withdraw.WithdrawItemService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WithdrawAnvilController implements Listener {

    private final WithdrawItemService withdrawItemService;
    private final NoticeService noticeService;

    public WithdrawAnvilController(WithdrawItemService withdrawItemService, NoticeService noticeService) {
        this.withdrawItemService = withdrawItemService;
        this.noticeService = noticeService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();

        if (!this.isRestrictedInventory(topInventory)) {
            return;
        }

        InventoryAction action = event.getAction();
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        /*
         * Clicking a slot in the top inventory.
         *
         * Raw slots belonging to the top inventory are:
         * 0 .. topInventory.getSize() - 1
         */
        boolean clickingTopInventory =
                event.getRawSlot() >= 0
                        && event.getRawSlot() < topInventory.getSize();

        /*
         * Player is trying to put a banknote from the cursor
         * into the anvil/crafting/workbench.
         */
        if (clickingTopInventory
                && this.withdrawItemService.isBanknote(cursorItem)) {

            event.setCancelled(true);
            this.sendInvalidInteractionNotice(event);
            return;
        }

        /*
         * Shift-clicking a banknote while an anvil/crafting/workbench
         * is open would move the item between inventories.
         *
         * Cancel it so the banknote cannot be automatically inserted
         * into the restricted inventory.
         */
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY
                && this.withdrawItemService.isBanknote(clickedItem)) {

            event.setCancelled(true);
            this.sendInvalidInteractionNotice(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();

        if (!this.isRestrictedInventory(topInventory)) {
            return;
        }

        ItemStack draggedItem = event.getOldCursor();

        if (!this.withdrawItemService.isBanknote(draggedItem)) {
            return;
        }

        int topInventorySize = topInventory.getSize();

        /*
         * If any dragged slot belongs to the top inventory,
         * cancel the whole drag operation.
         */
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot >= 0 && rawSlot < topInventorySize) {
                event.setCancelled(true);
                this.sendInvalidInteractionNotice(event);
                return;
            }
        }
    }

    private boolean isRestrictedInventory(Inventory inventory) {
        InventoryType type = inventory.getType();
        return type == InventoryType.ANVIL
                || type == InventoryType.WORKBENCH;
    }

    private void sendInvalidInteractionNotice(InventoryClickEvent event) {
        this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.invalidInteraction)
                .viewer(event.getWhoClicked())
                .send();
    }

    private void sendInvalidInteractionNotice(InventoryDragEvent event) {
        this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.invalidInteraction)
                .viewer(event.getWhoClicked())
                .send();
    }
}
