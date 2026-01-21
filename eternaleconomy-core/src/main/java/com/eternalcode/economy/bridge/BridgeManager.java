package com.eternalcode.economy.bridge;

import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.bridge.placeholderapi.PlaceholderEconomyExpansion;
import com.eternalcode.economy.format.DecimalFormatter;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class BridgeManager {

    private final PluginMeta pluginMeta;

    private final AccountManager accountManager;
    private final DecimalFormatter decimalFormatter;

    private final Server server;
    private final Plugin plugin;
    private final Logger logger;

    public BridgeManager(
        PluginMeta pluginMeta,
        AccountManager accountManager,
        DecimalFormatter decimalFormatter,
        Server server,
        Plugin plugin,
        Logger logger
    ) {
        this.pluginMeta = pluginMeta;
        this.accountManager = accountManager;
        this.decimalFormatter = decimalFormatter;
        this.server = server;
        this.plugin = plugin;
        this.logger = logger;
    }

    public void init() {
        // Using "load: STARTUP" in plugin.yml causes the plugin to load before PlaceholderAPI.
        // Therefore, we need to delay the bridge initialization until the server is fully started.
        // The scheduler runs the code after the "Done" message, ensuring the server is fully operational.
        Bukkit.getScheduler().runTask(
            this.plugin, () -> {
                this.setupBridge(
                    "PlaceholderAPI", () -> {
                        PlaceholderEconomyExpansion placeholderEconomyExpansion = new PlaceholderEconomyExpansion(
                            this.pluginMeta,
                            this.accountManager,
                            this.decimalFormatter
                        );

                        placeholderEconomyExpansion.register();

                        System.out.println("PlaceholderAPI bridge initialized!");
                    });
            });

        // other bridges (do not put bridges in the scheduler if not needed)
    }

    private void setupBridge(String pluginName, BridgeInitializer bridge) {
        PluginManager pluginManager = this.server.getPluginManager();

        if (pluginManager.isPluginEnabled(pluginName)) {
            bridge.initialize();

            this.logger.info("Successfully hooked into " + pluginName + " bridge!");
        }
    }
}
