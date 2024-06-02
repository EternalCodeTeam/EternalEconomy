package com.eternalcode.eternaleconomy.system;

import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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

    public BalancetopSystem(UserService userService) {
        this.userService = userService;
    }

    public Gui balanceTopGui() throws NullPointerException{
        List<User> users = getTopUsers(10);
        Gui gui = Gui.gui().title(Component.text("&6BalanceTop &c#10")).rows(5).type(GuiType.CHEST).create();
        List<GuiItem> items = new ArrayList<>();
        for (User user : users) {
            if (user != null) {
                GuiItem guiItem = ItemBuilder.from(getHead(user.getUniqueId())).name(Component.text("" + user.getName())).asGuiItem();
                items.add(guiItem);
            } else {
                GuiItem guiItem = ItemBuilder.from(Material.BARRIER).name(Component.text("NULL")).asGuiItem();
                items.add(guiItem);
            }
        }
        try{
            for(User user : users){
                System.out.println(user.getName());
            }
        }catch(NullPointerException e){
            throw new NullPointerException();
        }

      for(GuiItem guiItem : items){
            gui.addItem(guiItem);
        }
        return gui;
    }

    public Gui balanceTopGui(int numberOfPlayers) {
        Gui gui = Gui.gui().title(Component.text("&6BalanceTop &c#10")).rows(5).type(GuiType.CHEST).create();
        return gui;
    }
    private static ItemStack getHead(UUID uuid){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        head.setItemMeta(meta);
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
    public List<User> tempGetUsers(int n) {
        return getTopUsers(n);
    }


    public void openGui(Player player, Gui gui){
        gui.open(player);
    }
}
