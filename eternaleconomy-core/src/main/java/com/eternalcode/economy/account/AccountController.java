package com.eternalcode.economy.account;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AccountController implements Listener {

    private final AccountManager accountManager;

    public AccountController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.accountManager.getOrCreate(uniqueId, player.getName());
    }
}
