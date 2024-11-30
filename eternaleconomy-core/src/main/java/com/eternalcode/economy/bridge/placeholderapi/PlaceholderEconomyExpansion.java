package com.eternalcode.economy.bridge.placeholderapi;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.bridge.BridgeInitializer;
import com.eternalcode.economy.format.DecimalFormatter;
import java.util.UUID;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderEconomyExpansion extends PlaceholderExpansion implements BridgeInitializer {

    private final PluginDescriptionFile pluginDescriptionFile;
    private final AccountManager accountManager;
    private final DecimalFormatter decimalFormatter;

    public PlaceholderEconomyExpansion(
        PluginDescriptionFile pluginDescriptionFile,
        AccountManager accountManager,
        DecimalFormatter decimalFormatter
    ) {
        this.pluginDescriptionFile = pluginDescriptionFile;
        this.accountManager = accountManager;
        this.decimalFormatter = decimalFormatter;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.pluginDescriptionFile.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return this.pluginDescriptionFile.getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return this.pluginDescriptionFile.getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        UUID uniqueId = player.getUniqueId();

        Account account = this.accountManager.getAccount(uniqueId);

        if (account == null) {
            return "";
        }

        switch (params) {
            case "balance" -> {
                return String.format("%.2f", account.balance());
            }
            case "balance_formatted" -> {
                return this.decimalFormatter.format(account.balance());
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void initialize() {
        this.register();
    }
}
