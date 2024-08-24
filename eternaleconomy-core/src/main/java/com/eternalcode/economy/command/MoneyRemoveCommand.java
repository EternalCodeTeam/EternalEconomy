package com.eternalcode.economy.command;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

@Command(name = "money remove")
public class MoneyRemoveCommand {

    private final AccountPaymentService accountPaymentService;
    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;

    public MoneyRemoveCommand(AccountPaymentService accountPaymentService, NoticeService noticeService, DecimalFormatter decimalFormatter) {
        this.accountPaymentService = accountPaymentService;
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
    }

    @Execute
    void execute(@Context CommandSender commandSender, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.removeBalance(receiver, amount);

        this.noticeService.create()
                .notice(messageConfig -> messageConfig.removeBalance)
                .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
                .placeholder("{BALANCE}", this.decimalFormatter.format(receiver.balance()))
                .player(receiver.uuid())
                .send();
    }
}
