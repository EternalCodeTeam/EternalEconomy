package com.eternalcode.economy.withdraw;

import java.math.BigDecimal;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WithdrawItemService {
    private final NamespacedKey amountKey;

    public WithdrawItemService(Plugin plugin) {
        this.amountKey = new NamespacedKey(plugin, "economy_withdraw_value");
    }

    public ItemStack markAsBanknote(ItemStack item, BigDecimal amount) {
        ItemStack taggedItem = item.clone();
        ItemMeta meta = taggedItem.getItemMeta();

        if (meta != null) {
            meta.getPersistentDataContainer().set(amountKey, PersistentDataType.STRING, amount.toPlainString());
            taggedItem.setItemMeta(meta);
        }

        return taggedItem;
    }

    public BigDecimal getValue(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return BigDecimal.ZERO;
        }

        String amount = meta.getPersistentDataContainer().get(amountKey, PersistentDataType.STRING);
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(amount);
    }

    public boolean isBanknote(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(amountKey, PersistentDataType.STRING);
    }
}
