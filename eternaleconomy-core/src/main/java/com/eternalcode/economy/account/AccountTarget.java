package com.eternalcode.economy.account;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public sealed interface AccountTarget {

    Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}(-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}$");
    Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.]{1,16}$");

    static Optional<AccountTarget> parse(String input) {
        if (UUID_PATTERN.matcher(input).matches()) {
            return Optional.of(new ByUuid(UUID.fromString(input)));
        }

        if (NAME_PATTERN.matcher(input).matches()) {
            return Optional.of(new ByName(input));
        }

        return Optional.empty();
    }

    record ByUuid(UUID uuid) implements AccountTarget {
    }

    record ByName(String name) implements AccountTarget {
    }
}
