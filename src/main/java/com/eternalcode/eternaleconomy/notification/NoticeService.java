package com.eternalcode.eternaleconomy.notification;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfig;
import com.eternalcode.eternaleconomy.user.UserService;
import com.eternalcode.multification.Multification;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.platform.PlatformBroadcaster;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NoticeService extends Multification<CommandSender, PluginConfig> {

    private final AudienceProvider audienceProvider;
    private final Server server;
    private final MessageProvider messageProvider;
    private final MiniMessage miniMessage;

    public NoticeService(
        AudienceProvider audienceProvider,
        Server server,
        MessageProvider messageProvider,
        MiniMessage miniMessage
    ) {
        this.audienceProvider = audienceProvider;
        this.server = server;
        this.messageProvider = messageProvider;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull ViewerProvider<CommandSender> viewerProvider() {
        return new MessageViewerProvider(this.server);
    }

    @Override
    protected @NotNull PlatformBroadcaster platformBroadcaster() {
        return PlatformBroadcaster.withSerializer(this.miniMessage);
    }

    @Override
    protected @NotNull TranslationProvider<PluginConfig> translationProvider() {
        return this.messageProvider;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return viewer -> {
            if (viewer instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }
}
