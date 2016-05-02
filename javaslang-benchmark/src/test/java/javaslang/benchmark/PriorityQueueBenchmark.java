package javaslang.benchmark;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.*;
import scala.math.Ordering;
import scala.math.Ordering$;
import scalaz.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static javaslang.benchmark.BenchmarkResultAggregator.displayRatios;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3, jvmArgsAppend = { "-XX:+UseG1GC", "-Xss100m", "-Xms1g", "-Xmx1g", "-disableassertions" })
public class PriorityQueueBenchmark {
    private static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);
    private static final Order<Integer> SCALAZ_ORDER = Order$.MODULE$.fromScalaOrdering(SCALA_ORDERING);

    @Param({ "10", "1000", "100000" })
    protected int CONTAINER_SIZE;

    @Test
    public void launchBenchmark() throws Exception {
        final Options opts = new OptionsBuilder()
                .include(PriorityQueueBenchmark.class.getSimpleName())
                .shouldDoGC(false)
                .shouldFailOnError(true)
                .build();

        displayRatios(new Runner(opts).run());
    }

    private List<Integer> ELEMENTS;

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

    @Benchmark
    public void sort_java_mutable() {
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
    public void sort_scala_mutable() {
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
    public void sort_scalaz_persistent() {
        scalaz.Heap<Integer> q = scalaz.Heap.Empty$.MODULE$.apply();
        for (Integer element : ELEMENTS) {
            q = q.insert(element, SCALAZ_ORDER);
        }
        assertEquals(q.size(), CONTAINER_SIZE);

        final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
        for (; !q.isEmpty(); q = q.deleteMin()) {
            result.add(q.minimum());
        }
        assertEquals(q.size(), 0);
        assertEquals(result.size(), CONTAINER_SIZE);
    }

    @Benchmark
    public void sort_slang_persistent() {
        javaslang.collection.PriorityQueue<Integer> q = javaslang.collection.PriorityQueue.ofAll(ELEMENTS);
        assertEquals(q.size(), CONTAINER_SIZE);

        final Collection<Integer> result = new ArrayList<>(CONTAINER_SIZE);
        for (; !q.isEmpty(); q = q.tail()) {
            result.add(q.head());
        }
        assertEquals(q.size(), 0);
        assertEquals(result.size(), CONTAINER_SIZE);
    }

    private static <T> void assertEquals(T a, T b) {
        if (!Objects.equals(a, b)) {
            throw new IllegalStateException(a + " != " + b);
        }
    }
}