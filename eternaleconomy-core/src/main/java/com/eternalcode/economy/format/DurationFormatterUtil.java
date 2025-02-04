package com.eternalcode.economy.format;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DurationFormatterUtil {

    private static final String PATTERN_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String ZONE = "Europe/Warsaw";

    private DurationFormatterUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String format(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(ZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
        return formatter.format(zonedDateTime);
    }

}
