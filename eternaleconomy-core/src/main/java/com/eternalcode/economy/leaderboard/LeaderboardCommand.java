package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.format.DurationFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import java.time.Instant;
import java.util.UUID;

@Command(name = "leaderboard")
@Permission(EconomyPermissionConstant.PLAYER_TOP_BALANCE_PERMISSION)
public class LeaderboardCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final DurationFormatter durationFormatter;
    private final LeaderboardService leaderboardService;
    private final PluginConfig pluginConfig;

    public LeaderboardCommand(
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        DurationFormatter durationFormatter,
        LeaderboardService leaderboardService,
        PluginConfig pluginConfig
    ) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.durationFormatter = durationFormatter;
        this.leaderboardService = leaderboardService;
        this.pluginConfig = pluginConfig;
    }

    @Execute
    void execute(@Context Account account) {
        UUID uuid = account.uuid();
        int position = 1;

        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.leaderboardHeader)
            .player(uuid)
            .send();

        for (Account topAccount : this.leaderboardService.getLeaderboard()) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.leaderboardEntry)
                .placeholder("{POSITION}", String.valueOf(position))
                .placeholder("{PLAYER}", topAccount.name())
                .placeholder("{BALANCE}", this.decimalFormatter.format(topAccount.balance()))
                .player(uuid)
                .send();

            position++;
        }

        if (this.pluginConfig.showLeaderboardPosition) {
            this.leaderboardService.getLeaderboardPosition(account).thenAccept(leaderboardPosition -> {
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardPosition)
                    .placeholder("{POSITION}", String.valueOf(leaderboardPosition.position()))
                    .player(uuid)
                    .send();
            });
        }

        if (this.pluginConfig.showLastLeaderboardUpdate) {
            Instant lastUpdated = this.leaderboardService.lastUpdated();
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.lastLeaderboardUpdate)
                .placeholder("{TIME}", this.durationFormatter.format(lastUpdated))
                .player(uuid)
                .send();
        }
    }
}
