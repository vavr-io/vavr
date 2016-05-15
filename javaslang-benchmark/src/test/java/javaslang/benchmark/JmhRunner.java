package javaslang.benchmark;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class JmhRunner {
    private static final int WARMUP_ITERATIONS = 10;
    private static final int MEASUREMENT_ITERATIONS = 40;

    private static final int QUICK_WARMUP_ITERATIONS = 5;
    private static final int QUICK_MEASUREMENT_ITERATIONS = 10;

    public static void runAndReport(Class clazz) {
        runAndReport(clazz, WARMUP_ITERATIONS, MEASUREMENT_ITERATIONS, Assertions.Disable);
    }

    public static void devRunAndReport(Class clazz) {
        runAndReport(clazz, QUICK_WARMUP_ITERATIONS, QUICK_MEASUREMENT_ITERATIONS, Assertions.Enable);
    }

    private static void runAndReport(Class clazz, int warmupIterations, int measurementIterations, Assertions assertions) {
        Collection<RunResult> results = run(clazz, warmupIterations, measurementIterations, assertions);
        BenchmarkPerformanceReporter.of(results).print();
    }

    private static Collection<RunResult> run(Class clazz, int warmupIterations, int measurementIterations, Assertions assertions) {
        final Options opts = new OptionsBuilder()
                .include(clazz.getSimpleName())
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)
                .warmupTime(TimeValue.milliseconds(500))
                .warmupIterations(warmupIterations)
                .measurementTime(TimeValue.milliseconds(500))
                .measurementIterations(measurementIterations)
                .forks(1)
                .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms1g", "-Xmx1g", assertions.vmArg)
                .build();

        try {
            return new Runner(opts).run();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static enum Assertions {
        Enable("-enableassertions"),
        Disable("-disableassertions")
        ;

        final String vmArg;
        Assertions(String vmArg) {
            this.vmArg = vmArg;
        }
    }
}
