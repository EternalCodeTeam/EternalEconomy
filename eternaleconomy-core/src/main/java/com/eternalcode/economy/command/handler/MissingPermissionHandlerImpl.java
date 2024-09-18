package com.eternalcode.economy.command.handler;

import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class MissingPermissionHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    private final NoticeService noticeService;

    public MissingPermissionHandlerImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
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
            .send();
    }
}
