package com.eternalcode.economy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.adventure.AdventureUrlPostProcessor;
import com.eternalcode.economy.config.ConfigService;
import com.eternalcode.economy.config.implementation.MessageConfig;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.multification.NoticeService;
import com.google.common.base.Stopwatch;
import java.io.File;
import java.time.Duration;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEconomyPlugin extends JavaPlugin {

    private static final String PLUGIN_STARTED = "EternalEconomy has been enabled in %dms.";

    private AudienceProvider audienceProvider;

    @Override
    public void onDisable() {
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

        Duration elapsed = started.elapsed();
        server.getLogger().info(String.format(PLUGIN_STARTED, elapsed.toMillis()));
    }

    @Override
    public void onEnable() {
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }
    }
}
