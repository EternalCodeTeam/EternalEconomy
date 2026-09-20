package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.item.ConfigItem;
import com.eternalcode.economy.format.DecimalFormatter;
import java.math.BigDecimal;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WithdrawItemServiceImpl implements WithdrawItemService {

    private static final String WITHDRAW_VALUE_KEY = "withdraw_value";
    private static final String WITHDRAW_CREATED_AT_KEY = "withdraw_created_at";
    private static final String WITHDRAW_CREATOR_KEY = "withdraw_creator";
    private static final String WITHDRAW_LAST_RENDERED_KEY = "withdraw_last_rendered";
    private static final String WITHDRAW_NEXT_UPDATE_KEY = "withdraw_next_update";
    private static final String WITHDRAW_SETTLED_KEY = "withdraw_settled";

    private final PluginConfig pluginConfig;
    private final DecimalFormatter moneyFormatter;
    private final WithdrawBanknoteRenderer renderer;
    private final WithdrawDecayCalculator decayCalculator;

    private final NamespacedKey banknoteValueKey;
    private final NamespacedKey createdAtKey;
    private final NamespacedKey creatorKey;
    private final NamespacedKey lastRenderedKey;
    private final NamespacedKey nextUpdateKey;
    private final NamespacedKey settledKey;

    public WithdrawItemServiceImpl(
        Plugin plugin,
        PluginConfig pluginConfig,
        DecimalFormatter moneyFormatter,
        MiniMessage miniMessage
    ) {
        this.pluginConfig = pluginConfig;
        this.moneyFormatter = moneyFormatter;
        this.renderer = new WithdrawBanknoteRenderer(pluginConfig, miniMessage);
        this.decayCalculator = new WithdrawDecayCalculator();

        this.banknoteValueKey = new NamespacedKey(plugin, WITHDRAW_VALUE_KEY);
        this.createdAtKey = new NamespacedKey(plugin, WITHDRAW_CREATED_AT_KEY);
        this.creatorKey = new NamespacedKey(plugin, WITHDRAW_CREATOR_KEY);
        this.lastRenderedKey = new NamespacedKey(plugin, WITHDRAW_LAST_RENDERED_KEY);
        this.nextUpdateKey = new NamespacedKey(plugin, WITHDRAW_NEXT_UPDATE_KEY);
        this.settledKey = new NamespacedKey(plugin, WITHDRAW_SETTLED_KEY);
    }

    @Override
    public ItemStack createBanknote(BigDecimal value, String creatorName) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Banknote value must be positive, got: " + value);
        }

        ConfigItem configItem = this.renderer.selectConfigItem(value);
        String formattedValue = this.moneyFormatter.format(value);
        long createdAt = System.currentTimeMillis();
        PluginConfig.WithdrawItem.Decay decayConfig = this.pluginConfig.withdraw.decay;

        ItemStack itemStack = new ItemStack(configItem.material());

        itemStack.editMeta(meta -> {
            if (configItem.texture() != null) {
                meta.setCustomModelData(configItem.texture());
            }

            this.renderer.renderAppearance(
                meta, configItem,
                formattedValue, formattedValue, creatorName,
                decayConfig.hourlyRatePercent.toPlainString());

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(this.banknoteValueKey, PersistentDataType.STRING, value.toPlainString());
            pdc.set(this.creatorKey, PersistentDataType.STRING, creatorName);
            pdc.set(this.createdAtKey, PersistentDataType.LONG, createdAt);
            pdc.set(this.lastRenderedKey, PersistentDataType.STRING, formattedValue);

            if (decayConfig.enabled) {
                BigDecimal floor = decayConfig.minValue.min(value);

                long nextUpdate = this.decayCalculator.computeNextUpdateAtMillis(
                    value, createdAt, value, floor,
                    decayConfig.hourlyRatePercent.doubleValue(),
                    decayConfig.displayUpdateThresholdPercent.doubleValue());

                if (nextUpdate != Long.MAX_VALUE) {
                    pdc.set(this.nextUpdateKey, PersistentDataType.LONG, nextUpdate);
                }
            }
        });

        return itemStack;
    }

    @Override
    public boolean isBanknote(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().has(this.banknoteValueKey, PersistentDataType.STRING);
    }

    @Override
    public BigDecimal getValue(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return BigDecimal.ZERO;
        }

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        String rawValue = pdc.get(this.banknoteValueKey, PersistentDataType.STRING);
        if (rawValue == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal nominal;
        try {
            nominal = new BigDecimal(rawValue);
        }
        catch (NumberFormatException ignored) {
            return BigDecimal.ZERO;
        }

        PluginConfig.WithdrawItem.Decay decayConfig = this.pluginConfig.withdraw.decay;
        if (!decayConfig.enabled) {
            return nominal;
        }

        Long createdAt = pdc.get(this.createdAtKey, PersistentDataType.LONG);
        if (createdAt == null) {
            // banknote was created before the decay feature was enabled - never decays
            return nominal;
        }

        BigDecimal floor = decayConfig.minValue.min(nominal);

        return this.decayCalculator.computeDecayedValue(
            nominal, createdAt, System.currentTimeMillis(),
            decayConfig.hourlyRatePercent.doubleValue(), floor);
    }

    @Override
    public long getNextUpdate(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return Long.MAX_VALUE;
        }

        Long stored = itemStack.getItemMeta()
            .getPersistentDataContainer()
            .get(this.nextUpdateKey, PersistentDataType.LONG);

        return stored == null ? Long.MAX_VALUE : stored;
    }

    @Override
    public long refreshLore(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !this.pluginConfig.withdraw.decay.enabled) {
            return Long.MAX_VALUE;
        }

        ItemMeta readMeta = itemStack.getItemMeta();
        PersistentDataContainer readPdc = readMeta.getPersistentDataContainer();

        if (!readPdc.has(this.banknoteValueKey, PersistentDataType.STRING)) {
            return Long.MAX_VALUE;
        }
        if (readPdc.has(this.settledKey, PersistentDataType.BYTE)) {
            return Long.MAX_VALUE;
        }

        String rawValue = readPdc.get(this.banknoteValueKey, PersistentDataType.STRING);
        Long createdAt = readPdc.get(this.createdAtKey, PersistentDataType.LONG);
        String creatorName = readPdc.get(this.creatorKey, PersistentDataType.STRING);

        if (rawValue == null || createdAt == null || creatorName == null) {
            // banknote predates the decay feature - nothing to track
            return Long.MAX_VALUE;
        }

        BigDecimal nominal;
        try {
            nominal = new BigDecimal(rawValue);
        }
        catch (NumberFormatException exception) {
            return Long.MAX_VALUE;
        }

        PluginConfig.WithdrawItem.Decay decayConfig = this.pluginConfig.withdraw.decay;
        BigDecimal floor = decayConfig.minValue.min(nominal);

        long now = System.currentTimeMillis();
        BigDecimal currentValue = this.decayCalculator.computeDecayedValue(
            nominal, createdAt, now, decayConfig.hourlyRatePercent.doubleValue(), floor);
        String formattedCurrent = this.moneyFormatter.format(currentValue);
        String lastRendered = readPdc.get(this.lastRenderedKey, PersistentDataType.STRING);

        boolean valueChanged = !formattedCurrent.equals(lastRendered);
        boolean settled = currentValue.compareTo(floor) <= 0;

        String formattedNominal = this.moneyFormatter.format(nominal);
        ConfigItem configItem = this.renderer.selectConfigItem(nominal);
        long[] nextUpdateHolder = {Long.MAX_VALUE};

        itemStack.editMeta(meta -> {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            if (valueChanged) {
                this.renderer.renderAppearance(
                    meta, configItem,
                    formattedNominal, formattedCurrent, creatorName,
                    decayConfig.hourlyRatePercent.toPlainString());
                pdc.set(this.lastRenderedKey, PersistentDataType.STRING, formattedCurrent);
            }

            if (settled) {
                pdc.set(this.settledKey, PersistentDataType.BYTE, (byte) 1);
                pdc.remove(this.nextUpdateKey);
                nextUpdateHolder[0] = Long.MAX_VALUE;
                return;
            }

            long nextUpdate = this.decayCalculator.computeNextUpdateAtMillis(
                nominal, createdAt, currentValue, floor,
                decayConfig.hourlyRatePercent.doubleValue(),
                decayConfig.displayUpdateThresholdPercent.doubleValue());

            if (nextUpdate == Long.MAX_VALUE) {
                pdc.remove(this.nextUpdateKey);
            }
            else {
                pdc.set(this.nextUpdateKey, PersistentDataType.LONG, nextUpdate);
            }
            nextUpdateHolder[0] = nextUpdate;
        });

        return nextUpdateHolder[0];
    }
}
