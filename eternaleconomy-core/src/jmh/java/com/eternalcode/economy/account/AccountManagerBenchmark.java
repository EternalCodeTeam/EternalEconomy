package com.eternalcode.economy.account;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class AccountManagerBenchmark {

    private Map<String, String> hashMap;
    private NavigableMap<String, String> skipListMap;
    private String searchPrefix;

    @Setup(Level.Trial)
    public void setup() {
        this.hashMap = new ConcurrentHashMap<>();
        this.skipListMap = new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER);
        this.searchPrefix = "Player_50";

        // Generate 100,000 accounts
        for (int i = 0; i < 100_000; i++) {
            String name = "Player_" + i;
            this.hashMap.put(name, name);
            this.skipListMap.put(name, name);
        }
    }

    @Benchmark
    public void testStreamFilter(Blackhole blackhole) {
        Collection<String> result = this.hashMap.values().stream()
                .filter(name -> name.toLowerCase().startsWith(this.searchPrefix.toLowerCase()))
                .collect(Collectors.toList());
        blackhole.consume(result);
    }

    @Benchmark
    public void testSubMap(Blackhole blackhole) {
        Collection<String> result = Collections.unmodifiableCollection(
                this.skipListMap.subMap(this.searchPrefix, true, this.searchPrefix + Character.MAX_VALUE, true)
                        .values());
        blackhole.consume(result);
    }
}
