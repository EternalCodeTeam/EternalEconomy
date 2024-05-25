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
                .placeholder("{AMOUNT}", configuration.minimalPayAmount.toString())
                .player(senderUUID)
                .send();

            return;
        }
        Optional<User> senderUser = userService.getUser(sender.getUniqueId());
        Optional<User> targetUser = userService.getUser(target.getUniqueId());

        if (!(has(sender, amount))) {

            this.noticeService.create()
                .notice(configInterface -> configInterface.messages().notEnoughMoneyMessage())
                .player(senderUUID)
                .send();

            return;
        }

        senderUser.ifPresent(user -> user.removeBalance(amount));
        targetUser.ifPresent(user -> user.addBalance(amount));

        String amountString = DecimalFormatter.getFormattedDecimal(amount);

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().receivePayMessage())
            .placeholder("{AMOUNT}", amountString)
            .placeholder("{PLAYER}", sender.getName())
            .player(target.getUniqueId())
            .send();

        this.noticeService.create()
            .notice(configInterface -> configInterface.messages().paySentMessage())
            .placeholder("{AMOUNT}", amountString)
            .placeholder("{PLAYER}", target.getName())
            .player(senderUUID)
            .send();
    }

    private boolean has(Player player, BigDecimal amount) {
        Optional<User> targetUser = userService.getUser(player.getUniqueId());
        BigDecimal playerBalance = targetUser.map(user -> user.getBalance()).orElse(BigDecimal.ZERO);
        return playerBalance.compareTo(amount) >= 0;
    }
}
