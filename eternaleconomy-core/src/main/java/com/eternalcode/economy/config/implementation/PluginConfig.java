package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.config.section.DatabaseSection;
import com.eternalcode.economy.format.DecimalUnit;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
public class PluginConfig extends OkaeriConfig {

    @Comment("Units settings")
    public Units units = new Units();

    @Comment("Database settings")
    public DatabaseSection database = new DatabaseSection();

    @Comment("Default balance for new accounts")
    public BigDecimal defaultBalance = BigDecimal.valueOf(0.0);

    public static class Units extends OkaeriConfig {

        public List<DecimalUnit> format = Arrays.asList(
                new DecimalUnit(1_000L, 'k'),
                new DecimalUnit(1_000_000L, 'm'),
                new DecimalUnit(1_000_000_000L, 'b'),
                new DecimalUnit(1_000_000_000_000L, 't'),
                new DecimalUnit(1_000_000_000_000_000L, 'p'),
                new DecimalUnit(1_000_000_000_000_000_000L, 'e')
        );
    }
}
