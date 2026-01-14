package com.eternalcode.economy.command.impl;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.delay.Delay;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.economy.withdraw.WithdrawService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Command(name = "withdraw", aliases = {"paycheck", "check"})
@Permission(EconomyPermissionConstant.PLAYER_WITHDRAW_PERMISSION)
public class WithdrawCommand {

    public static final int MIN_WITHDRAWAL_AMOUNT = 1;

    private final WithdrawService withdrawService;
    private final Delay<UUID> cooldown;
    private final NoticeService noticeService;

    public WithdrawCommand(
        WithdrawService withdrawService,
        Duration cooldownDuration,
        NoticeService noticeService
    ) {
        this.withdrawService = withdrawService;
        this.cooldown = Delay.withDefault(() -> cooldownDuration);
        this.noticeService = noticeService;
    }

    @Execute
    void execute(@Context Account account, @Arg("amount") @Min(MIN_WITHDRAWAL_AMOUNT) BigDecimal value) {
        UUID playerId = account.uuid();

        if (this.cooldown.hasDelay(playerId)) {
            long remainingSeconds = Math.max(0, this.cooldown.getRemaining(playerId).toSeconds());
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.withdraw.withdrawCooldown)
                .placeholder("{TIME}", String.valueOf(remainingSeconds))
                .player(playerId)
                .send();
            return;
        }

        this.withdrawService.addBanknote(playerId, value);
        this.cooldown.markDelay(playerId);
    }
}
