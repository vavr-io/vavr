/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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
package io.vavr.other;

import io.vavr.API;
import io.vavr.JmhRunner;
import io.vavr.collection.Array;
import io.vavr.control.HashCodes;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Objects;

public class ObjectsBenchmark {
    static final Array<Class<?>> CLASSES = API.Array(
            HashCode.class
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
        // arbitrary value which is not in the default integer cache of the jvm
        int primitive = 7783;
        Integer boxed = 152837981;
    }

    public static class HashCode extends Base {
        @Benchmark
        public int java_primitive_primitive() {
            return Objects.hash(primitive, primitive);
        }

        @Benchmark
        public int java_primitive_boxed() {
            return Objects.hash(primitive, boxed);
        }

        @Benchmark
        public int java_boxed_boxed() {
            return Objects.hash(boxed, boxed);
        }

        @Benchmark
        public int java_primitive() {
            return Objects.hash(primitive);
        }

        @Benchmark
        public int java_boxed() {
            return Objects.hash(boxed);
        }

        @Benchmark
        public int java_boxed_8() {
            Integer object = boxed;
            return Objects.hash(object, object, object, object, object, object, object, object);
        }

        @Benchmark
        public int vavr_primitive_primitive() {
            return HashCodes.hash(primitive, primitive);
        }

        @Benchmark
        public int vavr_primitive_boxed() {
            return HashCodes.hash(primitive, boxed);
        }

        @Benchmark
        public int vavr_boxed_boxed() {
            return HashCodes.hash(boxed, boxed);
        }

        @Benchmark
        public int vavr_primitive() {
            return HashCodes.hash(primitive);
        }

        @Benchmark
        public int vavr_boxed() {
            return HashCodes.hash(boxed);
        }

        @Benchmark
        public int vavr_boxed_8() {
            Integer object = boxed;
            return HashCodes.hash(object, object, object, object, object, object, object, object);
        }
    }
}
