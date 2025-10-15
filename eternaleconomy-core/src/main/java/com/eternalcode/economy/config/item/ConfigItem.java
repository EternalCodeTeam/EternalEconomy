package com.eternalcode.economy.config.item;

import eu.okaeri.configs.OkaeriConfig;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class ConfigItem extends OkaeriConfig {

    public String name = "&6Item";
    public List<String> lore = Collections.singletonList("&7This is an item");
    public Material material = Material.PLAYER_HEAD;
    public Integer texture = null;
    public boolean glow = false;

    public ConfigItem(String name, List<String> lore, Material material, Integer texture, boolean glow) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.texture = texture;
        this.glow = glow;
    }

    public ConfigItem() {

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
        private final ConfigItem configItem = new ConfigItem();

        public Builder withName(String name) {
            this.configItem.name = name;

            return this;
        }

        public Builder withLore(List<String> lore) {
            this.configItem.lore = lore;

            return this;
        }

        public Builder withMaterial(Material material) {
            this.configItem.material = material;

            return this;
        }

        public Builder withTexture(Integer texture) {
            this.configItem.texture = texture;

            return this;
        }

        public Builder withGlow(boolean glow) {
            this.configItem.glow = glow;

            return this;
        }

        public ConfigItem build() {
            return this.configItem;
        }
    }
}
