/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package io.vavr.collection;

import io.vavr.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

import static io.vavr.JmhRunner.Includes.*;
import static io.vavr.JmhRunner.getRandomValues;

@SuppressWarnings({ "ALL", "unchecked", "rawtypes" })
public class IteratorBenchmark {
    
    static final Array<Class<?>> CLASSES = Array.of(
            Sliding.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES, JAVA, SCALA, JAVASLANG);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        Integer[] ELEMENTS;

        scala.collection.Iterator<Integer> scalaIterator;
        io.vavr.collection.Iterator<Integer> slangIterator;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            scalaIterator = (scala.collection.Iterator<Integer>) (Object) scala.collection.mutable.WrappedArray$.MODULE$.make(ELEMENTS).iterator();
            slangIterator = Iterator.of(ELEMENTS);
        }
    }

    public static class Sliding extends Base {

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            final scala.collection.Iterator.GroupedIterator values = scalaIterator.sliding(3, 1);
            while (values.hasNext()) {
                bh.consume(values.next());
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            final Iterator<Seq<Integer>> values = slangIterator.sliding(3);
            while (values.hasNext()) {
                bh.consume(values.next());
            }
        }
    }
}
