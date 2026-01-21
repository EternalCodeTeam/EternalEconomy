package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.leaderboard.menu.LeaderboardMenu;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.execute.ExecuteDefault;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@Command(name = "balancetop", aliases = {"baltop", "btgui", "topgui"})
@Permission(EconomyPermissionConstant.PLAYER_BALANCE_TOP_PERMISSION)
public class LeaderboardCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final LeaderboardService leaderboardService;
    private final PluginConfig pluginConfig;
    private final LeaderboardMenu leaderboardMenu;

    public LeaderboardCommand(
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        LeaderboardService leaderboardService,
        PluginConfig pluginConfig,
        LeaderboardMenu leaderboardMenu
    ) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.leaderboardService = leaderboardService;
        this.pluginConfig = pluginConfig;
        this.leaderboardMenu = leaderboardMenu;
    }

    @ExecuteDefault
    void executeDefault(@Context Account account) {
        this.execute(account, 1);
    }

    @Execute
    void execute(@Context Account account, @Min(1) @Arg("page") int page) {
        if (this.pluginConfig.showLeaderboardGui) {
            Player bukkitPlayer = Bukkit.getPlayer(account.uuid());

            if (bukkitPlayer != null) {
                this.leaderboardMenu.open(bukkitPlayer, page);
                return;
            }
        }

        this.leaderboardService.getLeaderboardPage(page - 1, this.pluginConfig.leaderboardPageSize)
            .thenAccept(leaderboardPage -> showPage(account, leaderboardPage));
    }

    private void showPage(Account account, LeaderboardPage page) {
        int currentPage = page.currentPage() + 1;
        List<LeaderboardEntry> entries = page.entries();

        if (entries.isEmpty()) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.leaderboardEmpty)
                .player(account.uuid())
                .send();
            return;
        }

        this.noticeService.create()
            .notice(messages -> messages.player.leaderboardHeader)
            .placeholder("{PAGE}", String.valueOf(currentPage))
            .placeholder("{TOTAL_PAGES}", String.valueOf(page.maxPages()))
            .player(account.uuid())
            .send();

        for (LeaderboardEntry entry : entries) {
            this.noticeService.create()
                .notice(messages -> messages.player.leaderboardEntry)
                .placeholder("{POSITION}", String.valueOf(entry.position()))
                .placeholder("{PLAYER}", entry.account().name())
                .placeholder("{BALANCE}", this.decimalFormatter.format(entry.account().balance()))
                .placeholder("{BALANCE_RAW}", String.valueOf(entry.account().balance()))
                .player(account.uuid())
                .send();
        }

        if (this.pluginConfig.showLeaderboardPosition) {
            this.leaderboardService.getLeaderboardPosition(account)
                .thenAccept(entry -> {
                    this.noticeService.create()
                        .notice(messageConfig -> messageConfig.player.leaderboardPosition)
                        .placeholder("{POSITION}", String.valueOf(entry.position()))
                        .player(account.uuid())
                        .send();
                });
        }

        if (page.nextPage() != -1) {
            this.noticeService.create()
                .notice(messages -> messages.player.leaderboardFooter)
                .placeholder("{NEXT_PAGE}", String.valueOf(page.nextPage() + 1))
                .placeholder("{TOTAL_PAGES}", String.valueOf(page.maxPages()))
                .placeholder("{PAGE}", String.valueOf(currentPage))
                .player(account.uuid())
                .send();
        }
    }
}
