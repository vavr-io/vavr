package javaslang;

import javaslang.collection.Array;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

public class JmhRunner {
    /** enables debugging and assertions for benchmarks and production code - the speed results will be totally unreliable */
    public static void runDebug(Array<Class<?>> groups) {
        runAndReport(groups, 0, 1, 1, 0, PrintGc.Disable, Assertions.Enable);
    }

    public static void runQuick(Array<Class<?>> groups) {
        runAndReport(groups, 10, 10, 10, 1, PrintGc.Disable, Assertions.Disable);
    }

    public static void runNormal(Array<Class<?>> groups) {
        runAndReport(groups, 15, 10, 100, 1, PrintGc.Disable, Assertions.Disable);
    }

    public static void runSlow(Array<Class<?>> groups) {
        runAndReport(groups, 15, 15, 300, 1, PrintGc.Enable, Assertions.Disable);
    }

    public static void runAndReport(Array<Class<?>> groups, int warmupIterations, int measurementIterations, int millis, int forks, PrintGc printGc, Assertions assertions) {
        final Array<String> classNames = groups.map(Class::getCanonicalName);
        final Array<RunResult> results = run(classNames, warmupIterations, measurementIterations, millis, forks, printGc, assertions);
        BenchmarkPerformanceReporter.of(classNames, results).print();
    }

    private static Array<RunResult> run(Array<String> classNames, int warmupIterations, int measurementIterations, int millis, int forks, PrintGc printGc, Assertions assertions) {
        final ChainedOptionsBuilder builder = new OptionsBuilder()
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
                .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms4g", "-Xmx4g", "-XX:MaxGCPauseMillis=1000", "-XX:+UnlockExperimentalVMOptions", "-XX:G1NewSizePercent=100", "-XX:G1MaxNewSizePercent=100", printGc.vmArg, assertions.vmArg);

        classNames.forEach(builder::include);

        try {
            return Array.ofAll(new Runner(builder.build()).run());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public enum PrintGc {
        Enable("-XX:+PrintGC"),
        Disable("-XX:-PrintGC");

        final String vmArg;

        PrintGc(String vmArg) {
            this.vmArg = vmArg;
        }
    }

    public enum Assertions {
        Enable("-enableassertions"),
        Disable("-disableassertions");

        final String vmArg;

        Assertions(String vmArg) {
            this.vmArg = vmArg;
        }
    }

    public static <T> void require(T value, Predicate<T> predicate) {
        require(() -> predicate.test(value));
    }

    public static <T1, T2> void require(T1 value1, Predicate<T1> predicate1, T2 value2, Predicate<T2> predicate2) {
        require(() -> predicate1.test(value1),
                () -> predicate2.test(value2));
    }

    public static <T1, T2, T3> void require(T1 value1, Predicate<T1> predicate1, T2 value2, Predicate<T2> predicate2, T3 value3, Predicate<T3> predicate3) {
        require(() -> predicate1.test(value1),
                () -> predicate2.test(value2),
                () -> predicate3.test(value3));
    }

    public static void require(BooleanSupplier... suppliers) {
        // TODO inline all requires
        for (BooleanSupplier supplier : suppliers) {
            assert supplier.getAsBoolean();
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

    /** used for dead code elimination and correctness assertion inside the benchmarks */
    public static int aggregate(int x, int y) {
        return x ^ y;
    }
}
