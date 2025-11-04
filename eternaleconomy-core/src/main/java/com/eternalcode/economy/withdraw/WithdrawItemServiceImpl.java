package com.eternalcode.economy.withdraw;

import com.cryptomorin.xseries.XEnchantment;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.item.ConfigItem;
import com.eternalcode.economy.format.DecimalFormatter;
import java.math.BigDecimal;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WithdrawItemServiceImpl implements WithdrawItemService {

    private static final String VALUE_PLACEHOLDER = "{VALUE}";
    private static final String WITHDRAW_VALUE_KEY = "withdraw_value";

    private final PluginConfig pluginConfig;
    private final DecimalFormatter moneyFormatter;
    private final MiniMessage miniMessage;
    private final NamespacedKey banknoteValueKey;

    public WithdrawItemServiceImpl(
        Plugin plugin,
        PluginConfig pluginConfig,
        DecimalFormatter moneyFormatter,
        MiniMessage miniMessage
    ) {
        this.banknoteValueKey = new NamespacedKey(plugin, WITHDRAW_VALUE_KEY);
        this.pluginConfig = pluginConfig;
        this.moneyFormatter = moneyFormatter;
        this.miniMessage = miniMessage;
    }

    @Override
    public ItemStack createBanknote(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Banknote value must be positive, got: " + value);
        }

        ItemStack banknoteItem = this.createBaseBanknoteItem(value);
        return this.attachBanknoteValue(banknoteItem, value);
    }

    @Override
    public boolean isBanknote(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(this.banknoteValueKey, PersistentDataType.STRING);
    }

    @Override
    public BigDecimal getValue(ItemStack itemStack) {
        if (itemStack == null) {
            return BigDecimal.ZERO;
        }

        if (!itemStack.hasItemMeta()) {
            return BigDecimal.ZERO;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        String storedValue = itemMeta.getPersistentDataContainer()
            .get(this.banknoteValueKey, PersistentDataType.STRING);

        if (storedValue == null) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(storedValue);
        }
        catch (NumberFormatException exception) {
            return BigDecimal.ZERO;
        }
    }

    private ItemStack createBaseBanknoteItem(BigDecimal value) {
        ConfigItem configItem = this.pluginConfig.withdraw.item;
        String formattedValue = this.moneyFormatter.format(value);

        ItemStack banknoteItem = new ItemStack(configItem.material());

        banknoteItem.editMeta(meta -> {
            if (configItem.texture() != null) {
                meta.setCustomModelData(configItem.texture());
            }

            Component displayName = this.miniMessage.deserialize(
                configItem.name().replace(VALUE_PLACEHOLDER, formattedValue),
                TagResolver.empty()
            ).decoration(TextDecoration.ITALIC, false);
            meta.displayName(displayName);

            List<Component> loreComponents = configItem.lore().stream()
                .map(line -> this.miniMessage.deserialize(
                    line.replace(VALUE_PLACEHOLDER, formattedValue),
                    TagResolver.empty()
                ).decoration(TextDecoration.ITALIC, false))
                .toList();
            meta.lore(loreComponents);
        });

        if (configItem.glow()) {
            banknoteItem.editMeta(meta -> {
                Enchantment enchantment = XEnchantment.UNBREAKING.get(); // for version compatibility
                meta.addEnchant(enchantment, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            });
        }

        return banknoteItem;
    }

    private ItemStack attachBanknoteValue(ItemStack banknoteItem, BigDecimal value) {
        ItemStack taggedItem = banknoteItem.clone();
        taggedItem.editMeta(meta ->
            meta.getPersistentDataContainer().set(
                this.banknoteValueKey,
                PersistentDataType.STRING,
                value.toPlainString()
            )
        );
        return taggedItem;
    }
}
