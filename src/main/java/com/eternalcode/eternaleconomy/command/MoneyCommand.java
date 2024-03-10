package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Optional;


@Command(name = "money", aliases = {"balance", "bal"})
public class MoneyCommand {


    private UserService userService;
    private User user;
    private final PluginConfiguration configuration;
    private final EternalEconomy eternalEconomy;

    public MoneyCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
    }

    @Execute
    public void execute(@Context Player sender) {
        Optional<User> targetUser = userService.findUser(sender.getUniqueId());
        if (userService.findUser(sender.getUniqueId()).isEmpty()) {
            userService.create(sender.getUniqueId(), sender.getName());
        }
        sender.sendMessage(configuration.checking_balance_message.replaceAll("%balance%", targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO) + ""));

    }

    @Execute
    public void executeOthers(@Context Player sender, @Arg Player target) {
        Optional<User> targetUser = userService.findUser(sender.getUniqueId());
        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }
        sender.sendMessage(configuration.checking_balance_other_message.replaceAll("%balance%", targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO) + "").replaceAll("%target%", target.getName()));
    }
}
