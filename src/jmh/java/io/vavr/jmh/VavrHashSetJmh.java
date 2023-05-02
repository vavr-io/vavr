package io.vavr.jmh;

import io.vavr.collection.HashSet;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                       (mask)    (size)  Mode  Cnt           Score   Error  Units
 * VavrHashSetJmh.mAddAll             -65        10  avgt              333.421          ns/op
 * VavrHashSetJmh.mAddAll             -65      1000  avgt            75065.071          ns/op
 * VavrHashSetJmh.mAddAll             -65    100000  avgt         17047511.761          ns/op
 * VavrHashSetJmh.mAddAll             -65  10000000  avgt       4858104338.667          ns/op
 * VavrHashSetJmh.mAddOneByOne        -65        10  avgt              279.495          ns/op
 * VavrHashSetJmh.mAddOneByOne        -65      1000  avgt            91640.610          ns/op
 * VavrHashSetJmh.mAddOneByOne        -65    100000  avgt         24110705.034          ns/op
 * VavrHashSetJmh.mAddOneByOne        -65  10000000  avgt       6494104454.500          ns/op
 * VavrHashSetJmh.mRemoveAll          -65        10  avgt              243.695          ns/op
 * VavrHashSetJmh.mRemoveAll          -65      1000  avgt           100452.008          ns/op
 * VavrHashSetJmh.mRemoveAll          -65    100000  avgt         23237449.239          ns/op
 * VavrHashSetJmh.mRemoveAll          -65  10000000  avgt       6311906939.000          ns/op
 * VavrHashSetJmh.mRemoveOneByOne     -65        10  avgt              247.507          ns/op
 * VavrHashSetJmh.mRemoveOneByOne     -65      1000  avgt           105832.777          ns/op
 * VavrHashSetJmh.mRemoveOneByOne     -65    100000  avgt         26077578.885          ns/op
 * VavrHashSetJmh.mRemoveOneByOne     -65  10000000  avgt       6345551732.000          ns/op
 * VavrHashSetJmh.mContainsFound              -65      1000  avgt              19.979          ns/op
 * VavrHashSetJmh.mContainsFound              -65    100000  avgt              68.201          ns/op
 * VavrHashSetJmh.mContainsFound              -65  10000000  avgt             297.289          ns/op
 * VavrHashSetJmh.mContainsNotFound           -65        10  avgt               4.701          ns/op
 * VavrHashSetJmh.mContainsNotFound           -65      1000  avgt              18.683          ns/op
 * VavrHashSetJmh.mContainsNotFound           -65    100000  avgt              57.650          ns/op
 * VavrHashSetJmh.mContainsNotFound           -65  10000000  avgt             294.516          ns/op
 * VavrHashSetJmh.mHead                       -65        10  avgt               1.417          ns/op
 * VavrHashSetJmh.mHead                       -65      1000  avgt               3.624          ns/op
 * VavrHashSetJmh.mHead                       -65    100000  avgt               8.269          ns/op
 * VavrHashSetJmh.mHead                       -65  10000000  avgt              10.851          ns/op
 * VavrHashSetJmh.mIterate                    -65        10  avgt              77.806          ns/op
 * VavrHashSetJmh.mIterate                    -65      1000  avgt           15320.315          ns/op
 * VavrHashSetJmh.mIterate                    -65    100000  avgt         1574129.072          ns/op
 * VavrHashSetJmh.mIterate                    -65  10000000  avgt       601405168.353          ns/op
 * VavrHashSetJmh.mRemoveThenAdd              -65        10  avgt              67.765          ns/op
 * VavrHashSetJmh.mRemoveThenAdd              -65      1000  avgt             179.879          ns/op
 * VavrHashSetJmh.mRemoveThenAdd              -65    100000  avgt             313.706          ns/op
 * VavrHashSetJmh.mRemoveThenAdd              -65  10000000  avgt             714.447          ns/op
 * VavrHashSetJmh.mTail                       -65        10  avgt              30.410          ns/op
 * VavrHashSetJmh.mTail                       -65      1000  avgt              50.203          ns/op
 * VavrHashSetJmh.mTail                       -65    100000  avgt              88.762          ns/op
 * VavrHashSetJmh.mTail                       -65  10000000  avgt             113.403          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashSetJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private HashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA = HashSet.ofAll(data.setA);
    }

    @Benchmark
    public HashSet<Key> mAddAll() {
        return HashSet.ofAll(data.listA);
    }

    @Benchmark
    public HashSet<Key> mAddOneByOne() {
        HashSet<Key> set = HashSet.of();
        for (Key key : data.listA) {
            set = set.add(key);
        }
        return set;
    }

    @Benchmark
    public HashSet<Key> mRemoveOneByOne() {
        HashSet<Key> set = setA;
        for (Key key : data.listA) {
            set = set.remove(key);
        }
        return set;
    }

    //DISABLED - Loops endlessly with (mask = -65, size = 10000000)
    //@Benchmark
    public HashSet<Key> mRemoveAll() {
        HashSet<Key> set = setA;
        return set.removeAll(data.listA);
    }
    
/*
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
    public HashSet<Key> mTail() {
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
