package io.vavr.jmh;

import io.vavr.collection.Map;
import io.vavr.collection.champ.ChampMap;
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
 * Benchmark                           (size)  Mode  Cnt         Score         Error  Units
 * VavrChampMapJmh.mContainsFound          10  avgt    4         4.780 ±       0.072  ns/op
 * VavrChampMapJmh.mContainsFound     1000000  avgt    4       204.861 ±      11.674  ns/op
 * VavrChampMapJmh.mContainsNotFound       10  avgt    4         4.762 ±       0.046  ns/op
 * VavrChampMapJmh.mContainsNotFound  1000000  avgt    4       201.403 ±       4.942  ns/op
 * VavrChampMapJmh.mHead                   10  avgt    4        15.325 ±       0.233  ns/op
 * VavrChampMapJmh.mHead              1000000  avgt    4        38.001 ±       0.898  ns/op
 * VavrChampMapJmh.mIterate                10  avgt    4        52.887 ±       0.341  ns/op
 * VavrChampMapJmh.mIterate           1000000  avgt    4  60767798.045 ± 1693446.487  ns/op
 * VavrChampMapJmh.mPut                    10  avgt    4        25.176 ±       2.415  ns/op
 * VavrChampMapJmh.mPut               1000000  avgt    4       338.119 ±       8.195  ns/op
 * VavrChampMapJmh.mRemoveThenAdd          10  avgt    4        66.013 ±       4.305  ns/op
 * VavrChampMapJmh.mRemoveThenAdd     1000000  avgt    4       536.347 ±      10.961  ns/op
 * VavrChampMapJmh.mTail                   10  avgt    4        37.362 ±       2.984  ns/op
 * VavrChampMapJmh.mTail              1000000  avgt    4       118.842 ±       1.472  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrChampMapJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ChampMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA = ChampMap.empty();
        for (Key key : data.setA) {
            mapA = mapA.put(key, Boolean.TRUE);
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
    public void mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        mapA.remove(key).put(key, Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key = data.nextKeyInA();
        mapA.put(key, Boolean.FALSE);
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
        return mapA.head()._1;
    }

    @Benchmark
    public Map<Key, Boolean> mTail() {
        return mapA.tail();
    }

}
