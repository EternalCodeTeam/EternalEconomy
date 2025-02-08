package com.eternalcode.economy.format;

import com.eternalcode.economy.config.implementation.PluginConfig;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DurationFormatter {

    private final PluginConfig pluginConfig;

    public DurationFormatter(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public String format(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.pluginConfig.timePatternFormat);

        return formatter.format(zonedDateTime);
    }

}
