package com.eternalcode.economy.vault;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.DecimalFormatter;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class VaultEconomyProvider extends VaultEconomyAdapter {

    private final Plugin plugin;
    private final DecimalFormatter formatter;

    private final AccountPaymentService accountPaymentService;
    private final AccountManager accountManager;

    public VaultEconomyProvider(
            Plugin plugin,
            DecimalFormatter formatter,
            AccountPaymentService accountPaymentService,
            AccountManager accountManager
    ) {
        this.plugin = plugin;
        this.formatter = formatter;
        this.accountPaymentService = accountPaymentService;
        this.accountManager = accountManager;
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.isEnabled();
    }

    @Override
    public String getName() {
        return this.plugin.getName();
    }

    @Override
    public String format(double value) {
        return this.formatter.format(value);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        return account != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return this.hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        return account.balance().doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return this.getBalance(offlinePlayer);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        return account.balance().doubleValue() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        return account.balance().doubleValue() >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        this.accountPaymentService.removeBalance(account, BigDecimal.valueOf(amount));
        return new EconomyResponse(amount, account.balance().doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return this.withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        this.accountPaymentService.addBalance(account, BigDecimal.valueOf(amount));
        return new EconomyResponse(amount, account.balance().doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    // TODO: Zwróćcie uwage na te metody w Review, nie wiem czy to ma sens?
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        Account account = this.accountManager.create(offlinePlayer.getUniqueId(), offlinePlayer.getName());

        return account != null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
        return this.createPlayerAccount(offlinePlayer);
    }
}
