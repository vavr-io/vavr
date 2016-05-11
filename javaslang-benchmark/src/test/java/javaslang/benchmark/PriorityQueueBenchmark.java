package javaslang.benchmark;

import javaslang.Tuple2;
import javaslang.collection.Traversable;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;
import scala.math.Ordering;
import scala.math.Ordering$;
import scalaz.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static javaslang.benchmark.BenchmarkResultAggregator.displayRatios;

public class PriorityQueueBenchmark {
    public static void main(String... args) throws Exception { /* main is more reliable than a test */
        final Options opts = new OptionsBuilder()
                .include(PriorityQueueBenchmark.class.getSimpleName())
                .shouldDoGC(false)
                .shouldFailOnError(true)
                .build();

        final Collection<RunResult> results = new Runner(opts).run();
        displayRatios(results, "^.*slang.*$");
    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(value = 1, jvmArgsAppend = { "-XX:+UseG1GC", "-Xss100m", "-Xms1g", "-Xmx1g", "-disableassertions" }) /* set fork to 0 if you want to debug */
    public static class Base {
        protected static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);
        protected static final Order<Integer> SCALAZ_ORDER = Order$.MODULE$.fromScalaOrdering(SCALA_ORDERING);

        @Param({ "10", "1000", "100000" })
        public int CONTAINER_SIZE;

        public Integer[] ELEMENTS;
        int expectedAggregate = 0;

        @Setup
        public void setup() {
            final Random random = new Random(0);

            ELEMENTS = new Integer[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final int value = random.nextInt(CONTAINER_SIZE) - (CONTAINER_SIZE / 2);
                ELEMENTS[i] = value;
                expectedAggregate ^= value;
            }
        }

        protected static <T> void assertEquals(T a, T b) {
            if (!Objects.equals(a, b)) {
                throw new IllegalStateException(a + " != " + b);
            }
        }
    }

    public static class Enqueue extends Base {
        @Benchmark
        @SuppressWarnings({ "Convert2streamapi", "ManualArrayToCollectionCopy" })
        public void java_mutable() {
            final java.util.PriorityQueue<Integer> values = new java.util.PriorityQueue<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        @SuppressWarnings({ "Convert2streamapi", "ManualArrayToCollectionCopy" })
        public void java_blocking_mutable() {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = new java.util.concurrent.PriorityBlockingQueue<>(ELEMENTS.length);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable() {
            final scala.collection.mutable.PriorityQueue<Integer> values = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                values.$plus$eq(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scalaz_persistent() {
            scalaz.Heap<Integer> values = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                values = values.insert(element, SCALAZ_ORDER);
            }
            assertEquals(values.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> q = javaslang.collection.PriorityQueue.empty();
            for (Integer element : ELEMENTS) {
                q = q.enqueue(element);
            }
            assertEquals(q.size(), CONTAINER_SIZE);
        }
    }

    public static class Dequeue extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            java.util.PriorityQueue<Integer> javaMutable = new java.util.PriorityQueue<>();
            java.util.concurrent.PriorityBlockingQueue<Integer> javaBlockingMutable = new java.util.concurrent.PriorityBlockingQueue<>();
            scala.collection.mutable.PriorityQueue<Integer> scalaMutable = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);

            boolean initializedPersistent = false;
            scalaz.Heap<Integer> scalazPersistent = scalaz.Heap.Empty$.MODULE$.apply();
            javaslang.collection.PriorityQueue<Integer> slangPersistent = javaslang.collection.PriorityQueue.empty();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaMutable.size(), 0);
                Collections.addAll(javaMutable, state.ELEMENTS);
                assertEquals(javaMutable.size(), state.CONTAINER_SIZE);

                assertEquals(javaBlockingMutable.size(), 0);
                Collections.addAll(javaBlockingMutable, state.ELEMENTS);
                assertEquals(javaBlockingMutable.size(), state.CONTAINER_SIZE);

                assertEquals(scalaMutable.size(), 0);
                for (Integer element : state.ELEMENTS) {
                    scalaMutable.$plus$eq(element);
                }
                assertEquals(scalaMutable.size(), state.CONTAINER_SIZE);

                if (!initializedPersistent) {
                    assertEquals(scalazPersistent.size(), 0);
                    assertEquals(slangPersistent.size(), 0);
                    for (Integer element : state.ELEMENTS) {
                        scalazPersistent = scalazPersistent.insert(element, SCALAZ_ORDER);
                        slangPersistent = slangPersistent.enqueue(element);
                    }
                    assertEquals(scalazPersistent.size(), state.CONTAINER_SIZE);
                    assertEquals(slangPersistent.size(), state.CONTAINER_SIZE);

                    initializedPersistent = true;
                }
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
                javaBlockingMutable.clear();
                scalaMutable.clear();
            }
        }

