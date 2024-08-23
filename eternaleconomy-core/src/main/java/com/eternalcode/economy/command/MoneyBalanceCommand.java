package com.eternalcode.economy.command;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "balance")
public class MoneyBalanceCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;

    public MoneyBalanceCommand(NoticeService noticeService, DecimalFormatter decimalFormatter) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
    }

    @Execute
    void execute(@Context Account account) {
        this.noticeService.create()
                .notice(messageConfig -> messageConfig.balance)
                .placeholder("{BALANCE}", this.decimalFormatter.format(account.balance()))
                .player(account.uuid())
                .send();
    }
}
