package com.eternalcode.economy.command.handler;

import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    private final NoticeService noticeService;

    public InvalidUsageHandlerImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        InvalidUsage<CommandSender> commandSenderInvalidUsage,
        ResultHandlerChain<CommandSender> resultHandlerChain) {
        Schematic schematic = commandSenderInvalidUsage.getSchematic();

        if (schematic.isOnlyFirst()) {
            this.noticeService.create()
                .viewer(invocation.sender())
                .notice(notice -> notice.correctUsage)
                .placeholder("{USAGE}", schematic.first())
                .send();
        }

        this.noticeService.create()
            .viewer(invocation.sender())
            .notice(notice -> notice.correctUsageHeader)
            .send();

        for (String usage : schematic.all()) {
            this.noticeService.create()
                .viewer(invocation.sender())
                .notice(notice -> notice.correctUsageEntry)
                .placeholder("{USAGE}", usage)
                .send();
        }
    }
}
