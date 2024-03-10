package com.eternalcode.eternaleconomy.listeners;

import com.eternalcode.eternaleconomy.EternalEconomy;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    private UserService userService;
    private User user;


    public PlayerJoinListener(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
    }

    public PlayerJoinListener(EternalEconomy eternalEconomy) {
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Optional<User> targetUser = userService.findUser(e.getPlayer().getUniqueId());
        if(targetUser.isEmpty()){
            userService.create(e.getPlayer().getUniqueId(), e.getPlayer().getName());
        }
    }

}
