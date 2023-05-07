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
 * Benchmark      (mask)    (size)  Mode  Cnt           Score   Error  Units
 * mAddOneByOne          -65  100000  avgt       28603515.989          ns/op
 * mContainsFound        -65  100000  avgt             71.910          ns/op
 * mContainsNotFound     -65  100000  avgt            101.819          ns/op
 * mHead                 -65  100000  avgt             10.082          ns/op
 * mIterate              -65  100000  avgt        6150139.070          ns/op
 * mOfAll                -65  100000  avgt       20939278.918          ns/op
 * mRemoveAll            -65  100000  avgt       26670647.515          ns/op
 * mRemoveOneByOne       -65  100000  avgt       31792853.537          ns/op
 * mRemoveThenAdd        -65  100000  avgt            658.193          ns/op
 * mTail                 -65  100000  avgt            134.754          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashSetJmh {
    @Param({/*"10", "1000",*/ "100000"/*, "10000000"*/})
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
        public HashSet<Key> mOfAll() {
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

    @Benchmark
    public HashSet<Key> mRemoveAll() {
        HashSet<Key> set = setA;
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
}
