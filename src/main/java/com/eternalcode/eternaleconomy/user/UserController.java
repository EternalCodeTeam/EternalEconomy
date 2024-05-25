package com.eternalcode.eternaleconomy.user;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserController implements Listener {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.userService.getUser(uniqueId)
            .ifPresentOrElse(
                user -> user.setName(player.getName()),
                () -> this.userService.create(uniqueId, player.getName()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.userService.getUser(uniqueId).ifPresent(this.userService::saveUser);
    }
}
