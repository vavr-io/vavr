package javaslang.benchmark.collection;

import fj.data.Array;
import javaslang.benchmark.JmhRunner;
import javaslang.collection.Iterator;
import org.openjdk.jmh.annotations.*;

import java.util.*;

import static javaslang.benchmark.JmhRunner.*;

public class ArrayBenchmark {
    public static void main(String... args) {
        JmhRunner.runDev(ArrayBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000", "10000" })
        public int CONTAINER_SIZE;

        public Integer[] ELEMENTS;

        java.util.ArrayList<Integer> javaMutable;
        fj.data.Array<Integer> fjavaPersistent;
        javaslang.collection.Array<Integer> slangPersistent;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            assertEquals(javaMutable, null);
            javaMutable = new ArrayList<>(CONTAINER_SIZE);
            Collections.addAll(javaMutable, ELEMENTS);
            assertEquals(javaMutable.size(), CONTAINER_SIZE);

            assertEquals(fjavaPersistent, null);
            fjavaPersistent = Array.array(ELEMENTS);
            assertEquals(fjavaPersistent.length(), CONTAINER_SIZE);

            assertEquals(slangPersistent, null);
            slangPersistent = javaslang.collection.Array.of(ELEMENTS);
            assertEquals(slangPersistent.size(), CONTAINER_SIZE);
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() { return javaMutable.get(0); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.get(0); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.head(); }
    }

    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            java.util.ArrayList<Integer> javaMutable;
            javaslang.collection.Array<Integer> slangPersistent = javaslang.collection.Array.empty();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable, null);
                javaMutable = new java.util.ArrayList<>(state.CONTAINER_SIZE);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);

                if (slangPersistent.isEmpty()) {
                    slangPersistent = slangPersistent.appendAll(Iterator.of(state.ELEMENTS));
                    assertEquals(slangPersistent.size(), state.CONTAINER_SIZE);
                }
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable = null;
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            state.javaMutable.remove(0);
            return state.javaMutable;
        }

        @Benchmark
        public Object slang_persistent(Initialized state) { return state.slangPersistent.tail(); }
    }

    public static class Get extends Base {
        final int index = CONTAINER_SIZE / 2;

        @Benchmark
        public Object java_mutable() { return javaMutable.get(index); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.get(index); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.get(index); }
    }

    public static class Update extends Base {
        final int index = CONTAINER_SIZE / 2, replacement = 0;

        @Benchmark
        public Object java_mutable() {
            javaMutable.set(index, replacement);
            return javaMutable;
        }

        @Benchmark
        public Object fjava_persistent() {
            fjavaPersistent.set(index, replacement);
            return fjavaPersistent;
        }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.update(index, replacement); }
    }

    public static class Prepend extends Base {
        @Benchmark
        public void java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Array<Integer> values = javaslang.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }
    }

    public static class Append extends Base {
        @SuppressWarnings("ManualArrayToCollectionCopy")
        @Benchmark
        public void java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(Array.array(element));
            }
            assertEquals(values.length(), CONTAINER_SIZE);
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
            java.util.ArrayList<Integer> javaMutable;

            int expectedAggregate = 0;
            javaslang.collection.Array<Integer> slangPersistent = javaslang.collection.Array.empty();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable, null);
                javaMutable = new java.util.ArrayList<>(state.CONTAINER_SIZE);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);

                if (expectedAggregate == 0) {
                    for (Integer element : state.ELEMENTS) {
                        expectedAggregate ^= element;
                    }

                    assertEquals(slangPersistent.size(), 0);
                    slangPersistent = slangPersistent.appendAll(Iterator.of(state.ELEMENTS));
                    assertEquals(slangPersistent.size(), state.CONTAINER_SIZE);
                }
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable = null;
            }
        }

        @Benchmark
        public void java_mutable(Initialized state) {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= state.javaMutable.get(i);
            }
            assertEquals(aggregate, state.expectedAggregate);
        }

        @Benchmark
        public void slang_persistent(Initialized state) {
            int aggregate = 0;
            for (javaslang.collection.Array<Integer> values = state.slangPersistent; !values.isEmpty(); values = values.tail()) {
                aggregate ^= values.head();
            }
            assertEquals(aggregate, state.expectedAggregate);
        }
    }
}