package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WithdrawService {
    private final Server server;
    private final NoticeService noticeService;
    private final PluginConfig config;
    private final WithdrawItemService withdrawItemService;
    private final DecimalFormatter decimalFormatter;

    private final AccountPaymentService accountPaymentService;
    private final AccountManager accountManager;

    private final MiniMessage miniMessage;

    public WithdrawService(
        Server server,
        NoticeService noticeService,
        PluginConfig pluginConfig,
        DecimalFormatter decimalFormatter,
        WithdrawItemService withdrawItemService,
        AccountPaymentService accountPaymentService,
        AccountManager accountManager,
        MiniMessage miniMessage
    ) {
        this.server = server;
        this.noticeService = noticeService;
        this.config = pluginConfig;
        this.decimalFormatter = decimalFormatter;
        this.withdrawItemService = withdrawItemService;

        this.accountPaymentService = accountPaymentService;
        this.accountManager = accountManager;

        this.miniMessage = miniMessage;
    }

    public void setItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noItemHeld)
                .player(player.getUniqueId())
                .send();

            return;
        }

        this.config.currencyItem.item = item;

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String displayName = meta.getDisplayName();

            if (displayName.isEmpty()) {
                this.config.currencyItem.name = item.getType().name();
            }
            else {
                this.config.currencyItem.name = displayName;
            }
        }

        CompletableFuture.runAsync(this.config::save);

        noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.itemSet)
            .placeholder("{ITEM}", this.config.currencyItem.name)
            .player(player.getUniqueId())
            .send();
    }

    public void addBanknote(UUID uuid, BigDecimal value) {
        Player player = server.getPlayer(uuid);

        if (player == null) {
            return;
        }

        ItemStack item = withdrawItemService.markAsBanknote(setUpItem(value), value);
        player.getInventory().addItem(item);

        Account account = accountManager.getAccount(player.getUniqueId());
        accountPaymentService.removeBalance(account, value);

        noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.banknoteWithdrawn)
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
        }
        else if (itemInOffHand.isSimilar(item)) {
            activeItem = itemInOffHand;
        }
        else {
            noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noBanknoteInHand)
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
            .notice(messageConfig -> messageConfig.withdraw.banknoteRedeemed)
            .placeholder("{VALUE}", decimalFormatter.format(finalValue))
            .player(player.getUniqueId())
            .send();
    }

    private ItemStack setUpItem(BigDecimal value) {
        ItemStack item = this.config.currencyItem.item.clone();
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());

        String displayName = this.config.currencyItem.name
            .replace("{VALUE}", decimalFormatter.format(value));

        Component component = miniMessage.deserialize(displayName).decoration(TextDecoration.ITALIC, false);
        String legacyName = LegacyComponentSerializer.legacySection().serialize(component);

        meta.setDisplayName(legacyName);
        item.setItemMeta(meta);

        return item;
    }
}
