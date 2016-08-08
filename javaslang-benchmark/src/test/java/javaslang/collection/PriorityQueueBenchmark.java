package javaslang.collection;

import fj.P;
import javaslang.JmhRunner;
import javaslang.Tuple2;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import scala.math.Ordering;
import scala.math.Ordering$;
import scalaz.Heap;
import scalaz.Order;
import scalaz.Order$;

import java.util.Collections;

import static java.util.Arrays.asList;
import static javaslang.JmhRunner.create;
import static javaslang.JmhRunner.getRandomValues;
import static scala.collection.JavaConversions.asScalaBuffer;

@SuppressWarnings({"UnnecessaryFullyQualifiedName", "UnnecessarilyQualifiedInnerClassAccess"})
public class PriorityQueueBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Enqueue.class,
            Dequeue.class,
            Sort.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);
        static final Order<Integer> SCALAZ_ORDER = Order$.MODULE$.fromScalaOrdering(SCALA_ORDERING);

        @Param({"10", "100", "1000"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;

        scalaz.Heap<Integer> scalazPersistent;
        javaslang.collection.PriorityQueue<Integer> slangPersistent;
        fj.data.PriorityQueue<Integer, Integer> fjava_persistent;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            EXPECTED_AGGREGATE = Iterator.of(ELEMENTS).reduce(JmhRunner::aggregate);

            scalazPersistent = create(v -> scalaz.Heap.Empty$.MODULE$.<Integer> apply().insertAll(asScalaBuffer(v), SCALAZ_ORDER), asList(ELEMENTS), v -> v.size() == CONTAINER_SIZE);
            fjava_persistent = create(v -> fj.data.PriorityQueue.<Integer> emptyInt().enqueue(v), List.of(ELEMENTS).map(v -> P.p(v, v)).toJavaList(), ELEMENTS.length, v -> v.toList().length() == CONTAINER_SIZE);
            slangPersistent = create(javaslang.collection.PriorityQueue::of, ELEMENTS, ELEMENTS.length, v -> v.size() == CONTAINER_SIZE);
        }
    }

    public static class Enqueue extends Base {
        @Benchmark
        @SuppressWarnings({"Convert2streamapi", "ManualArrayToCollectionCopy"})
        public Object java_mutable() {
            final java.util.PriorityQueue<Integer> values = new java.util.PriorityQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert values.size() == CONTAINER_SIZE;
            return values;
        }

        @Benchmark
        @SuppressWarnings({"Convert2streamapi", "ManualArrayToCollectionCopy"})
        public Object java_blocking_mutable() {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = new java.util.concurrent.PriorityBlockingQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert values.size() == CONTAINER_SIZE;
            return values;
        }

        @Benchmark
        public Object scala_mutable() {
            final scala.collection.mutable.PriorityQueue<Integer> values = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                values.$plus$eq(element);
            }
            assert values.size() == CONTAINER_SIZE;
            return values;
        }

        @Benchmark
        public Object scalaz_persistent() {
            scalaz.Heap<Integer> values = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                values = values.insert(element, SCALAZ_ORDER);
            }
            assert values.size() == CONTAINER_SIZE;
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.PriorityQueue<Integer, Integer> values = fj.data.PriorityQueue.emptyInt();
            for (Integer element : ELEMENTS) {
                values = values.enqueue(element, element);
            }
            assert values.toList().length() == CONTAINER_SIZE;
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> values = javaslang.collection.PriorityQueue.empty();
            for (Integer element : ELEMENTS) {
                values = values.enqueue(element);
            }
            assert values.size() == CONTAINER_SIZE;
            return values;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Dequeue extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            java.util.PriorityQueue<Integer> javaMutable = new java.util.PriorityQueue<>();
            java.util.concurrent.PriorityBlockingQueue<Integer> javaBlockingMutable = new java.util.concurrent.PriorityBlockingQueue<>();
            scala.collection.mutable.PriorityQueue<Integer> scalaMutable = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                Collections.addAll(javaMutable, state.ELEMENTS);
                Collections.addAll(javaBlockingMutable, state.ELEMENTS);
                for (Integer element : state.ELEMENTS) {
                    scalaMutable.$plus$eq(element);
                }

                assert (javaMutable.size() == state.CONTAINER_SIZE)
                       && (javaBlockingMutable.size() == state.CONTAINER_SIZE)
                       && (scalaMutable.size() == state.CONTAINER_SIZE);
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
                javaBlockingMutable.clear();
                scalaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            final java.util.PriorityQueue<Integer> values = state.javaMutable;

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object java_blocking_mutable(Initialized state) {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = state.javaBlockingMutable;

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object scala_mutable(Initialized state) {
            final scala.collection.mutable.PriorityQueue<Integer> values = state.scalaMutable;

            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.dequeue();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object scalaz_persistent() {
            scalaz.Heap<Integer> values = scalazPersistent;

            int aggregate = 0;
            while (!values.isEmpty()) {
                final scala.Tuple2<Integer, scalaz.Heap<Integer>> uncons = values.uncons().get();
                aggregate ^= uncons._1;
                values = uncons._2;
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.PriorityQueue<Integer, Integer> values = fjava_persistent;

            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.top().some()._1();
                values = values.dequeue();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> values = slangPersistent;

            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = values.dequeue();
                aggregate ^= dequeue._1;
                values = dequeue._2;
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    public static class Sort extends Base {
        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public Object java_mutable() {
            final java.util.PriorityQueue<Integer> values = new java.util.PriorityQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert values.size() == CONTAINER_SIZE;
            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public Object java_blocking_mutable() {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = new java.util.concurrent.PriorityBlockingQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert values.size() == CONTAINER_SIZE;
            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public Object java_treeset_mutable() {
            javaslang.collection.TreeMap<Integer, javaslang.collection.List<Integer>> values = javaslang.collection.TreeMap.empty();
            for (Integer element : ELEMENTS) {
                final javaslang.collection.List<Integer> vs = values.get(element).getOrElse(javaslang.collection.List.empty()).prepend(element);
                values = values.put(element, vs);
            }
            assert values.values().map(Traversable::size).sum().intValue() == CONTAINER_SIZE;
            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.List<Integer>> min = values.head();
                for (Integer integer : min._2) {
                    aggregate ^= integer;
                }
                values = values.remove(min._1);
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object scala_mutable() {
            scala.collection.mutable.PriorityQueue<Integer> values = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                values = values.$plus$eq(element);
            }
            assert values.size() == CONTAINER_SIZE;
            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.dequeue();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object scalaz_persistent() {
            scalaz.Heap<Integer> values = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                values = values.insert(element, SCALAZ_ORDER);
            }
            assert values.size() == CONTAINER_SIZE;
            int aggregate = 0;
            while (!values.isEmpty()) {
                final scala.Tuple2<Integer, Heap<Integer>> uncons = values.uncons().get();
                aggregate ^= uncons._1;
                values = uncons._2;
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.PriorityQueue<Integer, Integer> values = fj.data.PriorityQueue.emptyInt();
            for (Integer element : ELEMENTS) {
                values = values.enqueue(element, element);
            }
            assert values.toList().length() == CONTAINER_SIZE;

            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.top().some()._1();
                values = values.dequeue();
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> values = javaslang.collection.PriorityQueue.of(ELEMENTS);
            assert values.size() == CONTAINER_SIZE;

            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = values.dequeue();
                aggregate ^= dequeue._1;
                values = dequeue._2;
            }
            assert values.isEmpty() && (aggregate == EXPECTED_AGGREGATE);
            return values;
        }
    }
}