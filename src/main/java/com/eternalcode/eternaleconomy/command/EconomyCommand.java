package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import com.eternalcode.multification.Multification;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeBroadcast;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

    @Execute(name = "economy add")
    void executeAdd(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrect_economy_usage())
                .player(sender.getUniqueId())
                .send();

            return;
        }

        Notice notice = Notice.builder().build();

        targetUser.ifPresent(user -> user.addBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().adding_balance_message())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "economy set")
    void executeSet(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }
        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrect_economy_usage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.setBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().set_balance_message())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "economy remove")
    void executeRemove(@Context Player sender, @Arg("target") Player target, @OptionalArg("amount") BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        if (amount == null) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().incorrect_economy_usage())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        targetUser.ifPresent(user -> user.removeBalance(amount));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().removing_balance_message())
            .placeholder("%amount%", amount.toString())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    @Execute(name = "economy reset")
    void executeReset(@Context Player sender, @Arg("target") Player target) {
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        targetUser.ifPresent(user -> user.setBalance(configuration.starting_balance));

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().reset_balance_message())
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();
    }
}
