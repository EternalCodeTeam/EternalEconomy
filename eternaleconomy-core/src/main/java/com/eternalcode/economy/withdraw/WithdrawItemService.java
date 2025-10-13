package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import java.math.BigDecimal;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WithdrawItemService {
    private final PluginConfig config;
    private final DecimalFormatter decimalFormatter;
    private final MiniMessage miniMessage;

    private final NamespacedKey amountKey;

    public WithdrawItemService(
        Plugin plugin, PluginConfig config, DecimalFormatter decimalFormatter,
        MiniMessage miniMessage) {
        this.amountKey = new NamespacedKey(plugin, "economy_withdraw_value");

        this.config = config;
        this.decimalFormatter = decimalFormatter;
        this.miniMessage = miniMessage;
    }

    public ItemStack markAsBanknote(ItemStack item, BigDecimal amount) {
        ItemStack taggedItem = item.clone();

        taggedItem.editMeta(meta -> {
            meta.getPersistentDataContainer().set(amountKey, PersistentDataType.STRING, amount.toPlainString());
        });

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

    public ItemStack setUpItem(BigDecimal value) {
        ItemStack item = this.config.currencyItem.item.clone();

        String displayName = this.config.currencyItem.name
            .replace("{VALUE}", decimalFormatter.format(value));

        Component component = miniMessage.deserialize(displayName).decoration(TextDecoration.ITALIC, false);

        item.editMeta(meta -> {
            meta.displayName(component);
        });

        return item;
    }
}
