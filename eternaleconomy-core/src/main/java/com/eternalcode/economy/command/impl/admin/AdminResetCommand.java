package com.eternalcode.economy.command.impl.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;

@Command(name = "economy reset", aliases = "eco reset")
@Permission(EconomyPermissionConstant.ADMIN_RESET_PERMISSION)
public class AdminResetCommand {

    private final AccountPaymentService accountPaymentService;
    private final NoticeService noticeService;

    public AdminResetCommand(
        AccountPaymentService accountPaymentService,
        NoticeService noticeService
    ) {
        this.accountPaymentService = accountPaymentService;
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver) {
        this.accountPaymentService.resetBalance(receiver);

        this.noticeService.create()
            .notice(notice -> notice.admin.reset)
            .placeholder("{PLAYER}", receiver.name())
            .viewer(sender)
            .send();

        this.noticeService.create()
            .notice(notice -> notice.player.reset)
            .placeholder("{PLAYER}", receiver.name())
            .player(receiver.uuid())
            .send();
    }
}
