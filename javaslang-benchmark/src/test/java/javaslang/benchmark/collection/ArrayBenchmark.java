package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.Objects;
import java.util.Random;

public class ArrayBenchmark {

    public static void main(String... args) { /* main is more reliable than a test */
        JmhRunner.run(ArrayBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000", "10000"})
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