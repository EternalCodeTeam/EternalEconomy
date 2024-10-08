package com.eternalcode.economy;

import com.eternalcode.economy.config.ConfigService;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;
import org.bukkit.command.CommandSender;

@Command(name = "economy admin reload")
@Permission(EconomyPermissionConstant.ADMIN_RELOAD_PERMISSION)
public class EconomyReloadCommand {

    private static final Notice RELOADED = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Reloaded "
        + "EternalEconomy in <gradient:#00FFA2:#34AE00>{TIME}ms!</gradient></white>");

    private final ConfigService configService;
    private final NoticeService noticeService;

    public EconomyReloadCommand(ConfigService configService, NoticeService noticeService) {
        this.configService = configService;
        this.noticeService = noticeService;
    }

    @Execute
    @Async
    void execute(@Context CommandSender sender) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        this.configService.reload();

        Duration elapsed = stopwatch.elapsed();
        this.noticeService.create()
            .notice(RELOADED)
            .placeholder("{TIME}", String.valueOf(elapsed.toMillis()))
            .viewer(sender)
            .send();
    }
}
