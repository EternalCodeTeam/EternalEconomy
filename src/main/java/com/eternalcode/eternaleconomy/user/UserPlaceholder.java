package com.eternalcode.eternaleconomy.user;

import java.util.UUID;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserPlaceholder extends PlaceholderExpansion {

    private final UserMoneyService userMoneyService;
    private final PluginDescriptionFile pluginDescriptionFile;

    public UserPlaceholder(
        UserMoneyService userMoneyService,
        PluginDescriptionFile pluginDescriptionFile
    ) {
        this.userMoneyService = userMoneyService;
        this.pluginDescriptionFile = pluginDescriptionFile;
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
        if (player == null) {
            return "";
        }

        String[] args = params.split("_");
        if (args.length < 2) {
            return "";
        }

        String action = args[0];
        UUID uniqueId = player.getUniqueId();

        return switch (action) {
            case "balance" -> this.userMoneyService.getFormattedBalance(uniqueId);
            case "balance_raw" -> this.userMoneyService.getBalance(uniqueId).toString();
            default -> "";
        };
    }
}
