package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Array;
import javaslang.collection.Iterator;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static javaslang.API.For;
import static javaslang.JmhRunner.getRandomValues;

/**
 * Benchmark for Tuple2, Tuple3 vs an array.
 */
public class TupleBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Tuple2Benchmark.class,
            Tuple3Benchmark.class
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
    }

    public static class Tuple2Benchmark extends Base {

        @Benchmark
        public Object slang_tuple2_creation() {
            Tuple2<String, String> pair = Tuple.of("some", "string");
            return pair;
        }

        @Benchmark
        public Object java_tuple2_creation() {
            String[] pair = new String[] { "some", "string" };
            return pair;
        }
    }

    public static class Tuple3Benchmark extends Base {

        @Benchmark
        public Object slang_tuple2_creation() {
            Tuple3<String, String, String> triplet = Tuple.of("some", "string", "value");
            return triplet;
        }

        @Benchmark
        public Object java_tuple2_creation() {
            String[] triplet = new String[] { "some", "string", "value" };
            return triplet;
        }
    }

}