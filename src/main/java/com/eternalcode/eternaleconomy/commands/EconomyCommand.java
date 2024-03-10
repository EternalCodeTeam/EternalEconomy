package com.eternalcode.eternaleconomy.commands;

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

    public EconomyCommand(UserService userService, User user, PluginConfiguration configuration) {
        this.userService = userService;
        this.user = user;
        this.configuration = configuration;
    }

    @Execute
    public void execute(@Context Player sender, @Arg("type") String type, @Arg("target") Player target, @Arg @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }
        switch (type) {
            case "add":
                if (amount == null) {
                    sender.sendMessage(configuration.incorrect_economy_usage);
                    break;
                }
                targetUser.ifPresent(user -> user.addBalance(amount));
                sender.sendMessage(configuration.adding_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));
                break;
            case "set":
                if (amount == null) {
                    sender.sendMessage(configuration.incorrect_economy_usage);
                    break;
                }
                targetUser.ifPresent(user -> user.setBalance(amount));
                sender.sendMessage(configuration.set_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));
                break;
            case "remove":
                if (amount == null) {
                    sender.sendMessage(configuration.incorrect_economy_usage);
                    break;
                }
                targetUser.ifPresent(user -> user.removeBalance(amount));
                sender.sendMessage(configuration.removing_balance_message.replaceAll("%amount%", amount.toString()).replaceAll("%player%", target.getName()));
                break;
            case "reset":
                if (amount != null) {
                    sender.sendMessage(configuration.incorrect_economy_usage);
                    break;
                }
                targetUser.ifPresent(user -> user.setBalance(configuration.starting_balance));
                sender.sendMessage(configuration.reset_balance_message.replaceAll("%player%", target.getName()));
                break;
            default:
                sender.sendMessage(configuration.incorrect_economy_usage);
                break;
        }
    }
}
