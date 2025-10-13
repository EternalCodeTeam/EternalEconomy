package com.eternalcode.economy.withdraw.controller;

import com.eternalcode.economy.withdraw.WithdrawItemService;
import com.eternalcode.economy.withdraw.WithdrawService;
import java.math.BigDecimal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WithdrawController implements Listener {
    private final WithdrawService withdrawService;
    private final WithdrawItemService withdrawItemService;

    public WithdrawController(WithdrawService withdrawService, WithdrawItemService withdrawItemService) {
        this.withdrawService = withdrawService;
        this.withdrawItemService = withdrawItemService;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        BigDecimal value = withdrawItemService.getValue(item);

        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        int itemAmount = item.getAmount();

        if (!player.isSneaking()) {
            itemAmount = 1;
        }

        this.withdrawService.redeem(player, item, value, itemAmount);
    }
}
