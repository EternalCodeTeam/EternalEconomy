package com.eternalcode.eternaleconomy.viewer;

import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import com.eternalcode.multification.viewer.ViewerProvider;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BukktiViewerProvider implements ViewerProvider<Viewer>, ViewerService {

    private final UserService userService;
    private final Server server;

    public BukktiViewerProvider(UserService userService, Server server) {
        this.userService = userService;
        this.server = server;
    }


    @Override
    public Viewer user(User user) {
        return this.player(user.getUniqueId());
    }

    @Override
    public Viewer any(Object any) {
        if (any instanceof Player player) {
            Optional<User> userOption = this.userService.findUser(player.getUniqueId());

            return userOption.map(user -> (Viewer) user).orElseGet(() -> BukkitViewerImpl.player(player.getUniqueId()));

        }

        if (any instanceof ConsoleCommandSender || any instanceof RemoteConsoleCommandSender || any instanceof BlockCommandSender) {
            return BukkitViewerImpl.console();
        }

        throw new IllegalArgumentException("Unsupported sender type: " + any.getClass().getName());

    }

    @Override
    public Viewer console() {
        return BukkitViewerImpl.console();
    }

    @Override
    public Viewer player(UUID uuid) {
        return this.userService.findUser(uuid)
            .map(user -> new BukkitViewerImpl(uuid)).orElse(null);
    }

    @Override
    public Collection<Viewer> onlinePlayers() {
        Set<Viewer> audiences = new HashSet<>();

        for (Player player : this.server.getOnlinePlayers()) {
            audiences.add(this.player(player.getUniqueId()));
        }

        return audiences;
    }

    @Override
    public Collection<Viewer> all() {
        Collection<Viewer> audience = this.onlinePlayers();

        audience.add(this.console());

        return audience;
    }
}
