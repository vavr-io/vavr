package io.vavr.jmh;

import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
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
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                               (mask)  (size)  Mode  Cnt         Score   Error  Units
 * VavrLinkedHashSetJmh.mAddOneByOne          -65  100000  avgt       40653585.118          ns/op
 * VavrLinkedHashSetJmh.mContainsFound        -65  100000  avgt             76.753          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound     -65  100000  avgt             79.134          ns/op
 * VavrLinkedHashSetJmh.mHead                 -65  100000  avgt              6.823          ns/op
 * VavrLinkedHashSetJmh.mFilter50Percent      -65  100000  avgt       16430612.189          ns/op
 * VavrLinkedHashSetJmh.mPartition50Percent   -65  100000  avgt       33035176.673          ns/op
 * VavrLinkedHashSetJmh.mIterate              -65  100000  avgt        2018939.713          ns/op
 * VavrLinkedHashSetJmh.mOfAll                -65  100000  avgt       34549431.707          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll            -65  100000  avgt       81758211.593          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne       -65  100000  avgt       88570933.779          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd        -65  100000  avgt            706.920          ns/op
 * VavrLinkedHashSetJmh.mTail                 -65  100000  avgt            120.102          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations =0)
@Fork(value =0, jvmArgsAppend = {"-Xmx28g"})

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashSetJmh {
    @Param({/*"10", "1000",*/ "100000"/*, "10000000"*/})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private LinkedHashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  LinkedHashSet.ofAll(data.setA);
    }

    @Benchmark
    public LinkedHashSet<Key> mFilter50Percent() {
        LinkedHashSet<Key> set = setA;
        return set.filter(e->(e.value&1)==0);
    }
    @Benchmark
    public Tuple2<LinkedHashSet<Key>,LinkedHashSet<Key>> mPartition50Percent() {
        LinkedHashSet<Key> set = setA;
        return set.partition(e -> (e.value & 1) == 0);
    }
/*
    @Benchmark
    public LinkedHashSet<Key> mOfAll() {
        return LinkedHashSet.ofAll(data.listA);
    }

    @Benchmark
    public LinkedHashSet<Key> mAddOneByOne() {
        LinkedHashSet<Key> set = LinkedHashSet.of();
        for (Key key : data.listA) {
            set=set.add(key);
        }
        return set;
    }

    @Benchmark
    public LinkedHashSet<Key> mRemoveOneByOne() {
        LinkedHashSet<Key> set = setA;
        for (Key key : data.listA) {
            set=set.remove(key);
        }
        return set;
    }

    @Benchmark
    public LinkedHashSet<Key> mRemoveAll() {
        LinkedHashSet<Key> set = setA;
        return set.removeAll(data.listA);
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
*/
}
