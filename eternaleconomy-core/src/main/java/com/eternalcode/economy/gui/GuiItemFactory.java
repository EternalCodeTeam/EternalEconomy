package com.eternalcode.economy.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Factory for creating ItemStack from GuiItemRepresenter configuration.
 */
public class GuiItemFactory {

    private final MiniMessage miniMessage;

    public GuiItemFactory(MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    /**
     * Creates ItemStack from configuration.
     */
    public ItemStack createItem(GuiItemRepresenter config) {
        return createItem(config, Map.of());
    }

    /**
     * Creates ItemStack from configuration with placeholders.
     */
    public ItemStack createItem(GuiItemRepresenter config, Map<String, String> placeholders) {
        UnaryOperator<String> replacer = text -> {
            String result = text;
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
            return result;
        };

        return createItem(config, replacer);
    }

    /**
     * Creates ItemStack from configuration with placeholder replacement function.
     */
    public ItemStack createItem(GuiItemRepresenter config, UnaryOperator<String> placeholderReplacer) {
        ItemStack item = new ItemStack(config.material, config.amount);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        // Name
        if (config.name != null && !config.name.isEmpty()) {
            String processedName = placeholderReplacer.apply(config.name);
            meta.displayName(this.miniMessage.deserialize(processedName));
        }

        // Lore
        if (config.lore != null && !config.lore.isEmpty()) {
            List<Component> loreComponents = config.lore.stream()
                .map(placeholderReplacer)
                .map(this.miniMessage::deserialize)
                .collect(Collectors.toList());
            meta.lore(loreComponents);
        }

        // Custom Model Data
        if (config.customModelData != null) {
            meta.setCustomModelData(config.customModelData);
        }

        // Glow effect
        if (config.glow) {
            Enchantment unbreaking = Registry.ENCHANTMENT.get(org.bukkit.NamespacedKey.minecraft("unbreaking"));
            if (unbreaking != null) {
                meta.addEnchant(unbreaking, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        // Hide flags
        if (config.hideFlags) {
            meta.addItemFlags(ItemFlag.values());
        }

        // Head texture
        if (config.material == Material.PLAYER_HEAD && config.texture != null) {
            applyHeadTexture((SkullMeta) meta, config.texture);
        }

        item.setItemMeta(meta);
        return item;
    }

    private void applyHeadTexture(SkullMeta meta, String texture) {
        // Base64 texture (longer than typical player name)
        if (texture.length() > 32) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", texture));
            meta.setPlayerProfile(profile);
        } else {
            // Player name
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(texture));
        }
    }
}
