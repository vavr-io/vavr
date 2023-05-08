package io.vavr.jmh;


import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.collections.immutable.PersistentSet;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                                    (mask)    (size)  Mode  Cnt    _        Score           Error  Units
 * KotlinxPersistentHashSetJmh.mAddAll             -65        10  avgt         _      314.606                  ns/op
 * KotlinxPersistentHashSetJmh.mAddAll             -65      1000  avgt         _    44389.022                  ns/op
 * KotlinxPersistentHashSetJmh.mAddAll             -65    100000  avgt         _ 17258612.386                  ns/op
 * KotlinxPersistentHashSetJmh.mAddAll             -65  10000000  avgt        6_543269527.000                  ns/op
 * KotlinxPersistentHashSetJmh.mAddOneByOne        -65        10  avgt         _      658.427                  ns/op
 * KotlinxPersistentHashSetJmh.mAddOneByOne        -65      1000  avgt         _   207562.899                  ns/op
 * KotlinxPersistentHashSetJmh.mAddOneByOne        -65    100000  avgt         _ 47867380.737                  ns/op
 * KotlinxPersistentHashSetJmh.mAddOneByOne        -65  10000000  avgt       10_085283626.000                  ns/op
 * KotlinxPersistentHashSetJmh.mRemoveOneByOne     -65        10  avgt         _      308.915                  ns/op
 * KotlinxPersistentHashSetJmh.mRemoveOneByOne     -65      1000  avgt         _    77775.838                  ns/op
 * KotlinxPersistentHashSetJmh.mRemoveOneByOne     -65    100000  avgt         _ 27273753.703                  ns/op
 * KotlinxPersistentHashSetJmh.mRemoveOneByOne     -65  10000000  avgt        7_240761155.500                  ns/op
 *
 * Benchmark           (size)  Mode  Cnt    _     Score         Error  Units
 * mContainsFound     1000000  avgt    4    _   165.449 ±      13.209  ns/op
 * mContainsNotFound  1000000  avgt    4    _   169.791 ±       2.502  ns/op
 * mHead              1000000  avgt    4    _   104.946 ±       3.025  ns/op
 * mIterate           1000000  avgt    4  71_505927.591 ± 1063359.317  ns/op
 * mRemoveThenAdd     1000000  avgt    4    _   458.736 ±       6.936  ns/op
 * mTail              1000000  avgt    4    _   197.068 ±       3.920  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class KotlinxPersistentHashSetJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private PersistentSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA = ExtensionsKt.toPersistentHashSet(data.setA);
    }

public static void main (String...args){
    KotlinxPersistentHashSetJmh t = new KotlinxPersistentHashSetJmh();
    t.size=10;
    t.mask=-65;
    t.setup();
    PersistentSet<Key> keys = t.mAddAll();
    System.out.println(keys);

}
    @Benchmark
    public PersistentSet<Key> mAddAll() {
        return ExtensionsKt.toPersistentHashSet(data.listA);
    }

    @Benchmark
    public PersistentSet<Key> mAddOneByOne() {
        PersistentSet<Key> set = ExtensionsKt.persistentSetOf();
        for (Key key : data.listA) {
            set = set.add(key);
        }
        return set;
    }

    @Benchmark
    public PersistentSet<Key> mRemoveOneByOne() {
        PersistentSet<Key> set = setA;
        for (Key key : data.listA) {
            set = set.remove(key);
        }
        return set;
    }

    //FIXME We get endless loops here - or it is quadratic somehow
    //@Benchmark
    public PersistentSet<Key> mRemoveAll() {
        PersistentSet<Key> set = setA;
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
    public PersistentSet<Key> mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        return setA.remove(key).add(key);
    }

    @Benchmark
    public Key mHead() {
        return setA.iterator().next();
    }
    @Benchmark
    public PersistentSet<Key> mTail() {
        return setA.remove(setA.iterator().next());
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
