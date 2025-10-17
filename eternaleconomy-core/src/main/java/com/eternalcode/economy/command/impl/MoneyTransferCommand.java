package com.eternalcode.economy.command.impl;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.command.argument.MoneyFormatArgument;
import com.eternalcode.economy.command.validator.notsender.NotSender;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.math.BigDecimal;

@Command(name = "pay", aliases = "transfer")
@Permission(EconomyPermissionConstant.PLAYER_PAY_PERMISSION)
public class MoneyTransferCommand {

    private final AccountPaymentService accountPaymentService;
    private final DecimalFormatter decimalFormatter;
    private final NoticeService noticeService;
    private PluginConfig pluginConfig;

    public MoneyTransferCommand(
        AccountPaymentService accountPaymentService,
        DecimalFormatter decimalFormatter,
        NoticeService noticeService,
        PluginConfig pluginConfig
    ) {
        this.accountPaymentService = accountPaymentService;
        this.decimalFormatter = decimalFormatter;
        this.noticeService = noticeService;
        this.pluginConfig = pluginConfig;
    }

    @Execute
    void execute(@Context Account payer, @Arg @NotSender Account receiver, @Arg @Key(MoneyFormatArgument.KEY) BigDecimal amount) {
        if (payer.balance().compareTo(amount) < 1) {
            BigDecimal subtract = amount.subtract(payer.balance());
            this.noticeService.create()
                .notice(notice -> notice.player.insufficientBalance)
                .placeholder("{MISSING_BALANCE}", this.decimalFormatter.format(subtract))
                .player(payer.uuid())
                .send();

            return;
        }

        if (amount.compareTo(this.pluginConfig.transactionLimit) > 0) {
            this.noticeService.create()
                .notice(notice -> notice.player.transferLimit)
                .placeholder("{LIMIT}", this.decimalFormatter.format(this.pluginConfig.transactionLimit))
                .player(payer.uuid())
                .send();

            return;
        }

        this.accountPaymentService.payment(payer, receiver, amount);

        this.noticeService.create()
            .notice(notice -> notice.player.transferSuccess)
            .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
            .placeholder("{PLAYER}", receiver.name())
            .player(payer.uuid())
            .send();

        this.noticeService.create()
            .notice(notice -> notice.player.transferReceived)
            .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
            .placeholder("{PLAYER}", payer.name())
            .player(receiver.uuid())
            .send();
    }
}
