package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import com.eternalcode.economy.config.implementation.PluginConfig;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class LeaderboardServiceBenchmark {

    private LeaderboardService leaderboardService;

    @Setup
    public void setUp() {
        AccountRepositoryInMemory accountRepository = new AccountRepositoryInMemory();

        PluginConfig pluginConfig = new PluginConfig();
        pluginConfig.leaderboardSize = 100;

        this.leaderboardService = new LeaderboardService(accountRepository, pluginConfig);

        for (int i = 0; i < 10_000; i++) {
            UUID uuid = UUID.randomUUID();
            String name = "Player" + i;
            BigDecimal balance = BigDecimal.valueOf(10_000 - i);
            Account account = new Account(uuid, name, balance);
            accountRepository.save(account);
        }

        this.leaderboardService.updateLeaderboard();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    public void benchmarkUpdateLeaderboard(Blackhole blackhole) {
        this.leaderboardService.updateLeaderboard();
        blackhole.consume(this.leaderboardService.getLastUpdated());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    public void benchmarkGetLeaderboard(Blackhole blackhole) {
        Collection<Account> leaderboard = this.leaderboardService.getLeaderboard();
        blackhole.consume(leaderboard);
    }
}
