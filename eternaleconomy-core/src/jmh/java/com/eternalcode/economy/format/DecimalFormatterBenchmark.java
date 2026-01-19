package com.eternalcode.economy.format;

import com.eternalcode.economy.config.implementation.PluginConfig;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS) // Faster operations, so nanos are better
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class DecimalFormatterBenchmark {

    private DecimalFormatter formatter;
    private DecimalFormat decimalFormat;
    private double smallValue;
    private double largeValue;
    private BigDecimal smallBigDecimal;

    @Setup
    public void setup() {
        this.formatter = new DecimalFormatterImpl(new PluginConfig());
        this.decimalFormat = new DecimalFormat("#,##0.00");

        this.smallValue = 123.456;
        this.largeValue = 123456789.123456;

        this.smallBigDecimal = BigDecimal.valueOf(this.smallValue);
    }

    @Benchmark
    public void testFormatterSmall(Blackhole blackhole) {
        blackhole.consume(this.formatter.format(this.smallValue));
    }

    @Benchmark
    public void testFormatterLarge(Blackhole blackhole) {
        blackhole.consume(this.formatter.format(this.largeValue));
    }

    @Benchmark
    public void testDecimalFormatSmall(Blackhole blackhole) {
        blackhole.consume(this.decimalFormat.format(this.smallValue));
    }

    @Benchmark
    public void testDecimalFormatLarge(Blackhole blackhole) {
        blackhole.consume(this.decimalFormat.format(this.largeValue));
    }

    @Benchmark
    public void testBigDecimalToStringSmall(Blackhole blackhole) {
        blackhole.consume(this.smallBigDecimal.toPlainString());
    }
}
