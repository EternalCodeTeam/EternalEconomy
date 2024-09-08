package com.eternalcode.economy.command.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;

@Command(name = "economy admin balance")
@Permission(EconomyPermissionConstant.ADMIN_BALANCE_PERMISSION)
public class AdminBalanceCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;

    public AdminBalanceCommand(NoticeService noticeService, DecimalFormatter decimalFormatter) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
    }

    @Execute
    public void execute(@Context CommandSender sender, @Arg Account account) {
        this.noticeService.create()
                .notice(notice -> notice.admin.balance)
                .placeholder("{BALANCE}", this.decimalFormatter.format(account.balance()))
                .placeholder("{PLAYER}", account.name())
                .viewer(sender)
                .send();
    }
}
