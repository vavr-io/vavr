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
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                               (mask)    (size)  Mode  Cnt          Score   Error  Units
 * Benchmark                             (mask)    (size)  Mode  Cnt            Score   Error  Units
 * VavrLinkedHashSetJmh.mAddAll             -65        10  avgt               977.393          ns/op
 * VavrLinkedHashSetJmh.mAddAll             -65      1000  avgt            198221.760          ns/op
 * VavrLinkedHashSetJmh.mAddAll             -65    100000  avgt          35429322.314          ns/op
 * VavrLinkedHashSetJmh.mAddAll             -65  10000000  avgt        7755345733.000          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne        -65        10  avgt               809.518          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne        -65      1000  avgt            178117.088          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne        -65    100000  avgt          41538622.162          ns/op
 * VavrLinkedHashSetJmh.mAddOneByOne        -65  10000000  avgt        8207656477.500          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll          -65        10  avgt               546.006          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll          -65      1000  avgt            113494.907          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll          -65    100000  avgt          29366083.795          ns/op
 * VavrLinkedHashSetJmh.mRemoveAll          -65  10000000  avgt        8929774581.500          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne     -65        10  avgt               936.830          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne     -65      1000  avgt            322820.093          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne     -65    100000  avgt          85707601.060          ns/op
 * VavrLinkedHashSetJmh.mRemoveOneByOne     -65  10000000  avgt       20218899949.000          ns/op
 * VavrLinkedHashSetJmh.mContainsFound        -65        10  avgt               5.347          ns/op
 * VavrLinkedHashSetJmh.mContainsFound        -65      1000  avgt              18.177          ns/op
 * VavrLinkedHashSetJmh.mContainsFound        -65    100000  avgt              83.205          ns/op
 * VavrLinkedHashSetJmh.mContainsFound        -65  10000000  avgt             317.635          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound     -65        10  avgt               5.355          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound     -65      1000  avgt              17.647          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound     -65    100000  avgt              77.740          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound     -65  10000000  avgt             315.888          ns/op
 * VavrLinkedHashSetJmh.mHead                 -65        10  avgt               3.093          ns/op
 * VavrLinkedHashSetJmh.mHead                 -65      1000  avgt               3.953          ns/op
 * VavrLinkedHashSetJmh.mHead                 -65    100000  avgt               6.751          ns/op
 * VavrLinkedHashSetJmh.mHead                 -65  10000000  avgt               9.106          ns/op
 * VavrLinkedHashSetJmh.mIterate              -65        10  avgt              62.141          ns/op
 * VavrLinkedHashSetJmh.mIterate              -65      1000  avgt            6469.218          ns/op
 * VavrLinkedHashSetJmh.mIterate              -65    100000  avgt         1123209.779          ns/op
 * VavrLinkedHashSetJmh.mIterate              -65  10000000  avgt       781421602.308          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd        -65        10  avgt             159.546          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd        -65      1000  avgt             342.371          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd        -65    100000  avgt             667.755          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd        -65  10000000  avgt            1752.124          ns/op
 * VavrLinkedHashSetJmh.mTail                 -65        10  avgt              45.633          ns/op
 * VavrLinkedHashSetJmh.mTail                 -65      1000  avgt              76.260          ns/op
 * VavrLinkedHashSetJmh.mTail                 -65    100000  avgt             114.869          ns/op
 * VavrLinkedHashSetJmh.mTail                 -65  10000000  avgt             155.635          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1, jvmArgsAppend = {"-Xmx28g"})

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashSetJmh {
    @Param({"10","1000","100000","10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private LinkedHashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  LinkedHashSet.ofAll(data.setA);
    }

    @Benchmark
    public LinkedHashSet<Key> mAddAll() {
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
