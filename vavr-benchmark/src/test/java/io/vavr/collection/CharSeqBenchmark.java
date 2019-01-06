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
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.Objects;
import java.util.Random;

import static java.lang.String.valueOf;
import static io.vavr.JmhRunner.create;
import static io.vavr.collection.Collections.areEqual;

public class CharSeqBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Repeat.class,
            Prepend.class,
            Append.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(java.lang.String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"10", "100", "1000", "2500"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        char[] ELEMENTS;

        java.lang.String javaPersistent;
        fj.data.LazyString fjavaPersistent;
        io.vavr.collection.CharSeq vavrPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = new char[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                ELEMENTS[i] = (char) random.nextInt(Character.MAX_VALUE);
            }
            EXPECTED_AGGREGATE = Iterator.ofAll(ELEMENTS).reduce((x, y) -> (char) JmhRunner.aggregate((int) x, (int) y));

            javaPersistent = create(java.lang.String::new, ELEMENTS, ELEMENTS.length, v -> java.util.Arrays.equals(v.toCharArray(), ELEMENTS));
            fjavaPersistent = create(fj.data.LazyString::str, javaPersistent, javaPersistent.length(), v -> Objects.equals(v.toStringEager(), javaPersistent));
            vavrPersistent = create(io.vavr.collection.CharSeq::of, javaPersistent, javaPersistent.length(), v -> v.contentEquals(javaPersistent));
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_persistent() {
            final Object head = javaPersistent.charAt(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object vavr_persistent() {
            final Object head = vavrPersistent.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Tail extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.substring(1);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.CharSeq values = vavrPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public int java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (int i = 0; i < ELEMENTS.length; i++) {
                aggregate ^= fjavaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int vavr_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= vavrPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    public static class Update extends Base {
        final char replacement = '❤';

        @Benchmark
        public Object java_persistent() {
            java.lang.String values = javaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.substring(0, i) + replacement + values.substring(i + 1);
            }
            assert Array.ofAll(values.toCharArray()).forAll(c -> c == replacement);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.CharSeq values = vavrPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, replacement);
            }
            assert values.forAll(c -> c == replacement);
            return values;
        }
    }

    public static class Repeat extends Base {
        final char value = '❤';

        @Benchmark
        public Object vavr_persistent() {
            return CharSeq.of(value).repeat(CONTAINER_SIZE);
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = "";
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = ELEMENTS[i] + values;
            }
            assert Objects.equals(values, javaPersistent);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = fj.data.LazyString.str(valueOf(ELEMENTS[i])).append(values);
            }
            assert Objects.equals(values.eval(), javaPersistent);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.CharSeq values = io.vavr.collection.CharSeq.empty();
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                values = values.prepend(ELEMENTS[i]);
            }
            assert values.contentEquals(vavrPersistent);
            return values;
        }
    }

    public static class Append extends Base {
        @Benchmark
        public Object java_persistent() {
            java.lang.String values = "";
            for (char c : ELEMENTS) {
                values = values + c;
            }
            assert Objects.equals(values, javaPersistent);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.LazyString values = fj.data.LazyString.empty;
            for (char c : ELEMENTS) {
                values = values.append(valueOf(c));
            }
            assert areEqual(values.toStream(), vavrPersistent);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.CharSeq values = io.vavr.collection.CharSeq.empty();
            for (char c : ELEMENTS) {
                values = values.append(c);
            }
            assert values.contentEquals(vavrPersistent);
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public int java_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaPersistent.charAt(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Character> iterator = fjavaPersistent.toStream().iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int vavr_persistent() {
            int aggregate = 0;
            for (final Iterator<Character> iterator = vavrPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}
