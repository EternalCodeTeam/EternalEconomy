package com.eternalcode.economy.leaderboard;

import com.eternalcode.commons.concurrent.FutureHandler;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.time.Duration;

public final class LeaderboardRefreshTask {

    private final LeaderboardService leaderboardService;
    private final Scheduler scheduler;
    private final PluginConfig pluginConfig;

    public LeaderboardRefreshTask(
        LeaderboardService leaderboardService,
        Scheduler scheduler,
        PluginConfig pluginConfig) {
        this.leaderboardService = leaderboardService;
        this.scheduler = scheduler;
        this.pluginConfig = pluginConfig;
    }

    public void start() {
        this.scheduler.timerAsync(
            () -> this.leaderboardService.refreshSnapshot()
                .exceptionally(FutureHandler::handleException),
            Duration.ZERO,
            this.pluginConfig.leaderboardRefreshInterval);
    }
}
