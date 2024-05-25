package com.eternalcode.eternaleconomy;

import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class EconomyProvider implements Economy {

    private final UserService userService;
    private final Server server;

    public EconomyProvider(UserService userService, Server server) {
        this.userService = userService;
        this.server = server;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "eternaleconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return Double.toString(amount); // TODO: Format the amount
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    @Override
    public double getBalance(String playerName) {
        return this.userService.getUser(playerName)
            .map(user -> user.balance.doubleValue())
            .orElse(0.0);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.userService.getUser(player.getUniqueId())
            .map(user -> user.balance.doubleValue())
            .orElse(0.0);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return this.userService.getUser(playerName)
            .map(user -> user.balance.doubleValue())
            .orElse(0.0);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.userService.getUser(player.getUniqueId())
            .map(user -> user.balance.doubleValue())
            .orElse(0.0);
    }

    @Override
    public boolean has(String playerName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return false;
        }

        return user.get().balance.doubleValue() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return false;
        }

        return user.get().balance.doubleValue() >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return false;
        }

        return user.get().balance.doubleValue() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return false;
        }

        return user.get().balance.doubleValue() >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().removeBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().removeBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().removeBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().removeBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().addBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().addBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(playerName);

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().addBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        Optional<User> user = this.userService.getUser(player.getUniqueId());

        if (user.isEmpty()) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        user.get().addBalance(BigDecimal.valueOf(amount));
        this.userService.saveUser(user.get());

        return new EconomyResponse(amount, user.get().balance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented yet");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return this.userService.getUser(playerName).isPresent();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return this.userService.getUser(player.getUniqueId()).isPresent();
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return this.userService.getUser(playerName).isPresent();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return this.userService.getUser(player.getUniqueId()).isPresent();
    }
}
