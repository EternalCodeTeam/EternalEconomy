package com.eternalcode.economy.command.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

@Command(name = "economy admin set")
@Permission(EconomyPermissionConstant.ADMIN_SET_PERMISSION)
public class AdminSetCommand {

    private final AccountPaymentService accountPaymentService;
    private final DecimalFormatter decimalFormatter;
    private final NoticeService noticeService;

    public AdminSetCommand(
            AccountPaymentService accountPaymentService,
            DecimalFormatter decimalFormatter,
            NoticeService noticeService
    ) {
        this.accountPaymentService = accountPaymentService;
        this.decimalFormatter = decimalFormatter;
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver, @Arg BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .placeholder("{AMOUNT}", amount.toString())
                    .viewer(sender)
                    .send();

            return;
        }

        boolean successful = this.accountPaymentService.setBalance(receiver, amount);

        if (successful) {
            this.noticeService.create()
                    .notice(notice -> notice.admin.set)
                    .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                    .placeholder("{PLAYER}", receiver.name())
                    .viewer(sender)
                    .send();

            this.noticeService.create()
                    .notice(notice -> notice.player.set)
                    .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                    .placeholder("{PLAYER}", receiver.name())
                    .player(receiver.uuid())
                    .send();
        }
    }
}
