package com.eternalcode.economy.paycheck;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

public class PaycheckTagger {
    private final NamespacedKey amountKey;

    public PaycheckTagger(Plugin plugin) {
        this.amountKey = new NamespacedKey(plugin, "paycheck");
    }

    public ItemStack tagItem(ItemStack item, BigDecimal amount) {
        ItemStack taggedItem = item.clone();
        ItemMeta meta = taggedItem.getItemMeta();

        if(meta != null) {
            meta.getPersistentDataContainer().set(amountKey, PersistentDataType.DOUBLE, amount.doubleValue());
        }

        return taggedItem;
    }
}
