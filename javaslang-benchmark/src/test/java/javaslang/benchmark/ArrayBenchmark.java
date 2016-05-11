package javaslang.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static javaslang.benchmark.BenchmarkResultAggregator.displayRatios;

public class ArrayBenchmark {
    public static void main(String... args) throws Exception { /* main is more reliable than a test */
        final Options opts = new OptionsBuilder()
                .include(ArrayBenchmark.class.getSimpleName())
                .shouldDoGC(false)
                .shouldFailOnError(true)
                .build();

        final Collection<RunResult> results = new Runner(opts).run();
        displayRatios(results, "^.*slang.*$");
    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(value = 1, jvmArgsAppend = { "-XX:+UseG1GC", "-Xss100m", "-Xms1g", "-Xmx1g", "-disableassertions" }) /* set fork to 0 if you want to debug */
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        public Integer[] ELEMENTS;

        @Setup
        public void setup() {
            final Random random = new Random(0);

            ELEMENTS = new Integer[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final int value = random.nextInt(CONTAINER_SIZE) - (CONTAINER_SIZE / 2);
                ELEMENTS[i] = value;
            }
        }

        protected static <T> void assertEquals(T a, T b) {
            if (!Objects.equals(a, b)) {
                throw new IllegalStateException(a + " != " + b);
            }
        }
    }

    public static class AddAll extends Base {
        @Benchmark
        @SuppressWarnings("ManualArrayCopy")
        public void java_mutable() {
            final Integer[] values = new Integer[ELEMENTS.length];
            for (int i = 0; i < ELEMENTS.length; i++) {
                values[i] = ELEMENTS[i];
            }
            assertEquals(values.length, CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Array<Integer> values = javaslang.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }
    }

    public static class Iterate extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            Integer[] javaMutable;

            int expectedAggregate = 0;
            javaslang.collection.Array<Integer> slangPersistent = javaslang.collection.Array.empty();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable, null);
                javaMutable = state.ELEMENTS.clone();
                assertEquals(javaMutable.length, state.CONTAINER_SIZE);

                if (expectedAggregate == 0) {
                    for (Integer element : state.ELEMENTS) {
                        expectedAggregate ^= element;
                    }

                    assertEquals(slangPersistent.size(), 0);
                    for (Integer element : state.ELEMENTS) {
                        slangPersistent = slangPersistent.prepend(element);
                    }
                    assertEquals(slangPersistent.size(), state.CONTAINER_SIZE);
                }
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable = null;
            }
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void java_mutable(Initialized state) {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= state.javaMutable[i];
            }
            assertEquals(aggregate, state.expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void slang_persistent(Initialized state) {
            int aggregate = 0;
            for (javaslang.collection.Array<Integer> values = state.slangPersistent; !values.isEmpty(); values = values.tail()) {
                aggregate ^= values.head();
            }
            assertEquals(aggregate, state.expectedAggregate);
        }
    }
}