package com.eternalcode.economy.gui;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

/**
 * Representation of a GUI item in configuration.
 * Serializable by OkaeriConfig.
 */
public class GuiItemRepresenter extends OkaeriConfig {

    @Comment("Item name (supports MiniMessage)")
    public String name = "";

    @Comment("Item material")
    public Material material = Material.STONE;

    @Comment("Item lore (list of lines, supports MiniMessage)")
    public List<String> lore = new ArrayList<>();

    @Comment("Action on click")
    public GuiAction action = GuiAction.NONE;

    @Comment("Command to execute (for COMMAND/CONSOLE_COMMAND actions)")
    public String command = "";

    @Comment("Custom Model Data (for resource packs)")
    public Integer customModelData = null;

    @Comment("Amount of items in stack")
    public int amount = 1;

    @Comment("Player head texture (Base64 or player name)")
    public String texture = null;

    @Comment("Should item glow (enchantment effect)")
    public boolean glow = false;

    @Comment("Hide item flags (enchantments, attributes, etc.)")
    public boolean hideFlags = true;

    public GuiItemRepresenter() {
    }

    public GuiItemRepresenter(String name, Material material) {
        this.name = name;
        this.material = material;
    }
}
