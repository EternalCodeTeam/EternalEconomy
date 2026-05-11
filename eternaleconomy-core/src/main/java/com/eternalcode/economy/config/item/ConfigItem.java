package com.eternalcode.economy.config.item;

import eu.okaeri.configs.OkaeriConfig;
import java.util.List;
import org.bukkit.Material;

public class ConfigItem extends OkaeriConfig {

    private final String name;
    private final List<String> lore;
    private final Material material;
    private final Integer texture;
    private final boolean glow;

    public ConfigItem(String name, List<String> lore, Material material, Integer texture, boolean glow) {
        this.name = name;
        this.lore = lore != null ? lore : List.of();
        this.material = material;
        this.texture = texture;
        this.glow = glow;
    }

    public ConfigItem() {
        this.name = "§6Item";
        this.lore = List.of("§7This is an item");
        this.material = Material.PLAYER_HEAD;
        this.texture = null;
        this.glow = false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String name() {
        return this.name;
    }

    public List<String> lore() {
        return this.lore;
    }

    public Material material() {
        return this.material;
    }

    public Integer texture() {
        return this.texture;
    }

    public boolean glow() {
        return this.glow;
    }

    public static class Builder {
        private String name = "§6Item";
        private List<String> lore = List.of("§7This is an item");
        private Material material = Material.PLAYER_HEAD;
        private Integer texture = null;
        private boolean glow = false;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLore(List<String> lore) {
            this.lore = lore != null ? lore : List.of();
            return this;
        }

        public Builder withMaterial(Material material) {
            this.material = material;
            return this;
        }

        public Builder withTexture(Integer texture) {
            this.texture = texture;
            return this;
        }

        public Builder withGlow(boolean glow) {
            this.glow = glow;
            return this;
        }

        public ConfigItem build() {
            return new ConfigItem(this.name, this.lore, this.material, this.texture, this.glow);
        }
    }
}
