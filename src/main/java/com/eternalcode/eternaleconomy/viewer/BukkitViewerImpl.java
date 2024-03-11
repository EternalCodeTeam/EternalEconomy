package com.eternalcode.eternaleconomy.viewer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

class BukkitViewerImpl implements Viewer {

    private static final BukkitViewerImpl CONSOLE = new BukkitViewerImpl(UUID.nameUUIDFromBytes("CONSOLE".getBytes()), true);

    private final UUID uniqueId;
    private static boolean console;

    private BukkitViewerImpl(UUID uniqueId, boolean console) {
        this.uniqueId = uniqueId;
        this.console = console;
    }

    BukkitViewerImpl(UUID uniqueId) {
        this(uniqueId, false);
    }


    public static BukkitViewerImpl console() {
        return CONSOLE;
    }

    public static BukkitViewerImpl player(UUID uuid) {
        return new BukkitViewerImpl(uuid, false);
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public boolean isConsole() {
        return this.console;
    }

    @Override
    public String getName() {
        if (this.console) {
            return "CONSOLE";
        }

        Player player = Bukkit.getServer().getPlayer(this.uniqueId);

        if (player == null) {
            throw new IllegalStateException("Unknown player name");
        }

        return player.getName();
    }

}
