package com.eternalcode.economy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.adventure.AdventureUrlPostProcessor;
import com.eternalcode.commons.bukkit.scheduler.BukkitSchedulerImpl;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.account.database.AccountRepositoryImpl;
import com.eternalcode.economy.config.ConfigService;
import com.eternalcode.economy.config.implementation.MessageConfig;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.database.DatabaseManager;
import com.eternalcode.economy.multification.NoticeService;
import com.google.common.base.Stopwatch;
import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEconomyPlugin extends JavaPlugin {

    private static final String PLUGIN_STARTED = "EternalEconomy has been enabled in %dms.";

    private AudienceProvider audienceProvider;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();

        this.audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new AdventureUrlPostProcessor())
                .postProcessor(new AdventureLegacyColorPostProcessor())
                .preProcessor(new AdventureLegacyColorPreProcessor())
                .build();

        File dataFolder = this.getDataFolder();

        MessageConfig messageConfig = new MessageConfig();
        PluginConfig pluginConfig = new PluginConfig();

        NoticeService noticeService = new NoticeService(messageConfig, this.audienceProvider, miniMessage);
        ConfigService configService = new ConfigService(noticeService.getNoticeRegistry());
        configService.create(messageConfig.getClass(), new File(dataFolder, "messages.yml"));
        configService.create(pluginConfig.getClass(), new File(dataFolder, "config.yml"));

        Scheduler scheduler = new BukkitSchedulerImpl(this);

        this.databaseManager = new DatabaseManager(this.getLogger(), dataFolder, pluginConfig.database);
        try {
            AccountRepository accountRepository = new AccountRepositoryImpl(this.databaseManager, scheduler);
        }
        catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        Duration elapsed = started.elapsed();
        server.getLogger().info(String.format(PLUGIN_STARTED, elapsed.toMillis()));
    }

    @Override
    public void onDisable() {
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }

        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
    }
}
