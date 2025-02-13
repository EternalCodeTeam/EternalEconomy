package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Measurement(iterations = 10, time = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class LeaderboardServiceBenchmark {

    private static final int ACCOUNTS_COUNT = 2_000_000;
    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGES = 50;

    private LeaderboardService leaderboardService;
    private List<Account> targetAccounts;

    @Setup(Level.Trial)
    public void setUp() {
        AccountRepositoryInMemory repository = new AccountRepositoryInMemory();
        AccountManager accountManager = new AccountManager(repository);
        this.leaderboardService = new LeaderboardService(accountManager);
        this.targetAccounts = new ArrayList<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Generowanie kont w batchach
        for (int i = 0; i < ACCOUNTS_COUNT; i++) {
            String name = "user_" + UUID.randomUUID().toString().substring(0, 8);
            BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 100_000));
            Account account = new Account(UUID.randomUUID(), name, balance);

            futures.add(CompletableFuture.runAsync(() -> accountManager.create(account)));

            if (i % 100_000 == 0) {
                targetAccounts.add(account);
            }
        }

        // Czekaj na zakończenie wszystkich operacji
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Benchmark
    public void benchmarkGetLeaderboardPage(Blackhole blackhole) {
        int randomPage = ThreadLocalRandom.current().nextInt(1, MAX_PAGES + 1);
        CompletableFuture<LeaderboardPage> future = leaderboardService.getLeaderboardPage(randomPage, PAGE_SIZE);
        blackhole.consume(future.join());
    }

    @Benchmark
    public void benchmarkGetLeaderboardPosition(Blackhole blackhole) {
        Account target = targetAccounts.get(ThreadLocalRandom.current().nextInt(targetAccounts.size()));
        CompletableFuture<LeaderboardEntry> future = leaderboardService.getLeaderboardPosition(target);
        blackhole.consume(future.join());
    }

    @Benchmark
    public void benchmarkPageIteration(Blackhole blackhole) {
        for (int page = 1; page <= MAX_PAGES; page++) {
            CompletableFuture<LeaderboardPage> future = leaderboardService.getLeaderboardPage(page, PAGE_SIZE);
            blackhole.consume(future.join());
        }
    }
}
