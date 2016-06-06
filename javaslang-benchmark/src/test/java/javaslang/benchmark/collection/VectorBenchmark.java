package javaslang.benchmark.collection;

import javaslang.benchmark.JmhRunner;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.compat.java8.JFunction;

import java.util.*;
import java.util.stream.Collectors;

import static javaslang.benchmark.JmhRunner.*;

public class VectorBenchmark {
    public static void main(String... args) {
        JmhRunner.runQuick(VectorBenchmark.class);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({ "10", "100", "1000" })
        public int CONTAINER_SIZE;

        int expectedAggregate = 0;
        public Integer[] ELEMENTS;

        /* Only use these for non-mutating operations */
        final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

        fj.data.Seq<Integer> fjavaPersistent = fj.data.Seq.empty();
        org.pcollections.PVector<Integer> pcollectionsPersistent = org.pcollections.TreePVector.empty();
        scala.collection.immutable.Vector<Integer> scalaPersistent = scala.collection.immutable.Vector$.MODULE$.empty();
        javaslang.collection.Vector<Integer> slangPersistent = javaslang.collection.Vector.empty();

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);

            assertEquals(javaMutable.size(), 0);
            assertEquals(fjavaPersistent.length(), 0);
            assertEquals(pcollectionsPersistent.size(), 0);
            assertEquals(scalaPersistent.size(), 0);
            assertEquals(slangPersistent.size(), 0);
            for (Integer element : ELEMENTS) {
                expectedAggregate ^= element;

                javaMutable.add(element);

                fjavaPersistent = fjavaPersistent.snoc(element);
                pcollectionsPersistent = pcollectionsPersistent.plus(element);
                scalaPersistent = scalaPersistent.appendBack(element);
                slangPersistent = slangPersistent.append(element);
            }
            assertEquals(javaMutable.size(), CONTAINER_SIZE);
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
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, scala.collection.immutable.Vector.empty());
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, fj.data.Seq.empty());
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            assertEquals(values, org.pcollections.TreePVector.empty());
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assertEquals(values, javaslang.collection.Vector.empty());
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
        public Object java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.set(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.updateAt(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.update(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.with(i, 0);
            }
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
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
        public void scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendFront(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
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
        public void scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendBack(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.snoc(element);
            }
            assertEquals(values.length(), CONTAINER_SIZE);
        }

        @Benchmark
        public void pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(values.size(), element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
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
        public Object slang_persistent() { return slangPersistent.groupBy(Integer::bitCount); }
    }

    public static class Slice extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < CONTAINER_SIZE; j++) {
                    bh.consume(javaMutable.subList(i, j));
                }
            }
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < CONTAINER_SIZE; j++) {
                    bh.consume(scalaPersistent.slice(i, j));
                }
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                for (int j = i; j < CONTAINER_SIZE; j++) {
                    bh.consume(slangPersistent.slice(i, j));
                }
            }
        }
    }

    public static class Iterate extends Base {
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
        @SuppressWarnings("ForLoopReplaceableByForEach")
        public void java_mutable(Initialized state) {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = state.javaMutable.iterator(); iterator.hasNext(); ) {
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