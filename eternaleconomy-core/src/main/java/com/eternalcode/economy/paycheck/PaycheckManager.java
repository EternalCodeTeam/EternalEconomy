package com.eternalcode.economy.paycheck;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
    private final DecimalFormatter decimalFormatter;

    private final AccountPaymentService accountPaymentService;
    private final AccountManager accountManager;

    private final MiniMessage mm;

    public PaycheckManager(
        NoticeService noticeService,
        PluginConfig pluginConfig,
        DecimalFormatter decimalFormatter,
        PaycheckTagger paycheckTagger,
        AccountPaymentService accountPaymentService,
        AccountManager accountManager
    ) {
        this.noticeService = noticeService;
        this.config = pluginConfig;
        this.decimalFormatter = decimalFormatter;
        this.paycheckTagger = paycheckTagger;

        this.accountPaymentService = accountPaymentService;
        this.accountManager = accountManager;

        this.mm = MiniMessage.miniMessage();
    }

    public void setItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.paycheck.noItem)
                .player(player.getUniqueId())
                .send();

            return;
        }

        this.config.currencyItem.item = item;

        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.isEmpty()) {
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
        if (player == null) {
            return;
        }

        ItemStack item = paycheckTagger.tagItem(setUpItem(value), value);
        player.getInventory().addItem(item);
        player.updateInventory();

        Account account = accountManager.getAccount(player.getUniqueId());
        accountPaymentService.removeBalance(account, value);

        noticeService.create()
            .notice(messageConfig -> messageConfig.paycheck.withdraw)
            .placeholder("{VALUE}", decimalFormatter.format(value))
            .player(player.getUniqueId())
            .send();
    }

    public void redeem(Player player, ItemStack item, BigDecimal value) {
        ItemStack activeItem;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        if (itemInHand.isSimilar(item)) {
            activeItem = itemInHand;
        } else if (itemInOffHand.isSimilar(item)) {
            activeItem = itemInOffHand;
        } else {
            noticeService.create()
                .notice(messageConfig -> messageConfig.paycheck.noCheck)
                .player(player.getUniqueId())
                .send();
            return;
        }

        player.getInventory().removeItem(activeItem);
        activeItem.setAmount(activeItem.getAmount() - item.getAmount());
        player.getInventory().addItem(activeItem);
        player.updateInventory();

        BigDecimal finalValue = value.multiply(BigDecimal.valueOf(item.getAmount()));

        Account account = accountManager.getAccount(player.getUniqueId());
        accountPaymentService.addBalance(account, finalValue);

        noticeService.create()
            .notice(messageConfig -> messageConfig.paycheck.redeem)
            .placeholder("{VALUE}", decimalFormatter.format(finalValue))
            .player(player.getUniqueId())
            .send();
    }

    private ItemStack setUpItem(BigDecimal value) {
        ItemStack item = this.config.currencyItem.item;
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());

        String displayName = this.config.currencyItem.name
            .replace("{VALUE}", decimalFormatter.format(value));

        Component component = mm.deserialize(displayName).decoration(TextDecoration.ITALIC, false);
        String legacyName = LegacyComponentSerializer.legacySection().serialize(component);

        meta.setDisplayName(legacyName);
        item.setItemMeta(meta);

        return item;
    }
}
