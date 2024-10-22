package com.eternalcode.economy.command.cooldown;

import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.time.Duration;

import static com.eternalcode.economy.config.implementation.messages.MessageConfig.*;

public class CommandCooldownConfig extends OkaeriConfig {
    @Comment("Duration of the cooldown (e.g. 5s, 10m, 1h)")
    public Duration duration = Duration.ofSeconds(5);
    @Comment("Permission for admins to bypass the cooldown")
    public String bypassPermission = "eternaleconomy.player.pay.bypass";
    public Notice message = Notice.builder()
        .chat(messagesPrefix + "<white>You must wait <gradient:#00FFA2:#34AE00>{TIME}</gradient> before using /pay again.")
        .actionBar("<gradient:#00FFA2:#34AE00>Wait {TIME}!</gradient>")
        .build();
}
