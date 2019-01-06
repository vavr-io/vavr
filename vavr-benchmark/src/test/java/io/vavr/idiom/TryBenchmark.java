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
import io.vavr.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import static io.vavr.API.Array;
import static io.vavr.API.Try;

/**
 * Benchmark for Try vs try/catch.
 */
public class TryBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            Try.class
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
        int inverse(int divisor) throws ArithmeticException { return 1 / divisor; }
    }

    public static class Try extends Base {
        @Benchmark
        public void java_try(Blackhole bh) {
            for (int i = 0; i <= 1; i++) {
                int result;
                try {
                    result = inverse(i);
                } catch (ArithmeticException e) {
                    result = 0;
                }
                assert result == i;
                bh.consume(result);
            }
        }

        @Benchmark
        public void vavr_try(Blackhole bh) {
            for (int i = 0; i <= 1; i++) {
                int i2 = i;
                final int result = Try(() -> inverse(i2))
                        .recover(ArithmeticException.class, 0)
                        .get();

                assert result == i;
                bh.consume(result);
            }
        }
    }
}
