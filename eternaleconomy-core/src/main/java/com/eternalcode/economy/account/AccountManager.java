package com.eternalcode.economy.account;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.leaderboard.LeaderboardEntry;
import com.eternalcode.economy.leaderboard.LeaderboardPage;
import com.eternalcode.economy.leaderboard.LeaderboardService;
import com.google.common.collect.BoundType;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountManager implements LeaderboardService {

    private final Map<UUID, Account> accountByUniqueId = new HashMap<>();
    private final Map<String, Account> accountByName = new HashMap<>();
    private final TreeMap<String, Account> accountIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final TreeMultimap<BigDecimal, Account> accountsByBalance = TreeMultimap.create(
        Comparator.reverseOrder(),
        Comparator.comparing(Account::uuid)
    );
    private final TreeMultiset<Account> accountsByBalanceSet = TreeMultiset.create(
        Comparator.comparing(Account::balance, Comparator.reverseOrder())
            .thenComparing(Account::uuid)
    );

    private final AccountRepository accountRepository;
    private final Scheduler scheduler;

    public AccountManager(AccountRepository accountRepository, Scheduler scheduler) {
        this.accountRepository = accountRepository;
        this.scheduler = scheduler;
    }

    public static AccountManager create(AccountRepository accountRepository, Scheduler scheduler) {
        AccountManager accountManager = new AccountManager(accountRepository, scheduler);

        accountRepository.getAllAccounts().thenAccept(accounts -> {
            for (Account account : accounts) {
                accountManager.save(account);
            }
        });

        return accountManager;
    }

    public Account getAccount(UUID uuid) {
        return this.accountByUniqueId.get(uuid);
    }

    public Account getAccount(String name) {
        return this.accountByName.get(name);
    }

    public Account getOrCreate(UUID uuid, String name) {
        Account byUniqueId = this.accountByUniqueId.get(uuid);

        if (byUniqueId != null) {
            return byUniqueId;
        }

        Account byName = this.accountByName.get(name);

        if (byName != null) {
            return byName;
        }

        Account account = new Account(uuid, name, BigDecimal.ZERO);
        this.save(account);
        return account;
    }

    void save(Account newAccount) {
        Account oldAccount = this.accountByUniqueId.get(newAccount.uuid());

        if (oldAccount != null) {
            this.accountsByBalance.remove(oldAccount.balance(), oldAccount);
            this.accountsByBalanceSet.remove(oldAccount);
        }

        this.accountByUniqueId.put(newAccount.uuid(), newAccount);
        this.accountByName.put(newAccount.name(), newAccount);
        this.accountIndex.put(newAccount.name(), newAccount);
        this.accountsByBalance.put(newAccount.balance(), newAccount);
        this.accountsByBalanceSet.add(newAccount);

        this.accountRepository.save(newAccount);
    }

    public Collection<Account> getAccountStartingWith(String prefix) {
        return Collections.unmodifiableCollection(
            this.accountIndex.subMap(prefix, true, prefix + Character.MAX_VALUE, true).values()
        );
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(this.accountByUniqueId.values());
    }

    @Override
    public LeaderboardEntry getLeaderboardPosition(Account target) {
        int position = accountsByBalanceSet.headMultiset(target, BoundType.CLOSED).size();
        return new LeaderboardEntry(target, position);
    }

    @Override
    public LeaderboardPage getLeaderboardPage(int page, int pageSize) {
        List<LeaderboardEntry> list = new ArrayList<>();

        int totalEntries = accountsByBalance.size();
        int maxPages = (int) Math.ceil((double) totalEntries / pageSize);
        int nextPage = page + 1 < maxPages ? page + 1 : -1;

        int count = 0;
        int toSkip = page * pageSize;
        for (Collection<Account> accounts : accountsByBalance.asMap().values()) {
            if (count + accounts.size() < toSkip) {
                count += accounts.size();
                continue;
            }

            for (Account account : accounts) {
                if (list.size() >= pageSize) {
                    return new LeaderboardPage(list, page, maxPages, nextPage);
                }

                if (count++ < toSkip) {
                    continue;
                }

                list.add(new LeaderboardEntry(account, count));
            }
        }

        return new LeaderboardPage(list, page, maxPages, nextPage);
    }

}
