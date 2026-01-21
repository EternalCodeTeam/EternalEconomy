package com.eternalcode.economy.leaderboard.menu;

import com.eternalcode.commons.concurrent.FutureHandler;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.MiniMessageHolder;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.leaderboard.LeaderboardEntry;
import com.eternalcode.economy.leaderboard.LeaderboardPage;
import com.eternalcode.economy.leaderboard.LeaderboardService;
import com.eternalcode.economy.leaderboard.menu.LeaderboardConfig.LeaderboardItem;
import com.eternalcode.multification.shared.Formatter;
import dev.rollczi.liteskullapi.SkullAPI;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LeaderboardMenu implements MiniMessageHolder {

    private static final Component RESET = Component.text().decoration(TextDecoration.ITALIC, false).build();

    private final LeaderboardConfig config;
    private final Scheduler scheduler;
    private final LeaderboardService leaderboardService;
    private final DecimalFormatter decimalFormatter;
    private final SkullAPI skullAPI;

    public LeaderboardMenu(
        LeaderboardConfig config,
        Scheduler scheduler,
        LeaderboardService leaderboardService,
        DecimalFormatter decimalFormatter,
        SkullAPI skullAPI
    ) {
        this.config = config;
        this.scheduler = scheduler;
        this.leaderboardService = leaderboardService;
        this.decimalFormatter = decimalFormatter;
        this.skullAPI = skullAPI;
    }

    public void open(Player player, int page) {
        int pageSize = config.playerItems.slots.size();

        this.scheduler.runAsync(() ->
            this.leaderboardService.getLeaderboardPage(page, pageSize)
                .thenCompose(result -> preloadSkulls(result)
                    .thenAccept(skullCache -> scheduler.run(() -> {
                        Gui gui = createGui(player, result, skullCache);
                        gui.open(player);
                    }))
                )
                .exceptionally(FutureHandler::handleException)
        );
    }

    private CompletableFuture<List<ItemStack>> preloadSkulls(LeaderboardPage page) {
        List<LeaderboardEntry> entries = page.entries();
        List<CompletableFuture<ItemStack>> futures = new ArrayList<>(entries.size());

        for (LeaderboardEntry entry : entries) {
            LeaderboardItemRepresenter item = this.config.playerItems.template;

            String textureToUse = item.texture;

            if (textureToUse != null && textureToUse.equals("{PLAYER_HEAD}")) {
                futures.add(this.skullAPI.getSkull(entry.account().uuid()));
            }
            else if (textureToUse != null && !textureToUse.equals("none")) {
                futures.add(this.skullAPI.getSkull(textureToUse));
            }
            else {
                futures.add(CompletableFuture.completedFuture(null));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<ItemStack> skulls = new ArrayList<>(futures.size());
                for (CompletableFuture<ItemStack> future : futures) {
                    skulls.add(future.join());
                }
                return skulls;
            });
    }

    private Gui createGui(Player player, LeaderboardPage page, List<ItemStack> skullCache) {
        int current = page.currentPage();
        int max = page.maxPages();
        int pageSize = this.config.playerItems.slots.size();
        int players = page.maxPages() <= 1
            ? page.entries().size()
            : (page.maxPages() - 1) * pageSize + page.entries().size();

        Formatter formatter = new Formatter()
            .register("{CURRENT_PAGE}", current)
            .register("{TOTAL_PAGES}", max)
            .register("{TOTAL_PLAYERS}", players);

        Component title = RESET.append(
            MINI_MESSAGE.deserialize(formatter.format(this.config.title))
        );

        Gui gui = Gui.gui()
            .title(title)
            .rows(this.config.rows)
            .disableAllInteractions()
            .create();

        if (this.config.fillSettings.enableFillItems) {
            gui.getFiller().fill(
                this.config.fillSettings.fillItems.stream()
                    .map(ItemBuilder::from)
                    .map(ItemBuilder::asGuiItem)
                    .toList()
            );
        }

        renderEntries(gui, page, skullCache);
        renderPagination(gui, player, current, max, players);

        return gui;
    }

    private void renderEntries(Gui gui, LeaderboardPage page, List<ItemStack> skullCache) {
        List<Integer> slots = this.config.playerItems.slots;
        List<LeaderboardEntry> entries = page.entries();

        for (int i = 0; i < Math.min(slots.size(), entries.size()); i++) {
            LeaderboardEntry entry = entries.get(i);
            int position = entry.position();
            int slot = slots.get(i);
            ItemStack preloadedSkull = skullCache.get(i);

            String formattedBalance = this.decimalFormatter.format(entry.account().balance());
            LeaderboardItemRepresenter item = this.config.playerItems.template;

            Formatter formatter = new Formatter()
                .register("{POSITION}", position)
                .register("{PLAYER}", entry.account().name())
                .register("{BALANCE}", entry.account().balance()::toPlainString)
                .register("{FORMATTED_BALANCE}", formattedBalance);

            GuiItem guiItem = item.asGuiItemPreloaded(formatter, event -> {}, preloadedSkull);
            gui.setItem(slot, guiItem);
        }
    }

    private void renderPagination(Gui gui, Player player, int current, int max, int players) {
        Formatter formatter = new Formatter()
            .register("{CURRENT_PAGE}", current)
            .register("{TOTAL_PAGES}", max)
            .register("{TOTAL_PLAYERS}", players);

        LeaderboardItemRepresenter item1 =
            current > 1 ? this.config.paginationItems.previousPage : this.config.paginationItems.previousPageDisabled;
        GuiItem guiItem1 = item1.asGuiItem(
            this.skullAPI, formatter, e2 -> {
                if (!(current > 1)) {
                    return;
                }

                ((Runnable) () -> open(player, current - 1)).run();
            });

        gui.setItem(item1.slot, guiItem1);

        LeaderboardItemRepresenter item =
            current < max ? this.config.paginationItems.nextPage : this.config.paginationItems.nextPageDisabled;
        GuiItem guiItem = item.asGuiItem(
            this.skullAPI, formatter, e1 -> {
                if (!(current < max)) {
                    return;
                }

                ((Runnable) () -> open(player, current + 1)).run();
            });

        gui.setItem(item.slot, guiItem);

        gui.setItem(
            this.config.paginationItems.pageInfo.slot,
            this.config.paginationItems.pageInfo.asGuiItem(this.skullAPI, formatter, e -> {}));

        LeaderboardItemRepresenter close = this.config.paginationItems.closeButton;
        gui.setItem(close.slot, close.asGuiItem(this.skullAPI, e -> gui.close(player)));
    }
}
