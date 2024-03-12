package com.eternalcode.eternaleconomy.inventory;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfigImpl;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.triumphteam.gui.guis.Gui;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class BalanceTopInventory {

    private final PluginConfigImpl configuration;
    private final User user;
    private final UserService userService;

    public BalanceTopInventory(PluginConfigImpl configuration, User user, UserService userService) {
        this.configuration = configuration;
        this.user = user;
        this.userService = userService;
    }

    void openTopTen() {
        Gui gui = Gui.gui()
            .title(Component.text(configuration.balanceTop.inventory_title))
            .rows(6)
            .disableAllInteractions()
            .create();

        List<User> leaderboard = userService.getTopUsersByBalance(10);
    }

    void getPlayerSkull(Player player) {

    }
}
