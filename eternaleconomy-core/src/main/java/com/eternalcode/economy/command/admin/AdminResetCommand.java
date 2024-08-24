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

@Command(name = "economy admin reset")
@Permission(EconomyPermissionConstant.ADMIN_ADD_PERMISSION)
public class AdminResetCommand {

    private final AccountPaymentService accountPaymentService;

    public AdminResetCommand(AccountPaymentService accountPaymentService) {
        this.accountPaymentService = accountPaymentService;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver) {
        this.accountPaymentService.resetBalance(receiver);
    }

}
