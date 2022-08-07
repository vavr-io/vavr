package io.vavr.jmh;

import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.collections.immutable.PersistentMap;
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
 * Benchmark           (size)  Mode  Cnt     _     Score        Error  Units
 * mContainsFound     1000000  avgt    4     _   170.787 ±       3.521  ns/op
 * mContainsNotFound  1000000  avgt    4     _   194.571 ±      11.060  ns/op
 * mHead              1000000  avgt    4     _    29.280 ±       0.585  ns/op
 * mIterate           1000000  avgt    4  282_756947.000 ± 4260232.503  ns/op
 * mPut               1000000  avgt    4     _   380.131 ±       9.081  ns/op
 * mRemoveThenAdd     1000000  avgt    4     _  1442.891 ±      30.088  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class KotlinxPersistentOrderedMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private PersistentMap<Key, Boolean> mapA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA =  ExtensionsKt.persistentMapOf();
        for (Key key : data.setA) {
            mapA=mapA.put(key,Boolean.TRUE);
        }
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapA.keySet()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        mapA.remove(key).put(key,Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapA.put(key,Boolean.FALSE);
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapA.containsKey(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapA.containsKey(key);
    }

    @Benchmark
    public Key mHead() {
        return mapA.keySet().iterator().next();
    }
}
