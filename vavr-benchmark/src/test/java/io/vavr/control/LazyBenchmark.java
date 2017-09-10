/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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
package io.vavr.control;

import io.vavr.JmhRunner;
import io.vavr.Lazy;
import io.vavr.collection.Array;
import io.vavr.collection.Iterator;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import static io.vavr.API.Array;
import static io.vavr.JmhRunner.Includes.JAVA;
import static io.vavr.JmhRunner.Includes.VAVR;

public class LazyBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            Get.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES, JAVA, VAVR);
        JmhRunner.runSlowNoAsserts(CLASSES, JAVA, VAVR);
    }

    @State(Scope.Benchmark)
    public static class Base {
        final int SIZE = 10;

        Integer[] EAGERS;
        io.vavr.Lazy<Integer>[] INITED_LAZIES;

        @Setup
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void setup() {
            EAGERS = Iterator.range(0, SIZE).toJavaArray(Integer.class);
            INITED_LAZIES = Iterator.of(EAGERS).map(i -> {
                final Lazy<Integer> lazy = Lazy.of(() -> i);
                lazy.get();
                return lazy;
            }).toJavaList().toArray(new Lazy[0]);
        }
    }

    @Threads(4)
    @SuppressWarnings({ "WeakerAccess", "rawtypes" })
    public static class Get extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            io.vavr.Lazy<Integer>[] LAZIES;

            @Setup(Level.Invocation)
            @SuppressWarnings("unchecked")
            public void initializeMutable(Base state) {
                LAZIES = Iterator.of(state.EAGERS).map(i -> Lazy.of(() -> i)).toJavaList().toArray(new Lazy[0]);
            }
        }

        @Benchmark
        public void java_eager(Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                bh.consume(EAGERS[i]);
            }
        }

        @Benchmark
        public void vavr_inited_lazy(Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                assert INITED_LAZIES[i].isEvaluated();
                bh.consume(INITED_LAZIES[i].get());
            }
        }

        @Benchmark
        public void vavr_lazy(Initialized state, Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                assert !state.LAZIES[i].isEvaluated();
                bh.consume(state.LAZIES[i].get());
            }
        }
    }
}
