package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, jvmArgs = {"-Xms4G", "-Xmx8G", "-XX:+UseG1GC"})
public class LeaderboardServiceBenchmark {

    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGES = 50;

    @Param({"100000", "1000000", "2000000"})
    private int accountsCount;

    private LeaderboardService leaderboardService;
    private List<Account> targetAccounts;

    @Setup(Level.Trial)
    public void setUp() {
        AccountRepositoryInMemory repository = new AccountRepositoryInMemory();
        AccountManager accountManager = new AccountManager(repository);
        this.leaderboardService = new LeaderboardService(accountManager);
        this.targetAccounts = new ArrayList<>();

        ParetoDistribution pareto = new ParetoDistribution(1.0, 2.0);
        for (int i = 0; i < accountsCount; i++) {
            String name = "user_" + i;
            BigDecimal balance = BigDecimal.valueOf(pareto.sample()).min(BigDecimal.valueOf(100_000));
            Account account = new Account(UUID.randomUUID(), name, balance);
            accountManager.create(account);
        }

        TreeSet<Account> accounts = accountManager.getAccountsByBalanceSet();
        List<Account> allAccounts = new ArrayList<>(accounts);
        int size = allAccounts.size();
        targetAccounts.add(allAccounts.get(size / 10));
        targetAccounts.add(allAccounts.get(size / 2));
        targetAccounts.add(allAccounts.get(size - size / 10));
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
}
