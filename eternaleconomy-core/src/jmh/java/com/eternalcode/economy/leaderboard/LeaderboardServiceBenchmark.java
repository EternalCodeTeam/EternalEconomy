package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
public class LeaderboardServiceBenchmark {

    private LeaderboardService leaderboardService;

    @Setup
    public void setUp() {
        AccountManager accountManager = new AccountManager(new AccountRepositoryInMemory());

        PluginConfig pluginConfig = new PluginConfig();
        pluginConfig.leaderboardEntriesPerPage = 100;

        this.leaderboardService = new LeaderboardService(accountManager);

        for (int i = 0; i < 10_000; i++) {
            UUID uuid = UUID.randomUUID();
            String name = "Player" + i;
            BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(0, 10_001));
            Account account = new Account(uuid, name, balance);
            accountManager.create(account);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkGetLeaderboard(Blackhole blackhole) {
        CompletableFuture<List<LeaderboardEntry>> future = this.leaderboardService.getLeaderboard();
        List<LeaderboardEntry> leaderboard = future.join();
        blackhole.consume(leaderboard);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkGetLeaderboardPage(Blackhole blackhole) {
        CompletableFuture<LeaderboardPage> future = this.leaderboardService.getLeaderboardPage(1, 100);
        LeaderboardPage leaderboardPage = future.join();
        blackhole.consume(leaderboardPage);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkGetLeaderboardPosition(Blackhole blackhole) {
        CompletableFuture<List<LeaderboardEntry>> future = this.leaderboardService.getLeaderboard();
        List<LeaderboardEntry> leaderboard = future.join();
        LeaderboardEntry targetAccount = leaderboard.iterator().next();

        CompletableFuture<LeaderboardEntry> positionFuture = this.leaderboardService.getLeaderboardPosition(targetAccount.account());
        LeaderboardEntry position = positionFuture.join();
        blackhole.consume(position);
    }
}
