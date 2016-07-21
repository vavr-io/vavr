package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.collection.Array;
import javaslang.collection.Iterator;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static javaslang.API.For;
import static javaslang.JmhRunner.getRandomValues;

/**
 * Benchmark for nested loops vs javaslang's For().yield comprehensions.
 *
 * @see javaslang.API.For2
 */
public class ForBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            ForComprehensionNestedFor.class
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
        @Param({ "10", "20", "30" })
        public int CONTAINER_SIZE;

        List<Integer> ELEMENTS;

        @Setup
        public void setup(Blackhole bh) {
            Integer[] array = getRandomValues(CONTAINER_SIZE, 0, true);
            ELEMENTS = Arrays.asList(array);
        }
    }

    public static class ForComprehensionNestedFor extends Base {

        @Benchmark
        public void slang_for(Blackhole bh) {
            BiFunction<Integer,Integer,Integer> bf = (i,j) -> {bh.consume(i);bh.consume(j); return 0;};
            List<Integer> res = For(ELEMENTS, ELEMENTS).yield(bf).collect(Collectors.toList());

            assert Iterator.ofAll(res).forAll(i -> i == 0);
        }

        @Benchmark
        public void java_for(Blackhole bh) {
            BiFunction<Integer,Integer,Integer> bf = (i,j) -> {bh.consume(i);bh.consume(j); return 0;};
            List<Integer> result = new ArrayList<>(CONTAINER_SIZE * CONTAINER_SIZE);
            for (Integer i : ELEMENTS) {
                for (Integer j : ELEMENTS) {
                    result.add(bf.apply(i,j));
                }
            }

            assert Iterator.ofAll(result).forAll(i -> i == 0);
        }
    }

}