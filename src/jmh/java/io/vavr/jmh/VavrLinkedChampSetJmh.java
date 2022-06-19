package io.vavr.jmh;

import io.vavr.collection.LinkedChampSet;
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
 * Benchmark          (size)  Mode  Cnt    _     Score         Error  Units
 * ContainsFound     1000000  avgt    4    _   187.804 ±       7.898  ns/op
 * ContainsNotFound  1000000  avgt    4    _   189.635 ±      11.438  ns/op
 * Head              1000000  avgt    4  17_254402.086 ± 6508953.518  ns/op
 * Iterate           1000000  avgt    4  51_883556.621 ± 8627597.187  ns/op
 * RemoveAdd         1000000  avgt    4    _   576.505 ±      45.590  ns/op
 * Tail              1000000  avgt    4  18_164028.334 ± 2231690.063  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedChampSetJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private LinkedChampSet<Key> setA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  LinkedChampSet.ofAll(data.setA);
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
    public LinkedChampSet<Key> mTail() {
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
