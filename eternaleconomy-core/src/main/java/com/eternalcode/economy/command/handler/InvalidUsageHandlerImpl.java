package com.eternalcode.economy.command.handler;

import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    private final NoticeService noticeService;
    private final MessageConfig messageConfig;

    public InvalidUsageHandlerImpl(
        NoticeService noticeService,
        MessageConfig messageConfig
    ) {
        this.noticeService = noticeService;
        this.messageConfig = messageConfig;
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
                .placeholder("{PREFIX}", messageConfig.messagesPrefix)
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
                .placeholder("{PREFIX}", messageConfig.messagesPrefix)
                .send();
        }
    }
}
