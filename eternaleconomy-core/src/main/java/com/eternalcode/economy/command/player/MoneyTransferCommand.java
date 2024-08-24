package com.eternalcode.economy.command.player;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;

@Command(name = "pay", aliases = {"economy pay"})
@Permission(EconomyPermissionConstant.PLAYER_PAY_PERMISSION)
public class MoneyTransferCommand {

    private final AccountPaymentService accountPaymentService;

    public MoneyTransferCommand(AccountPaymentService accountPaymentService) {
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context Account payer, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.payment(payer, receiver, amount);
    }
}
