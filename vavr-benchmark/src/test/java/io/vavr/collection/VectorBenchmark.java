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
import org.openjdk.jmh.infra.Blackhole;
import scala.collection.generic.CanBuildFrom;
import scala.math.Ordering;
import scala.math.Ordering$;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static io.vavr.JmhRunner.Includes.*;
import static io.vavr.JmhRunner.*;
import static io.vavr.collection.Collections.areEqual;
import static io.vavr.collection.Vector.collector;
import static scala.collection.JavaConverters.asJavaCollection;
import static scala.collection.JavaConverters.asScalaBuffer;

@SuppressWarnings({ "ALL", "unchecked", "rawtypes" })
public class VectorBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Map.class,
            Filter.class,
            Prepend.class,
            PrependAll.class,
            Append.class,
            AppendAll.class,
            Insert.class,
            GroupBy.class,
            Slice.class,
            Sort.class,
            Iterate.class
            , Fill.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES, JAVA, FUNCTIONAL_JAVA, PCOLLECTIONS, ECOLLECTIONS, CLOJURE, SCALA, VAVR);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"10", "100", "1000", "1026", "2500"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;
        int[] INT_ELEMENTS;
        int[] RANDOMIZED_INDICES;

        /* Only use this for non-mutating operations */
        java.util.ArrayList<Integer> javaMutable;

        fj.data.Seq<Integer> fjavaPersistent;
        org.pcollections.PVector<Integer> pCollectionsPersistent;
        org.eclipse.collections.api.list.ImmutableList<Integer> eCollectionsPersistent;
        clojure.lang.PersistentVector clojurePersistent;
        scala.collection.immutable.Vector<Integer> scalaPersistent;
        io.vavr.collection.Vector<Integer> vavrPersistent;
        io.vavr.collection.Vector<Integer> vavrPersistentInt;
        io.vavr.collection.Vector<Byte> vavrPersistentByte;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            INT_ELEMENTS = ArrayType.asPrimitives(int.class, Array.of(ELEMENTS));
            RANDOMIZED_INDICES = shuffle(Array.range(0, CONTAINER_SIZE).toJavaStream().mapToInt(Integer::intValue).toArray(), random);

            EXPECTED_AGGREGATE = Array.of(ELEMENTS).reduce(JmhRunner::aggregate);

            javaMutable = create(java.util.ArrayList::new, asList(ELEMENTS), v -> areEqual(v, asList(ELEMENTS)));
            fjavaPersistent = create(fj.data.Seq::fromJavaList, javaMutable, v -> areEqual(v, javaMutable));
            pCollectionsPersistent = create(org.pcollections.TreePVector::from, javaMutable, v -> areEqual(v, javaMutable));
            eCollectionsPersistent = create(org.eclipse.collections.impl.factory.Lists.immutable::ofAll, javaMutable, v -> areEqual(v, javaMutable));
            clojurePersistent = create(clojure.lang.PersistentVector::create, javaMutable, v -> areEqual(v, javaMutable));
            scalaPersistent = create(v -> (scala.collection.immutable.Vector<Integer>) scala.collection.immutable.Vector$.MODULE$.apply(asScalaBuffer(v)), javaMutable, v -> areEqual(asJavaCollection(v), javaMutable));
            vavrPersistent = create(io.vavr.collection.Vector::ofAll, javaMutable, v -> areEqual(v, javaMutable));
            vavrPersistentInt = create(v -> io.vavr.collection.Vector.ofAll(INT_ELEMENTS), javaMutable, v -> areEqual(v, javaMutable) && (v.trie.type.type() == int.class));

            final byte[] BYTE_ELEMENTS = new byte[CONTAINER_SIZE];
            random.nextBytes(BYTE_ELEMENTS);
            vavrPersistentByte = create(v -> io.vavr.collection.Vector.ofAll(BYTE_ELEMENTS), javaMutable, v -> areEqual(v, Array.ofAll(BYTE_ELEMENTS)) && (v.trie.type.type() == byte.class));
        }
    }

    /** Bulk creation from array based, boxed source */
    public static class Create extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.List<Integer> values = new java.util.ArrayList<>(java.util.Arrays.asList(ELEMENTS));
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object java_mutable_boxed() {
            final java.util.List<Integer> values = new java.util.ArrayList<>();
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.add(INT_ELEMENTS[i]);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object java_mutable_boxed_stream() {
            final java.util.List<Integer> values = java.util.Arrays.stream(INT_ELEMENTS).boxed().collect(toList());
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            final fj.data.Seq<Integer> values = fj.data.Seq.fromJavaList(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.from(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            final org.eclipse.collections.api.list.ImmutableList<Integer> values = org.eclipse.collections.impl.factory.Lists.immutable.ofAll(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            final clojure.lang.PersistentVector values = clojure.lang.PersistentVector.create(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<?> values = scala.collection.immutable.Vector$.MODULE$.apply(scalaPersistent);
            assert Objects.equals(values, scalaPersistent);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            final io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.ofAll(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            final io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.ofAll(INT_ELEMENTS);
            assert (values.trie.type.type() == int.class) && areEqual(values, javaMutable);
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
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final Object head = pCollectionsPersistent.get(0);
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object ecollections_persistent() {
            final Object head = eCollectionsPersistent.getFirst();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object clojure_persistent() {
            final Object head = clojurePersistent.nth(0);
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object scala_persistent() {
            final Object head = scalaPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object vavr_persistent() {
            final Object head = vavrPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }
    }

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
            public void tearDown() { javaMutable.clear(); }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            java.util.List<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.subList(1, values.size()); /* remove(0) would copy everything, but this will slow access down because of nesting */
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pCollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = eCollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.drop(1);
            }
            assert values.isEmpty() && (values != eCollectionsPersistent);
            return values;
        }

        @Benchmark
        public void clojure_persistent(Blackhole bh) { /* stores the whole collection underneath */
            java.util.List<?> values = clojurePersistent;
            while (!values.isEmpty()) {
                values = values.subList(1, values.size());
                bh.consume(values);
            }
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert ((scala.collection.immutable.Seq) values).isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Vector<Integer> values = vavrPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            io.vavr.collection.Vector<Integer> values = vavrPersistentInt;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }
    }

    /** Aggregated, randomized access to every element */
    public static class Get extends Base {
        @Benchmark
        public int java_mutable() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= javaMutable.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= fjavaPersistent.index(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int pcollections_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= pCollectionsPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int ecollections_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= eCollectionsPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int clojure_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= (int) clojurePersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int scala_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= scalaPersistent.apply(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int vavr_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= vavrPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    /** Randomized update of every element */
    public static class Update extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() { javaMutable.clear(); }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i : RANDOMIZED_INDICES) {
                values.set(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pCollectionsPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.with(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = eCollectionsPersistent;
            for (int i : RANDOMIZED_INDICES) {
                final org.eclipse.collections.api.list.MutableList<Integer> copy = values.toList();
                copy.set(i, 0);
                values = copy.toImmutable();
            }
            assert Array.ofAll(values).forAll(e -> e == 0) && (values != eCollectionsPersistent);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojurePersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.assocN(i, 0);
            }
            assert Array.of(values.toArray()).forAll(e -> Objects.equals(e, 0));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.updateAt(i, 0);
            }
            assert Array.ofAll(asJavaCollection(values)).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Vector<Integer> values = vavrPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert values.forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            io.vavr.collection.Vector<Integer> values = vavrPersistentInt;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert (values.trie.type.type() == int.class) && values.forAll(e -> e == 0);
            return values;
        }
    }

    public static class Map extends Base {
        final CanBuildFrom canBuildFrom = scala.collection.immutable.Vector.canBuildFrom();
        private static int mapper(int i) { return i + 1; }

        @Benchmark
        public Object java_mutable_loop() {
            final Integer[] values = ELEMENTS.clone();
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values[i] = mapper(values[i]);
            }
            assert areEqual(Array.of(values), Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }

        @Benchmark
        public Object java_mutable() {
            final java.util.List<Integer> values = javaMutable.stream().map(Map::mapper).collect(toList());
            assert areEqual(values, Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            final org.eclipse.collections.api.list.ImmutableList<Integer> values = eCollectionsPersistent.collect(Map::mapper);
            assert areEqual(values, Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<Integer> values = (scala.collection.immutable.Vector<Integer>) scalaPersistent.map(Map::mapper, canBuildFrom);
            assert areEqual(asJavaCollection(values), Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            final io.vavr.collection.Vector<Integer> values = vavrPersistent.map(Map::mapper);
            assert areEqual(values, Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            final io.vavr.collection.Vector<Integer> values = vavrPersistentInt.map(Map::mapper);
            assert areEqual(values, Array.of(ELEMENTS).map(Map::mapper));
            return values;
        }
    }

    public static class Filter extends Base {
        private static boolean isOdd(int i) { return (i & 1) == 1; }

        @Benchmark
        public Object java_mutable() {
            final java.util.List<Integer> someValues = javaMutable.stream().filter(Filter::isOdd).collect(toList());
            assert areEqual(someValues, Array.of(ELEMENTS).filter(Filter::isOdd));
            return someValues;
        }

        @Benchmark
        public Object ecollections_persistent() {
            final org.eclipse.collections.api.list.ImmutableList<Integer> someValues = eCollectionsPersistent.select(Filter::isOdd);
            assert areEqual(someValues, Array.of(ELEMENTS).filter(Filter::isOdd));
            return someValues;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<Integer> someValues = (scala.collection.immutable.Vector<Integer>) ((scala.collection.Traversable<Integer>) scalaPersistent).filter(Filter::isOdd);
            assert areEqual(asJavaCollection(someValues), Array.of(ELEMENTS).filter(Filter::isOdd));
            return someValues;
        }

        @Benchmark
        public Object vavr_persistent() {
            final io.vavr.collection.Vector<Integer> someValues = vavrPersistent.filter(Filter::isOdd);
            assert areEqual(someValues, Array.of(ELEMENTS).filter(Filter::isOdd));
            return someValues;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            final io.vavr.collection.Vector<Integer> someValues = vavrPersistentInt.filter(Filter::isOdd);
            assert (someValues.trie.type.type() == int.class) && areEqual(someValues, Array.of(ELEMENTS).filter(Filter::isOdd));
            return someValues;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(); /* no initial value, as we're simulating dynamic usage */
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(0, element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = org.eclipse.collections.impl.factory.Lists.immutable.empty();
            for (Integer element : ELEMENTS) {
                final org.eclipse.collections.api.list.MutableList<Integer> copy = values.toList();
                copy.add(0, element);
                values = copy.toImmutable();
            }
            assert areEqual(values.toReversed(), javaMutable) && (values != eCollectionsPersistent);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (int i = 0; i < ELEMENTS.length; i++) {
                clojure.lang.PersistentVector prepended = clojure.lang.PersistentVector.create(ELEMENTS[i]);
                for (Object value : values) {
                    prepended = prepended.cons(value);  /* rebuild everything via append */
                }
                values = prepended;
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendFront(element);
            }
            assert areEqual(Array.ofAll(asJavaCollection(values)).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assert areEqual(values.reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.ofAll(ELEMENTS[0]);
            for (int i = 1; i < ELEMENTS.length; i++) {
                values = values.prepend(ELEMENTS[i]);
            }
            assert (values.trie.type.type() == int.class) && areEqual(values.reverse(), javaMutable);
            return values;
        }
    }

    public static class PrependAll extends Base {
        final CanBuildFrom canBuildFrom = scala.collection.immutable.Vector.canBuildFrom();

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final java.util.List<Integer> front = javaMutable.subList(0, i);
                final scala.collection.immutable.Vector<Integer> back = scalaPersistent.slice(i, CONTAINER_SIZE);

                scala.collection.immutable.Vector<Integer> values = back;
                for (int j = front.size() - 1; j >= 0; j--) {
                    values = values.appendFront(front.get(j));
                }
                assert areEqual(asJavaCollection(values), javaMutable);
                bh.consume(values);
            }
        }

        @Benchmark
        public void vavr_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final java.util.List<Integer> front = javaMutable.subList(0, i);
                final io.vavr.collection.Vector<Integer> back = vavrPersistent.slice(i, CONTAINER_SIZE);
                final io.vavr.collection.Vector<Integer> values = back.prependAll(front);
                assert areEqual(values, javaMutable);
                bh.consume(values);
            }
        }
    }

    /** Add all elements (one-by-one, as we're not testing bulk operations) */
    public static class Append extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(); /* no initial value as we're simulating dynamic usage */
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.snoc(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = org.eclipse.collections.impl.factory.Lists.immutable.empty();
            for (Integer element : ELEMENTS) {
                final org.eclipse.collections.api.list.MutableList<Integer> copy = values.toList();
                copy.add(element);
                values = copy.toImmutable();
            }
            assert areEqual(values, javaMutable) && (values != eCollectionsPersistent);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendBack(element);
            }
            assert areEqual(asJavaCollection(values), javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent() {
            io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object vavr_persistent_int() {
            io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.ofAll(INT_ELEMENTS[0]);
            for (int i = 1; i < INT_ELEMENTS.length; i++) {
                values = values.append(INT_ELEMENTS[i]);
            }
            assert (values.trie.type.type() == int.class) && areEqual(values, javaMutable);
            return values;
        }
    }

    public static class AppendAll extends Base {
        final CanBuildFrom canBuildFrom = scala.collection.immutable.Vector.canBuildFrom();

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final scala.collection.immutable.Vector<Integer> front = scalaPersistent.slice(0, i);
                final java.util.List<Integer> back = javaMutable.subList(i, CONTAINER_SIZE);
                final scala.collection.immutable.Vector<Integer> values = front.$plus$plus(asScalaBuffer(back), (CanBuildFrom<scala.collection.immutable.Vector<Integer>, Integer, scala.collection.immutable.Vector<Integer>>) canBuildFrom);
                assert areEqual(asJavaCollection(values), javaMutable);
                bh.consume(values);
            }
        }

        @Benchmark
        public void vavr_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final io.vavr.collection.Vector<Integer> front = vavrPersistent.slice(0, i);
                final java.util.List<Integer> back = javaMutable.subList(i, CONTAINER_SIZE);
                final io.vavr.collection.Vector<Integer> values = front.appendAll(back);
                assert areEqual(values, javaMutable);
                bh.consume(values);
            }
        }
    }

    public static class Insert extends Base {
        @Benchmark
        public void vavr_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final Vector<Integer> values = vavrPersistent.insert(i, 0);
                assert values.size() == CONTAINER_SIZE + 1;
                bh.consume(values);
            }
        }
    }

    public static class GroupBy extends Base {
        @Benchmark
        public Object java_mutable() { return javaMutable.stream().collect(groupingBy(Integer::bitCount)); }

        @Benchmark
        public Object scala_persistent() { return scalaPersistent.groupBy(Integer::bitCount); }

        @Benchmark
        public Object vavr_persistent() { return vavrPersistent.groupBy(Integer::bitCount); }
    }

    /** Consume the vector one-by-one, from the front and back */
    public static class Slice extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) { /* stores the whole collection underneath */
            java.util.List<Integer> values = javaMutable;
            while (!values.isEmpty()) {
                values = values.subList(1, values.size());
                values = values.subList(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void pcollections_persistent(Blackhole bh) {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = eCollectionsPersistent;
            for (int i = 1; !values.isEmpty(); i++) {
                values = values.subList(1, values.size());
                values = values.subList(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void clojure_persistent(Blackhole bh) { /* stores the whole collection underneath */
            java.util.List<?> values = clojurePersistent;
            while (!values.isEmpty()) {
                values = values.subList(1, values.size());
                values = values.subList(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void vavr_persistent(Blackhole bh) {
            io.vavr.collection.Vector<Integer> values = vavrPersistent;
            for (int i = 1; !values.isEmpty(); i++) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void vavr_persistent_int(Blackhole bh) {
            io.vavr.collection.Vector<Integer> values = this.vavrPersistentInt;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }
    }

    public static class Sort extends Base {
        static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);

        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() { javaMutable.clear(); }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            state.javaMutable.sort(Comparator.naturalOrder());
            assert areEqual(state.javaMutable, vavrPersistent.sorted());
            return state.javaMutable;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.Seq<Integer> results = ((scala.collection.Seq<Integer>) scalaPersistent).sorted(SCALA_ORDERING);
            assert areEqual(asJavaCollection(results), vavrPersistent.sorted());
            return results;
        }

        @Benchmark
        public Object vavr_persistent() {
            final Vector<Integer> results = vavrPersistent.sorted();
            assert areEqual(results, vavrPersistent.toJavaStream().sorted().collect(collector()));
            return results;
        }
    }

    /** Sequential access for all elements */
    public static class Iterate extends Base {
        @Benchmark
        public int java_mutable() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = javaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = fjavaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int ecollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = eCollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int pcollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pCollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int clojure_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = clojurePersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
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
        public int vavr_persistent_int() {
            final int[] aggregate = { 0 };
            vavrPersistentInt.trie.<int[]> visit((ordinal, leaf, start, end) -> {
                for (int i = start; i < end; i++) {
                    aggregate[0] ^= leaf[i];
                }
                return -1;
            });
            assert aggregate[0] == EXPECTED_AGGREGATE;
            return aggregate[0];
        }
    }

    public static class Fill extends Base {
        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<?> values = scala.collection.immutable.Vector$.MODULE$.fill(CONTAINER_SIZE, () -> ELEMENTS[0]);
            final Object head = values.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object vavr_persistent_constant_supplier() {
            final io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.fill(CONTAINER_SIZE, () -> ELEMENTS[0]);
            final Integer head = values.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object vavr_persistent_constant_object() {
            final io.vavr.collection.Vector<Integer> values = io.vavr.collection.Vector.fill(CONTAINER_SIZE, ELEMENTS[0]);
            final Integer head = values.head();
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }
    }
}
