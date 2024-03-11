package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.notification.NoticeService;
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
    private final PluginConfiguration configuration;
    private final EternalEconomy eternalEconomy;
    private final NoticeService noticeService;


    public EconomyCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration, NoticeService noticeService) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
        this.noticeService = noticeService;
    }

    @Execute(name = "add")
    void executeAdd(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }

        targetUser.ifPresent(user -> user.addBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().addBalanceMessage())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "set")
    void executeSet(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }
        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.setBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().setBalanceMessage())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "remove")
    void executeRemove(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.removeBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().removeBalanceMessage())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "reset")
    void executeReset(@Context Player sender, @Arg("target") Player target) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        targetUser.ifPresent(user -> user.setBalance(configuration.startingBalance));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().resetBalanceMessage())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();
    }
}
