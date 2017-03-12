/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

import static java.util.Arrays.asList;
import static javaslang.JmhRunner.Includes.*;
import static javaslang.JmhRunner.getRandomValues;
import static scala.collection.JavaConversions.asScalaBuffer;

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

        /* Only use these for non-mutating operations */
        java.util.List<Integer> javaMutable;
        scala.collection.Iterator<Integer> scalaPersistent;
        javaslang.collection.Iterator<Integer> slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            javaMutable = asList(ELEMENTS);
            scalaPersistent = (scala.collection.Iterator<Integer>) scala.collection.immutable.Seq$.MODULE$.apply(asScalaBuffer(javaMutable)).iterator();
            slangPersistent = Iterator.of(ELEMENTS);
        }
    }

    public static class Sliding extends Base {

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            final scala.collection.Iterator.GroupedIterator values = scalaPersistent.sliding(3, 1);
            while (values.hasNext()) {
                bh.consume(values.next());
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            final Iterator<Seq<Integer>> values = slangPersistent.sliding(3);
            while (values.hasNext()) {
                bh.consume(values.next());
            }
        }
    }
}