        @Benchmark
        public void java_mutable(Initialized state) {
            final java.util.PriorityQueue<Integer> values = state.javaMutable;

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void java_blocking_mutable(Initialized state) {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = state.javaBlockingMutable;

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scala_mutable(Initialized state) {
            final scala.collection.mutable.PriorityQueue<Integer> values = state.scalaMutable;

            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.dequeue();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scalaz_persistent(Initialized state) {
            scalaz.Heap<Integer> values = state.scalazPersistent;

            int aggregate = 0;
            while (!values.isEmpty()) {
                final scala.Tuple2<Integer, scalaz.Heap<Integer>> uncons = values.uncons().get();
                aggregate ^= uncons._1;
                values = uncons._2;
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void slang_persistent(Initialized state) {
            javaslang.collection.PriorityQueue<Integer> values = state.slangPersistent;

            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = values.dequeue();
                aggregate ^= dequeue._1;
                values = dequeue._2;
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }
    }

    public static class Sort extends Base {
        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_mutable() {
            final java.util.PriorityQueue<Integer> values = new java.util.PriorityQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_blocking_mutable() {
            final java.util.concurrent.PriorityBlockingQueue<Integer> values = new java.util.concurrent.PriorityBlockingQueue<>(CONTAINER_SIZE);
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);

            int aggregate = 0;
            for (; !values.isEmpty(); values.poll()) {
                aggregate ^= values.peek();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scala_mutable() {
            scala.collection.mutable.PriorityQueue<Integer> values = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                values = values.$plus$eq(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);

            int aggregate = 0;
            while (!values.isEmpty()) {
                aggregate ^= values.dequeue();
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void scalaz_persistent() {
            scalaz.Heap<Integer> values = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                values = values.insert(element, SCALAZ_ORDER);
            }
            assertEquals(values.size(), CONTAINER_SIZE);

            int aggregate = 0;
            while (!values.isEmpty()) {
                final scala.Tuple2<Integer, Heap<Integer>> uncons = values.uncons().get();
                aggregate ^= uncons._1;
                values = uncons._2;
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        @SuppressWarnings("ManualArrayToCollectionCopy")
        public void java_treeset() {
            javaslang.collection.TreeMap<Integer, javaslang.collection.List<Integer>> values = javaslang.collection.TreeMap.empty();
            for (Integer element : ELEMENTS) {
                final javaslang.collection.List<Integer> vs = values.get(element).getOrElse(javaslang.collection.List.empty()).prepend(element);
                values = values.put(element, vs);
            }
            assertEquals(values.values().map(Traversable::size).sum().intValue(), CONTAINER_SIZE);

            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.List<Integer>> min = values.head();
                for (Integer integer : min._2) {
                    aggregate ^= integer;
                }
                values = values.remove(min._1);
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> values = javaslang.collection.PriorityQueue.empty();
            for (Integer element : ELEMENTS) {
                values = values.enqueue(element);
            }
            assertEquals(values.size(), CONTAINER_SIZE);

            int aggregate = 0;
            while (!values.isEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = values.dequeue();
                aggregate ^= dequeue._1;
                values = dequeue._2;
            }
            assertEquals(values.size(), 0);
            assertEquals(aggregate, expectedAggregate);
        }
    }
}