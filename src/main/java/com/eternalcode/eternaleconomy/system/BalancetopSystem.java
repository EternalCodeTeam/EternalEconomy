package com.eternalcode.eternaleconomy.system;

import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BalancetopSystem {

    private final UserService userService;

    public BalancetopSystem(UserService userService) {
        this.userService = userService;
    }

    public Gui balanceTopGui() {
        List<User> users = getTopUsers(10);
        Gui gui = Gui.gui().title(Component.text("&6BalanceTop &c#10")).rows(5).type(GuiType.CHEST).create();
        List<GuiItem> items = new ArrayList<>();
        for (User user : users) {
            if (user != null) {
                GuiItem guiItem = ItemBuilder.from(Material.STONE).name(Component.text("" + user.getName())).asGuiItem();
                items.add(guiItem);
            } else {
                GuiItem guiItem = ItemBuilder.from(Material.BARRIER).name(Component.text("NULL")).asGuiItem();
                items.add(guiItem);
            }
        }
        for(int i =0; i<11; i++){
            GuiItem guiItem = ItemBuilder.from(Material.BARRIER).name(Component.text("NULL")).asGuiItem();
            gui.addItem(guiItem);
        }
        for(User user : users){
            System.out.println(user.getName());
        }
      //  for(GuiItem guiItem : items){
       //     System.out.println(guiItem.toString());
        //}
        return gui;
    }

    public Gui balanceTopGui(int numberOfPlayers) {
        Gui gui = Gui.gui().title(Component.text("&6BalanceTop &c#10")).rows(5).type(GuiType.CHEST).create();
        return gui;
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
