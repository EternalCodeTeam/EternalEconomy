package com.eternalcode.economy.command.admin;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

@Command(name = "economy admin add")
@Permission(EconomyPermissionConstant.ADMIN_ADD_PERMISSION)
public class AdminAddCommand {

    private final AccountPaymentService accountPaymentService;
    private final DecimalFormatter decimalFormatter;
    private final NoticeService noticeService;
    private final MessageConfig messageConfig;

    public AdminAddCommand(
        AccountPaymentService accountPaymentService,
        DecimalFormatter decimalFormatter,
        NoticeService noticeService,
        MessageConfig messageConfig
    ) {
        this.accountPaymentService = accountPaymentService;
        this.decimalFormatter = decimalFormatter;
        this.noticeService = noticeService;
        this.messageConfig = messageConfig;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg Account receiver, @Arg @Positive BigDecimal amount) {
        this.accountPaymentService.addBalance(receiver, amount);

        this.noticeService.create()
            .notice(notice -> notice.admin.added)
            .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
            .placeholder("{PLAYER}", receiver.name())
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .viewer(sender)
            .send();

        this.noticeService.create()
            .notice(notice -> notice.player.added)
            .placeholder("{AMOUNT}", this.decimalFormatter.format(amount))
            .placeholder("{PLAYER}", receiver.name())
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .player(receiver.uuid())
            .send();
    }
}
