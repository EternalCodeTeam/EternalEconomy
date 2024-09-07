package com.eternalcode.economy.account;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Collection;
import java.util.UUID;

@State(Scope.Thread)
public class AccountBenchmark {

    // TODO: Przejrzyjcie czy to w ogóle ma sens? benchamrk dziala i wykazuje poprawne dane, jednak jest to moj
    //  pierwszy benchmark jmh w zyciu

    private AccountManager accountManager;
    private static final int NUM_USERS = 1000;
    private static final String PREFIX = "User";

    @Setup
    public void setUp() {
        accountManager = new AccountManager(null);

        for (int i = 0; i < NUM_USERS; i++) {
            String userName = PREFIX + i;
            accountManager.create(UUID.randomUUID(), userName);
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    public void benchmarkGetAccountStartingWith() {
        String prefix = "User";
        Collection<Account> results = accountManager.getAccountStartingWith(prefix);
    }
}