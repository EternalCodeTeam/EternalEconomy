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
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import pl.auroramc.commons.decimal.DecimalFormatter;

@Command(name = "money", aliases = { "balance", "bal" })
public class MoneyCommand {

    private final UserService userService;
    private final PluginConfigImpl configuration;
    private final EternalEconomy eternalEconomy;
    private final NoticeService noticeService;
    private User user;

    public MoneyCommand(
        EternalEconomy eternalEconomy,
        UserService userService,
        PluginConfigImpl configuration,
        NoticeService noticeService) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
        this.noticeService = noticeService;
    }

    @Execute
    public void execute(@Context Player sender) {

        UUID uuid = sender.getUniqueId();

        if (userService.getUser(uuid).isEmpty()) {
            userService.create(uuid, sender.getName());
        }

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().checkBalanceMessage())
            .placeholder(
                "{BALANCE}",
                userService.getUser(uuid).map(user -> DecimalFormatter.getFormattedDecimal(user.getBalance())))
            .player(uuid)
            .send();
    }

    @Execute
    public void executeOthers(@Context Player sender, @Arg Player target) {
        Optional<User> targetUser = userService.getUser(sender.getUniqueId());
        if (userService.getUser(target.getUniqueId()).isEmpty()) {
            userService.create(target.getUniqueId(), target.getName());
        }

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().checkBalanceOtherMessage())
            .placeholder("{BALANCE}", targetUser.map(user -> DecimalFormatter.getFormattedDecimal(user.getBalance())))
            .placeholder("{TARGET}", target.getName())
            .player(sender.getUniqueId())
            .send();
    }
}
