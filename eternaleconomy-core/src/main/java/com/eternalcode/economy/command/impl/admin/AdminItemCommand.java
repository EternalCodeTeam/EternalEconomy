package com.eternalcode.economy.command.impl.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.withdraw.WithdrawManager;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "economy item")
@Permission(EconomyPermissionConstant.ADMIN_ITEM_PERMISSION)
public class AdminItemCommand {
    private final WithdrawManager withdrawManager;

    public AdminItemCommand(WithdrawManager withdrawManager) {
        this.withdrawManager = withdrawManager;
    }

    @Execute
    void execute(@Context Player player) {
        withdrawManager.setItem(player);
    }
}
