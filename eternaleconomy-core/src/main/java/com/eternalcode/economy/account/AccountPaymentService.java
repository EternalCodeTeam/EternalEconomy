package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;

public class AccountPaymentService {

    private final NoticeService noticeService;
    private final AccountRepository accountRepository;
    private final DecimalFormatter formatter;

    public AccountPaymentService(
            NoticeService noticeService,
            AccountRepository accountRepository,
            DecimalFormatter formatter
    ) {
        this.noticeService = noticeService;
        this.accountRepository = accountRepository;
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

        BigDecimal subtractedBalance = payer.balance().subtract(amount);
        BigDecimal addedBalance = receiver.balance().add(amount);

        this.update(payer, subtractedBalance);
        this.update(receiver, addedBalance);

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

        this.update(account, amount);
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

        BigDecimal added = account.balance().add(amount);
        this.update(account, added);
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

        BigDecimal subtracted = account.balance().subtract(amount);
        this.update(account, subtracted);
        this.noticeService.create()
                .notice(notice -> notice.removeBalance)
                .placeholder("{REMOVED}", this.formatter.format(amount))
                .player(account.uuid())
                .send();

        return true;
    }

    private void update(Account account, BigDecimal newBalance) {
        Account updatedAccount = new Account(account.uuid(), account.name(), newBalance);
        this.accountRepository.save(updatedAccount);
    }
}