package com.eternalcode.economy.account;

import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import java.util.UUID;

@State(Scope.Thread)
public class AccountBenchmark {

    private static final Logger LOGGER = Logger.getLogger(AccountBenchmark.class.getName());
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private final List<String> searches = new ArrayList<>();
    private AccountManager accountManager;

    @Setup
    public void setUp() {
        accountManager = new AccountManager(new AccountRepositoryInMemory());

        // zapełnienie TreeMapy różnymi nazwami zapewnia, że będzie ona miała optymalne wyniki
        // tree mapa rozdziela elementy na podstawie ich klucza, więc im bardziej zróżnicowane klucze, tym "lepsze' wyniki
        for (char first : ALPHABET) {
            for (char second : ALPHABET) {
                for (char third : ALPHABET) {
                    String name = String.valueOf(first) + second + third;

                    accountManager.create(UUID.randomUUID(), name);
                }
            }
        }

        // pre-generowanie losowych wyszukiwań, które będą wykonywane w benchmarku (nie wpływamy na czas wykonania samego benchmarku)
        for (char first : ALPHABET) {
            searches.add(String.valueOf(first));
            for (char second : ALPHABET) {
                searches.add(String.valueOf(first) + second);
                for (char third : ALPHABET) {
                    searches.add(String.valueOf(first) + second + third);
                }
            }
        }

        Collections.shuffle(searches); // mieszamy, aby zapewnić losowy dostęp

        LOGGER.info("Acounts size: " + accountManager.getAccounts().size() + ", Searches size: " + searches.size());
    }

    // mimo że nie jest to bezpieczne dla wielu wątków, to w przypadku JMH można to zignorować i tak potrzebujemy losowości
    private int index = 0;

    @Benchmark
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    public void benchmarkGetAccountStartingWith() {
        accountManager.getAccountStartingWith(searches.get(index++ % searches.size()));
    }

}
