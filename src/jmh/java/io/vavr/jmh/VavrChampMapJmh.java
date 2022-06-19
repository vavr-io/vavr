package io.vavr.jmh;

import io.vavr.collection.ChampMap;
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
 * Benchmark         (size)  Mode  Cnt    _     Score        Error  Units
 * ContainsFound     1000000  avgt    4       179.705 ±       5.735  ns/op
 * ContainsNotFound  1000000  avgt    4       178.312 ±       8.082  ns/op
 * Iterate           1000000  avgt    4  48892070.205 ± 4267871.730  ns/op
 * Put               1000000  avgt    4       334.626 ±      17.592  ns/op
 * RemoveAdd         1000000  avgt    4       397.613 ±      10.868  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrChampMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ChampMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
         mapA =  ChampMap.empty();
        for (Key key : data.setA) {
            mapA=mapA.put(key,Boolean.TRUE);
        }
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapA.keysIterator()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveAdd() {
        Key key =data.nextKeyInA();
        mapA.remove(key);
        mapA.put(key,Boolean.TRUE);
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
}
