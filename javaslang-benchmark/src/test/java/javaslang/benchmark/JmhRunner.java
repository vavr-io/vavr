package javaslang.benchmark;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class JmhRunner {
    private static final int FORKS = 1;

    private static final int WARMUP_ITERATIONS = 20;
    private static final int MEASUREMENT_ITERATIONS = 30;
    private static final int DURATION_MILLIS = 500;

    private static final int QUICK_WARMUP_ITERATIONS = 5;
    private static final int QUICK_MEASUREMENT_ITERATIONS = 5;
    private static final int QUICK_DURATION_MILLIS = 100;

    public static void runDebug(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, 1, 1, 1, PrintGc.Disable, 0);
    }

    public static void runQuick(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, QUICK_WARMUP_ITERATIONS, QUICK_MEASUREMENT_ITERATIONS, QUICK_DURATION_MILLIS, PrintGc.Disable, FORKS);
    }

    public static void runSlow(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, WARMUP_ITERATIONS, MEASUREMENT_ITERATIONS, DURATION_MILLIS, PrintGc.Enable, FORKS);
    }

    private static void runAndReport(Class<?> benchmarkClass, int warmupIterations, int measurementIterations, int millis, PrintGc printGc, int forks) {
        final Collection<RunResult> results = run(benchmarkClass, warmupIterations, measurementIterations, millis, printGc, forks);
        BenchmarkPerformanceReporter.of(benchmarkClass, results).print();
    }

    private static Collection<RunResult> run(Class<?> benchmarkClass, int warmupIterations, int measurementIterations, int millis, PrintGc printGc, int forks) {
        final Options opts = new OptionsBuilder()
                .include(benchmarkClass.getSimpleName())
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)
                .warmupTime(TimeValue.milliseconds(millis))
                .warmupIterations(warmupIterations)
                .measurementTime(TimeValue.milliseconds(millis))
                .measurementIterations(measurementIterations)
                .forks(forks)
                // We are using 4Gb and setting NewGen to 100% to avoid GC during testing.
                // Any GC during testing will destroy the iteration, which should get ignored as an outlier
                .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms4g", "-Xmx4g", "-XX:MaxGCPauseMillis=1000", "-XX:+UnlockExperimentalVMOptions", "-XX:G1NewSizePercent=100", "-XX:G1MaxNewSizePercent=100", "-disableassertions", printGc.vmArg)
                .build();

        try {
            return new Runner(opts).run();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private enum PrintGc {
        Enable("-XX:+PrintGC"),
        Disable("-XX:-PrintGC");

        final String vmArg;

        PrintGc(String vmArg) {
            this.vmArg = vmArg;
        }
    }

    public static <T> void assertEquals(T a, T b) {
        if (!Objects.equals(a, b)) {
            throw new IllegalStateException(a + " != " + b);
        }
    }

    public static Integer[] getRandomValues(int size, int seed) {
        return getRandomValues(size, seed, false);
    }

    public static Integer[] getRandomValues(int size, int seed, boolean nonNegative) {
        final Random random = new Random(seed);

        final Integer[] results = new Integer[size];
        for (int i = 0; i < size; i++) {
            final int value = random.nextInt(size) - (nonNegative ? 0 : (size / 2));
            results[i] = value;
        }
        return results;
    }
}
