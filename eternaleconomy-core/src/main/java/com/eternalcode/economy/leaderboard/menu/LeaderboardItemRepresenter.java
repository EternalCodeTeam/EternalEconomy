package com.eternalcode.economy.leaderboard.menu;

import com.eternalcode.economy.MiniMessageHolder;
import com.eternalcode.multification.shared.Formatter;
import dev.rollczi.liteskullapi.SkullAPI;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.configs.annotation.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LeaderboardItemRepresenter implements Serializable, MiniMessageHolder {

    @Exclude
    private static final Component RESET_ITEM = Component.text()
        .decoration(TextDecoration.ITALIC, false)
        .build();

    public Material material;
    public String name;
    public List<String> lore;
    public boolean glow;
    public String texture;
    public int slot;

    private LeaderboardItemRepresenter(
        Material material,
        String name,
        List<String> lore,
        boolean glow,
        String texture,
        int slot
    ) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
        this.texture = texture;
        this.slot = slot;
    }

    public LeaderboardItemRepresenter() {
    }

    public static LeaderboardItemRepresenter of(
        Material material,
        String name,
        List<String> lore,
        boolean glow,
        String texture,
        int slot
    ) {
        return new LeaderboardItemRepresenter(material, name, lore, glow, texture, slot);
    }

    public GuiItem asGuiItem(SkullAPI skullAPI, Formatter formatter, GuiAction<InventoryClickEvent> action) {
        return createGuiItem(skullAPI, formatter, action, null);
    }

    public GuiItem asGuiItem(SkullAPI skullAPI, GuiAction<InventoryClickEvent> action) {
        return this.asGuiItem(skullAPI, new Formatter(), action);
    }

    public GuiItem asGuiItemPreloaded(
        Formatter formatter,
        GuiAction<InventoryClickEvent> action,
        ItemStack preloadedSkull
    ) {
        return createGuiItem(null, formatter, action, preloadedSkull);
    }

    private GuiItem createGuiItem(
        SkullAPI skullAPI,
        Formatter formatter,
        GuiAction<InventoryClickEvent> action,
        ItemStack preloadedSkull
    ) {
        int size = this.lore.size();
        List<Component> loreComponents = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String formatted = formatter.format(this.lore.get(i));
            loreComponents.add(RESET_ITEM.append(MINI_MESSAGE.deserialize(formatted)));
        }

        Component nameComponent = RESET_ITEM.append(MINI_MESSAGE.deserialize(formatter.format(this.name)));

        ItemBuilder builder;

        if (preloadedSkull != null) {
            builder = ItemBuilder.from(preloadedSkull);
        }
        else if (this.texture != null && !this.texture.equals("none") && skullAPI != null) {
            builder = ItemBuilder.from(Material.PLAYER_HEAD);
        }
        else {
            builder = ItemBuilder.from(this.material);
        }

        builder.name(nameComponent)
            .lore(loreComponents)
            .glow(this.glow);

        GuiItem guiItem = builder.asGuiItem(action);

        if (preloadedSkull == null && this.texture != null && !this.texture.equals("none") && skullAPI != null) {
            skullAPI.getSkull(this.texture).thenAccept(guiItem::setItemStack);
        }

        return guiItem;
    }
}
