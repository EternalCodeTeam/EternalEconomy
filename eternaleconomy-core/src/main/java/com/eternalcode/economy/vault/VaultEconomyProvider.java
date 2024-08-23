package com.eternalcode.economy.vault;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.EconomyFormatter;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class VaultEconomyProvider extends VaultEconomyAdapter {

    private final Plugin plugin;
    private final EconomyFormatter formatter;

    private final AccountManager accountManager;
    private final AccountPaymentService accountPaymentService;

    public VaultEconomyProvider(
            Plugin plugin,
            EconomyFormatter formatter,
            AccountManager accountManager,
            AccountPaymentService accountPaymentService
    ) {
        this.plugin = plugin;
        this.formatter = formatter;
        this.accountManager = accountManager;
        this.accountPaymentService = accountPaymentService;
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

        EconomyResponse.ResponseType responseType = Optional.of(account)
                .filter(balance -> {
                    BigDecimal decimal = BigDecimal.valueOf(amount);
                    return this.accountPaymentService.removeBalance(balance, decimal);
                })
                .map(player -> EconomyResponse.ResponseType.SUCCESS)
                .orElse(EconomyResponse.ResponseType.FAILURE);

        return new EconomyResponse(amount, account.balance().doubleValue(), responseType, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return this.withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        Account account = this.accountManager.getAccount(offlinePlayer.getUniqueId());

        EconomyResponse.ResponseType responseType = Optional.of(account)
                .filter(balance -> {
                    BigDecimal decimal = BigDecimal.valueOf(amount);
                    return this.accountPaymentService.addBalance(balance, decimal);
                })
                .map(player -> EconomyResponse.ResponseType.SUCCESS)
                .orElse(EconomyResponse.ResponseType.FAILURE);

        return new EconomyResponse(amount, account.balance().doubleValue(), responseType, "");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
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
