package com.eternalcode.economy.withdraw;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Objects;


public class WithdrawListener implements Listener {
    private final WithdrawManager withdrawManager;
    private final WithdrawTagger withdrawTagger;

    public WithdrawListener(WithdrawManager withdrawManager, WithdrawTagger withdrawTagger) {
        this.withdrawManager = withdrawManager;
        this.withdrawTagger = withdrawTagger;
    }


    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        item = item.clone();

        BigDecimal value = withdrawTagger.getValue(item);

        if (Objects.equals(value, BigDecimal.ZERO)) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.isSneaking()) {
                withdrawManager.redeem(player, item, value);
            } else {
                item.setAmount(1);
                withdrawManager.redeem(player, item, value);
            }
        }
    }
}
