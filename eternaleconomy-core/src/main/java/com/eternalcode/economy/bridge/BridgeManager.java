package com.eternalcode.economy.bridge;

import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.bridge.placeholderapi.PlaceholderEconomyExpansion;
import com.eternalcode.economy.format.DecimalFormatter;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Logger;

public class BridgeManager {

    private final PluginDescriptionFile pluginDescriptionFile;

    private final AccountManager accountManager;
    private final DecimalFormatter decimalFormatter;

    private final Server server;
    private final Logger logger;

    public BridgeManager(
            PluginDescriptionFile pluginDescriptionFile,
            AccountManager accountManager,
            DecimalFormatter decimalFormatter,
            Server server,
            Logger logger
    ) {
        this.pluginDescriptionFile = pluginDescriptionFile;
        this.accountManager = accountManager;
        this.decimalFormatter = decimalFormatter;
        this.server = server;
        this.logger = logger;
    }

    public void init() {
        this.setupBridge("PlaceholderAPI", () -> {
            new PlaceholderEconomyExpansion(
                    this.pluginDescriptionFile,
                    this.accountManager,
                    this.decimalFormatter
            ).initialize();
        });
    }

    private void setupBridge(String pluginName, BridgeInitializer bridge) {
        PluginManager pluginManager = this.server.getPluginManager();

        if (pluginManager.isPluginEnabled(pluginName)) {
            bridge.initialize();

            this.logger.info("Successfully hooked into " + pluginName + " bridge!");
        }
    }

}