package com.eternalcode.economy.withdraw;

import com.cryptomorin.xseries.XEnchantment;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.item.ConfigItem;
import com.eternalcode.economy.config.item.WithdrawItemEntry;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class WithdrawBanknoteRenderer {

    private static final String VALUE_PLACEHOLDER = "{VALUE}";
    private static final String CURRENT_VALUE_PLACEHOLDER = "{CURRENT_VALUE}";
    private static final String DECAY_RATE_PLACEHOLDER = "{DECAY_RATE}";
    private static final String PLAYER_PLACEHOLDER = "{PLAYER}";
    private static final TagResolver EMPTY_RESOLVER = TagResolver.empty();

    private final PluginConfig pluginConfig;
    private final MiniMessage miniMessage;
    private final Enchantment glowEnchantment;

    private volatile NavigableMap<BigDecimal, ConfigItem> cachedThresholdMap;
    private volatile List<WithdrawItemEntry> cachedEntries;

    public WithdrawBanknoteRenderer(PluginConfig pluginConfig, MiniMessage miniMessage) {
        this.pluginConfig = pluginConfig;
        this.miniMessage = miniMessage;
        this.glowEnchantment = XEnchantment.UNBREAKING.get();
    }

    public ConfigItem selectConfigItem(BigDecimal value) {
        if (!this.pluginConfig.withdraw.multiItemEnabled) {
            return this.pluginConfig.withdraw.item;
        }

        NavigableMap<BigDecimal, ConfigItem> thresholdMap = this.getOrRebuildThresholdMap();
        if (thresholdMap.isEmpty()) {
            return this.pluginConfig.withdraw.item;
        }

        Map.Entry<BigDecimal, ConfigItem> entry = thresholdMap.floorEntry(value);
        return entry == null ? this.pluginConfig.withdraw.item : entry.getValue();
    }

    public void renderAppearance(
        ItemMeta meta, ConfigItem configItem,
        String formattedNominal, String formattedCurrent,
        String creatorName, String decayRatePercent
    ) {
        Component name = this.miniMessage.deserialize(
            this.replaceBanknotePlaceholders(
                configItem.name(), formattedNominal, formattedCurrent, creatorName, decayRatePercent),
            EMPTY_RESOLVER
        ).decoration(TextDecoration.ITALIC, false);

        meta.displayName(name);

        if (!configItem.lore().isEmpty()) {
            List<Component> lore = new ArrayList<>(configItem.lore().size());
            for (String line : configItem.lore()) {
                lore.add(
                    this.miniMessage.deserialize(
                        this.replaceBanknotePlaceholders(
                            line, formattedNominal, formattedCurrent, creatorName, decayRatePercent),
                        EMPTY_RESOLVER
                    ).decoration(TextDecoration.ITALIC, false)
                );
            }
            meta.lore(lore);
        }

        if (configItem.glow() && !meta.hasEnchants()) {
            meta.addEnchant(this.glowEnchantment, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    private String replaceBanknotePlaceholders(
        String text, String formattedNominal, String formattedCurrent, String creatorName, String decayRatePercent
    ) {
        return text
            .replace(VALUE_PLACEHOLDER, formattedNominal)
            .replace(CURRENT_VALUE_PLACEHOLDER, formattedCurrent)
            .replace(DECAY_RATE_PLACEHOLDER, decayRatePercent)
            .replace(PLAYER_PLACEHOLDER, creatorName);
    }

    private NavigableMap<BigDecimal, ConfigItem> getOrRebuildThresholdMap() {
        List<WithdrawItemEntry> entries = this.pluginConfig.withdraw.multiItemEntries;

        if (this.cachedThresholdMap == null || this.cachedEntries != entries) {
            this.cachedThresholdMap = this.buildThresholdMap(entries);
            this.cachedEntries = entries;
        }

        return this.cachedThresholdMap;
    }

    private NavigableMap<BigDecimal, ConfigItem> buildThresholdMap(List<WithdrawItemEntry> entries) {
        NavigableMap<BigDecimal, ConfigItem> map = new TreeMap<>();

        if (entries == null || entries.isEmpty()) {
            return map;
        }

        for (WithdrawItemEntry entry : entries) {
            if (entry.minValue() != null && entry.item() != null) {
                map.put(entry.minValue(), entry.item());
            }
        }

        return map;
    }
}
