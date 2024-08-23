package com.eternalcode.economy.account;

import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;

final class AccountUtil {

    AccountUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static boolean isNegative(NoticeService noticeService, Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        noticeService.create()
                .notice(notice -> notice.invalidAmount)
                .player(account.uuid())
                .send();

        return true;
    }
}
