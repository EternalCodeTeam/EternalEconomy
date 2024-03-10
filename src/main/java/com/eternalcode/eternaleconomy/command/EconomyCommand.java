package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Optional;

@Command(name = "economy", aliases = "eco")
public class EconomyCommand {
    private UserService userService;
    private User user;
    private final PluginConfiguration configuration;
    private final EternalEconomy eternalEconomy;

    public EconomyCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
    }

    @Execute(name = "economy add")
    public void executeAdd(@Context Player sender, @Arg("target") Player target, @Arg @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {
            sender.sendMessage(configuration.incorrect_economy_usage);
            return;
        }
        targetUser.ifPresent(user -> user.addBalance(amount));
        sender.sendMessage(configuration.adding_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));
    }

    @Execute(name = "economy set")
    public void executeSet(@Context Player sender, @Arg("target") Player target, @Arg @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }
        if (amount == null) {
            sender.sendMessage(configuration.incorrect_economy_usage);
            return;
        }
        targetUser.ifPresent(user -> user.setBalance(amount));
        sender.sendMessage(configuration.set_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));
    }

    @Execute(name = "economy remove")
    public void executeRemove(@Context Player sender, @Arg("target") Player target, @Arg @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {
            sender.sendMessage(configuration.incorrect_economy_usage);
            return;
        }
        targetUser.ifPresent(user -> user.removeBalance(amount));
        sender.sendMessage(configuration.removing_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));

    }

    @Execute(name = "economy reset")
    public void executeReset(@Context Player sender, @Arg("target") Player target) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        targetUser.ifPresent(user -> user.setBalance(configuration.starting_balance));
        sender.sendMessage(configuration.reset_balance_message.replaceAll("%player%", target.getName()));

    }


}
