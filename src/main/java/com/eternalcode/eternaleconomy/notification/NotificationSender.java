package com.eternalcode.eternaleconomy.notification;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotificationSender {

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NotificationSender(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void send(CommandSender sender, Notification notification) {
        this.send(sender, notification.type(), notification.content());
    }

    public void send(CommandSender commandSender, NoticeTextType type, String content) {
        Audience audience = this.audience(commandSender);
        Component deserializedContent = this.miniMessage.deserialize(content);

        switch (type) {
            case CHAT -> audience.sendMessage(deserializedContent);
            case ACTION_BAR -> audience.sendActionBar(deserializedContent);
            case TITLE -> {
                Title title = Title.title(deserializedContent, Component.empty(), Title.DEFAULT_TIMES);
                audience.showTitle(title);
            }
            case SUBTITLE -> {
                Title title = Title.title(Component.empty(), deserializedContent, Title.DEFAULT_TIMES);
                audience.showTitle(title);
            }
            default -> throw new IllegalStateException("Unexpected notification type: " + type);
        }
    }

    private Audience audience(CommandSender sender) {
        if (sender instanceof Player player) {
            return this.audienceProvider.player(player.getUniqueId());
        }

        return this.audienceProvider.console();
    }
}
