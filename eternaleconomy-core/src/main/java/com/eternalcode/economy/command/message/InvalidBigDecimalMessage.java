package com.eternalcode.economy.command.message;

import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.jakarta.JakartaViolation;
import dev.rollczi.litecommands.message.InvokedMessage;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

public class InvalidBigDecimalMessage<A extends Annotation>
    implements InvokedMessage<CommandSender, Object, JakartaViolation<A, BigDecimal>> {
    private final NoticeService noticeService;
    private final MessageConfig messageConfig;

    public InvalidBigDecimalMessage(NoticeService noticeService, MessageConfig messageConfig) {
        this.noticeService = noticeService;
        this.messageConfig = messageConfig;
    }

    @Override
    public Object get(Invocation<CommandSender> invocation, JakartaViolation<A, BigDecimal> positive) {
        BigDecimal invalidValue = positive.getInvalidValue();

        return noticeService.create()
            .notice(notice -> notice.invalidAmount)
            .placeholder("{AMOUNT}", invalidValue.toString())
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .viewer(invocation.sender());
    }
}
