package com.eternalcode.eternaleconomy.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private final UserService userService;

    public UserListener(UserService userService) {
        this.userService = userService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        // todo: implement, create or load
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        // todo: implement save user
    }

}
