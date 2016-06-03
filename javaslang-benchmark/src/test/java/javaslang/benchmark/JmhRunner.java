package javaslang.benchmark;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class JmhRunner {
    public static final int FORKS = 1;

    private static final int WARMUP_ITERATIONS = 20;
    private static final int MEASUREMENT_ITERATIONS = 30;
    public static final int DURATION_MILLIS = 500;

    private static final int QUICK_WARMUP_ITERATIONS = 10;
    private static final int QUICK_MEASUREMENT_ITERATIONS = 10;
    public static final int QUICK_DURATION_MILLIS = 200;

    public static void run(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, WARMUP_ITERATIONS, MEASUREMENT_ITERATIONS, DURATION_MILLIS, PrintGc.Enable, Assertions.Disable, FORKS);
    }

    public static void runDebug(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, 1, 1, 1, PrintGc.Disable, Assertions.Disable, 0);
    }

    public static void runDev(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, QUICK_WARMUP_ITERATIONS, QUICK_MEASUREMENT_ITERATIONS, QUICK_DURATION_MILLIS, PrintGc.Disable, Assertions.Disable, FORKS);
    }

    public static void runDevWithAssertions(Class<?> benchmarkClass) {
        runAndReport(benchmarkClass, QUICK_WARMUP_ITERATIONS, QUICK_MEASUREMENT_ITERATIONS, QUICK_DURATION_MILLIS, PrintGc.Disable, Assertions.Enable, FORKS);
    }

    private static void runAndReport(Class<?> benchmarkClass, int warmupIterations, int measurementIterations, int millis, PrintGc printGc, Assertions assertions, int forks) {
        final Collection<RunResult> results = run(benchmarkClass, warmupIterations, measurementIterations, millis, printGc, assertions, forks);
        BenchmarkPerformanceReporter.of(results).print();
    }

    private static Collection<RunResult> run(Class<?> benchmarkClass, int warmupIterations, int measurementIterations, int millis, PrintGc printGc, Assertions assertions, int forks) {
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
                .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms4g", "-Xmx4g", "-XX:MaxGCPauseMillis=1000", "-XX:+UnlockExperimentalVMOptions", "-XX:G1NewSizePercent=100", "-XX:G1MaxNewSizePercent=100", printGc.vmArg, assertions.vmArg)
                .build();

        try {
            return new Runner(opts).run();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private enum Assertions {
        Enable("-enableassertions"),
        Disable("-disableassertions");

        final String vmArg;

        Assertions(String vmArg) {
            this.vmArg = vmArg;
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
