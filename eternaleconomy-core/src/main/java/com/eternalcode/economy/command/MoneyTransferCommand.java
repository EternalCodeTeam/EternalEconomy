package com.eternalcode.economy.command;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import java.math.BigDecimal;

@Command(name = "pay")
public class MoneyTransferCommand {

    private final NoticeService noticeService;

    public MoneyTransferCommand(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context Account payer, @Arg Account receiver, @Arg BigDecimal amount) {

    }
}
