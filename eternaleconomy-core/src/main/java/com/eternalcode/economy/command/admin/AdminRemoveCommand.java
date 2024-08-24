package com.eternalcode.economy.command.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

@Command(name = "economy admin remove")
@Permission(EconomyPermissionConstant.ADMIN_REMOVE_PERMISSION)
public class AdminRemoveCommand {

    private final AccountPaymentService accountPaymentService;

    public AdminRemoveCommand(AccountPaymentService accountPaymentService) {
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.removeBalance(receiver, amount);
    }
}
