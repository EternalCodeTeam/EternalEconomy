package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.command.argument.PriceArgumentResolver;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Command(name = "withdraw", aliases = {"paycheck", "check"})
@Permission(EconomyPermissionConstant.ADMIN_ITEM_PERMISSION)
public class WithdrawCommand {
    private final WithdrawManager withdrawManager;
    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;

    public WithdrawCommand(
        WithdrawManager withdrawManager,
        NoticeService noticeService,
        DecimalFormatter decimalFormatter
    ) {
        this.withdrawManager = withdrawManager;
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
    }

    @Execute
    void execute(@Context Account account, @Arg @Positive @Key(PriceArgumentResolver.KEY) BigDecimal value) {
        BigDecimal balance = account.balance();
        BigDecimal subtract = balance.subtract(value);

        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            noticeService.create()
                .notice(messageConfig -> messageConfig.player.insufficientBalance)
                .placeholder("{MISSING_BALANCE}", decimalFormatter.format(subtract.abs()))
                .player(account.uuid())
                .send();

            return;
        }

        withdrawManager.givePaycheck(account.uuid(), value);
    }
}
