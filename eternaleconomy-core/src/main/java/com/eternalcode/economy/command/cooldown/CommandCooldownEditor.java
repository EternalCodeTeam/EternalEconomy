package com.eternalcode.economy.command.cooldown;

import com.eternalcode.economy.config.implementation.CommandsConfig;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.cooldown.CooldownContext;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.meta.Meta;
import java.util.Map;
import org.bukkit.command.CommandSender;

public class CommandCooldownEditor implements Editor<CommandSender> {

    private final CommandsConfig commandsConfig;

    public CommandCooldownEditor(CommandsConfig commandsConfig) {
        this.commandsConfig = commandsConfig;
    }

    @Override
    public CommandBuilder<CommandSender> edit(CommandBuilder<CommandSender> commandBuilder) {
        Meta meta = commandBuilder.meta();

        for (Map.Entry<String, CommandCooldownConfig> entry : commandsConfig.cooldowns.entrySet()) {
            String commandName = entry.getKey();
            boolean isCurrent = commandBuilder.isNameOrAlias(commandName);

            if (!isCurrent) {
                continue;
            }

            CommandCooldownConfig cooldown = entry.getValue();

            meta.put(Meta.COOLDOWN, new CooldownContext(commandName, cooldown.duration, cooldown.bypassPermission));
            break;
        }

        return commandBuilder;
    }
}
