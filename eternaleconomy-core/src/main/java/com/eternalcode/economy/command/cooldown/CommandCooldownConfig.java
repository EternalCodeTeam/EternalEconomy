package com.eternalcode.economy.command.cooldown;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

import java.time.Duration;

public class CommandCooldownConfig extends OkaeriConfig {
    public Duration duration = Duration.ofSeconds(5);
    public String bypassPermission = "eternaleconomy.player.pay.bypass";
    public Notice message = Notice.builder()
        .chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
            + "<white>You must wait <gradient:#00FFA2:#34AE00>{TIME}</gradient> before using /pay again.")
        .actionBar("<gradient:#00FFA2:#34AE00>Wait {TIME}!</gradient>")
        .build();
}
