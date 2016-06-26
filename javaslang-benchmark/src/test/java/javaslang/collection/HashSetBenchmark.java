package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import static javaslang.JmhRunner.*;

public class HashSetBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Add.class,
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
        Set<Integer> SET;

        org.pcollections.PSet<Integer> pcollectionsPersistent = org.pcollections.HashTreePSet.empty();
        scala.collection.immutable.HashSet<Integer> scalaPersistent = (scala.collection.immutable.HashSet<Integer>) scala.collection.immutable.HashSet$.MODULE$.empty();
        javaslang.collection.Set<Integer> slangPersistent = javaslang.collection.HashSet.empty();

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            SET = TreeSet.of(ELEMENTS);
            EXPECTED_AGGREGATE = SET.reduce(JmhRunner::aggregate);

            require(pcollectionsPersistent::isEmpty,
                    scalaPersistent::isEmpty,
                    slangPersistent::isEmpty);

            for (int value : SET) {
                pcollectionsPersistent = pcollectionsPersistent.plus(value);
                scalaPersistent = scalaPersistent.$plus(value);
            }
            slangPersistent = javaslang.collection.HashSet.ofAll(SET);

            require(() -> SET.forAll(v -> pcollectionsPersistent.contains(v)),
                    () -> SET.forAll(v -> scalaPersistent.contains(v)),
                    () -> SET.forAll(v -> slangPersistent.contains(v)));
        }
    }

    public static class Add extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PSet<Integer> values = org.pcollections.HashTreePSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            require(values, vs -> SET.forAll(vs::contains));
            return values;
        }

        @Benchmark
        public Object scala_immutable() {
            scala.collection.immutable.HashSet<Integer> values = new scala.collection.immutable.HashSet<>();
            for (Integer element : ELEMENTS) {
                values = values.$plus(element);
            }
            require(values, vs -> SET.forAll(vs::contains));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Set<Integer> values = javaslang.collection.HashSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.add(element);
            }
            require(values, vs -> SET.forAll(vs::contains));
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public Object scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object pcollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pcollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object slang_persistent() {
            int aggregate = 0;
            for (final javaslang.collection.Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }
}