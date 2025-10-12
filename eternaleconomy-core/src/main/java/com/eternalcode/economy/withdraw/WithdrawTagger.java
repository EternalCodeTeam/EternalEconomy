package com.eternalcode.economy.withdraw;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

public class WithdrawTagger {
    private final NamespacedKey amountKey;

    public WithdrawTagger(Plugin plugin) {
        this.amountKey = new NamespacedKey(plugin, "value");
    }

    public ItemStack tagItem(ItemStack item, BigDecimal amount) {
        ItemStack taggedItem = item.clone();
        ItemMeta meta = taggedItem.getItemMeta();

        if (meta != null) {
            meta.getPersistentDataContainer().set(amountKey, PersistentDataType.DOUBLE, amount.doubleValue());
            taggedItem.setItemMeta(meta);
        }

        return taggedItem;
    }

    public BigDecimal getValue(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return BigDecimal.ZERO;
        }

        Double amount = meta.getPersistentDataContainer().get(amountKey, PersistentDataType.DOUBLE);
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(amount);
    }
}
