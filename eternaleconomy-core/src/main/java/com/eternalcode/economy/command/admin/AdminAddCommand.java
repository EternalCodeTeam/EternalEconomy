package com.eternalcode.economy.command.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;

import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

@Command(name = "economy admin add")
@Permission(EconomyPermissionConstant.ADMIN_ADD_PERMISSION)
public class AdminAddCommand {

    private final AccountPaymentService accountPaymentService;

    public AdminAddCommand(AccountPaymentService accountPaymentService) {
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver, @Arg BigDecimal amount) {
        this.accountPaymentService.addBalance(receiver, amount);
    }
}
