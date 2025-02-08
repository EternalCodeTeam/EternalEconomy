package com.eternalcode.economy.command.cooldown;

import com.eternalcode.economy.config.implementation.CommandsConfig;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.multification.NoticeService;
import dev.rollczi.litecommands.cooldown.CooldownState;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.InvokedMessage;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.time.DurationParser;
import org.bukkit.command.CommandSender;

import java.time.Duration;

public class CommandCooldownMessage implements InvokedMessage<CommandSender, Object, CooldownState> {

    private final NoticeService noticeService;
    private final CommandsConfig commandsConfig;
    private final MessageConfig messageConfig;

    public CommandCooldownMessage(
        NoticeService noticeService,
        CommandsConfig commandsConfig,
        MessageConfig messageConfig
    ) {
        this.noticeService = noticeService;
        this.commandsConfig = commandsConfig;
        this.messageConfig = messageConfig;
    }

    @Override
    public Object get(Invocation<CommandSender> invocation, CooldownState cooldownState) {
        CommandCooldownConfig cooldown = commandsConfig.cooldowns.get(cooldownState.getCooldownContext().getKey());

        if (cooldown == null) {
            return LiteMessages.COMMAND_COOLDOWN.getDefaultMessage(cooldownState);
        }

        String formatted = DurationParser.TIME_UNITS.format(Duration.ofSeconds(cooldownState.getRemainingDuration().getSeconds()));

        return noticeService.create()
            .notice(notice -> cooldown.message)
            .placeholder("{TIME}", formatted)
            .placeholder("{PREFIX}", messageConfig.messagesPrefix)
            .viewer(invocation.sender());
    }

}
