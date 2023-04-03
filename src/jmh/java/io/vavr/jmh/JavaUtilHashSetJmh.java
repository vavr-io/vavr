package io.vavr.jmh;

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

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark               (size)  Mode  Cnt    _     Score        Error  Units
 * mIterate               1000000  avgt    4  33_497667.586 ± 522756.433  ns/op
 * mRemoveThenAdd         1000000  avgt    4    _   164.231 ±     12.128  ns/op
 * mContainsFound         1000000  avgt    4    _    92.212 ±      2.679  ns/op
 * mContainsNotFound      1000000  avgt    4    _    91.997 ±      3.519  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class JavaUtilHashSetJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private HashSet<Key> setA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA = new HashSet<>(data.setA);
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
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        setA.remove(key);
        setA.add(key);
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
