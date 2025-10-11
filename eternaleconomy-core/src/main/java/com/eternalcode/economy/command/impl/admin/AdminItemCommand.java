package com.eternalcode.economy.command.impl.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.paycheck.PaycheckManager;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "economy item")
@Permission(EconomyPermissionConstant.ADMIN_ITEM_PERMISSION)
public class AdminItemCommand {
    private final PaycheckManager paycheckManager;

    public AdminItemCommand(PaycheckManager paycheckManager) {
        this.paycheckManager = paycheckManager;
    }

    @Execute
    void execute(@Context Player player) {
        paycheckManager.setItem(player);
    }
}
