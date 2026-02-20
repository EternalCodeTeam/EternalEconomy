package com.eternalcode.economy.leaderboard;

import com.eternalcode.commons.bukkit.scheduler.BukkitSchedulerImpl;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.config.ConfigService;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.leaderboard.menu.LeaderboardConfig;
import com.eternalcode.economy.leaderboard.menu.LeaderboardMenu;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import dev.rollczi.liteskullapi.SkullAPI;
import java.io.File;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class LeaderboardConfigurer {

    public void configure(
        Plugin plugin,
        LeaderboardService leaderboardService,
        Scheduler scheduler,
        ConfigService configService,
        PluginConfig pluginConfig,
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        SkullAPI skullAPI,
        LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> liteCommandsBuilder
    ) {

        LeaderboardRefreshTask refreshTask = new LeaderboardRefreshTask(
            leaderboardService, scheduler,
            pluginConfig
        );
        refreshTask.start();

        LeaderboardConfig leaderboardConfig = configService.create(
            LeaderboardConfig.class,
            new File(plugin.getDataFolder(), "baltop-gui.yml")
        );

        Scheduler bukkitScheduler = new BukkitSchedulerImpl(plugin);

        LeaderboardMenu leaderboardMenu = new LeaderboardMenu(
            leaderboardConfig,
            bukkitScheduler,
            leaderboardService,
            decimalFormatter,
            skullAPI
        );

        liteCommandsBuilder.commands(
            new LeaderboardCommand(
                noticeService,
                decimalFormatter,
                leaderboardService,
                pluginConfig,
                leaderboardMenu
            )
        );
    }
}
