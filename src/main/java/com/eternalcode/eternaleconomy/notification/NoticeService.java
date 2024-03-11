package com.eternalcode.eternaleconomy.notification;

import com.eternalcode.eternaleconomy.configuration.ConfigInterface;
import com.eternalcode.eternaleconomy.user.UserService;
import com.eternalcode.eternaleconomy.viewer.BukktiViewerProvider;
import com.eternalcode.eternaleconomy.viewer.Viewer;
import com.eternalcode.multification.Multification;
import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.platform.PlatformBroadcaster;
import com.eternalcode.multification.translation.TranslationProvider;
import com.eternalcode.multification.viewer.ViewerProvider;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NoticeService extends Multification<Viewer, ConfigInterface> {

    private final AudienceProvider audienceProvider;
    private final UserService userService;
    private final Server server;
    private final MessageProvider messageProvider;
    private final MiniMessage miniMessage;


    public NoticeService(AudienceProvider audienceProvider, UserService userService, Server server, MessageProvider messageProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.userService = userService;
        this.server = server;
        this.messageProvider = messageProvider;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull ViewerProvider<Viewer> viewerProvider() {
        return new BukktiViewerProvider(this.userService, this.server);
    }

    @Override
    protected @NotNull PlatformBroadcaster platformBroadcaster() {
        return PlatformBroadcaster.withSerializer(this.miniMessage);
    }

    @Override
    protected @NotNull TranslationProvider<ConfigInterface> translationProvider() {
        return this.messageProvider;
    }

    @Override
    protected @NotNull AudienceConverter<Viewer> audienceConverter() {

        return viewer -> {
            if (viewer.isConsole()) {
                return audienceProvider.console();
            }
            return audienceProvider.player(viewer.getUniqueId());
        };
    }
}