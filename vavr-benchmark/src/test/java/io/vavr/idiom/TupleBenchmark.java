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
package io.vavr.idiom;

import io.vavr.JmhRunner;
import io.vavr.Tuple;
import io.vavr.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static io.vavr.API.Array;

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
        public Object vavr_tuple() { return Tuple.of(0, 1); }
    }

    public static class Tuple4Benchmark {
        @Benchmark
        public Object java_tuple() { return new Integer[] { 0, 1, 2 }; }

        @Benchmark
        public Object vavr_tuple() { return Tuple.of(0, 1, 2, 3); }
    }

    public static class Tuple8Benchmark {
        @Benchmark
        public Object java_tuple() { return new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 }; }

        @Benchmark
        public Object vavr_tuple() { return Tuple.of(0, 1, 2, 3, 4, 5, 6, 7); }
    }
}
