package com.eternalcode.economy.command;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

@Command(name = "economy set")
public class MonetSetCommand {

    private final AccountPaymentService accountPaymentService;

    public MonetSetCommand(AccountPaymentService accountPaymentService) {
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.setBalance(receiver, amount);
    }
}
