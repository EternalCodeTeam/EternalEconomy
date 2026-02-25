package com.eternalcode.economy.bridge;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.bridge.placeholderapi.PlaceholderEconomyExpansion;
import com.eternalcode.economy.format.DecimalFormatter;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

public class BridgeManager {

    private final PluginMeta pluginMeta;

    private final AccountManager accountManager;
    private final DecimalFormatter decimalFormatter;

    private final Server server;
    private final Logger logger;
    private final Scheduler scheduler;

    public BridgeManager(
        PluginMeta pluginMeta,
        AccountManager accountManager,
        DecimalFormatter decimalFormatter,
        Server server,
        Logger logger,
        Scheduler scheduler
    ) {
        this.pluginMeta = pluginMeta;
        this.accountManager = accountManager;
        this.decimalFormatter = decimalFormatter;
        this.server = server;
        this.logger = logger;
        this.scheduler = scheduler;
    }

    public void init() {
        // Using "load: STARTUP" in plugin.yml causes the plugin to load before
        // PlaceholderAPI.
        // Therefore, we need to delay the bridge initialization until the server is
        // fully started.
        // The scheduler runs the code after the "Done" message, ensuring the server is
        // fully operational.
        this.scheduler.run(
            () -> {
                this.setupBridge(
                    "PlaceholderAPI", () -> {
                        PlaceholderEconomyExpansion placeholderEconomyExpansion = new PlaceholderEconomyExpansion(
                            this.pluginMeta,
                            this.accountManager,
                            this.decimalFormatter);

                        placeholderEconomyExpansion.register();
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
