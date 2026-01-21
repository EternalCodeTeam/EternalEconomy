package com.eternalcode.economy.config.implementation;

import com.eternalcode.economy.config.item.ConfigItem;
import com.eternalcode.economy.config.item.WithdrawItemEntry;
import com.eternalcode.economy.database.DatabaseConfig;
import com.eternalcode.economy.format.DecimalUnit;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
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

        @Comment("Should leaderboard command show GUI instead of chat message")
        public boolean showLeaderboardGui = true;

        @Comment("Interval for refreshing the leaderboard cache")
        public java.time.Duration leaderboardRefreshInterval = java.time.Duration.ofSeconds(30);

        @Comment("Currency item settings")
        public WithdrawItem withdraw = new WithdrawItem();

        public static class Units extends OkaeriConfig {

                public List<DecimalUnit> format = List.of(
                                new DecimalUnit(1_000L, 'k'),
                                new DecimalUnit(1_000_000L, 'm'),
                                new DecimalUnit(1_000_000_000L, 'b'),
                                new DecimalUnit(1_000_000_000_000L, 't'),
                                new DecimalUnit(1_000_000_000_000_000L, 'p'),
                                new DecimalUnit(1_000_000_000_000_000_000L, 'e'));
        }

        public static class WithdrawItem extends OkaeriConfig {

                @Comment("Cooldown in seconds between withdraw commands (0 = disabled)")
                public int cooldownSeconds = 5;

                @Comment("Default item used when multi-item system is disabled or as fallback")
                public ConfigItem item = ConfigItem.builder()
                                .withName("<white>Check worth <green>{VALUE}$")
                                .withLore(List.of("<gray>Right click to redeem"))
                                .withMaterial(Material.PAPER)
                                .withTexture(0)
                                .withGlow(true)
                                .build();

                @Comment({
                                "Enable multi-item system: different items for different banknote values",
                                "When disabled, always uses the default 'item' above"
                })
                public boolean multiItemEnabled = false;

                @Comment({
                                "Item configurations for specific value thresholds",
                                "System selects the entry with highest minValue <= banknote value",
                                "Example: minValue 1.0 = coins, minValue 100.0 = banknotes"
                })
                public List<WithdrawItemEntry> multiItemEntries = List.of(
                                new WithdrawItemEntry(
                                                BigDecimal.ONE,
                                                ConfigItem.builder()
                                                                .withName("<white>Coin worth <green>{VALUE}$")
                                                                .withLore(List.of("<gray>Right click to redeem"))
                                                                .withMaterial(Material.GOLD_NUGGET)
                                                                .withTexture(1)
                                                                .withGlow(false)
                                                                .build()),
                                new WithdrawItemEntry(
                                                BigDecimal.valueOf(100),
                                                ConfigItem.builder()
                                                                .withName("<white>Banknote worth <green>{VALUE}$")
                                                                .withLore(List.of("<gray>Right click to redeem"))
                                                                .withMaterial(Material.PAPER)
                                                                .withTexture(100)
                                                                .withGlow(true)
                                                                .build()));
        }
}
