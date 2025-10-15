package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.EconomyPermissionConstant;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "economy withdraw setitem")
@Permission(EconomyPermissionConstant.ADMIN_ITEM_PERMISSION)
public class WithdrawSetItemCommand {

    private final WithdrawService withdrawService;

    public WithdrawSetItemCommand(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Execute
    void execute(@Context Player player) {
        this.withdrawService.setItem(player);
    }
}
