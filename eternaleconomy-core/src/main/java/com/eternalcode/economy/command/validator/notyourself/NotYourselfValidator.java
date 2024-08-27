package com.eternalcode.economy.command.validator.notyourself;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotYourselfValidator implements AnnotatedValidator<CommandSender, Account, NotYourself> {

    private final MessageConfig config;

    public NotYourselfValidator(MessageConfig config) {
        this.config = config;
    }

    @Override
    public ValidatorResult validate(Invocation<CommandSender> invocation, CommandExecutor<CommandSender> commandExecutor, Requirement<Account> requirement, Account account, NotYourself notYourself) {
        if (invocation.sender() instanceof Player sender && sender.getUniqueId().equals(account.uuid())) {
            return ValidatorResult.invalid(config.notYourself);
        }

        return ValidatorResult.valid();
    }

}
