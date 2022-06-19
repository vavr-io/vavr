package io.vavr.jmh;

import io.vavr.collection.LinkedHashSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark           (size)  Mode  Cnt   _      Score         Error  Units
 * ContainsFound     1000000  avgt    4    _   204.841 ±       27.686  ns/op
 * ContainsNotFound  1000000  avgt    4    _   207.064 ±       28.109  ns/op
 * Head              1000000  avgt    4   4_572643.356 ±   304792.025  ns/op
 * Iterate           1000000  avgt    4  72_354050.601 ±  4164487.060  ns/op
 * RemoveAdd         1000000  avgt    4  55_789995.082 ±  6626404.364  ns/op
 * Tail              1000000  avgt    4  48_914447.602 ± 16458725.793  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashSetJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private LinkedHashSet<Key> setA;

    private int index;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  LinkedHashSet.ofAll(data.setA);
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : setA) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveAdd() {
        Key key =data.nextKeyInA();
        setA.remove(key).add(key);
    }
    @Benchmark
    public Key mHead() {
        return setA.head();
    }
    @Benchmark
    public LinkedHashSet<Key> mTail() {
        return setA.tail();
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return setA.contains(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return setA.contains(key);
    }
}
