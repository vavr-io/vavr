package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import org.openjdk.jmh.annotations.*;
import scala.compat.java8.JFunction;

import java.util.*;
import java.util.stream.Collectors;

import static javaslang.benchmark.JmhRunner.*;

public class ListBenchmark {
    public static void main(String... args) {
        JmhRunner.runQuick(ListBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int expectedAggregate = 0;
        public Integer[] ELEMENTS;

        /* Only use these for non-mutating operations */
        final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();
        final java.util.LinkedList<Integer> javaMutableLinked = new java.util.LinkedList<>();
        final scala.collection.mutable.MutableList<Integer> scalaMutable = new scala.collection.mutable.MutableList<>();

        fj.data.List<Integer> fjavaPersistent = fj.data.List.list();
        org.pcollections.PStack<Integer> pcollectionsPersistent = org.pcollections.ConsPStack.empty();
        scala.collection.immutable.List<Integer> scalaPersistent = scala.collection.immutable.List$.MODULE$.empty();
        javaslang.collection.List<Integer> slangPersistent = javaslang.collection.List.empty();

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            for (Integer element : ELEMENTS) {
                expectedAggregate ^= element;
            }

            assertEquals(javaMutable.size(), 0);
            assertEquals(javaMutableLinked.size(), 0);
            assertEquals(scalaMutable.size(), 0);
            assertEquals(fjavaPersistent.length(), 0);
            assertEquals(pcollectionsPersistent.size(), 0);
            assertEquals(scalaPersistent.size(), 0);
            assertEquals(slangPersistent.size(), 0);
            for (int i = CONTAINER_SIZE - 1; i >= 0; i--) {
                final Integer element = ELEMENTS[i];

                javaMutable.add(0, element);
                javaMutableLinked.addFirst(element);
                scalaMutable.prependElem(element);

                fjavaPersistent = fjavaPersistent.cons(element);
                pcollectionsPersistent = pcollectionsPersistent.plus(element);
                scalaPersistent = scalaPersistent.$colon$colon(element);
                slangPersistent = slangPersistent.prepend(element);
            }
            assertEquals(javaMutable.size(), CONTAINER_SIZE);
            assertEquals(javaMutableLinked.size(), CONTAINER_SIZE);
            assertEquals(scalaMutable.size(), CONTAINER_SIZE);
            assertEquals(fjavaPersistent.length(), CONTAINER_SIZE);
            assertEquals(pcollectionsPersistent.size(), CONTAINER_SIZE);
            assertEquals(scalaPersistent.size(), CONTAINER_SIZE);
            assertEquals(slangPersistent.size(), CONTAINER_SIZE);
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() { return javaMutable.get(0); }

        @Benchmark
        public Object java_mutable_linked() { return javaMutableLinked.get(0); }

        @Benchmark
        public Object scala_mutable() { return scalaMutable.head(); }

        @Benchmark
        public Object scala_persistent() { return scalaPersistent.head(); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.head(); }

        @Benchmark
        public Object pcollections_persistent() { return pcollectionsPersistent.get(0); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.head(); }
    }

    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable.size(), 0);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public void java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.remove(0);
            }
            assertEquals(values, new java.util.ArrayList<>());
        }

        @Benchmark
        public void scala_persistent() {
            scala.collection.immutable.List<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.drop(1);
            }
            assertEquals(values, scala.collection.immutable.List.empty());
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.List<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, fj.data.List.list());
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PStack<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            assertEquals(values, org.pcollections.ConsPStack.empty());
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.List<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, javaslang.collection.List.empty());
        }
    }

    public static class Get extends Base {
        @Benchmark
        public void java_mutable() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(javaMutable.get(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void java_mutable_linked() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(javaMutableLinked.get(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void scala_mutable() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(scalaMutable.get(i).get(), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void scala_persistent() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(scalaPersistent.apply(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void fjava_persistent() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(fjavaPersistent.index(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void pcollections_persistent() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(pcollectionsPersistent.get(i), ELEMENTS[i]);
            }
        }

        @Benchmark
        public void slang_persistent() {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                assertEquals(slangPersistent.get(i), ELEMENTS[i]);
            }
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
                assertEquals(javaMutable.size(), 0);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);

                assertEquals(javaMutableLinked.size(), 0);
                Collections.addAll(javaMutableLinked, state.ELEMENTS);
                assertEquals(javaMutableLinked.size(), state.CONTAINER_SIZE);

                assertEquals(scalaMutable.size(), 0);
                for (int i = state.CONTAINER_SIZE - 1; i >= 0; i--) {
                    scalaMutable.prependElem(state.ELEMENTS[i]);
                }
                assertEquals(scalaMutable.size(), state.CONTAINER_SIZE);
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
            return values;
        }

        @Benchmark
        public Object java_mutable_linked(Initialized state) {
            final java.util.LinkedList<Integer> values = state.javaMutableLinked;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.set(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object scala_mutable(Initialized state) {
            final scala.collection.mutable.MutableList<Integer> values = state.scalaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.update(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PStack<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.with(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.List<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, 0);
            }
            return values;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_mutable_linked() {
            final java.util.LinkedList<Integer> values = new java.util.LinkedList<>();
            for (Integer element : ELEMENTS) {
                values.addFirst(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable() {
            final scala.collection.mutable.MutableList<Integer> values = new scala.collection.mutable.MutableList<>();
            for (Integer element : ELEMENTS) {
                values.prependElem(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_persistent() {
            scala.collection.immutable.List<Integer> values = scala.collection.immutable.List$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.$colon$colon(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.List<Integer> values = fj.data.List.list();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PStack<Integer> values = org.pcollections.ConsPStack.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.List<Integer> values = javaslang.collection.List.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }
    }

    public static class Append extends Base {
        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_mutable_linked() {
            final java.util.LinkedList<Integer> values = new java.util.LinkedList<>();
            for (Integer element : ELEMENTS) {
                values.addLast(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable() {
            final scala.collection.mutable.MutableList<Integer> values = new scala.collection.mutable.MutableList<>();
            for (Integer element : ELEMENTS) {
                values.appendElem(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_persistent() {
            scala.collection.immutable.List<Integer> values = scala.collection.immutable.List$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.$colon$colon$colon(scala.collection.immutable.List$.MODULE$.empty().$colon$colon(element)); // TODO there should be a better way to append an element to the end of the list in Scala
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.List<Integer> values = fj.data.List.list();
            for (Integer element : ELEMENTS) {
                values = values.snoc(element);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PStack<Integer> values = org.pcollections.ConsPStack.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(values.size(), element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.List<Integer> values = javaslang.collection.List.empty();
            for (Integer element : ELEMENTS) {
                values = values.append(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }
    }

    public static class GroupBy extends Base {
        @Benchmark
        public Object java_mutable() { return javaMutable.stream().collect(Collectors.groupingBy(Integer::bitCount)); }

        @Benchmark
        public Object scala_persistent() { return scalaPersistent.groupBy(JFunction.func(Integer::bitCount)); }

        @Benchmark
        public Object fjava_persistent() { return fjavaPersistent.groupBy(Integer::bitCount); }

        @Benchmark
        public Object slang_persistent() { return slangPersistent.groupBy(Integer::bitCount); }
    }

    public static class Iterate extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();
            final java.util.LinkedList<Integer> javaMutableLinked = new java.util.LinkedList<>();
            final scala.collection.mutable.MutableList<Integer> scalaMutable = new scala.collection.mutable.MutableList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable.size(), 0);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);

                assertEquals(javaMutableLinked.size(), 0);
                Collections.addAll(javaMutableLinked, state.ELEMENTS);
                assertEquals(javaMutableLinked.size(), state.CONTAINER_SIZE);

                assertEquals(scalaMutable.size(), 0);
                for (int i = state.CONTAINER_SIZE - 1; i >= 0; i--) {
                    scalaMutable.prependElem(state.ELEMENTS[i]);
                }
                assertEquals(scalaMutable.size(), state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
                javaMutableLinked.clear();
                scalaMutable.clear();
            }
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void java_mutable(Initialized state) {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = state.javaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void java_mutable_linked(Initialized state) {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = state.javaMutableLinked.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scala_mutable(Initialized state) {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = state.scalaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void fjava_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = fjavaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void pcollections_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = pcollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assertEquals(aggregate, expectedAggregate);
        }
    }
}