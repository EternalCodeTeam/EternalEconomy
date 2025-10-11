package com.eternalcode.economy.paycheck;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Positive;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

@Command(name = "withdraw", aliases = {"paycheck", "check"})
@Permission(EconomyPermissionConstant.ADMIN_ITEM_PERMISSION)
public class PaycheckCommand {
    private final PaycheckManager paycheckManager;
    private final NoticeService noticeService;

    public PaycheckCommand(
        PaycheckManager paycheckManager,
        NoticeService noticeService
    ) {
        this.paycheckManager = paycheckManager;
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context Account account, @Arg @Positive BigDecimal value) {
        BigDecimal balance = account.balance();
        BigDecimal subtract = balance.subtract(value);

        if(subtract.compareTo(BigDecimal.ZERO) < 0) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.player.insufficientBalance)
                .placeholder("{MISSING_BALANCE}", subtract.toString())
                .player(account.uuid())
                .send();

            return;
        }

        paycheckManager.givePaycheck(account.uuid(), value);
    }
}
