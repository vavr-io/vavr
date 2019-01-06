/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import io.vavr.collection.*;
import io.vavr.control.LazyBenchmark;
import io.vavr.idiom.ForBenchmark;
import io.vavr.idiom.PatternMatchingBenchmark;
import io.vavr.idiom.TryBenchmark;
import io.vavr.idiom.TupleBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.vavr.API.Array;

public class JmhRunner {
    /**
     * Runs all the available benchmarks in precision mode.
     * Note: it takes about 3 hours.
     */
    public static void main(String[] args) {
        final Array<Class<?>> CLASSES = Array(
                ArrayBenchmark.class,
                BitSetBenchmark.class,
                CharSeqBenchmark.class,
                HashSetBenchmark.class,
                ListBenchmark.class,
                MapBenchmark.class,
                PriorityQueueBenchmark.class,
                VectorBenchmark.class,

                LazyBenchmark.class,

                ForBenchmark.class,
                PatternMatchingBenchmark.class,
                TryBenchmark.class,
                TupleBenchmark.class
        );
        runDebugWithAsserts(CLASSES);
        runSlowNoAsserts(CLASSES);
    }

    public enum Includes {
        JAVA("java"),
        FUNCTIONAL_JAVA("fjava"),
        PCOLLECTIONS("pcollections"),
        ECOLLECTIONS("ecollections"),
        CAPSULE("capsule"),
        CLOJURE("clojure"),
        SCALAZ("scalaz"),
        SCALA("scala"),
        VAVR("vavr");

        private final String name;

        Includes(String name) { this.name = name; }

        @Override
        public String toString() { return name; }
    }

    /** enables debugging and assertions for benchmarks and production code - the speed results will be totally unreliable */
    public static void runDebugWithAsserts(Array<Class<?>> groups, Includes... includes) {
        run(0, 1, 1, ForkJvm.DISABLE, VerboseMode.SILENT, Assertions.ENABLE, PrintInlining.DISABLE, groups, includes);
        MemoryUsage.printAndReset();
    }

    @SuppressWarnings("unused")
    public static void runQuickNoAsserts(Array<Class<?>> groups, Includes... includes) {
        run(5, 5, 10, ForkJvm.ENABLE, VerboseMode.NORMAL, Assertions.DISABLE, PrintInlining.DISABLE, groups, includes).print();
    }

    @SuppressWarnings("unused")
    public static void runNormalNoAsserts(Array<Class<?>> groups, Includes... includes) {
        run(7, 7, 300, ForkJvm.ENABLE, VerboseMode.NORMAL, Assertions.DISABLE, PrintInlining.DISABLE, groups, includes).print();
    }

    @SuppressWarnings("unused")
    public static void runSlowNoAsserts(Array<Class<?>> groups, Includes... includes) {
        run(15, 15, 400, ForkJvm.ENABLE, VerboseMode.EXTRA, Assertions.DISABLE, PrintInlining.DISABLE, groups, includes).print();
    }

    private static BenchmarkPerformanceReporter run(int warmupIterations, int measurementIterations, int millis, ForkJvm forkJvm, VerboseMode silent, Assertions assertions, PrintInlining printInlining, Array<Class<?>> groups, Includes[] includes) {
        final Array<String> includeNames = Array.of(includes.length == 0 ? Includes.values() : includes).map(Includes::toString);
        final Array<String> classNames = groups.map(Class::getCanonicalName);
        final Array<RunResult> results = run(warmupIterations, measurementIterations, millis, forkJvm, silent, assertions, printInlining, classNames, includeNames);
        return BenchmarkPerformanceReporter.of(includeNames, classNames, results);
    }

    private static Array<RunResult> run(int warmupIterations, int measurementIterations, int millis, ForkJvm forkJvm, VerboseMode verboseMode, Assertions assertions, PrintInlining printInlining, Array<String> classNames, Array<String> includeNames) {
        try {
            final ChainedOptionsBuilder builder = new OptionsBuilder()
                    .shouldDoGC(true)
                    .verbosity(verboseMode)
                    .shouldFailOnError(true)
                    .mode(Mode.Throughput)
                    .timeUnit(TimeUnit.SECONDS)
                    .warmupTime(TimeValue.milliseconds(millis))
                    .warmupIterations(warmupIterations)
                    .measurementTime(TimeValue.milliseconds(millis))
                    .measurementIterations(measurementIterations)
                    .forks(forkJvm.forkCount)
                  /* We are using 4Gb and setting NewGen to 100% to avoid GC during testing.
                     Any GC during testing will destroy the iteration (i.e. introduce unreliable noise in the measurement), which should get ignored as an outlier */
                    .jvmArgsAppend("-XX:+UseG1GC", "-Xss100m", "-Xms4g", "-Xmx4g", "-XX:MaxGCPauseMillis=1000", "-XX:+UnlockExperimentalVMOptions", "-XX:G1NewSizePercent=100", "-XX:G1MaxNewSizePercent=100", assertions.vmArg);

            final String includePattern = includeNames.mkString("\\..*?\\b(", "|", ")_");
            classNames.forEach(name -> builder.include(name + includePattern));

            if (printInlining == PrintInlining.ENABLE) {
                builder.jvmArgsAppend("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining"); /* might help in deciding when the JVM is properly warmed up - or where to optimize the code */
            }

            return Array.ofAll(new Runner(builder.build()).run());
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

    /* Options */
    private enum ForkJvm {
        ENABLE(1),
        DISABLE(0);

        final int forkCount;

        ForkJvm(int forkCount) {
            this.forkCount = forkCount;
        }
    }

    private enum Assertions {
        ENABLE("-enableassertions"),
        DISABLE("-disableassertions");

        final String vmArg;

        Assertions(String vmArg) {
            this.vmArg = vmArg;
        }
    }

    private enum PrintInlining {
        ENABLE,
        DISABLE
    }

    /* Helper methods */

    public static Integer[] getRandomValues(int size, int seed) {
        return getRandomValues(size, seed, false);
    }

    public static Integer[] getRandomValues(int size, int seed, boolean nonNegative) {
        return getRandomValues(size, nonNegative, new Random(seed));
    }

    public static Integer[] getRandomValues(int size, boolean nonNegative, Random random) {
        final Integer[] results = new Integer[size];
        for (int i = 0; i < size; i++) {
            final int value = random.nextInt(size) - (nonNegative ? 0 : (size / 2));
            results[i] = value;
        }
        return results;
    }

    /** Randomly mutate array positions */
    public static <T> int[] shuffle(int[] array, Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    static <T> void swap(int[] array, int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /** used for dead code elimination and correctness assertion inside the benchmarks */
    public static int aggregate(int x, int y) {
        return x ^ y;
    }

    /** simplifies construction of immutable collections - with assertion and memory benchmarking */
    public static <T extends Collection<?>, R> R create(Function1<T, R> function, T source, Function1<R, Boolean> assertion) {
        return create(function, source, source.size(), assertion);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R create(Function1<T, R> function, T source, int elementCount, Function1<R, Boolean> assertion) {
        final R result = function.apply(source);
        assert assertion.apply(result);

        MemoryUsage.storeMemoryUsages(elementCount, result);

        return result;
    }
}
