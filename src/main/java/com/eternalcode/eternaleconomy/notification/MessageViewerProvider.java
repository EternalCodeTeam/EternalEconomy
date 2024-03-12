package com.eternalcode.eternaleconomy.notification;

import com.eternalcode.multification.viewer.ViewerProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class MessageViewerProvider implements ViewerProvider<CommandSender> {

    private final Server server;

    public MessageViewerProvider(Server server) {
        this.server = server;
    }

    @Override
    public CommandSender console() {
        return this.server.getConsoleSender();
    }

    @Override
    public CommandSender player(UUID uuid) {
        return this.server.getPlayer(uuid);
    }

    @Override
    public Collection<CommandSender> onlinePlayers() {
        return new ArrayList<>(this.server.getOnlinePlayers());
    }

    @Override
    public Collection<CommandSender> all() {
        return new ArrayList<>(this.server.getOnlinePlayers());
    }
}
