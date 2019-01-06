/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.JmhRunner;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

import static io.vavr.JmhRunner.Includes.*;
import static io.vavr.JmhRunner.getRandomValues;

@SuppressWarnings({ "ALL", "unchecked", "rawtypes" })
public class IteratorBenchmark {

    static final Array<Class<?>> CLASSES = Array.of(
            Sliding.class,
            Concat.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES, JAVA, SCALA, VAVR);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"10", "100", "1000", "2500"})
        public int CONTAINER_SIZE;

        Integer[] ELEMENTS;

        scala.collection.Iterator<Integer> scalaIterator;
        io.vavr.collection.Iterator<Integer> vavrIterator;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            scalaIterator = (scala.collection.Iterator<Integer>) (Object) scala.collection.mutable.WrappedArray$.MODULE$.make(ELEMENTS).iterator();
            vavrIterator = Iterator.of(ELEMENTS);
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
        public void vavr_persistent(Blackhole bh) {
            final Iterator<Seq<Integer>> values = vavrIterator.sliding(3);
            while (values.hasNext()) {
                bh.consume(values.next());
            }
        }
    }

    @State(Scope.Benchmark)
    public static class Concat {

        @Param({ "10", "20" , "100", "1000" })
        private int size;

        @Benchmark
        public void vavr_persistent(Blackhole bh) {
            Iterator<Integer> iterator = Iterator.range(0, size)
                    .foldLeft(Iterator.empty(), (result, __) -> result.concat(Iterator.of(1)));

            long sum = 0;
            while (iterator.hasNext()) {
                sum += iterator.next();
            }
            Assert.assertEquals(size, sum);
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            final scala.collection.Iterator<Integer> iterator = scala.collection.Iterator.range(0, size)
                    .foldLeft((scala.collection.Iterator<Integer>) (Object) scala.collection.Iterator.empty(),
                    (result, i) -> result.$plus$plus(() -> scala.collection.Iterator.single(1)));
            
            long sum = 0;
            while (iterator.hasNext()) {
                sum += iterator.next();
            }
            Assert.assertEquals(size, sum);
        }
    }
}
