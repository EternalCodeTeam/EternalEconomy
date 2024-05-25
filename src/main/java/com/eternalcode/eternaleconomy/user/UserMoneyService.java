package com.eternalcode.eternaleconomy.user;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import pl.auroramc.commons.decimal.DecimalFormatter;

public class UserMoneyService {

    private final UserService userService;

    public UserMoneyService(UserService userService) {
        this.userService = userService;
    }

    public BigDecimal getBalance(UUID uniqueId) {
        Optional<User> user = this.userService.getUser(uniqueId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return user.get().getBalance();
    }

    public String getFormattedBalance(UUID uniqueId) {
        Optional<User> user = this.userService.getUser(uniqueId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        BigDecimal balance = user.get().getBalance();
        return DecimalFormatter.getFormattedDecimal(balance);
    }
}
