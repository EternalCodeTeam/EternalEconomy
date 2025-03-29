package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.database.DatabaseConfig;
import com.eternalcode.economy.format.DecimalUnit;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class PluginConfig extends OkaeriConfig {

    @Comment("Units settings")
    public Units units = new Units();

    @Comment("Database settings")
    public DatabaseConfig database = new DatabaseConfig();

    @Comment("Default balance for new accounts")
    public BigDecimal defaultBalance = BigDecimal.valueOf(0.0);

    @Comment("Limit on the amount of money sent in one transaction")
    public BigDecimal transactionLimit = BigDecimal.valueOf(1_000_000_000.0);

    @Comment("Limit of the entries per leaderboard page")
    public int leaderboardPageSize = 10;

    @Comment("Should leaderboard command show player's position in the leaderboard")
    public boolean showLeaderboardPosition = true;

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
