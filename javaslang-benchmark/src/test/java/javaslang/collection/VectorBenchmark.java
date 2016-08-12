package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.Objects;
import java.util.Random;

import static java.util.Arrays.asList;
import static javaslang.JmhRunner.create;
import static javaslang.JmhRunner.getRandomValues;
import static javaslang.collection.Collections.areEqual;

@SuppressWarnings({"ALL", "unchecked"})
public class VectorBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Get.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({/*"32",*/ "1024"/*, "32768"*/ /*, "1048576", "33554432", "1073741824" */}) /* i.e. depth 1,2,3(,4,5,6) for a branching factor of 32 */
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;
        int[] INT_ELEMENTS;
        int[] RANDOMIZED_INDICES;

        /* Only use this for non-mutating operations */
        java.util.ArrayList<Integer> javaMutable;
        javaslang.collection.Vector<Integer> slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            INT_ELEMENTS = new int[ELEMENTS.length];
            for (int i = 0; i < ELEMENTS.length; i++) {
                INT_ELEMENTS[i] = ELEMENTS[i];
            }
            RANDOMIZED_INDICES = Arrays2.shuffle(Array.range(0, CONTAINER_SIZE).toJavaStream().mapToInt(Integer::intValue).toArray(), random);

            EXPECTED_AGGREGATE = Array.of(ELEMENTS).reduce(JmhRunner::aggregate);

            javaMutable = create(java.util.ArrayList::new, asList(ELEMENTS), v -> areEqual(v, asList(ELEMENTS)));
            slangPersistent = create(javaslang.collection.Vector::ofAll, javaMutable, v -> areEqual(v, javaMutable));
        }
    }

    /** Bulk creation from array based, boxed source */
    public static class Create extends Base {
        @Benchmark
        public Object slang_persistent_list() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(javaMutable);
            assert areEqual(values, javaMutable);
            return values.head();
        }

        @Benchmark
        public Object slang_persistent_primitive_array() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(INT_ELEMENTS);
            assert areEqual(values, javaMutable);
            return values.head();
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public int slang_persistent_int() {
            final int head = slangPersistent.intHead();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }
    }

    /** Aggregated, randomized access to every element */
    public static class Get extends Base {
        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= slangPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent_int() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= slangPersistent.getInt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    /* Sequential access for all elements */
    public static class Iterate extends Base {
        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}