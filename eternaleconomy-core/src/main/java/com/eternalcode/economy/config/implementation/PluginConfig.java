package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.config.section.DatabaseSection;
import com.eternalcode.economy.database.DatabaseDriverType;
import com.eternalcode.economy.database.DatabaseSettings;
import com.eternalcode.economy.format.EconomyUnit;
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
        public List<EconomyUnit> format = Arrays.asList(
                new EconomyUnit(1_000L, 'k'),
                new EconomyUnit(1_000_000L, 'm'),
                new EconomyUnit(1_000_000_000L, 'g'),
                new EconomyUnit(1_000_000_000_000L, 't'),
                new EconomyUnit(1_000_000_000_000_000L, 'p'),
                new EconomyUnit(1_000_000_000_000_000_000L, 'e')
        );
    }
}
