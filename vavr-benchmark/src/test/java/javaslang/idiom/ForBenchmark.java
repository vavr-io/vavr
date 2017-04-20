/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;
import static javaslang.API.*;
import static javaslang.JmhRunner.getRandomValues;

/**
 * Benchmark for nested loops vs javaslang's For().yield comprehensions.
 *
 * @see For2
 */
public class ForBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            For.class
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
        int CONTAINER_SIZE = 1000;
        int AGGREGATE;
        List<Integer> ELEMENTS;

        final BiFunction<Integer, Integer, Integer> AGGREGATOR = (i, j) -> i ^ j;

        @Setup
        public void setup() {
            ELEMENTS = Arrays.asList(getRandomValues(CONTAINER_SIZE, 0, true));

            AGGREGATE = 0;
            for (Integer i : ELEMENTS) {
                for (Integer j : ELEMENTS) {
                    AGGREGATE += AGGREGATOR.apply(i, j);
                }
            }

        }
    }

    public static class For extends Base {
        @Benchmark
        public Object java_for() {
            final List<Integer> result = new ArrayList<>(CONTAINER_SIZE * CONTAINER_SIZE);
            for (Integer i : ELEMENTS) {
                for (Integer j : ELEMENTS) {
                    result.add(AGGREGATOR.apply(i, j));
                }
            }

            assert Array(result).sum().intValue() == AGGREGATE;
            return result;
        }

        @Benchmark
        public Object slang_for() {
            final List<Integer> result = For(ELEMENTS, ELEMENTS).yield(AGGREGATOR).collect(toList());
            assert Array(result).sum().intValue() == AGGREGATE;
            return result;
        }
    }

}
