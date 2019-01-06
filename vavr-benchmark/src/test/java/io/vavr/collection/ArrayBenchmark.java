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

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Arrays.asList;
import static io.vavr.JmhRunner.create;
import static io.vavr.JmhRunner.getRandomValues;
import static io.vavr.collection.Collections.areEqual;

public class ArrayBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Prepend.class,
            Append.class,
            Iterate.class
            , Fill.class
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
        @Param({"10", "100", "1000", "2500"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;

        java.util.ArrayList<Integer> javaMutable;
        fj.data.Array<Integer> fjavaMutable;
        io.vavr.collection.Array<Integer> vavrPersistent;
        org.pcollections.PVector<Integer> pcollVector;


        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            EXPECTED_AGGREGATE = Iterator.of(ELEMENTS).reduce(JmhRunner::aggregate);

            javaMutable = create(java.util.ArrayList::new, asList(ELEMENTS), v -> areEqual(v, asList(ELEMENTS)));
            fjavaMutable = create(fj.data.Array::array, ELEMENTS, ELEMENTS.length, v -> areEqual(v, asList(ELEMENTS)));
            vavrPersistent = create(io.vavr.collection.Array::ofAll, javaMutable, v -> areEqual(v, javaMutable));
            pcollVector = create(org.pcollections.TreePVector::from, javaMutable, v -> areEqual(v, javaMutable));
        }
    }

    public static class Create extends Base {
        @Benchmark
        public Object java_mutable() {
            final ArrayList<Integer> values = new ArrayList<>(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            final fj.data.Array<Integer> values = fj.data.Array.iterableArray(javaMutable);
            assert areEqual(values, fjavaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            final io.vavr.collection.Array<Integer> values = io.vavr.collection.Array.ofAll(javaMutable);
            assert areEqual(values, vavrPersistent);
            return values.head();
        }

        @Benchmark
        public Object pcoll_vector() {
            final org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.from(javaMutable);
            assert areEqual(values, pcollVector);
            return values;
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() {
            final Object head = javaMutable.get(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object fjava_mutable() {
            final Object head = fjavaMutable.get(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object vavr_persistent() {
            final Object head = vavrPersistent.get(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object pcoll_vector() {
            final Object head = pcollVector.get(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.remove(0);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Array<Integer> values = vavrPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object pcoll_vector() {
            org.pcollections.PVector<Integer> values = pcollVector;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(1);
            }
            assert values.isEmpty();
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public int java_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutable.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_mutable() {
            int aggregate = 0;
            for (int i = 0; i < ELEMENTS.length; i++) {
                aggregate ^= fjavaMutable.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int vavr_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= vavrPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public Object pcoll_vector() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= pcollVector.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    public static class Update extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.set(i, 0);
            }
            assert Iterator.ofAll(values).forAll(e -> e == 0);
            return javaMutable;
        }

        @Benchmark
        public Object fjava_mutable() {
            final fj.data.Array<Integer> values = fjavaMutable;
            for (int i = 0; i < ELEMENTS.length; i++) {
                values.set(i, 0);
            }
            assert values.forall(e -> e == 0);
            return fjavaMutable;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Array<Integer> values = vavrPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, 0);
            }
            assert values.forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object pcoll_vector() {
            org.pcollections.PVector<Integer> values = pcollVector;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.with(i, 0);
            }
            assert Iterator.ofAll(values).forAll(e -> e == 0);
            return values;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            assert areEqual(List.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = fj.data.Array.array(element).append(values);
            }
            assert areEqual(values.reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Array<Integer> values = io.vavr.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assert areEqual(values.reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object pcoll_vector() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(0, element);
            }
            assert areEqual(List.ofAll(values).reverse(), javaMutable);
            return values;
        }
    }

    public static class Append extends Base {
        @SuppressWarnings("ManualArrayToCollectionCopy")
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_mutable() {
            fj.data.Array<Integer> values = fj.data.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(fj.data.Array.array(element));
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Array<Integer> values = io.vavr.collection.Array.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object pcoll_vector() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public int java_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutable.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_mutable() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = fjavaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int vavr_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = vavrPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int pcoll_vector() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pcollVector.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    public static class Fill extends Base {
        @Benchmark
        public Object vavr_persistent_constant_supplier() {
            final io.vavr.collection.Array<Integer> values = io.vavr.collection.Array.fill(CONTAINER_SIZE, () -> ELEMENTS[0]);
            final Integer head = values.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object vavr_persistent_constant_object() {
            final io.vavr.collection.Array<Integer> values = io.vavr.collection.Array.fill(CONTAINER_SIZE, ELEMENTS[0]);
            final Integer head = values.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }
    }
}
