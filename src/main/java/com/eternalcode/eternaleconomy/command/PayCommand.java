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
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import pl.auroramc.commons.decimal.DecimalFormatter;

@Command(name = "pay")
public class PayCommand {

    private final UserService userService;
    private final PluginConfigImpl configuration;
    private final EternalEconomy eternalEconomy;
    private final NoticeService noticeService;
    private User user;
    private DecimalFormatter decimalFormatter;

    public PayCommand(
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
    public void execute(@Context Player sender, @Arg("target") Player target, @Arg("amount") BigDecimal amount) {

        UUID senderUUID = sender.getUniqueId();

        if (!(amount.compareTo(configuration.minimalPayAmount) >= 0)) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.messages().minimalPayAmountMessage())
                .placeholder("{ammount}", configuration.minimalPayAmount.toString())
                .player(senderUUID)
                .send();

            return;
        }
        Optional<User> senderUser = userService.findUser(sender.getUniqueId());
        Optional<User> targetUser = userService.findUser(target.getUniqueId());

        if (!(has(sender, amount))) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.messages().notEnoughMoneyMessage())
                .player(senderUUID)
                .send();

            return;
        }

        senderUser.ifPresent(user -> user.removeBalance(amount));
        targetUser.ifPresent(user -> user.addBalance(amount));

        String amountString = decimalFormatter.getFormattedDecimal(amount.doubleValue());

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().receivePayMessage())
            .placeholder("{amount}", amountString)
            .placeholder("{player}", sender.getName())
            .player(target.getUniqueId())
            .send();

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().paySentMessage())
            .placeholder("{amount}", amountString)
            .placeholder("{player}", target.getName())
            .player(senderUUID)
            .send();
    }

    private boolean has(Player player, BigDecimal amount) {
        Optional<User> targetUser = userService.findUser(player.getUniqueId());
        BigDecimal playerBalance = targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO);
        return playerBalance.compareTo(amount) >= 0;
    }
}
