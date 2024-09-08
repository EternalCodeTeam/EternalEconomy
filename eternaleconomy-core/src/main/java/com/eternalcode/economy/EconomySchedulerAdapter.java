package com.eternalcode.economy;

import com.eternalcode.commons.bukkit.scheduler.BukkitSchedulerImpl;
import com.eternalcode.commons.folia.scheduler.FoliaSchedulerImpl;
import com.eternalcode.commons.scheduler.Scheduler;
import org.bukkit.plugin.Plugin;

public class EconomySchedulerAdapter {

    public static final String FOLIA = "io.papermc.paper.threadedregions.RegionizedServer";

    public static Scheduler getAdaptiveScheduler(Plugin plugin) {
        if (isFolia()) {
            return new FoliaSchedulerImpl(plugin);
        }
        else {
            return new BukkitSchedulerImpl(plugin);
        }
    }

    private static boolean isFolia() {
        try {
            Class.forName(FOLIA);
            return true;
        }
        catch (ClassNotFoundException exception) {
            return false;
        }
    }
}
