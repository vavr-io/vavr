package javaslang.benchmark;

import javaslang.Tuple2;
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
    public static void main(String... args) throws Exception { // main is more reliable than a test
        final Options opts = new OptionsBuilder()
                .include(PriorityQueueBenchmark.class.getSimpleName())
                .shouldDoGC(false)
                .shouldFailOnError(true)
                .build();

        final Collection<RunResult> results = new Runner(opts).run();
        displayRatios(results);
    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(value = 1, jvmArgsAppend = { "-XX:+UseG1GC", "-Xss100m", "-Xms1g", "-Xmx1g", "-disableassertions" }) /* set fork to 0 if you want to debug */
    public static class Base {
        protected static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);
        protected static final Order<Integer> SCALAZ_ORDER = Order$.MODULE$.fromScalaOrdering(SCALA_ORDERING);

        @Param({ "10", "1000", "100000" })
        public int CONTAINER_SIZE;

        public List<Integer> ELEMENTS;

        @Setup
        public void setup() {
            final Random random = new Random(0);

            final List<Integer> copy = new ArrayList<>(CONTAINER_SIZE);
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                final int value = random.nextInt(CONTAINER_SIZE) - (CONTAINER_SIZE / 2);
                copy.add(value);
            }

            ELEMENTS = Collections.unmodifiableList(copy);
            assertEquals(ELEMENTS.size(), CONTAINER_SIZE);
        }

        protected static <T> void assertEquals(T a, T b) {
            if (!Objects.equals(a, b)) {
                throw new IllegalStateException(a + " != " + b);
            }
        }
    }

    public static class Enqueue extends Base {
        @Benchmark
        @SuppressWarnings("Convert2streamapi")
        public void java_mutable() {
            final java.util.PriorityQueue<Integer> q = new java.util.PriorityQueue<>(ELEMENTS.size());
            for (Integer element : ELEMENTS) {
                q.add(element);
            }
            assertEquals(q.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable() {
            scala.collection.mutable.PriorityQueue<Integer> q = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                q = q.$plus$eq(element);
            }
            assertEquals(q.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scalaz_persistent() {
            scalaz.Heap<Integer> q = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                q = q.insert(element, SCALAZ_ORDER);
            }
            assertEquals(q.size(), CONTAINER_SIZE);
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
        public static class InitializedQueues {
            java.util.PriorityQueue<Integer> javaQueueMutable = new java.util.PriorityQueue<>();
            scala.collection.mutable.PriorityQueue<Integer> scalaQueueMutable = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);

            boolean initializedPersistent = false;
            scalaz.Heap<Integer> scalazQueuePersistent = scalaz.Heap.Empty$.MODULE$.apply();
            javaslang.collection.PriorityQueue<Integer> slangQueuePersistent = javaslang.collection.PriorityQueue.empty();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                assertEquals(javaQueueMutable.size(), 0);
                javaQueueMutable.addAll(state.ELEMENTS);
                assertEquals(javaQueueMutable.size(), state.CONTAINER_SIZE);

                assertEquals(scalaQueueMutable.size(), 0);
                for (Integer element : state.ELEMENTS) {
                    scalaQueueMutable = scalaQueueMutable.$plus$eq(element);
                }
                assertEquals(scalaQueueMutable.size(), state.CONTAINER_SIZE);

                if (!initializedPersistent) {
                    assertEquals(scalazQueuePersistent.size(), 0);
                    assertEquals(slangQueuePersistent.size(), 0);
                    for (Integer element : state.ELEMENTS) {
                        scalazQueuePersistent = scalazQueuePersistent.insert(element, SCALAZ_ORDER);
                        slangQueuePersistent = slangQueuePersistent.enqueue(element);
                    }
                    assertEquals(scalazQueuePersistent.size(), state.CONTAINER_SIZE);
                    assertEquals(slangQueuePersistent.size(), state.CONTAINER_SIZE);

                    initializedPersistent = true;
                }
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaQueueMutable.clear();
                scalaQueueMutable.clear();
            }
        }

        @Benchmark
        public void java_mutable(InitializedQueues state) {
            final java.util.PriorityQueue<Integer> q = state.javaQueueMutable;

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            for (; !q.isEmpty(); q.poll()) {
                result.add(q.peek());
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable(InitializedQueues state) {
            final scala.collection.mutable.PriorityQueue<Integer> q = state.scalaQueueMutable;

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (!q.isEmpty()) {
                result.add(q.dequeue());
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scalaz_persistent(InitializedQueues state) {
            scalaz.Heap<Integer> q = state.scalazQueuePersistent;

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (!q.isEmpty()) {
                final scala.Tuple2<Integer, Heap<Integer>> uncons = q.uncons().get();
                result.add(uncons._1);
                q = uncons._2;
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent(InitializedQueues state) {
            javaslang.collection.PriorityQueue<Integer> q = state.slangQueuePersistent;

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (q.isNotEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = q.dequeue();
                result.add(dequeue._1);
                q = dequeue._2;
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

    }

    public static class Sort extends Base {
        @Benchmark
        public void java_mutable() {
            final java.util.PriorityQueue<Integer> q = new java.util.PriorityQueue<>(ELEMENTS);
            assertEquals(q.size(), CONTAINER_SIZE);

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            for (; !q.isEmpty(); q.poll()) {
                result.add(q.peek());
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scala_mutable() {
            scala.collection.mutable.PriorityQueue<Integer> q = new scala.collection.mutable.PriorityQueue<>(SCALA_ORDERING);
            for (Integer element : ELEMENTS) {
                q = q.$plus$eq(element);
            }
            assertEquals(q.size(), CONTAINER_SIZE);

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (!q.isEmpty()) {
                result.add(q.dequeue());
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void scalaz_persistent() {
            scalaz.Heap<Integer> q = scalaz.Heap.Empty$.MODULE$.apply();
            for (Integer element : ELEMENTS) {
                q = q.insert(element, SCALAZ_ORDER);
            }
            assertEquals(q.size(), CONTAINER_SIZE);

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (!q.isEmpty()) {
                final scala.Tuple2<Integer, Heap<Integer>> uncons = q.uncons().get();
                result.add(uncons._1);
                q = uncons._2;
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }

        @Benchmark
        public void slang_persistent() {
            javaslang.collection.PriorityQueue<Integer> q = javaslang.collection.PriorityQueue.ofAll(ELEMENTS);
            assertEquals(q.size(), CONTAINER_SIZE);

            final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
            while (q.isNotEmpty()) {
                final Tuple2<Integer, javaslang.collection.PriorityQueue<Integer>> dequeue = q.dequeue();
                result.add(dequeue._1);
                q = dequeue._2;
            }
            assertEquals(q.size(), 0);
            assertEquals(result.size(), CONTAINER_SIZE);
        }
    }
}