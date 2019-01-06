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
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

import static io.vavr.API.*;

/**
 * Benchmark for nested loops vs Vavr's For().yield comprehensions.
 *
 * @see For2
 */
public class PatternMatchingBenchmark {
    
    static final Array<Class<?>> CLASSES = Array(
            MatchVsSwitchIntValues.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class MatchVsSwitchIntValues {

        int[] VALUES;

        @Setup
        public void setup() {
            final int INSTANCES = 1000;
            final int CASES = 5;
            VALUES = Array.range(0, INSTANCES).map(i -> new Random(0).nextInt(CASES)).toJavaStream().mapToInt(i -> i).toArray();
        }
        
        @Benchmark
        public void java_switch(Blackhole bh) {
            for (int i : VALUES) {
                final String result;
                switch (i) {
                    case 0:
                        result = "0";
                        break;
                    case 1:
                        result = "1";
                        break;
                    case 2:
                        result = "2";
                        break;
                    case 3:
                        result = "3";
                        break;
                    default:
                        result = "4";
                        break;
                }

                assert String.valueOf(i).equals(result);
                bh.consume(result);
            }
        }

        @Benchmark
        public void vavr_match(Blackhole bh) {
            for (int i : VALUES) {
                final String result = Match(i).of(
                        Case($(0), "0"),
                        Case($(1), "1"),
                        Case($(2), "2"),
                        Case($(3), "3"),
                        Case($(), "4")
                );
                assert String.valueOf(i).equals(result);
                bh.consume(result);
            }
        }
    }
}
