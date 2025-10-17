package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.item.ConfigItem;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WithdrawItemServiceImpl implements WithdrawItemService {

    private final PluginConfig pluginConfig;
    private final DecimalFormatter moneyFormatter;
    private final MiniMessage miniMessage;
    private final NamespacedKey banknoteValueKey;
    private final NoticeService noticeService;

    public WithdrawItemServiceImpl(
        Plugin plugin,
        PluginConfig pluginConfig,
        DecimalFormatter moneyFormatter,
        NoticeService noticeService,
        MiniMessage miniMessage
    ) {
        this.banknoteValueKey = new NamespacedKey(plugin, "withdraw_value");
        this.pluginConfig = pluginConfig;
        this.moneyFormatter = moneyFormatter;
        this.miniMessage = miniMessage;
        this.noticeService = noticeService;
    }

    @Override
    public ItemStack createBanknote(BigDecimal value) {
        ItemStack banknoteItem = createBaseBanknoteItem(value);
        return attachBanknoteValue(banknoteItem, value);
    }

    @Override
    public boolean isBanknote(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(banknoteValueKey, PersistentDataType.STRING);
    }

    @Override
    public BigDecimal getValue(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return BigDecimal.ZERO;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        String storedValue = itemMeta.getPersistentDataContainer()
            .get(banknoteValueKey, PersistentDataType.STRING);
        return storedValue != null ? new BigDecimal(storedValue) : BigDecimal.ZERO;
    }

    @Override
    public void setItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noItemInHand)
                .player(player.getUniqueId())
                .send();

            return;
        }

        Component displayName = Objects.requireNonNull(item.getItemMeta()).displayName();

        if (displayName != null) {
            this.pluginConfig.currencyItem.item.name = PlainTextComponentSerializer.plainText().serialize(displayName);
        }
        else {
            this.pluginConfig.currencyItem.item.name = item.getType().name();
        }

        this.pluginConfig.currencyItem.item.glow = item.getItemMeta().hasEnchants();
        this.pluginConfig.currencyItem.item.lore = Objects.requireNonNullElse(item.getItemMeta().lore(), List.<Component>of())
            .stream()
            .map(miniMessage::serialize)
            .toList();

        this.pluginConfig.currencyItem.item.material = item.getType();
        this.pluginConfig.currencyItem.item.texture = item.getItemMeta().hasCustomModelData() ?
            item.getItemMeta().getCustomModelData() : null;

        CompletableFuture.runAsync(this.pluginConfig::save);

        noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.itemSetSuccess)
            .placeholder("{ITEM}", this.pluginConfig.currencyItem.item.name)
            .player(player.getUniqueId())
            .send();
    }

    private ItemStack createBaseBanknoteItem(BigDecimal value) {
        ConfigItem configItem = this.pluginConfig.currencyItem.item;

        ItemStack banknoteItem = new ItemStack(configItem.material());

        banknoteItem.editMeta(meta -> {
            meta.setCustomModelData(configItem.texture);
            meta.displayName(miniMessage.deserialize(configItem.name.replace("{VALUE}", moneyFormatter.format(value)), TagResolver.empty()));
            meta.lore(configItem.lore.stream()
                .map(line -> miniMessage.deserialize(line.replace("{VALUE}", moneyFormatter.format(value)),
                    TagResolver.empty()))
                .toList());
        });

        if (configItem.glow) {
            banknoteItem.editMeta(meta -> {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            });
        }

        return banknoteItem;
    }

    private ItemStack attachBanknoteValue(ItemStack banknoteItem, BigDecimal value) {
        ItemStack taggedItem = banknoteItem.clone();
        taggedItem.editMeta(meta ->
            meta.getPersistentDataContainer().set(banknoteValueKey, PersistentDataType.STRING, value.toPlainString())
        );
        return taggedItem;
    }
}
