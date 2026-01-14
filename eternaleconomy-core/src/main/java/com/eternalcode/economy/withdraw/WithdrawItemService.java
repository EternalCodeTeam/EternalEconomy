package com.eternalcode.economy.withdraw;

import java.math.BigDecimal;
import org.bukkit.inventory.ItemStack;

public interface WithdrawItemService {

    BigDecimal getValue(ItemStack itemStack);

    boolean isBanknote(ItemStack itemStack);

    ItemStack createBanknote(BigDecimal value);
}
