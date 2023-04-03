package io.vavr.jmh;

import io.vavr.collection.champ.ChampSet;
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
 * Benchmark                           (size)  Mode  Cnt         Score   Error  Units
 * VavrChampSetJmh.mContainsFound          10  avgt              4.720          ns/op
 * VavrChampSetJmh.mContainsFound     1000000  avgt            208.266          ns/op
 * VavrChampSetJmh.mContainsNotFound       10  avgt              4.397          ns/op
 * VavrChampSetJmh.mContainsNotFound  1000000  avgt            208.751          ns/op
 * VavrChampSetJmh.mHead                   10  avgt             10.912          ns/op
 * VavrChampSetJmh.mHead              1000000  avgt             25.173          ns/op
 * VavrChampSetJmh.mIterate                10  avgt             15.869          ns/op
 * VavrChampSetJmh.mIterate           1000000  avgt       39349325.941          ns/op
 * VavrChampSetJmh.mRemoveThenAdd          10  avgt             58.045          ns/op
 * VavrChampSetJmh.mRemoveThenAdd     1000000  avgt            614.303          ns/op
 * VavrChampSetJmh.mTail                   10  avgt             36.092          ns/op
 * VavrChampSetJmh.mTail              1000000  avgt            114.222          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrChampSetJmh {

    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ChampSet<Key> setA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  ChampSet.ofAll(data.setA);
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
        setA.remove(key).add(key);
    }
    @Benchmark
    public Key mHead() {
        return setA.head();
    }
    @Benchmark
    public ChampSet<Key> mTail() {
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
