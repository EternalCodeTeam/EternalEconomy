package com.eternalcode.eternaleconomy.command.handler;

import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;

public class NoticeHandler implements ResultHandler<CommandSender, Notice> {

    private final NoticeService noticeService;

    public NoticeHandler(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        Notice notice,
        ResultHandlerChain<CommandSender> resultHandlerChain) {

        this.noticeService.create()
            .notice(notice)
            .viewer(invocation.sender())
            .send();
    }
}
