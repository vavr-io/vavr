package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.compat.java8.JFunction;

import java.util.*;
import java.util.stream.Collectors;

import static javaslang.JmhRunner.*;
import static scala.collection.JavaConversions.asJavaCollection;

public class VectorBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Prepend.class,
            Append.class,
            GroupBy.class,
            Slice.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebug(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormal(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "32", "1024", "32768"/*, "1048576"*/ }) // i.e. depth 1,2,3(,4) for a branching factor of 32
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;

        /* Only use these for non-mutating operations */
        final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

        fj.data.Seq<Integer> fjavaPersistent = fj.data.Seq.empty();
        org.pcollections.PVector<Integer> pcollectionsPersistent = org.pcollections.TreePVector.empty();
        scala.collection.immutable.Vector<Integer> scalaPersistent = scala.collection.immutable.Vector$.MODULE$.empty();
        clojure.lang.PersistentVector clojurePersistent = clojure.lang.PersistentVector.EMPTY;
        javaslang.collection.Vector<Integer> slangPersistent = Vector.empty();

        @Setup
        @SuppressWarnings("unchecked")
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            EXPECTED_AGGREGATE = Array.of(ELEMENTS).reduce(JmhRunner::xor);

            require(javaMutable::isEmpty,
                    fjavaPersistent::isEmpty,
                    pcollectionsPersistent::isEmpty,
                    scalaPersistent::isEmpty,
                    clojurePersistent::isEmpty,
                    slangPersistent::isEmpty);

            java.util.Collections.addAll(javaMutable, ELEMENTS);
            pcollectionsPersistent = org.pcollections.TreePVector.from(javaMutable);
            fjavaPersistent = fj.data.Seq.fromJavaList(javaMutable);
            scalaPersistent = (scala.collection.immutable.Vector<Integer>) scala.collection.immutable.Vector$.MODULE$.apply(scala.collection.JavaConversions.asScalaBuffer(javaMutable));
            clojurePersistent = clojure.lang.PersistentVector.create(javaMutable);
            slangPersistent = Vector.ofAll(javaMutable);

            require(() -> Collections.equals(javaMutable, Arrays.asList(ELEMENTS)),
                    () -> Collections.equals(fjavaPersistent, javaMutable),
                    () -> Collections.equals(pcollectionsPersistent, javaMutable),
                    () -> Collections.equals(clojurePersistent, javaMutable),
                    () -> Collections.equals(asJavaCollection(scalaPersistent), javaMutable),
                    () -> Collections.equals(slangPersistent, javaMutable));
        }
    }

    public static class Create extends Base {
        @Benchmark
        public Object java_mutable() {
            final ArrayList<Integer> values = new ArrayList<>(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<?> values = scala.collection.immutable.Vector$.MODULE$.apply(scalaPersistent);
            require(() -> Objects.equals(values, scalaPersistent));
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            final clojure.lang.PersistentVector values = clojure.lang.PersistentVector.create(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark()
        public Object fjava_persistent() {
            final fj.data.Seq<Integer> values = fj.data.Seq.fromJavaList(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.from(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values.head();
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() {
            final Object head = javaMutable.get(0);
            require(() -> Objects.equals(head, ELEMENTS[0]));
            return head;
        }

        @Benchmark
        public Object scala_persistent() {
            final Object head = scalaPersistent.head();
            require(() -> Objects.equals(head, javaMutable.get(0)));
            return head;
        }

        @Benchmark
        public Object clojure_persistent() {
            final Object head = clojurePersistent.nth(0);
            require(() -> Objects.equals(head, javaMutable.get(0)));
            return head;
        }

        @Benchmark
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            require(() -> Objects.equals(head, javaMutable.get(0)));
            return head;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final Object head = pcollectionsPersistent.get(0);
            require(() -> Objects.equals(head, javaMutable.get(0)));
            return head;
        }

        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
            require(() -> Objects.equals(head, javaMutable.get(0)));
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
                require(javaMutable::isEmpty);
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                require(() -> javaMutable.size() == state.CONTAINER_SIZE);
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
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojurePersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.pop();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public Object java_mutable() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutable.get(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object scala_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= scalaPersistent.apply(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object clojure_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= (int) clojurePersistent.get(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object fjava_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= fjavaPersistent.index(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object pcollections_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= pcollectionsPersistent.get(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object slang_persistent() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= slangPersistent.get(i);
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }

    public static class Update extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                require(javaMutable::isEmpty);
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                require(() -> javaMutable.size() == state.CONTAINER_SIZE);
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
                values.set(i, 0);
            }
            require(values, v -> Array.ofAll(v).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.updateAt(i, 0);
            }
            require(values, v -> Array.ofAll(asJavaCollection(v)).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojurePersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.assocN(i, 0);
            }
            require(values, v -> Array.of(v.toArray()).forAll(e -> Objects.equals(e, 0)));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, 0);
            }
            require(values, v -> Array.ofAll(v).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.with(i, 0);
            }
            require(values, v -> Array.ofAll(v).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, 0);
            }
            require(values, v -> v.forAll(e -> e == 0));
            return values;
        }
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static class Prepend extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendFront(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(asJavaCollection(v)).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(0, element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            require(values, v -> Collections.equals(v.reverse(), javaMutable));
            return values;
        }
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static class Append extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendBack(element);
            }
            require(values, v -> Collections.equals(asJavaCollection(v), javaMutable));
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.snoc(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
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
            return scalaPersistent.groupBy(JFunction.func(Integer::bitCount));
        }

        @Benchmark
        public Object slang_persistent() {
            return slangPersistent.groupBy(Integer::bitCount);
        }
    }

    public static class Slice extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < Math.min(CONTAINER_SIZE, 100); j++) {
                    bh.consume(javaMutable.subList(i, j));
                }
            }
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < Math.min(CONTAINER_SIZE, 100); j++) {
                    bh.consume(scalaPersistent.slice(i, j));
                }
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < Math.min(CONTAINER_SIZE, 100); j++) {
                    bh.consume(slangPersistent.slice(i, j));
                }
            }
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                require(javaMutable::isEmpty);
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                require(() -> javaMutable.size() == state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = state.javaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object clojure_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = clojurePersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = fjavaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object pcollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pcollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }
}