package javaslang.collection;

import javaslang.JmhRunner;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.collection.generic.CanBuildFrom;
import scala.compat.java8.functionConverterImpls.FromJavaFunction;
import scala.compat.java8.functionConverterImpls.FromJavaPredicate;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static javaslang.JmhRunner.Includes.*;
import static javaslang.JmhRunner.*;
import static javaslang.collection.Collections.areEqual;
import static scala.collection.JavaConversions.asJavaCollection;
import static scala.collection.JavaConversions.asScalaBuffer;
import static scala.compat.java8.JFunction.func;

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
            Append.class,
            GroupBy.class,
            Slice.class,
            Iterate.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES, JAVA, FUNCTIONAL_JAVA, PCOLLECTIONS, ECOLLECTIONS, CLOJURE, SCALA, JAVASLANG);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "32", "1024", "32768" /*, "1048576", "33554432", "1073741824" */ }) /* i.e. depth 1,2,3(,4,5,6) for a branching factor of 32 */
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;
        int[] RANDOMIZED_INDICES;

        /* Only use this for non-mutating operations */
        java.util.ArrayList<Integer> javaMutable;

        fj.data.Seq<Integer> fjavaPersistent;
        org.pcollections.PVector<Integer> pCollectionsPersistent;
        org.eclipse.collections.api.list.ImmutableList<Integer> eCollectionsPersistent;
        clojure.lang.PersistentVector clojurePersistent;
        scala.collection.immutable.Vector<Integer> scalaPersistent;
        javaslang.collection.Vector<Integer> slangPersistent;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            RANDOMIZED_INDICES = shuffle(Array.range(0, CONTAINER_SIZE).toJavaStream().mapToInt(Integer::intValue).toArray(), random);

            EXPECTED_AGGREGATE = Array.of(ELEMENTS).reduce(JmhRunner::aggregate);

            javaMutable = create(java.util.ArrayList::new, asList(ELEMENTS), v -> areEqual(v, asList(ELEMENTS)));
            fjavaPersistent = create(fj.data.Seq::fromJavaList, javaMutable, v -> areEqual(v, javaMutable));
            pCollectionsPersistent = create(org.pcollections.TreePVector::from, javaMutable, v -> areEqual(v, javaMutable));
            eCollectionsPersistent = create(org.eclipse.collections.impl.factory.Lists.immutable::ofAll, javaMutable, v -> areEqual(v, javaMutable));
            clojurePersistent = create(clojure.lang.PersistentVector::create, javaMutable, v -> areEqual(v, javaMutable));
            scalaPersistent = create(v -> (scala.collection.immutable.Vector<Integer>) scala.collection.immutable.Vector$.MODULE$.apply(asScalaBuffer(v)), javaMutable, v -> areEqual(asJavaCollection(v), javaMutable));
            slangPersistent = create(javaslang.collection.Vector::ofAll, javaMutable, v -> areEqual(v, javaMutable));
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
        public Object slang_persistent() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(javaMutable);
            assert areEqual(values, javaMutable);
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
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
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
            public void tearDown() {
                javaMutable.clear();
            }
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
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
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
        public int slang_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= slangPersistent.get(i);
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
            public void tearDown() {
                javaMutable.clear();
            }
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
                final MutableList<Integer> copy = values.toList();
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
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert values.forAll(e -> e == 0);
            return values;
        }
    }

    public static class Map extends Base {
        private final Function<Integer, Integer> MAPPER = i -> i + 1;

        @Benchmark
        public Object java_mutable_loop() {
            final Integer[] values = ELEMENTS.clone();
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values[i] = MAPPER.apply(values[i]);
            }
            assert areEqual(Array.of(values), Array.of(ELEMENTS).map(MAPPER));
            return values;
        }

        @Benchmark
        public Object java_mutable() {
            final java.util.List<Integer> values = javaMutable.stream().map(MAPPER).collect(Collectors.toList());
            assert areEqual(values, Array.of(ELEMENTS).map(MAPPER));
            return values;
        }

        @Benchmark
        public Object ecollections_persistent() {
            org.eclipse.collections.api.list.ImmutableList<Integer> values = eCollectionsPersistent.collect(MAPPER::apply);
            assert areEqual(values, Array.of(ELEMENTS).map(MAPPER));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            final CanBuildFrom canBuildFrom = scala.collection.immutable.Vector.canBuildFrom();
            final scala.collection.immutable.Vector<Integer> values = (scala.collection.immutable.Vector<Integer>) scalaPersistent.map(new FromJavaFunction<>(MAPPER), canBuildFrom);
            assert areEqual(asJavaCollection(values), Array.of(ELEMENTS).map(MAPPER));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            final javaslang.collection.Vector<Integer> values = slangPersistent.map(MAPPER);
            assert areEqual(values, Array.of(ELEMENTS).map(MAPPER));
            return values;
        }
    }

    public static class Filter extends Base {
        private final Predicate<Integer> ALL = i -> true;
        private final Predicate<Integer> SOME = i -> (i & 1) == 1;
        private final Predicate<Integer> NONE = i -> false;

        @Benchmark
        public void java_mutable(Blackhole bh) {
            final java.util.List<Integer> allValues = javaMutable.stream().filter(ALL).collect(Collectors.toList());
            assert areEqual(allValues, Array.of(ELEMENTS).filter(ALL));
            bh.consume(allValues);

            final java.util.List<Integer> someValues = javaMutable.stream().filter(SOME).collect(Collectors.toList());
            assert areEqual(someValues, Array.of(ELEMENTS).filter(SOME));
            bh.consume(someValues);

            final java.util.List<Integer> noValues = javaMutable.stream().filter(NONE).collect(Collectors.toList());
            assert areEqual(noValues, Array.of(ELEMENTS).filter(NONE));
            bh.consume(noValues);
        }

        @Benchmark
        public void ecollections_persistent(Blackhole bh) {
            org.eclipse.collections.api.list.ImmutableList<Integer> allValues = eCollectionsPersistent.select(ALL::test);
            assert areEqual(allValues, Array.of(ELEMENTS).filter(ALL));
            bh.consume(allValues);

            org.eclipse.collections.api.list.ImmutableList<Integer> someValues = eCollectionsPersistent.select(SOME::test);
            assert areEqual(someValues, Array.of(ELEMENTS).filter(SOME));
            bh.consume(someValues);

            org.eclipse.collections.api.list.ImmutableList<Integer> noValues = eCollectionsPersistent.select(NONE::test);
            assert areEqual(noValues, Array.of(ELEMENTS).filter(NONE));
            bh.consume(noValues);
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            final scala.collection.immutable.Vector<Integer> allValues = (scala.collection.immutable.Vector<Integer>) scalaPersistent.filter(new FromJavaPredicate(ALL));
            assert areEqual(asJavaCollection(allValues), Array.of(ELEMENTS).filter(ALL));
            bh.consume(allValues);

            final scala.collection.immutable.Vector<Integer> someValues = (scala.collection.immutable.Vector<Integer>) scalaPersistent.filter(new FromJavaPredicate(SOME));
            assert areEqual(asJavaCollection(someValues), Array.of(ELEMENTS).filter(SOME));
            bh.consume(someValues);

            final scala.collection.immutable.Vector<Integer> noValues = (scala.collection.immutable.Vector<Integer>) scalaPersistent.filter(new FromJavaPredicate(NONE));
            assert areEqual(asJavaCollection(noValues), Array.of(ELEMENTS).filter(NONE));
            bh.consume(noValues);
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            final javaslang.collection.Vector<Integer> allValues = slangPersistent.filter(ALL);
            assert areEqual(allValues, Array.of(ELEMENTS).filter(ALL));
            bh.consume(allValues);

            final javaslang.collection.Vector<Integer> someValues = slangPersistent.filter(SOME);
            assert areEqual(someValues, Array.of(ELEMENTS).filter(SOME));
            bh.consume(someValues);

            final javaslang.collection.Vector<Integer> noValues = slangPersistent.filter(NONE);
            assert areEqual(noValues, Array.of(ELEMENTS).filter(NONE));
            bh.consume(noValues);
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
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assert areEqual(values.reverse(), javaMutable);
            return values;
        }
    }

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
                final fj.data.Seq<Integer> newValues = values.snoc(element);
                assert values != newValues;
                values = newValues;
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
                final org.pcollections.PVector<Integer> newValues = values.plus(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (Integer element : ELEMENTS) {
                final clojure.lang.PersistentVector newValues = values.cons(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                final scala.collection.immutable.Vector<Integer> newValues = values.appendBack(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(asJavaCollection(values), javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                final javaslang.collection.Vector<Integer> newValues = values.append(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }
    }

    public static class GroupBy extends Base {
        @Benchmark
        public Object java_mutable() {
            return javaMutable.stream().collect(Collectors.groupingBy(Integer::bitCount));
        }

        @Benchmark
        public Object scala_persistent() {
            return scalaPersistent.groupBy(func(Integer::bitCount));
        }

        @Benchmark
        public Object slang_persistent() {
            return slangPersistent.groupBy(Integer::bitCount);
        }
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
        public void slang_persistent(Blackhole bh) {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i = 1; !values.isEmpty(); i++) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }
    }

    /** Sequential access for all elements */
    public static class Iterate extends Base {
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
        public int java_mutable(Initialized state) {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = state.javaMutable.iterator(); iterator.hasNext(); ) {
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
        public int slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}