package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;

import static java.lang.String.valueOf;
import static javaslang.JmhRunner.require;

public class CharSeqBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
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

    public static void main(java.lang.String... args) {
        JmhRunner.runNormal(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        char[] ELEMENTS;

        java.lang.String javaPersistent;
        fj.data.LazyString fjavaPersistent;
        javaslang.collection.CharSeq slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = new char[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                ELEMENTS[i] = (char) random.nextInt(Character.MAX_VALUE);
            }
            EXPECTED_AGGREGATE = Iterator.ofAll(ELEMENTS).reduce((x, y) -> (char) JmhRunner.xor((int) x, (int) y));

            require(() -> javaPersistent == null,
                    () -> fjavaPersistent == null,
                    () -> slangPersistent == null);

            javaPersistent = new String(ELEMENTS);
            fjavaPersistent = fj.data.LazyString.str(javaPersistent);
            slangPersistent = CharSeq.of(javaPersistent);

            require(() -> Arrays.equals(javaPersistent.toCharArray(), ELEMENTS),
                    () -> Objects.equals(fjavaPersistent.eval(), javaPersistent),
                    () -> slangPersistent.contentEquals(javaPersistent));
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_persistent() {
            final Object head = javaPersistent.charAt(0);
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }

        @Benchmark
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }

        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
            require(() -> Objects.equals(head, ELEMENTS[0]));
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
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public void java_persistent(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = javaPersistent.charAt(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
            }
        }

        @Benchmark
        public void fjava_persistent(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = fjavaPersistent.charAt(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            for (int i = 0; i < ELEMENTS.length; i++) {
                final Object value = slangPersistent.charAt(i);
                bh.consume(value);
                require(i, j -> Objects.equals(value, ELEMENTS[j]));
            }
        }
    }

    public static class Update extends Base {
        final char replacement = '-';

        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values = values.substring(0, i) + replacement + values.substring(i + 1);
            }
            require(values, v -> Array.ofAll(v.toCharArray()).forAll(c -> c == replacement));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = slangPersistent;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values = values.update(i, replacement);
            }
            require(values, v -> v.forAll(c -> c == replacement));
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
            require(values, v -> Arrays.equals(v.toCharArray(), ELEMENTS));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = fj.data.LazyString.str(valueOf(ELEMENTS[i])).append(values);
            }
            require(values, v -> Objects.equals(fjavaPersistent.eval(), javaPersistent));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = values.prepend(ELEMENTS[i]);
            }
            require(values, v -> v.contentEquals(slangPersistent));
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
            require(values, v -> Arrays.equals(v.toCharArray(), ELEMENTS));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (char c : ELEMENTS) {
                values = values.append(valueOf(c));
            }
            require(values, v -> Collections.equals(v.toStream(), slangPersistent));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (char c : ELEMENTS) {
                values = values.append(c);
            }
            require(values, v -> v.contentEquals(slangPersistent));
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public Object java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Character> iterator = fjavaPersistent.toStream().iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Character> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }
}