package com.eternalcode.economy.paycheck;

import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.multification.NoticeService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class PaycheckManager {
    private final NoticeService noticeService;
    private final PluginConfig config;
    private final PaycheckTagger paycheckTagger;
    private final MiniMessage mm;

    public PaycheckManager(
        NoticeService noticeService,
        PluginConfig pluginConfig,
        PaycheckTagger paycheckTagger
    ) {
        this.noticeService = noticeService;
        this.config = pluginConfig;
        this.paycheckTagger = paycheckTagger;
        this.mm = MiniMessage.miniMessage();
    }

    public void setItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.paycheck.noItem)
                .player(player.getUniqueId())
                .send();

            return;
        }

        this.config.currencyItem.item = item;


        String displayName = item.getItemMeta().getDisplayName();
        if(displayName.isEmpty()) {
            this.config.currencyItem.name = item.getType().name();
        } else {
            this.config.currencyItem.name = displayName;
        }

        this.config.save();

        noticeService.create()
            .notice(messageConfig -> messageConfig.paycheck.setItem)
            .placeholder("{ITEM}", this.config.currencyItem.name)
            .player(player.getUniqueId())
            .send();
    }

    public void givePaycheck(UUID uuid, BigDecimal value) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) {
            return;
        }

        ItemStack item = this.config.currencyItem.item;
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());

        String displayName = this.config.currencyItem.name
            .replace("{VALUE}", value.toString());

        Component component = mm.deserialize(displayName);
        String legacyName = LegacyComponentSerializer.legacySection().serialize(component);

        meta.setDisplayName(legacyName);
        item.setItemMeta(meta);

        item = paycheckTagger.tagItem(item, value);
        player.getInventory().addItem(item);

        noticeService.create()
            .notice(messageConfig -> messageConfig.paycheck.withdraw)
            .placeholder("{VALUE}", value.toString())
            .player(player.getUniqueId())
            .send();
    }
}
