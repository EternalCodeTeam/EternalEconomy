package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.command.cooldown.CommandCooldownConfig;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.Map;

public class CommandsConfig extends OkaeriConfig {

    @Comment("Cooldowns for commands")
    public Map<String, CommandCooldownConfig> cooldowns = Map.of(
        "pay", new CommandCooldownConfig()
    );

}
