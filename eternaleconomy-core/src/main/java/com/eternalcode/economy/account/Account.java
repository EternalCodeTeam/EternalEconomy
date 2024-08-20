package com.eternalcode.economy.account;

import java.math.BigDecimal;
import java.util.UUID;

public record Account(UUID uuid, String name, BigDecimal balance) {

}