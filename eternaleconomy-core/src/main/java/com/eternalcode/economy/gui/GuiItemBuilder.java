package com.eternalcode.economy.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

/**
 * Fluent builder for GuiItemRepresenter.
 * Used for creating default values in configs.
 */
public final class GuiItemBuilder {

    private final GuiItemRepresenter item = new GuiItemRepresenter();

    private GuiItemBuilder(String name) {
        this.item.name = name;
    }

    public static GuiItemBuilder item(String name) {
        return new GuiItemBuilder(name);
    }

    public GuiItemBuilder material(Material material) {
        this.item.material = material;
        return this;
    }

    public GuiItemBuilder lore(String... lines) {
        this.item.lore = List.of(lines);
        return this;
    }

    public GuiItemBuilder lore(List<String> lines) {
        this.item.lore = new ArrayList<>(lines);
        return this;
    }

    public GuiItemBuilder action(GuiAction action) {
        this.item.action = action;
        return this;
    }

    public GuiItemBuilder action(GuiAction action, String command) {
        this.item.action = action;
        this.item.command = command;
        return this;
    }

    public GuiItemBuilder customModelData(int cmd) {
        this.item.customModelData = cmd;
        return this;
    }

    public GuiItemBuilder amount(int amount) {
        this.item.amount = amount;
        return this;
    }

    public GuiItemBuilder texture(String texture) {
        this.item.texture = texture;
        return this;
    }

    public GuiItemBuilder glow(boolean glow) {
        this.item.glow = glow;
        return this;
    }

    public GuiItemBuilder hideFlags(boolean hideFlags) {
        this.item.hideFlags = hideFlags;
        return this;
    }

    public GuiItemRepresenter build() {
        return this.item;
    }
}
