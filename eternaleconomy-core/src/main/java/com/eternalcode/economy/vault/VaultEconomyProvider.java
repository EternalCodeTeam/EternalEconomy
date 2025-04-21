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
    public boolean hasAccount(OfflinePlayer player) {
        return getPlayerAccount(player) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        Account account = getPlayerAccount(player);
        return account != null ? account.balance().doubleValue() : 0.0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        Account account = getPlayerAccount(player);
        return account != null && account.balance().doubleValue() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String world, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Account account = getOrCreatePlayerAccount(player);

        if (account == null) {
            return createFailureResponse();
        }

        this.accountPaymentService.removeBalance(account, BigDecimal.valueOf(amount));
        return createSuccessResponse(amount, account.balance().doubleValue());
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Account account = getOrCreatePlayerAccount(player);

        if (account == null) {
            return createFailureResponse();
        }

        this.accountPaymentService.addBalance(account, BigDecimal.valueOf(amount));
        return createSuccessResponse(amount, account.balance().doubleValue());
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return getOrCreatePlayerAccount(player) != null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return createPlayerAccount(player);
    }

    private Account getPlayerAccount(OfflinePlayer player) {
        return this.accountManager.getAccount(player.getUniqueId());
    }

    private Account getOrCreatePlayerAccount(OfflinePlayer player) {
        return this.accountManager.getOrCreate(player.getUniqueId(), player.getName());
    }

    private EconomyResponse createSuccessResponse(double amount, double balance) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
    }

    private EconomyResponse createFailureResponse() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Could not access player account");
    }
}
