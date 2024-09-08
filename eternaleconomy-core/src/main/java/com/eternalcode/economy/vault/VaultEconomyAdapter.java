package com.eternalcode.economy.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;

/**
 * Vault has many deprecated and unusable methods in the Economy interface.
 * This is for cleanup purposes, ensuring that the {@link VaultEconomyProvider}
 * only includes methods that are actually used in our plugin.
 **/
abstract class VaultEconomyAdapter implements Economy {

    private static final String BANK_NOT_SUPPORTED_MESSAGE = "EternalEconomy does not support bank accounts!";

    @Deprecated
    @Override
    public boolean hasAccount(String playerName) {
        return this.hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Deprecated
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Deprecated
    @Override
    public double getBalance(String playerName) {
        return this.getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Deprecated
    @Override
    public double getBalance(String playerName, String world) {
        return this.getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Deprecated
    @Override
    public boolean has(String playerName, double amount) {
        return this.has(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Deprecated
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.has(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return this.withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName) {
        return this.createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return this.createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Deprecated
    @Override
    public EconomyResponse createBank(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Deprecated
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Deprecated
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return this.depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, BANK_NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }
}