package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.Tuple;
import javaslang.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static javaslang.API.Array;

/**
 * Benchmark for Tuple[2,4,8] vs an array.
 */
@State(Scope.Benchmark)
public class TupleBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            Tuple2Benchmark.class,
            Tuple4Benchmark.class,
            Tuple8Benchmark.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    public static class Tuple2Benchmark {
        @Benchmark
        public Object java_tuple() { return new Integer[] { 0, 1 }; }

        @Benchmark
        public Object slang_tuple() { return Tuple.of(0, 1); }
    }

    public static class Tuple4Benchmark {
        @Benchmark
        public Object java_tuple() { return new Integer[] { 0, 1, 2 }; }

        @Benchmark
        public Object slang_tuple() { return Tuple.of(0, 1, 2, 3); }
    }

    public static class Tuple8Benchmark {
        @Benchmark
        public Object java_tuple() { return new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 }; }

        @Benchmark
        public Object slang_tuple() { return Tuple.of(0, 1, 2, 3, 4, 5, 6, 7); }
    }
}