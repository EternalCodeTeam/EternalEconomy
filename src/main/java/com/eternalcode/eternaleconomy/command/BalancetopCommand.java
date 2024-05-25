package com.eternalcode.eternaleconomy.command;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfigImpl;
import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.eternaleconomy.system.BalancetopSystem;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Async;

import java.util.List;

@Command(name = "balancetop", aliases = "baltop")
public class BalancetopCommand {

    private BalancetopSystem balancetopSystem;
    private final NoticeService noticeService;
    private final PluginConfigImpl configuration;

    public BalancetopCommand(NoticeService noticeService, PluginConfigImpl configuration, BalancetopSystem balancetopSystem) {
        this.noticeService = noticeService;
        this.configuration = configuration;
        this.balancetopSystem = balancetopSystem;
    }


    @Execute
    public void execute(@Context Player sender, @OptionalArg("number of results") Integer number) {
        balancetopSystem.openGui(sender, balancetopSystem.balanceTopGui());

        List<User> users = balancetopSystem.tempGetUsers(10);
        sender.sendMessage("s");
        for(User user : users){
            sender.sendMessage(user.getName() + "");
            sender.sendMessage("s");
        }
    }
}
