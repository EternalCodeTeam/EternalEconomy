package com.eternalcode.economy.command.player;

import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPosition;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Command(name = "topbalance", aliases = "topbal")
@Permission(EconomyPermissionConstant.PLAYER_TOP_BALANCE_PERMISSION)
public class TopBalanceCommand {

    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final AccountManager accountManager;
    private final PluginConfig pluginConfig;

    public TopBalanceCommand(
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        AccountManager accountManager,
        PluginConfig pluginConfig
    ) {
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.accountManager = accountManager;
        this.pluginConfig = pluginConfig;
    }

    @Execute
    void execute(@Context Account account) {
        UUID uuid = account.uuid();
        int topBalanceSize = this.pluginConfig.topBalanceSize;
        int position = 1;

        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.topBalanceHeader)
            .player(uuid)
            .send();

        for (Account topAccount : this.accountManager.getSortedTopAccounts(topBalanceSize)) {
            this.noticeService.create()
                .notice(messageConfig -> messageConfig.player.topBalanceEntry)
                .placeholder("{POSITION}", String.valueOf(position))
                .placeholder("{PLAYER}", topAccount.name())
                .placeholder("{BALANCE}", this.decimalFormatter.format(topAccount.balance()))
                .player(uuid)
                .send();

            position++;
        }

        AccountPosition accountPosition = this.accountManager.getAccountPosition(account);
        this.noticeService.create()
            .notice(messageConfig -> messageConfig.player.balancePosition)
            .placeholder("{POSITION}", String.valueOf(accountPosition.position()))
            .placeholder("{BALANCE}", this.decimalFormatter.format(account.balance()))
            .player(uuid)
            .send();
    }

}
