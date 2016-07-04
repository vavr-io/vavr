package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import static javaslang.JmhRunner.*;
import static javaslang.collection.Collections.areEqual;
import static scala.collection.JavaConversions.*;

public class BitSetBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            AddAll.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        int[] ELEMENTS;
        TreeSet<Integer> DISTINCT;

        scala.collection.immutable.BitSet scalaPersistent;
        javaslang.collection.BitSet<Integer> slangPersistent;

        @Setup
        @SuppressWarnings("RedundantCast")
        public void setup() {
            final Integer[] values = getRandomValues(CONTAINER_SIZE, 0, true);
            ELEMENTS = new int[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                ELEMENTS[i] = values[i];
            }

            DISTINCT = TreeSet.ofAll(ELEMENTS);
            EXPECTED_AGGREGATE = DISTINCT.reduce(JmhRunner::aggregate);

            scalaPersistent = create(v -> (scala.collection.immutable.BitSet) scala.collection.immutable.BitSet$.MODULE$.apply(asScalaBuffer(v)), DISTINCT.toJavaList(), v -> areEqual(asJavaCollection(v), DISTINCT));
            slangPersistent = create(javaslang.collection.BitSet::ofAll, ELEMENTS, ELEMENTS.length, v -> areEqual(v, DISTINCT));
        }
    }

    public static class AddAll extends Base {
        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.BitSet values = new scala.collection.immutable.BitSet.BitSet1(0L);
            for (int element : ELEMENTS) {
                values = values.$plus(element);
            }
            assert values.equals(scalaPersistent);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Set<Integer> values = javaslang.collection.BitSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.add(element);
            }
            assert values.equals(slangPersistent);
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public int scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Object> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= (Integer) iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (final javaslang.collection.Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}