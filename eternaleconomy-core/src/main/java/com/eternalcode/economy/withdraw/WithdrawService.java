package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawService {

    private static final int MINIMUM_AMOUNT = 1;

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
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Banknote value must be positive, got: " + value);
        }

        Player player = this.server.getPlayer(uuid);
        if (player == null) {
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noInventorySpace)
                .player(player.getUniqueId())
                .send();
            return;
        }

        ItemStack banknote = this.withdrawItemService.createBanknote(value);
        player.getInventory().addItem(banknote);

        Account account = this.accountManager.getAccount(player.getUniqueId());
        this.accountPaymentService.removeBalance(account, value);

        this.noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.banknoteWithdrawn)
            .placeholder("{VALUE}", this.decimalFormatter.format(value))
            .player(player.getUniqueId())
            .send();
    }

    public void redeem(Player player, ItemStack item, BigDecimal value, int amount) {
        if (amount < MINIMUM_AMOUNT) {
            throw new IllegalArgumentException("Amount must be at least " + MINIMUM_AMOUNT + ", got: " + amount);
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Banknote value must be positive, got: " + value);
        }

        if (item.getType() == Material.AIR) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.noBanknoteInHand)
                .player(player.getUniqueId())
                .send();
            return;
        }

        if (item.getAmount() < amount) {
            throw new IllegalArgumentException(
                "Cannot redeem " + amount + " items when only " + item.getAmount() + " are available"
            );
        }

        item.setAmount(item.getAmount() - amount);

        BigDecimal totalValue = value.multiply(BigDecimal.valueOf(amount));

        Account account = this.accountManager.getAccount(player.getUniqueId());
        this.accountPaymentService.addBalance(account, totalValue);

        this.noticeService.create()
            .notice(messageConfig -> messageConfig.withdraw.banknoteRedeemed)
            .placeholder("{VALUE}", this.decimalFormatter.format(totalValue))
            .player(player.getUniqueId())
            .send();
    }
}
