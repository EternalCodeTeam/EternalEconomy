package com.eternalcode.economy.withdraw.controller;

import com.eternalcode.economy.withdraw.WithdrawItemService;
import com.eternalcode.economy.withdraw.WithdrawService;
import java.math.BigDecimal;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class WithdrawController implements Listener {

    private final WithdrawService withdrawService;
    private final WithdrawItemService withdrawItemService;

    public WithdrawController(WithdrawService withdrawService, WithdrawItemService withdrawItemService) {
        this.withdrawService = withdrawService;
        this.withdrawItemService = withdrawItemService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onItemUse(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        if (hand == null) {
            return;
        }

        ItemStack item = hand == EquipmentSlot.HAND
            ? player.getInventory().getItemInMainHand()
            : player.getInventory().getItemInOffHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        BigDecimal value = this.withdrawItemService.getValue(item);
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        event.setCancelled(true);

        int itemAmount = player.isSneaking() ? item.getAmount() : 1;
        this.withdrawService.redeem(player, item, value, itemAmount);
    }
}
