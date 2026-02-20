package com.eternalcode.economy.command.message;

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

    public InvalidBigDecimalMessage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public Object get(Invocation<CommandSender> invocation, JakartaViolation<A, BigDecimal> positive) {
        BigDecimal invalidValue = positive.getInvalidValue();

        return noticeService.create()
            .notice(notice -> notice.positiveNumberRequired)
            .placeholder("{AMOUNT}", invalidValue.toString())
            .viewer(invocation.sender());
    }
}
