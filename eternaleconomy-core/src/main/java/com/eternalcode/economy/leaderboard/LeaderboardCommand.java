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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        UUID uuid = account.uuid();

        int entriesPerPage = this.pluginConfig.leaderboardEntriesPerPage;

        this.leaderboardService.getLeaderboard().thenAccept(leaderboard -> {
            List<Account> leaderboardList = new ArrayList<>(leaderboard);

            if (leaderboardList.isEmpty()) {
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardEmpty)
                    .player(uuid)
                    .send();
                return;
            }

            int totalPages = (int) Math.ceil((double) leaderboardList.size() / entriesPerPage);
            int finalPage = Math.max(1, Math.min(page, totalPages));

            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.leaderboardHeader)
                .placeholder("{PAGE}", String.valueOf(finalPage))
                .placeholder("{TOTAL_PAGES}", String.valueOf(totalPages))
                .player(uuid)
                .send();

            int startIndex = (finalPage - 1) * entriesPerPage;
            int endIndex = Math.min(startIndex + entriesPerPage, leaderboardList.size());
            List<Account> pageEntries = leaderboardList.subList(startIndex, endIndex);

            for (int i = 0; i < pageEntries.size(); i++) {
                Account topAccount = pageEntries.get(i);
                int position = startIndex + i + 1;

                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardEntry)
                    .placeholder("{POSITION}", String.valueOf(position))
                    .placeholder("{PLAYER}", topAccount.name())
                    .placeholder("{BALANCE}", this.decimalFormatter.format(topAccount.balance()))
                    .placeholder("{BALANCE_RAW}", String.valueOf(topAccount.balance()))
                    .player(uuid)
                    .send();
            }

            if (this.pluginConfig.showLeaderboardPosition) {
                this.leaderboardService.getLeaderboardPosition(account).thenAccept(leaderboardPosition ->
                    this.noticeService.create()
                        .notice(messageConfig -> messageConfig.player.leaderboardPosition)
                        .placeholder("{POSITION}", String.valueOf(leaderboardPosition.position()))
                        .player(uuid)
                        .send()
                );
            }

            if (finalPage < totalPages) {
                int nextPage = finalPage + 1;
                this.noticeService.create()
                    .notice(messageConfig -> messageConfig.player.leaderboardFooter)
                    .placeholder("{NEXT_PAGE}", String.valueOf(nextPage))
                    .placeholder("{TOTAL_PAGES}", String.valueOf(totalPages))
                    .placeholder("{PAGE}", String.valueOf(finalPage))
                    .player(uuid)
                    .send();
            }
        });
    }
}
