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
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


@Command(name = "money", aliases = {"balance", "bal"})
public class MoneyCommand {


    private UserService userService;
    private User user;
    private final PluginConfiguration configuration;
    private final EternalEconomy eternalEconomy;
    private final NoticeService noticeService;

    public MoneyCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration, NoticeService noticeService) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
        this.noticeService = noticeService;
    }

    @Execute
    public void execute(@Context Player sender) {

        UUID uuid = sender.getUniqueId();

        if (userService.findUser(uuid).isEmpty()) {
            userService.create(uuid, sender.getName());
        }

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().checkBalanceMessage())
            .placeholder("%balance%", userService.findUser(uuid).map(user -> user.getBalance()).orElse(BigDecimal.ZERO).toString())
            .player(uuid)
            .send();

    }

    @Execute
    public void executeOthers(@Context Player sender, @Arg Player target) {
        Optional<User> targetUser = userService.findUser(sender.getUniqueId());
        if (userService.findUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().checkBalanceOtherMessage())
            .placeholder("%balance%", targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO).toString())
            .placeholder("%target%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }
}
