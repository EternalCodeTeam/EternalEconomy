package com.eternalcode.economy.paycheck;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Objects;


public class PaycheckListener implements Listener {
    private final PaycheckManager paycheckManager;
    private final PaycheckTagger paycheckTagger;

    public PaycheckListener(PaycheckManager paycheckManager, PaycheckTagger paycheckTagger) {
        this.paycheckManager = paycheckManager;
        this.paycheckTagger = paycheckTagger;
    }


    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        item = item.clone();

        BigDecimal value = paycheckTagger.getValue(item);

        if (Objects.equals(value, BigDecimal.ZERO)) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.isSneaking()) {
                paycheckManager.redeem(player, item, value);
            } else {
                item.setAmount(1);
                paycheckManager.redeem(player, item, value);
            }
        }
    }
}
