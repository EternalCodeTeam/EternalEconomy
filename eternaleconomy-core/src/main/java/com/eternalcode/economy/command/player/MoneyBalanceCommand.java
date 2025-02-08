package com.eternalcode.economy.command.player;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;

@Command(name = "balance", aliases = "bal")
@Permission(EconomyPermissionConstant.PLAYER_BALANCE_PERMISSION)
public class MoneyBalanceCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final MessageConfig messageConfig;

    public MoneyBalanceCommand(
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        MessageConfig messageConfig
    ) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.messageConfig = messageConfig;
    }

    @Execute
    void execute(@Context Account account) {
        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.balance)
            .placeholder("{BALANCE}", this.decimalFormatter.format(account.balance()))
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .player(account.uuid())
            .send();
    }

    @Execute
    @Permission(EconomyPermissionConstant.PLAYER_BALANCE_OTHER_PERMISSION)
    void execute(@Context CommandSender sender, @Arg Account account) {
        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.balanceOther)
            .placeholder("{PLAYER}", account.name())
            .placeholder("{BALANCE}", this.decimalFormatter.format(account.balance()))
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .viewer(sender)
            .send();
    }
}
