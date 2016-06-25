package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import scala.compat.java8.JFunction;

import java.util.*;
import java.util.stream.Collectors;

import static javaslang.JmhRunner.*;
import static scala.collection.JavaConversions.*;

public class ListBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Prepend.class,
            Append.class,
            GroupBy.class,
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
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;

        /* Only use these for non-mutating operations */
        final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();
        final java.util.LinkedList<Integer> javaMutableLinked = new java.util.LinkedList<>();
        final scala.collection.mutable.MutableList<Integer> scalaMutable = new scala.collection.mutable.MutableList<>();

        fj.data.List<Integer> fjavaPersistent = fj.data.List.list();
        org.pcollections.PStack<Integer> pcollectionsPersistent = org.pcollections.ConsPStack.empty();
        scala.collection.immutable.List<Integer> scalaPersistent = scala.collection.immutable.List$.MODULE$.empty();
        clojure.lang.IPersistentList clojurePersistent = clojure.lang.PersistentList.EMPTY;
        javaslang.collection.List<Integer> slangPersistent = javaslang.collection.List.empty();

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            EXPECTED_AGGREGATE = Iterator.of(ELEMENTS).reduce(JmhRunner::xor);

            require(javaMutable::isEmpty,
                    javaMutableLinked::isEmpty,
                    scalaMutable::isEmpty,
                    scalaPersistent::isEmpty,
                    () -> clojurePersistent.equals(clojure.lang.PersistentList.EMPTY),
                    fjavaPersistent::isEmpty,
                    pcollectionsPersistent::isEmpty,
                    slangPersistent::isEmpty);

            java.util.Collections.addAll(javaMutable, ELEMENTS);
            javaMutableLinked.addAll(javaMutable);
            scalaMutable.$plus$plus$eq(asScalaBuffer(javaMutable));

            scalaPersistent = scala.collection.immutable.List$.MODULE$.apply(scalaMutable);
            clojurePersistent = clojure.lang.PersistentList.create(javaMutable);
            fjavaPersistent = fj.data.List.fromIterator(javaMutable.iterator());
            pcollectionsPersistent = org.pcollections.ConsPStack.from(javaMutable);
            slangPersistent = javaslang.collection.List.ofAll(javaMutable);

            require(() -> javaMutable.size() == CONTAINER_SIZE,
                    () -> javaMutableLinked.size() == CONTAINER_SIZE,
                    () -> scalaMutable.size() == CONTAINER_SIZE,
                    () -> fjavaPersistent.length() == CONTAINER_SIZE,
                    () -> pcollectionsPersistent.size() == CONTAINER_SIZE,
                    () -> scalaPersistent.size() == CONTAINER_SIZE,
                    () -> clojurePersistent.count() == CONTAINER_SIZE,
                    () -> slangPersistent.size() == CONTAINER_SIZE);
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
            final scala.collection.immutable.List<?> values = scala.collection.immutable.List$.MODULE$.apply(scalaMutable);
            require(() -> Objects.equals(values, scalaPersistent));
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            final clojure.lang.IPersistentStack values = clojure.lang.PersistentList.create(javaMutable);
            require(() -> Objects.equals(values, clojurePersistent));
            return values;
        }

        @Benchmark()
        public Object fjava_persistent() {
            final fj.data.List<Integer> values = fj.data.List.fromIterator(javaMutable.iterator());
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final org.pcollections.PStack<Integer> values = org.pcollections.ConsPStack.from(javaMutable);
            require(() -> Collections.equals(values, javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            final javaslang.collection.List<Integer> values = javaslang.collection.List.ofAll(javaMutable);
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
            final Object head = clojurePersistent.peek();
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
            require(values::isEmpty);
            return values;
        }

        @Benchmark
        public Object java_linked_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.remove(0);
            }
            require(values::isEmpty);
            return values;
        }

        @Benchmark
        @SuppressWarnings("RedundantCast")
        public Object scala_persistent() {
            scala.collection.immutable.List<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = (scala.collection.immutable.List<Integer>) values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.IPersistentStack values = clojurePersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.pop();
            }
            require(values, v -> Objects.equals(v, clojure.lang.PersistentList.EMPTY));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.List<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PStack<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            require(values, v -> v.isEmpty());
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.List<Integer> values = slangPersistent;
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
        public Object java_mutable_linked() {
            int aggregate = 0;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                aggregate ^= javaMutableLinked.get(i);
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
            final java.util.LinkedList<Integer> javaMutableLinked = new java.util.LinkedList<>();
            final scala.collection.mutable.MutableList<Integer> scalaMutable = new scala.collection.mutable.MutableList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                require(javaMutable::isEmpty,
                        javaMutableLinked::isEmpty,
                        scalaMutable::isEmpty);

                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                java.util.Collections.addAll(javaMutableLinked, state.ELEMENTS);
                for (int i = state.CONTAINER_SIZE - 1; i >= 0; i--) {
                    scalaMutable.prependElem(state.ELEMENTS[i]);
                }

                require(javaMutable, v -> v.size() == state.CONTAINER_SIZE,
                        javaMutableLinked, v -> v.size() == state.CONTAINER_SIZE,
                        scalaMutable, v -> v.size() == state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
                javaMutableLinked.clear();
                scalaMutable.clear();
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
        public Object java_mutable_linked(Initialized state) {
            final java.util.LinkedList<Integer> values = state.javaMutableLinked;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.set(i, 0);
            }
            require(values, v -> Array.ofAll(v).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object scala_mutable(Initialized state) {
            final scala.collection.mutable.MutableList<Integer> values = state.scalaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.update(i, 0);
            }
            require(values, v -> Array.ofAll(asJavaCollection(v)).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PStack<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.with(i, 0);
            }
            require(values, v -> Array.ofAll(v).forAll(e -> e == 0));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.List<Integer> values = slangPersistent;
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
        public Object java_mutable_linked() {
            final java.util.LinkedList<Integer> values = new java.util.LinkedList<>();
            for (Integer element : ELEMENTS) {
                values.addFirst(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object scala_mutable() {
            final scala.collection.mutable.MutableList<Integer> values = new scala.collection.mutable.MutableList<>();
            for (Integer element : ELEMENTS) {
                values.prependElem(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(asJavaCollection(v)).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.List<Integer> values = scala.collection.immutable.List$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.$colon$colon(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(asJavaCollection(v)).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.List<Integer> values = fj.data.List.list();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PStack<Integer> values = org.pcollections.ConsPStack.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            require(values, v -> Collections.equals(Array.ofAll(v).reverse(), javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.List<Integer> values = javaslang.collection.List.empty();
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
        public Object java_mutable_linked() {
            final java.util.LinkedList<Integer> values = new java.util.LinkedList<>();
            for (Integer element : ELEMENTS) {
                values.addLast(element);
            }
            require(values, v -> v.size() == CONTAINER_SIZE);
            return values;
        }

        @Benchmark
        public Object scala_mutable() {
            final scala.collection.mutable.MutableList<Integer> values = new scala.collection.mutable.MutableList<>();
            for (Integer element : ELEMENTS) {
                values.appendElem(element);
            }
            require(values, v -> Collections.equals(asJavaCollection(v), javaMutable));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.List<Integer> values = fj.data.List.list();
            for (Integer element : ELEMENTS) {
                values = values.snoc(element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PStack<Integer> values = org.pcollections.ConsPStack.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(values.size(), element);
            }
            require(values, v -> Collections.equals(v, javaMutable));
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.List<Integer> values = javaslang.collection.List.empty();
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
        public Object fjava_persistent() {
            return fjavaPersistent.groupBy(Integer::bitCount);
        }

        @Benchmark
        public Object slang_persistent() {
            return slangPersistent.groupBy(Integer::bitCount);
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static class Iterate extends Base {
        @Benchmark
        public Object java_mutable() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = javaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object java_mutable_linked() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = javaMutableLinked.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }

        @Benchmark
        public Object scala_mutable() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaMutable.iterator(); iterator.hasNext(); ) {
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
            for (final java.util.Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            require(aggregate, a -> a == EXPECTED_AGGREGATE);
            return aggregate;
        }
    }
}