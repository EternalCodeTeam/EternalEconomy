package com.eternalcode.economy.withdraw;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.commons.scheduler.Task;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.time.Duration;
import java.util.PriorityQueue;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WithdrawDecayTask implements Listener {

    private static final Duration TICK_INTERVAL = Duration.ofSeconds(1);
    private static final int OFFHAND_SLOT = -1;
    private static final int MIN_RECONCILIATION_INTERVAL_SECONDS = 60;

    private final Server server;
    private final Scheduler scheduler;
    private final WithdrawItemService withdrawItemService;
    private final PluginConfig pluginConfig;

    private final PriorityQueue<ScheduledUpdate> queue = new PriorityQueue<>();

    private Task tickTask;
    private Task reconciliationTask;

    public WithdrawDecayTask(
        Server server,
        Scheduler scheduler,
        WithdrawItemService withdrawItemService,
        PluginConfig pluginConfig
    ) {
        this.server = server;
        this.scheduler = scheduler;
        this.withdrawItemService = withdrawItemService;
        this.pluginConfig = pluginConfig;
    }

    public void start() {
        if (!this.pluginConfig.withdraw.decay.enabled) {
            return;
        }

        for (Player player : this.server.getOnlinePlayers()) {
            this.seedPlayerInventory(player);
        }

        this.tickTask = this.scheduler.timer(this::tick, TICK_INTERVAL, TICK_INTERVAL);

        Duration reconciliationInterval = Duration.ofSeconds(Math.max(
            MIN_RECONCILIATION_INTERVAL_SECONDS,
            this.pluginConfig.withdraw.decay.reconciliationIntervalSeconds));

        this.reconciliationTask = this.scheduler.timer(
            this::reconcile, reconciliationInterval, reconciliationInterval);
    }

    public void stop() {
        if (this.tickTask != null) {
            this.tickTask.cancel();
            this.tickTask = null;
        }
        if (this.reconciliationTask != null) {
            this.reconciliationTask.cancel();
            this.reconciliationTask = null;
        }
        this.queue.clear();
    }

    public void schedule(UUID playerId, int slot, long dueAtMillis) {
        if (dueAtMillis == Long.MAX_VALUE) {
            return;
        }
        this.queue.add(new ScheduledUpdate(playerId, slot, dueAtMillis));
    }

    private void tick() {
        if (!this.pluginConfig.withdraw.decay.enabled) {
            return;
        }

        long now = System.currentTimeMillis();
        int maxPerTick = Math.max(1, this.pluginConfig.withdraw.decay.maxUpdatesPerTick);
        int processed = 0;

        while (processed < maxPerTick) {
            ScheduledUpdate next = this.queue.peek();
            if (next == null || next.dueAtMillis() > now) {
                break;
            }

            this.queue.poll();
            this.process(next);
            processed++;
        }
    }

    private void process(ScheduledUpdate update) {
        Player player = this.server.getPlayer(update.playerId());
        if (player == null) {
            // offline - their check isn't visible to anyone right now, nothing to refresh.
            // it will be picked up again by seedPlayerInventory on their next join.
            return;
        }

        ItemStack item = this.getItemAt(player, update.slot());
        if (item == null) {
            return;
        }

        long nextUpdate = this.withdrawItemService.refreshLore(item);
        this.schedule(update.playerId(), update.slot(), nextUpdate);
    }

    private void reconcile() {
        if (!this.pluginConfig.withdraw.decay.enabled) {
            return;
        }

        for (Player player : this.server.getOnlinePlayers()) {
            this.seedPlayerInventory(player);
        }
    }

    private void seedPlayerInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getStorageContents();

        for (int slot = 0; slot < contents.length; slot++) {
            this.trySeed(player, slot, contents[slot]);
        }

        this.trySeed(player, OFFHAND_SLOT, inventory.getItemInOffHand());
    }

    private void trySeed(Player player, int slot, ItemStack item) {
        if (item == null || !this.withdrawItemService.isBanknote(item)) {
            return;
        }

        long nextUpdate = this.withdrawItemService.refreshLore(item);
        this.schedule(player.getUniqueId(), slot, nextUpdate);
    }

    private ItemStack getItemAt(Player player, int slot) {
        if (slot == OFFHAND_SLOT) {
            return player.getInventory().getItemInOffHand();
        }
        return player.getInventory().getItem(slot);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!this.pluginConfig.withdraw.decay.enabled) {
            return;
        }

        this.seedPlayerInventory(event.getPlayer());
    }

    private record ScheduledUpdate(UUID playerId, int slot, long dueAtMillis) implements Comparable<ScheduledUpdate> {

        @Override
        public int compareTo(ScheduledUpdate other) {
            return Long.compare(this.dueAtMillis, other.dueAtMillis);
        }
    }
}
