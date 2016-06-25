package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import org.openjdk.jmh.annotations.*;

import java.util.*;

import static javaslang.benchmark.JmhRunner.*;

public class ArrayBenchmark {
    public static void main(String... args) {
        JmhRunner.runQuick(ArrayBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000", "10000" })
        public int CONTAINER_SIZE;

        int expectedAggregate = 0;
        public Integer[] ELEMENTS;

        java.util.ArrayList<Integer> javaMutable;
        fj.data.Array<Integer> fjavaMutable;
        javaslang.collection.Array<Integer> slangPersistent;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            for (Integer element : ELEMENTS) {
                expectedAggregate ^= element;
            }

            assertEquals(javaMutable, null);
            javaMutable = new ArrayList<>(CONTAINER_SIZE);
            Collections.addAll(javaMutable, ELEMENTS);
            assertEquals(javaMutable.size(), CONTAINER_SIZE);

            assertEquals(fjavaMutable, null);
            fjavaMutable = fj.data.Array.array(ELEMENTS);
            assertEquals(fjavaMutable.length(), CONTAINER_SIZE);

            assertEquals(slangPersistent, null);
            slangPersistent = javaslang.collection.Array.of(ELEMENTS);
            assertEquals(slangPersistent.size(), CONTAINER_SIZE);
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() { return javaMutable.get(0); }

        @Benchmark
        public Object fjava_mutable() { return fjavaMutable.get(0); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.head(); }
    }

    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable.size(), 0);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public void java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.remove(0);
            }
            assertEquals(values, new java.util.ArrayList<>());
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Array<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, javaslang.collection.Array.empty());
        }
    }

    public static class Get extends Base {
        @Benchmark
        public void java_mutable() {
            for (int i = 0; i < ELEMENTS.length; i++) {
                assertEquals(javaMutable.get(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void fjava_mutable() {
            for (int i = 0; i < ELEMENTS.length; i++) {
                assertEquals(fjavaMutable.get(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void slang_persistent() {
            for (int i = 0; i < ELEMENTS.length; i++) {
                assertEquals(slangPersistent.get(i), ELEMENTS[i]);
            }
        }
    }

    public static class Update extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = javaMutable;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values.set(i, 0);
            }
            return javaMutable;
        }

        @Benchmark
        public Object fjava_mutable() {
            final fj.data.Array<Integer> values = fjavaMutable;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values.set(i, 0);
            }
            return fjavaMutable;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Array<Integer> values = slangPersistent;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values = values.update(i, 0);
            }
            return values;
        }
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
        public void fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = fj.data.Array.array(element).append(values);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
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
        public void fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(fj.data.Array.array(element));
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
        @Benchmark
        public void java_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutable.get(i);
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void fjava_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= fjavaMutable.get(i);
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void slang_persistent() {
            int aggregate = 0;
            for (javaslang.collection.Array<Integer> values = slangPersistent; !values.isEmpty(); values = values.tail()) {
                aggregate ^= values.head();
            }
            assertEquals(aggregate, expectedAggregate);
        }
    }
}