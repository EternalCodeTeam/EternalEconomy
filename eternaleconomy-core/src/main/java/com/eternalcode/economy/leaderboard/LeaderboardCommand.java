package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.execute.ExecuteDefault;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.util.List;

@SuppressWarnings("unused")
@Command(name = "balancetop", aliases = {"baltop"})
@Permission(EconomyPermissionConstant.PLAYER_BALANCE_TOP_PERMISSION)
public class LeaderboardCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final LeaderboardService leaderboardService;
    private final PluginConfig pluginConfig;

    public LeaderboardCommand(
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

    @ExecuteDefault
    void executeDefault(@Context Account account) {
        this.execute(account, 1);
    }

    @Execute
    void execute(@Context Account account, @Arg("page") int page) {
        int entriesPerPage = this.pluginConfig.leaderboardEntriesPerPage;

        this.leaderboardService.getLeaderboardPage(page, entriesPerPage).thenAccept(leadboardPage -> {
            List<LeaderboardEntry> entries = leadboardPage.entries();

            if (entries.isEmpty()) {
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardEmpty)
                    .player(account.uuid())
                    .send();
                return;
            }

            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.leaderboardHeader)
                .placeholder("{PAGE}", String.valueOf(leadboardPage.currentPage()))
                .placeholder("{TOTAL_PAGES}", String.valueOf(leadboardPage.maxPages()))
                .player(account.uuid())
                .send();

            for (LeaderboardEntry entry : entries) {
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardEntry)
                    .placeholder("{POSITION}", String.valueOf(entry.position()))
                    .placeholder("{PLAYER}", entry.account().name())
                    .placeholder("{BALANCE}", this.decimalFormatter.format(entry.account().balance()))
                    .placeholder("{BALANCE_RAW}", String.valueOf(entry.account().balance()))
                    .player(account.uuid())
                    .send();
            }

            if (this.pluginConfig.showLeaderboardPosition) {
                this.leaderboardService.getLeaderboardPosition(account).thenAccept(leaderboardPosition ->
                    this.noticeService.create()
                        .notice(messageConfig -> messageConfig.player.leaderboardPosition)
                        .placeholder("{POSITION}", String.valueOf(leaderboardPosition.position()))
                        .player(account.uuid())
                        .send()
                );
            }

            if (leadboardPage.nextPage() != -1) {
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardFooter)
                    .placeholder("{NEXT_PAGE}", String.valueOf(leadboardPage.nextPage()))
                    .placeholder("{TOTAL_PAGES}", String.valueOf(leadboardPage.maxPages()))
                    .placeholder("{PAGE}", String.valueOf(leadboardPage.currentPage()))
                    .player(account.uuid())
                    .send();
            }
        });
    }
}
