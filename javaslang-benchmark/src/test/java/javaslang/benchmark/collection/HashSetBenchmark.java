package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import javaslang.collection.HashSet;
import org.openjdk.jmh.annotations.*;

import static javaslang.benchmark.JmhRunner.*;

public class HashSetBenchmark {
    public static void main(String... args) {
        JmhRunner.runQuick(HashSetBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        Integer[] ELEMENTS;
        int SET_SIZE;
        int EXPECTED_AGGREGATE;

        org.pcollections.PSet<Integer> pcollectionsPersistent = org.pcollections.HashTreePSet.empty();
        scala.collection.immutable.HashSet<Integer> scalaPersistent = new scala.collection.immutable.HashSet<>();
        javaslang.collection.Set<Integer> slangPersistent = javaslang.collection.HashSet.empty();

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            final HashSet<Integer> set = HashSet.of(ELEMENTS);
            SET_SIZE = set.size();
            EXPECTED_AGGREGATE = set.fold(0, (i, j) -> i ^ j);

            assertEquals(pcollectionsPersistent.size(), 0);
            assertEquals(scalaPersistent.size(), 0);
            assertEquals(slangPersistent.size(), 0);
            for (Integer element : ELEMENTS) {
                pcollectionsPersistent = pcollectionsPersistent.plus(element);
                scalaPersistent = scalaPersistent.$plus(element);
                slangPersistent = slangPersistent.add(element);
            }
            assertEquals(pcollectionsPersistent.size(), SET_SIZE);
            assertEquals(scalaPersistent.size(), SET_SIZE);
            assertEquals(slangPersistent.size(), SET_SIZE);

        }
    }

    public static class Add extends Base {
        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PSet<Integer> values = org.pcollections.HashTreePSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            assertEquals(values.size(), SET_SIZE);
        }

        @Benchmark
        public void scala_immutable() {
            scala.collection.immutable.HashSet<Integer> values = new scala.collection.immutable.HashSet<>();
            for (Integer element : ELEMENTS) {
                values = values.$plus(element);
            }
            assertEquals(values.size(), SET_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Set<Integer> values = javaslang.collection.HashSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.add(element);
            }
            assertEquals(values.size(), SET_SIZE);
        }
    }

    public static class Iterate extends Base {
        @Benchmark
        public void scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, EXPECTED_AGGREGATE);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void pcollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pcollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, EXPECTED_AGGREGATE);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void slang_persistent() {
            int aggregate = 0;
            for (final javaslang.collection.Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, EXPECTED_AGGREGATE);
        }
    }
}