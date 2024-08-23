package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.config.section.DatabaseSection;
import com.eternalcode.economy.format.DecimalUnit;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.Arrays;
import java.util.List;

public class PluginConfig extends OkaeriConfig {

    @Comment("Units settings")
    public Units units = new Units();

    @Comment("Database settings")
    public DatabaseSection database = new DatabaseSection();

    public static class Units extends OkaeriConfig {
        public List<DecimalUnit> format = Arrays.asList(
                new DecimalUnit(1_000L, 'k'),
                new DecimalUnit(1_000_000L, 'm'),
                new DecimalUnit(1_000_000_000L, 'g'),
                new DecimalUnit(1_000_000_000_000L, 't'),
                new DecimalUnit(1_000_000_000_000_000L, 'p'),
                new DecimalUnit(1_000_000_000_000_000_000L, 'e')
        );
    }
}
