package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import javaslang.collection.CharSeq;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static java.lang.String.valueOf;
import static javaslang.benchmark.JmhRunner.assertEquals;

public class CharSeqBenchmark {
    public static void main(java.lang.String... args) {
        JmhRunner.runDebug(CharSeqBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int expectedAggregate = 0;
        char[] ELEMENTS;

        java.lang.String javaPersistent;
        fj.data.LazyString fjavaPersistent;
        javaslang.collection.CharSeq slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);

            final StringBuilder results = new StringBuilder(CONTAINER_SIZE);
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final char value = (char) random.nextInt(Character.MAX_VALUE);
                results.append(value);

                expectedAggregate ^= value;
            }
            ELEMENTS = results.toString().toCharArray();

            assertEquals(javaPersistent, null);
            javaPersistent = results.toString();
            assertEquals(javaPersistent.length(), CONTAINER_SIZE);

            assertEquals(fjavaPersistent, null);
            fjavaPersistent = fj.data.LazyString.str(results.toString());
            assertEquals(fjavaPersistent.length(), CONTAINER_SIZE);

            assertEquals(slangPersistent, null);
            slangPersistent = CharSeq.of(results);
            assertEquals(slangPersistent.size(), CONTAINER_SIZE);
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_persistent() { return javaPersistent.charAt(0); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.head(); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.head(); }
    }

    public static class Tail extends Base {
        @Benchmark
        public Object java_persistent() { return javaPersistent.substring(1); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.tail(); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.tail(); }
    }

    public static class Get extends Base {
        final int index = CONTAINER_SIZE / 2;

        @Benchmark
        public Object java_persistent() { return javaPersistent.charAt(index); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.charAt(index); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.charAt(index); }
    }

    public static class Update extends Base {
        final int index = CONTAINER_SIZE / 2;
        final char replacement = '-';

        @Benchmark
        public Object java_persistent() { return javaPersistent.substring(0, index) + replacement + javaPersistent.substring(index + 1); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.update(index, replacement); }
    }

    public static class Prepend extends Base {
        @Benchmark
        public void java_persistent() {
            java.lang.String values = "";
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = ELEMENTS[i] + values;
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = fj.data.LazyString.str(valueOf(ELEMENTS[i])).append(values);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = values.prepend(ELEMENTS[i]);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }
    }

    public static class Append extends Base {
        @Benchmark
        public void java_persistent() {
            java.lang.String values = "";
            for (char c : ELEMENTS) {
                values = values + c;
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (char c : ELEMENTS) {
                values = values.append(valueOf(c));
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.CharSeq values = javaslang.collection.CharSeq.empty();
            for (char c : ELEMENTS) {
                values = values.append(c);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }
    }

    public static class Iterate extends Base {
        @Benchmark
        public void java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void fjava_persistent() {
            int aggregate = 0;
            for (fj.data.LazyString iterable = fjavaPersistent; !iterable.isEmpty(); iterable = iterable.tail()) {
                aggregate ^= iterable.head();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void slang_persistent() {
            int aggregate = 0;
            for (Character c : slangPersistent) {
                aggregate ^= c;
            }
            assertEquals(aggregate, expectedAggregate);
        }
    }
}