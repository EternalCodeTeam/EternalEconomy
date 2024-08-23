package com.eternalcode.economy.account;

import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;

public class AccountPaymentService {

    private final NoticeService noticeService;
    private final AccountManager accountManager;
    private final DecimalFormatter formatter;

    public AccountPaymentService(
            NoticeService noticeService,
            AccountManager accountManager,
            DecimalFormatter formatter
    ) {
        this.noticeService = noticeService;
        this.accountManager = accountManager;
        this.formatter = formatter;
    }

    private static boolean validateBalance(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean payment(Account payer, Account receiver, BigDecimal amount) {
        if (!validateBalance(amount)) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .player(payer.uuid())
                    .send();
            return false;
        }

        if (payer.balance().compareTo(amount) < 0) {
            BigDecimal missingBalance = amount.subtract(payer.balance());
            String formattedMissingBalance = formatter.format(missingBalance);

            this.noticeService.create()
                    .notice(notice -> notice.insufficientBalance)
                    .placeholder("{MISSING_BALANCE}", formattedMissingBalance)
                    .player(payer.uuid())
                    .send();
            return false;
        }

        payer = new Account(payer.uuid(), payer.name(), payer.balance().subtract(amount));
        receiver = new Account(receiver.uuid(), receiver.name(), receiver.balance().add(amount));

        this.accountManager.save(payer);
        this.accountManager.save(receiver);

        String formattedAmount = this.formatter.format(amount);

        this.noticeService.create()
                .notice(notice -> notice.transferSuccess)
                .placeholder("{RECEIVER}", receiver.name())
                .placeholder("{AMOUNT}", formattedAmount)
                .player(payer.uuid())
                .send();

        this.noticeService.create()
                .notice(notice -> notice.transferReceived)
                .placeholder("{PAYER}", payer.name())
                .placeholder("{AMOUNT}", formattedAmount)
                .player(receiver.uuid())
                .send();

        return true;
    }

    public boolean setBalance(Account account, BigDecimal amount) {
        if (validateBalance(amount)) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .player(account.uuid())
                    .send();
            return false;
        }

        account = new Account(account.uuid(), account.name(), amount);
        this.accountManager.save(account);

        this.noticeService.create()
                .notice(notice -> notice.setBalance)
                .placeholder("{ADDED}", this.formatter.format(amount))
                .player(account.uuid())
                .send();

        return true;
    }

    public boolean addBalance(Account account, BigDecimal amount) {
        if (validateBalance(amount)) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .player(account.uuid())
                    .send();
            return false;
        }

        account = new Account(account.uuid(), account.name(), account.balance().add(amount));
        this.accountManager.save(account);

        this.noticeService.create()
                .notice(notice -> notice.addBalance)
                .placeholder("{ADDED}", this.formatter.format(amount))
                .player(account.uuid())
                .send();

        return true;
    }

    public boolean removeBalance(Account account, BigDecimal amount) {
        if (validateBalance(amount)) {
            this.noticeService.create()
                    .notice(notice -> notice.invalidAmount)
                    .player(account.uuid())
                    .send();
            return false;
        }

        if (account.balance().compareTo(amount) < 0) {
            this.noticeService.create()
                    .notice(notice -> notice.insufficientBalance)
                    .placeholder("{MISSING_BALANCE}", amount.subtract(account.balance()).toString())
                    .player(account.uuid())
                    .send();
            return false;
        }

        account = new Account(account.uuid(), account.name(), account.balance().subtract(amount));
        this.accountManager.save(account);

        this.noticeService.create()
                .notice(notice -> notice.removeBalance)
                .placeholder("{REMOVED}", this.formatter.format(amount))
                .player(account.uuid())
                .send();

        return true;
    }
}