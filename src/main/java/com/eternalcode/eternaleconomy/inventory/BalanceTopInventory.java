package com.eternalcode.eternaleconomy.inventory;

import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class BalanceTopInventory {

    private PluginConfiguration configuration;
    private User user;
    private UserService userService;

    public BalanceTopInventory(PluginConfiguration configuration, User user, UserService userService) {
        this.configuration = configuration;
        this.user = user;
        this.userService = userService;
    }

    void openTopTen() {
        Gui gui = Gui.gui().title(Component.text(configuration.balanceTop.inventory_title)).rows(6).disableAllInteractions().create();

        List<User> leaderboard = userService.getTopUsersByBalance(10);

    }

    void getPlayerSkull(Player player) {

    }


}
