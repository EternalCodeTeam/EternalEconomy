package com.eternalcode.eternaleconomy.eco;

import com.eternalcode.eternaleconomy.EternalEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class VaultImpl implements Economy {
    private final EternalEconomy plugin;

    public VaultImpl(EternalEconomy plugin) {
        this.plugin = plugin;
    }
    public boolean isEnabled() {
        return plugin != null;
    }
    public String getName() {
        return "EternalEconomy";
    }
    public String currencyNamePlural() {
        return "$";
    }

    public String currencyNameSingular() {
        return "$";
    }
    public boolean hasBankSupport() {
        return false;
    }
    public int fractionalDigits() {
        return -1;
    }
    public String format(double v) {
        BigDecimal bd = (new BigDecimal(v)).setScale(2, RoundingMode.HALF_EVEN);
        return String.valueOf(bd.doubleValue());
    }
    public boolean createPlayerAccount(String name) {
        return this.createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public boolean createPlayerAccount(OfflinePlayer player) {
        return this.createAccount(player.getUniqueId());
    }

    public boolean createPlayerAccount(String name, String world) {
        return this.createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return this.createAccount(player.getUniqueId());
    }
    private boolean createAccount(UUID uuid) {
        return EternalEconomy.getEconomy().createAccount(uuid);
    }
    public boolean hasAccount(String name) {
        return this.hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public boolean hasAccount(OfflinePlayer player) {
        return this.hasAccount(player.getUniqueId());
    }

    public boolean hasAccount(String name, String world) {
        return this.hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public boolean hasAccount(OfflinePlayer player, String world) {
        return this.hasAccount(player.getUniqueId());
    }
    private boolean hasAccount(UUID uuid) {
        return EternalEconomy.getEconomy().hasAccount(uuid);
    }
    public double getBalance(String name) {
        return this.getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public double getBalance(OfflinePlayer player) {
        return this.getBalance(player.getUniqueId());
    }

    public double getBalance(String name, String world) {
        return this.getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player.getUniqueId());
    }
    public boolean has(String name, double amount) {
        return this.has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public boolean has(OfflinePlayer player, double amount) {
        return this.has(player.getUniqueId(), amount);
    }

    public boolean has(String name, String world, double amount) {
        return this.has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public boolean has(OfflinePlayer player, String world, double amount) {
        return this.has(player.getUniqueId(), amount);
    }

    private boolean has(UUID uuid, double amount) {
        return EternalEconomy.getEconomy().has(uuid, amount);
    }

    private double getBalance(UUID uuid) {
        return EternalEconomy.getEconomy().getBalance(uuid).getBalance();

    }
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return this.withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdraw(player.getUniqueId(), amount);
    }

    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return this.withdraw(player.getUniqueId(), amount);
    }

    private EconomyResponse withdraw(UUID uuid, double amount) {
        return !EternalEconomy.getEconomy().withdraw(uuid, amount) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw funds.") : new EconomyResponse(amount, this.getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, "");
    }
    public EconomyResponse depositPlayer(String name, double amount) {
        return this.deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.deposit(player.getUniqueId(), amount);
    }

    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return this.deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return this.deposit(player.getUniqueId(), amount);
    }

    private EconomyResponse deposit(UUID uuid, double amount) {
        return !EternalEconomy.getEconomy().deposit(uuid, amount) ? new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Failed to deposit funds.") : new EconomyResponse(amount, this.getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, "");
    }
    public List<String> getBanks() {
        return null;
    }

    public EconomyResponse isBankMember(String arg0, String arg1) {
        return null;
    }

    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
        return null;
    }

    public EconomyResponse isBankOwner(String arg0, String arg1) {
        return null;
    }

    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
        return null;
    }

    public EconomyResponse bankBalance(String arg0) {
        return null;
    }

    public EconomyResponse bankDeposit(String arg0, double arg1) {
        return null;
    }

    public EconomyResponse bankHas(String arg0, double arg1) {
        return null;
    }

    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        return null;
    }

    public EconomyResponse createBank(String arg0, String arg1) {
        return null;
    }

    public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
        return null;
    }

    public EconomyResponse deleteBank(String arg0) {
        return null;
    }

}