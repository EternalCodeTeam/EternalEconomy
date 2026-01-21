package com.eternalcode.economy.config.item;

import eu.okaeri.configs.OkaeriConfig;
import java.math.BigDecimal;
import java.util.List;
import org.bukkit.Material;

public class WithdrawItemEntry extends OkaeriConfig {

    private final BigDecimal minValue;
    private final ConfigItem item;

    public WithdrawItemEntry(BigDecimal minValue, ConfigItem item) {
        this.minValue = minValue;
        this.item = item;
    }

    public WithdrawItemEntry() {
        this.minValue = BigDecimal.ONE;
        this.item = ConfigItem.builder()
            .withName("<white>Coin worth <green>{VALUE}$")
            .withLore(List.of("<gray>Right click to redeem"))
            .withMaterial(Material.GOLD_NUGGET)
            .withTexture(1)
            .withGlow(false)
            .build();
    }

    public BigDecimal minValue() {
        return this.minValue;
    }

    public ConfigItem item() {
        return this.item;
    }
}
