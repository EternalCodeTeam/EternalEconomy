package com.eternalcode.economy.command.validator.notsender;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotSenderValidator implements AnnotatedValidator<CommandSender, Account, NotSender> {

    private final MessageConfig config;

    public NotSenderValidator(MessageConfig config) {
        this.config = config;
    }

    @Override
    public ValidatorResult validate(
        Invocation<CommandSender> invocation,
        CommandExecutor<CommandSender> commandExecutor,
        Requirement<Account> requirement,
        Account account,
        NotSender notSender) {
        if (invocation.sender() instanceof Player sender && sender.getUniqueId().equals(account.uuid())) {
            return ValidatorResult.invalid(this.config.notSender);
        }

        return ValidatorResult.valid();
    }
}
