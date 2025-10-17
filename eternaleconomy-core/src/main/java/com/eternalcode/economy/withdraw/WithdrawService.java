package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawService {
    private final Server server;
    private final NoticeService noticeService;
    private final WithdrawItemService withdrawItemService;
    private final DecimalFormatter decimalFormatter;

    private final AccountPaymentService accountPaymentService;
    private final AccountManager accountManager;

    public WithdrawService(
        Server server,
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        WithdrawItemService withdrawItemService,
        AccountPaymentService accountPaymentService,
        AccountManager accountManager
    ) {
        this.server = server;
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.withdrawItemService = withdrawItemService;

        this.accountPaymentService = accountPaymentService;
        this.accountManager = accountManager;
    }

    public void addBanknote(UUID uuid, BigDecimal value) {
        Player player = server.getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noInventorySpace)
                .player(player.getUniqueId())
                .send();
            return;
        }

        ItemStack item = withdrawItemService.createBanknote(value);
        player.getInventory().addItem(item);

        Account account = accountManager.getAccount(player.getUniqueId());
        accountPaymentService.removeBalance(account, value);

        noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.banknoteWithdrawn)
            .placeholder("{VALUE}", decimalFormatter.format(value))
            .player(player.getUniqueId())
            .send();
    }

    public void redeem(Player player, ItemStack item, BigDecimal value, int amount) {
        if (item.getType() == Material.AIR) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noBanknoteInHand)
                .player(player.getUniqueId())
                .send();
            return;
        }

        item.setAmount(item.getAmount() - amount);

        BigDecimal finalValue = value.multiply(BigDecimal.valueOf(amount));

        Account account = accountManager.getAccount(player.getUniqueId());
        accountPaymentService.addBalance(account, finalValue);

        noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.banknoteRedeemed)
            .placeholder("{VALUE}", decimalFormatter.format(finalValue))
            .player(player.getUniqueId())
            .send();
    }
}
