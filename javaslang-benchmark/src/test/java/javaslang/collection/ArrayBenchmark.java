package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;

import static javaslang.JmhRunner.*;

public class ArrayBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Prepend.class,
            Append.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebug(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormal(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;

        java.util.ArrayList<Integer> javaMutable;
        fj.data.Array<Integer> fjavaMutable;
        javaslang.collection.Array<Integer> slangPersistent;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            EXPECTED_AGGREGATE = Iterator.of(ELEMENTS).reduce(JmhRunner::aggregate);

            require(() -> javaMutable == null,
                    () -> fjavaMutable == null,
                    () -> slangPersistent == null);

            javaMutable = new ArrayList<>(Arrays.asList(ELEMENTS));
            fjavaMutable = fj.data.Array.array(ELEMENTS);
            slangPersistent = javaslang.collection.Array.of(ELEMENTS);

            require(() -> Collections.equals(javaMutable, Arrays.asList(ELEMENTS)),
                    () -> Collections.equals(fjavaMutable, javaMutable),
                    () -> Collections.equals(slangPersistent, javaMutable));
        }
    }

    public static class Create extends Base {
        @Benchmark
        public Object java_mutable() {
            final ArrayList<Integer> values = new ArrayList<>(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            final fj.data.Array<Integer> values = fj.data.Array.iterableArray(javaMutable);
            require(() -> Collections.equals(values, fjavaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            final javaslang.collection.Array<Integer> values = javaslang.collection.Array.ofAll(javaMutable);
            require(() -> Collections.equals(values, slangPersistent));
            return values.head();
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() {
            final Object head = javaMutable.get(0);
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }

        @Benchmark
        public Object fjava_mutable() {
            final Object head = fjavaMutable.get(0);
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }

        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.get(0);
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                require(javaMutable::isEmpty);
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                require(() -> javaMutable.size() == state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.remove(0);
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Array<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = javaMutable.get(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
            }
        }

        @Benchmark
        public void fjava_mutable(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = fjavaMutable.get(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = slangPersistent.get(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
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
            require(values, v -> Iterator.ofAll(v).forAll(e -> e == 0));
            return javaMutable;
        }

        @Benchmark
        public Object fjava_mutable() {
            final fj.data.Array<Integer> values = fjavaMutable;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values.set(i, 0);
            }
            require(values, v -> v.forall(e -> e == 0));
            return fjavaMutable;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Array<Integer> values = slangPersistent;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values = values.update(i, 0);
            }
            require(values, v -> v.forAll(e -> e == 0));
            return values;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            require(values, v -> Collections.equals(List.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = fj.data.Array.array(element).append(values);
            }
            require(values, v -> Collections.equals(v.reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Array<Integer> values = javaslang.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            require(values, v -> Collections.equals(v.reverse(), javaMutable));
            return values;
        }
    }

    public static class Append extends Base {
        @SuppressWarnings("ManualArrayToCollectionCopy")
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(fj.data.Array.array(element));
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Array<Integer> values = javaslang.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public Object java_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutable.get(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object fjava_mutable() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = fjavaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }
}