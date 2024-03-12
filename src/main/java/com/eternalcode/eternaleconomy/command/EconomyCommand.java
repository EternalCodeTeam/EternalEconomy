package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.config.implementation.PluginConfigImpl;
import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import java.math.BigDecimal;
import java.util.Optional;
import org.bukkit.entity.Player;

@Command(name = "economy", aliases = "eco")
public class EconomyCommand {
    private final UserService userService;
    private final PluginConfigImpl configuration;
    private final EternalEconomy eternalEconomy;
    private final NoticeService noticeService;

    public EconomyCommand(
        EternalEconomy eternalEconomy,
        UserService userService,
        PluginConfigImpl configuration,
        NoticeService noticeService) {
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
                .notice(configInterface -> configInterface.messages().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }

        targetUser.ifPresent(user -> user.addBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().addBalanceMessage())
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
                .notice(configInterface -> configInterface.messages().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.setBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().setBalanceMessage())
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
                .notice(configInterface -> configInterface.messages().incorrectEconomyUsageMessage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.removeBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().removeBalanceMessage())
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
            .notice(configInterface -> configInterface.messages().resetBalanceMessage())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();
    }
}
