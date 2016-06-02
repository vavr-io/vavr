package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import org.openjdk.jmh.annotations.*;

import static javaslang.benchmark.JmhRunner.*;

public class BitSetBenchmark {
    public static void main(String... args) {
        JmhRunner.runDev(BitSetBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        public Integer[] ELEMENTS;
        public int[] PRIMITIVES;
        public int SET_SIZE;
        public int EXPECTED_AGGREGATE;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0, true);
            SET_SIZE = javaslang.collection.Stream.of(ELEMENTS).distinct().size();
            EXPECTED_AGGREGATE = javaslang.collection.Stream.of(ELEMENTS).distinct().fold(0, (i, j) -> i ^ j);
            PRIMITIVES = new int[ELEMENTS.length];
            for (int i = 0; i < ELEMENTS.length; i++) {
                PRIMITIVES[i] = ELEMENTS[i];
            }
        }
    }

    public static class AddAll extends Base {

        @Benchmark
        public void scala_persistent() {
            scala.collection.immutable.BitSet values = new scala.collection.immutable.BitSet.BitSet1(0L);
            for (int element : PRIMITIVES) {
                values = values.$plus(element);
            }
            assertEquals(values.size(), SET_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Set<Integer> values = javaslang.collection.BitSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.add(element);
            }
            assertEquals(values.size(), SET_SIZE);
        }
    }

    public static class Iterate extends Base {

        scala.collection.immutable.BitSet scalaPersistent = new scala.collection.immutable.BitSet.BitSet1(0L);
        javaslang.collection.Set<Integer> slangPersistent = javaslang.collection.BitSet.empty();

        @Setup
        public void initializeImmutable(Base state) {
            for (Integer element : state.ELEMENTS) {
                slangPersistent = slangPersistent.add(element);
            }
            for (int element : state.ELEMENTS) {
                scalaPersistent = scalaPersistent.$plus(element);
            }
            assertEquals(scalaPersistent.size(), state.SET_SIZE);
            assertEquals(slangPersistent.size(), state.SET_SIZE);
        }

        @Benchmark
        public void scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Object> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= (int) iterator.next();
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