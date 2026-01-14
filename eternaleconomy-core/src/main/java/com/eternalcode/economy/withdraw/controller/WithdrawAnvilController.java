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
        Inventory topInventory = event.getInventory();

        if (this.isRestrictedInventory(topInventory)) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (this.isBanknoteInteraction(clickedItem, cursorItem, event.getAction())) {
            event.setCancelled(true);
            event.getView().close();

            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.invalidInteraction)
                .viewer(event.getWhoClicked())
                .send();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (this.isRestrictedInventory(inventory)) {
            return;
        }

        ItemStack draggedItem = event.getOldCursor();
        if (draggedItem == null || !this.withdrawItemService.isBanknote(draggedItem)) {
            return;
        }

        int inventorySize = inventory.getSize();
        boolean isDraggingToTopInventory = false;

        for (Integer slot : event.getRawSlots()) {
            if (slot < inventorySize) {
                isDraggingToTopInventory = true;
                break;
            }
        }

        if (isDraggingToTopInventory) {
            event.setCancelled(true);
            event.getView().close();

            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.invalidInteraction)
                .viewer(event.getWhoClicked())
                .send();
        }
    }

    private boolean isRestrictedInventory(Inventory inventory) {
        InventoryType type = inventory.getType();
        return type != InventoryType.ANVIL && type != InventoryType.CRAFTING && type != InventoryType.WORKBENCH;
    }

    private boolean isBanknoteInteraction(ItemStack clickedItem, ItemStack cursorItem, InventoryAction action) {
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY && this.withdrawItemService.isBanknote(clickedItem)) {
            return true;
        }

        if (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE
            || action == InventoryAction.PLACE_SOME) {
            return this.withdrawItemService.isBanknote(cursorItem);
        }

        return false;
    }
}
