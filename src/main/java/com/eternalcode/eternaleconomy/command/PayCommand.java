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

@Command(name = "pay")
public class PayCommand {

    private UserService userService;
    private User user;
    private PluginConfiguration configuration;
    private final EternalEconomy eternalEconomy;

    public PayCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
    }


    @Execute
    public void execute(@Context Player sender, @Arg("target") Player target, @Arg("amount") BigDecimal amount) {
        if (!(amount.compareTo(configuration.minimal_pay_ammount) >= 0)) {
            sender.sendMessage(configuration.minimal_pay_ammount_message.replaceAll("%ammount%", configuration.minimal_pay_ammount.toString()));
            return;
        }
        Optional<User> senderUser = userService.findUser(sender.getUniqueId());
        Optional<User> targetUser = userService.findUser(target.getUniqueId());
        if (!(has(sender, amount))) {
            sender.sendMessage(configuration.not_enough_money_message);
            return;
        }

        senderUser.ifPresent(user -> user.removeBalance(amount));
        targetUser.ifPresent(user -> user.addBalance(amount));
        target.sendMessage(configuration.receive_pay_message.replaceAll("%amount%", amount.toString()));
        sender.sendMessage(configuration.pay_sent_message.replaceAll("%amount%", amount.toString()));

    }

    private boolean has(Player player, BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(player.getUniqueId());
        BigDecimal playerBalance = targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO);
        return playerBalance.compareTo(amount) >= 0;
    }
}
