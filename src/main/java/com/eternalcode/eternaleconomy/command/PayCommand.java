package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import com.eternalcode.multification.notice.NoticePart;
import com.eternalcode.multification.platform.PlatformBroadcaster;
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
    private NoticeService noticeService;

    public PayCommand(EternalEconomy eternalEconomy, UserService userService, PluginConfiguration configuration, NoticeService noticeService ) {
        this.eternalEconomy = eternalEconomy;
        this.userService = userService;
        this.configuration = configuration;
        this.noticeService = noticeService;
    }


    @Execute
    public void execute(@Context Player sender, @Arg("target") Player target, @Arg("amount") BigDecimal amount) {
        if (!(amount.compareTo(configuration.minimal_pay_ammount) >= 0)) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().minimal_pay_ammount_message())
                .placeholder("%ammount%", configuration.minimal_pay_ammount.toString())
                .player(sender.getUniqueId())
                .send();

            return;
        }
        Optional<User> senderUser = userService.findUser(sender.getUniqueId());
        Optional<User> targetUser = userService.findUser(target.getUniqueId());


        if (!(has(sender, amount))) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.economy().not_enough_money_message())
                .send();

            return;
        }

        senderUser.ifPresent(user -> user.removeBalance(amount));
        targetUser.ifPresent(user -> user.addBalance(amount));

        String amountString = amount.toString();

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().pay_received_message())
            .placeholder("%amount%", amountString)
            .placeholder("%player%", sender.getName())
            .player(target.getUniqueId())
            .send();

        this.noticeService.create()
            .notice(configInterface -> configInterface.economy().pay_sent_message())
            .placeholder("%amount%", amountString)
            .placeholder("%player%", target.getName())
            .player(sender.getUniqueId())
            .send();

    }

    private boolean has(Player player, BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(player.getUniqueId());
        BigDecimal playerBalance = targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO);
        return playerBalance.compareTo(amount) >= 0;
    }
}
