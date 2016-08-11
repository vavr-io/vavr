package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static java.lang.String.valueOf;
import static javaslang.JmhRunner.create;
import static javaslang.collection.Collections.areEqual;

@SuppressWarnings({"ALL", "unchecked"})
public class CharSeqBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Replace.class,
            Prepend.class,
            Append.class,
            Slice.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(java.lang.String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"10", "100", "1000"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        char[] ELEMENTS;

        java.lang.String javaPersistent;
        fj.data.LazyString fjavaPersistent; // cannot handle large Strings
        javaslang.collection.CharSeq slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = new char[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                ELEMENTS[i] = (char) random.nextInt(Character.MAX_VALUE);
            }
            EXPECTED_AGGREGATE = Iterator.ofAll(ELEMENTS).reduce((x, y) -> (char) JmhRunner.aggregate((int) x, (int) y));

            javaPersistent = create(java.lang.String::new, ELEMENTS, CONTAINER_SIZE, v -> Arrays.equals(v.toCharArray(), ELEMENTS));
            fjavaPersistent = create(fj.data.LazyString::str, javaPersistent, CONTAINER_SIZE, v -> Objects.equals(v.toStringEager(), javaPersistent));
            slangPersistent = create(javaslang.collection.CharSeq::of, javaPersistent, CONTAINER_SIZE, v -> v.contentEquals(javaPersistent));
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_persistent() {
            final Object head = javaPersistent.charAt(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Tail extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.substring(1);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public int java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (int i = 0; i < ELEMENTS.length; i++) {
                aggregate ^= fjavaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= slangPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    public static class Update extends Base {
        final char replacement = '❤';

        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.substring(0, i) + replacement + values.substring(i + 1);
            }
            assert Array.ofAll(values.toCharArray()).forAll(c -> c == replacement);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, replacement);
            }
            assert values.forAll(c -> c == replacement);
            return values;
        }
    }

    public static class Replace extends Base {
        final char replacement = '❤';

        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.replace(values.charAt(i), replacement);
            }
            assert Array.ofAll(values.toCharArray()).forAll(c -> c == replacement);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.replace(values.charAt(i), replacement);
            }
            assert values.forAll(c -> c == replacement);
            return values;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = "";
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = ELEMENTS[i] + values;
            }
            assert Objects.equals(values, javaPersistent);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = fj.data.LazyString.str(valueOf(ELEMENTS[i])).append(values);
            }
            assert Objects.equals(values.eval(), javaPersistent);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = values.prepend(ELEMENTS[i]);
            }
            assert values.contentEquals(slangPersistent);
            return values;
        }
    }

    public static class Append extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = "";
            for (char c : ELEMENTS) {
                values = values + c;
            }
            assert Objects.equals(values, javaPersistent);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (char c : ELEMENTS) {
                values = values.append(valueOf(c));
            }
            assert areEqual(values.toStream(), slangPersistent);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (char c : ELEMENTS) {
                values = values.append(c);
            }
            assert values.contentEquals(slangPersistent);
            return values;
        }
    }

    /** Consume the vector one-by-one, from the front and back */
    public static class Slice extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) { // stores the whole list underneath
            java.lang.String values = javaPersistent;
            while (!values.isEmpty()) {
                values = values.substring(1, values.length());
                values = values.substring(0, values.length() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            javaslang.collection.CharSeq values = slangPersistent;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public int java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Character> iterator = fjavaPersistent.toStream().iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (int i = 0; i < slangPersistent.length(); ) {
                char[] leaf = (char[]) slangPersistent.delegate.getLeafUnsafe(i);
                for (int value : leaf) {
                    aggregate ^= value;
                }
                i += leaf.length;
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}