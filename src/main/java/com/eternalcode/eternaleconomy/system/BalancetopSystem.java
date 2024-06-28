package com.eternalcode.eternaleconomy.system;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfigImpl;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BalancetopSystem {

    private final UserService userService;
    private final PluginConfigImpl configuration;

    public BalancetopSystem(UserService userService, PluginConfigImpl configuration) {
        this.userService = userService;
        this.configuration = configuration;
    }

    public Gui balanceTopGui() throws NullPointerException {
        List<User> users = getTopUsers(10);
        List<GuiItem> items = new ArrayList<>();
        List<Integer> slots = configuration.balanceTop.balance_top_slots;

        Component title = Component.text("BalanceTop ", NamedTextColor.YELLOW)
            .append(Component.text("#10", NamedTextColor.RED));

        Gui gui = Gui.gui()
            .title(title)
            .rows(3)
            .disableAllInteractions()
            .create();


        for (User user : users) {
            if (user != null) {
                GuiItem guiItem = ItemBuilder.from(getHead(user.getUniqueId())).name(Component.text(""
                    + user.getName())).asGuiItem();
                items.add(guiItem);
            } else {
                GuiItem guiItem = ItemBuilder.from(Material.BARRIER).name(Component.text("NULL")).asGuiItem();
                items.add(guiItem);
            }
        }

        for (GuiItem guiItem : items) {
            for (int slot : slots) {
                gui.setItem(slot, guiItem);
            }
        }
        return gui;
    }

    public PaginatedGui balanceTopGui(int numberOfPlayers) {

        List<User> users = getTopUsers(numberOfPlayers);
        List<Integer> slots = configuration.balanceTop.balance_top_slots;
        List<Integer> slotsToFill = new ArrayList<>();
        int rows = 4;

        for (int i = 0; i < (rows * 9) - 1; i++) {
            if (!slots.contains(i)) {
                slotsToFill.add(i);
            }
        }


        Component title = Component.text("BalanceTop ", NamedTextColor.YELLOW)
            .append(Component.text("Top " + numberOfPlayers, NamedTextColor.RED));

        PaginatedGui gui = Gui.paginated()
            .title(title)
            .rows(rows)
            .disableAllInteractions()
            .create();

        for (int slot : slotsToFill) {
            gui.setItem(slot, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        }

        gui.setItem(30, ItemBuilder.from(Material.PAPER)
            .name(Component.text("Poprzednia Strona", NamedTextColor.YELLOW))
            .asGuiItem(event -> {
                gui.previous();
                gui.setItem(31, ItemBuilder.from(Material.PAPER)
                    .name(Component.text("Strona: " + gui.getCurrentPageNum(), NamedTextColor.YELLOW))
                    .asGuiItem());
            }));

        gui.setItem(31, ItemBuilder.from(Material.PAPER)
            .name(Component.text("Strona: " + gui.getCurrentPageNum(), NamedTextColor.YELLOW))
            .asGuiItem());

        gui.setItem(32, ItemBuilder.from(Material.PAPER)
            .name(Component.text("Następna Strona", NamedTextColor.YELLOW))
            .asGuiItem(event -> {
                gui.next();
                gui.setItem(31, ItemBuilder.from(Material.PAPER)
                    .name(Component.text("Strona: " + gui.getCurrentPageNum(), NamedTextColor.YELLOW))
                    .asGuiItem());
            }));


        for (User user : users) {
            GuiItem guiItem = ItemBuilder.from(getHead(user.getUniqueId()))
                .name(Component.text(user.getName()))
                .asGuiItem();

            gui.addItem(guiItem);
        }
        if (users.size() < 10) {
            for (int i = users.size(); i < 11 - users.size(); i++) {
                gui.addItem(ItemBuilder.from(Material.BARRIER).name(Component.text("NULL")).asGuiItem());
            }
        }

        return gui;
    }

    private static void fillSlots(Gui gui, List<Integer> slots) {
        for (int slot : slots) {
            gui.setItem(slot, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem(event -> event.setCancelled(true)));
        }
    }

    public static ItemStack getHead(UUID uuid) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            skullMeta.setOwningPlayer(player);

            String displayName = ChatColor.translateAlternateColorCodes('&', "&c&l" + player.getName());
            skullMeta.setDisplayName(displayName);

            head.setItemMeta(skullMeta);
        }

        return head;
    }

    private List<User> getTopUsers(int amount) {
        if (userService == null) {
            throw new IllegalStateException("UserService is not initialized");
        }
        return userService.users().stream()
            .sorted(Comparator.comparing(User::getBalance).reversed())
            .limit(amount)
            .collect(Collectors.toList());
    }

    public void openGui(Player player, Gui gui) {
        gui.open(player);
    }

    public void openPaginatedGui(Player player, PaginatedGui gui) {
        gui.open(player);
    }
}
