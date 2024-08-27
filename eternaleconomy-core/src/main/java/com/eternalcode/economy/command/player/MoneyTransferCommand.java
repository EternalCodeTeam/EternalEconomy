package com.eternalcode.economy.command.player;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;

@Command(name = "pay")
@Permission(EconomyPermissionConstant.PLAYER_PAY_PERMISSION)
public class MoneyTransferCommand {

    private final AccountPaymentService accountPaymentService;
    private final DecimalFormatter decimalFormatter;
    private final NoticeService noticeService;

    public MoneyTransferCommand(
            AccountPaymentService accountPaymentService,
            DecimalFormatter decimalFormatter,
            NoticeService noticeService
    ) {
        this.accountPaymentService = accountPaymentService;
        this.decimalFormatter = decimalFormatter;
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context Account payer, @Arg Account receiver, @Arg BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                    .player(payer.uuid())
                    .send();

            return;
        }

        if (payer.balance().compareTo(amount) < 1) {
            BigDecimal subtract = amount.subtract(payer.balance());
            this.noticeService.create()
                    .notice(notice -> notice.player.insufficientBalance)
                    .placeholder("{MISSING_BALANCE}", this.decimalFormatter.format(subtract))
                    .player(payer.uuid())
                    .send();

            return;
        }

        boolean successful = this.accountPaymentService.payment(payer, receiver, amount);

        if (successful) {
            this.noticeService.create()
                    .notice(notice -> notice.player.transferSuccess)
                    .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                    .placeholder("{PLAYER}", receiver.name())
                    .player(payer.uuid())
                    .send();

            this.noticeService.create()
                    .notice(notice -> notice.player.transferReceived)
                    .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                    .placeholder("{PLAYER}", payer.name())
                    .player(receiver.uuid())
                    .send();
        }
    }
}