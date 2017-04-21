/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import static io.vavr.JmhRunner.create;
import static io.vavr.JmhRunner.getRandomValues;
import static io.vavr.collection.Collections.areEqual;
import static scala.collection.JavaConverters.asJavaCollection;
import static scala.collection.JavaConverters.asScalaBuffer;

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
        io.vavr.collection.BitSet<Integer> vavrPersistent;

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

            //noinspection unchecked
            scalaPersistent = create(v -> (scala.collection.immutable.BitSet) scala.collection.immutable.BitSet$.MODULE$.apply((scala.collection.mutable.Buffer<Object>) (Object) asScalaBuffer(v)), DISTINCT.toJavaList(), v -> areEqual(asJavaCollection(v), DISTINCT));
            vavrPersistent = create(io.vavr.collection.BitSet::ofAll, ELEMENTS, ELEMENTS.length, v -> areEqual(v, DISTINCT));
        }
    }

    public static class AddAll extends Base {
        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.BitSet values = new scala.collection.immutable.BitSet.BitSet1(0L);
            for (int element : ELEMENTS) {
                values = values.$plus(element);
            }
            assert ((scala.collection.immutable.SortedSet) values).equals(scalaPersistent);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Set<Integer> values = io.vavr.collection.BitSet.empty();
            for (Integer element : ELEMENTS) {
                values = values.add(element);
            }
            assert values.equals(vavrPersistent);
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
        public int vavr_persistent() {
            int aggregate = 0;
            for (final io.vavr.collection.Iterator<Integer> iterator = vavrPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}
