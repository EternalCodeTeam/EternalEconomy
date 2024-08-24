package com.eternalcode.economy.command;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

import java.math.BigDecimal;

@Command(name = "pay")
public class MoneyTransferCommand {

    private final NoticeService noticeService;
    private final AccountPaymentService accountPaymentService;

    public MoneyTransferCommand(NoticeService noticeService, AccountPaymentService accountPaymentService) {
        this.noticeService = noticeService;
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context Account payer, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.payment(payer, receiver, amount);

        this.noticeService.create()
                .notice(messageConfig -> messageConfig.transferSuccess)
                .placeholder("{AMOUNT}", amount.toString())
                .placeholder("{BALANCE}", payer.balance().toString())
                .player(payer.uuid())
                .send();

        this.noticeService.create()
                .notice(messageConfig -> messageConfig.transferReceived)
                .placeholder("{AMOUNT}", amount.toString())
                .placeholder("{BALANCE}", receiver.balance().toString())
                .player(receiver.uuid())
                .send();
    }
}
