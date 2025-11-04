package com.eternalcode.economy.withdraw;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.command.argument.MoneyFormatArgument;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;

@Command(name = "withdraw", aliases = {"paycheck", "check"})
@Permission(EconomyPermissionConstant.PLAYER_WITHDRAW_PERMISSION)
public class WithdrawCommand {

    private final WithdrawService withdrawService;
    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;

    public WithdrawCommand(
        WithdrawService withdrawService,
        NoticeService noticeService,
        DecimalFormatter decimalFormatter
    ) {
        this.withdrawService = withdrawService;
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
    }

    @Execute
    void execute(@Context Account account, @Arg("amount") @Key(MoneyFormatArgument.KEY) BigDecimal value) {
        BigDecimal balance = account.balance();
        BigDecimal subtract = balance.subtract(value);

        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.insufficientBalance)
                .placeholder("{MISSING_BALANCE}", this.decimalFormatter.format(subtract.abs()))
                .player(account.uuid())
                .send();

            return;
        }

        this.withdrawService.addBanknote(account.uuid(), value);
    }
}
