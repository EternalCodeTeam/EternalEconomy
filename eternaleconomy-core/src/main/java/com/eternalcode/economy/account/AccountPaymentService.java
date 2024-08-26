package com.eternalcode.economy.account;

import static com.eternalcode.economy.account.AccountUtil.isNegative;

import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import java.math.BigDecimal;
import org.bukkit.command.CommandSender;

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

    public boolean payment(Account payer, Account receiver, BigDecimal amount) {
        payer = new Account(payer.uuid(), payer.name(), payer.balance().subtract(amount));
        receiver = new Account(receiver.uuid(), receiver.name(), receiver.balance().add(amount));

        this.accountManager.save(payer);
        this.accountManager.save(receiver);

        return true;
    }

    public boolean setBalance(Account account, BigDecimal amount) {
        if (isNegative(this.noticeService, account, amount)) {
            return false;
        }

        account = new Account(account.uuid(), account.name(), amount);
        this.accountManager.save(account);

        this.noticeService.create()
                .notice(notice -> notice.admin.set)
                .placeholder("{AMOUNT}", this.formatter.format(amount))
                .player(account.uuid())
                .send();

        return true;
    }

    public boolean addBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().add(amount));
        this.accountManager.save(account);

        return true;
    }

    public boolean removeBalance(Account account, BigDecimal amount) {
        account = new Account(account.uuid(), account.name(), account.balance().subtract(amount));
        this.accountManager.save(account);

        return true;
    }

    public boolean resetBalance(Account account) {
        account = new Account(account.uuid(), account.name(), BigDecimal.ZERO);
        this.accountManager.save(account);

        return true;
    }
}