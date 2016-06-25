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
    public static boolean WITH_ASSERTS = false;

    public static void runDebug(Array<Class<?>> benchmarkClasses) {
        WITH_ASSERTS = true;
        runAndReport(0, 1, 1, PrintGc.Disable, 0, benchmarkClasses);
        WITH_ASSERTS = false;
    }

    public static void runQuick(Array<Class<?>> benchmarkClasses) {
        runAndReport(10, 10, 10, PrintGc.Disable, 1, benchmarkClasses);
    }

    public static void runNormal(Array<Class<?>> benchmarkClasses) {
        runAndReport(15, 10, 100, PrintGc.Disable, 1, benchmarkClasses);
    }

    public static void runSlow(Array<Class<?>> benchmarkClasses) {
        runAndReport(15, 15, 300, PrintGc.Enable, 1, benchmarkClasses);
    }

    public static void runAndReport(int warmupIterations, int measurementIterations, int millis, PrintGc printGc, int forks, Array<Class<?>> benchmarkClasses) {
        final Array<String> classNames = benchmarkClasses.map(Class::getCanonicalName);
        final Array<RunResult> results = run(warmupIterations, measurementIterations, millis, printGc, forks, classNames);
        BenchmarkPerformanceReporter.of(classNames, results).print();
    }

    private static Array<RunResult> run(int warmupIterations, int measurementIterations, int millis, PrintGc printGc, int forks, Array<String> classNames) {
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
                .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms4g", "-Xmx4g", "-XX:MaxGCPauseMillis=1000", "-XX:+UnlockExperimentalVMOptions", "-XX:G1NewSizePercent=100", "-XX:G1MaxNewSizePercent=100", "-disableassertions", printGc.vmArg);

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
        if (WITH_ASSERTS) {
            for (int i = 0; i < suppliers.length; i++) {
                if (!suppliers[i].getAsBoolean()) {
                    throw new IllegalStateException("Failure in supplier #" + (i + 1) + "!");
                }
            }
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

    public static int xor(int x, int y) {
        return x ^ y;
    }
}
