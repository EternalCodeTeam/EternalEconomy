package com.eternalcode.economy.multification;

import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.bukkit.BukkitMultification;
import com.eternalcode.multification.translation.TranslationProvider;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NoticeService extends BukkitMultification<MessageConfig> {

    private final MessageConfig messageConfig;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NoticeService(MessageConfig messageConfig, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.messageConfig = messageConfig;
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull TranslationProvider<MessageConfig> translationProvider() {
        return locale -> this.messageConfig;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> {
            if (commandSender instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }
}