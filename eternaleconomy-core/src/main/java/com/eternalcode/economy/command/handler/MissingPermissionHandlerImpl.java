package com.eternalcode.economy.command.handler;

import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class MissingPermissionHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    private final NoticeService noticeService;
    private final MessageConfig messageConfig;

    public MissingPermissionHandlerImpl(
        NoticeService noticeService,
        MessageConfig messageConfig)
    {
        this.noticeService = noticeService;
        this.messageConfig = messageConfig;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        MissingPermissions missingPermissions,
        ResultHandlerChain<CommandSender> resultHandlerChain) {
        String joinedText = missingPermissions.asJoinedText();

        this.noticeService.create()
            .viewer(invocation.sender())
            .notice(notice -> notice.missingPermission)
            .placeholder("{PERMISSION}", joinedText)
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .send();
    }
}
