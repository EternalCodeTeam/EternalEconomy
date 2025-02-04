package com.eternalcode.economy.command.player;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.format.DurationFormatterUtil;
import com.eternalcode.economy.leaderboard.LeaderboardService;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import java.time.Instant;
import java.util.UUID;

@Command(name = "topbalance", aliases = "topbal")
@Permission(EconomyPermissionConstant.PLAYER_TOP_BALANCE_PERMISSION)
public class TopBalanceCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final LeaderboardService leaderboardService;
    private final PluginConfig pluginConfig;

    public TopBalanceCommand(
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        LeaderboardService leaderboardService,
        PluginConfig pluginConfig
    ) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.leaderboardService = leaderboardService;
        this.pluginConfig = pluginConfig;
    }

    @Execute
    void execute(@Context Account account) {
        UUID uuid = account.uuid();
        int position = 1;

        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.topBalanceHeader)
            .player(uuid)
            .send();

        for (Account topAccount : this.leaderboardService.getTopAccounts()) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.topBalanceEntry)
                .placeholder("{POSITION}", String.valueOf(position))
                .placeholder("{PLAYER}", topAccount.name())
                .placeholder("{BALANCE}", this.decimalFormatter.format(topAccount.balance()))
                .player(uuid)
                .send();

            position++;
        }

        if (this.pluginConfig.showLastLeaderboardUpdate) {
            Instant lastUpdated = this.leaderboardService.lastUpdated();
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.lastLeaderboardUpdate)
                .placeholder("{TIME}", DurationFormatterUtil.format(lastUpdated))
                .player(uuid)
                .send();
        }
    }
}
