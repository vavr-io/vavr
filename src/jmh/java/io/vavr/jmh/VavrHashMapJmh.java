package io.vavr.jmh;

import io.vavr.collection.HashMap;
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
 * Benchmark                         (mask)  (size)  Mode  Cnt         Score   Error  Units
 * VavrHashMapJmh.mContainsFound        -65  100000  avgt             96.954          ns/op
 * VavrHashMapJmh.mContainsNotFound     -65  100000  avgt             71.149          ns/op
 * VavrHashMapJmh.mHead                 -65  100000  avgt              9.249          ns/op
 * VavrHashMapJmh.mIterate              -65  100000  avgt        2898172.970          ns/op
 * VavrHashMapJmh.mMerge                -65  100000  avgt        9478240.737          ns/op
 * VavrHashMapJmh.mOfAll                -65  100000  avgt       24415008.346          ns/op
 * VavrHashMapJmh.mPut                  -65  100000  avgt            474.236          ns/op
 * VavrHashMapJmh.mRemoveThenAdd        -65  100000  avgt            341.821          ns/op
 * VavrHashMapJmh.mReplaceAll           -65  100000  avgt       25068782.040          ns/op
 * VavrHashMapJmh.mRetainAll            -65  100000  avgt        3519589.647          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashMapJmh {
    @Param({/*"10","1000",*/"100000"/*,"10000000"*/})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private HashMap<Key, Boolean> mapATrue;
    private HashMap<Key, Boolean> mapAFalse;
    private HashMap<Key, Boolean> mapB;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapATrue =  HashMap.empty();
        mapAFalse =  HashMap.empty();
        mapB =  HashMap.empty();
        for (Key key : data.setA) {
            mapATrue = mapATrue.put(key,Boolean.TRUE);
            mapAFalse=mapAFalse.put(key,Boolean.FALSE);
        }
        for (Key key : data.listB) {
            mapB=mapB.put(key,Boolean.TRUE);
        }
    }

    @Benchmark
    public HashMap<Key, Boolean> mOfAll() {
        return HashMap.<Key, Boolean>ofAll(data.mapA);
    }
    @Benchmark
    public HashMap<Key, Boolean> mMerge() {
        return mapATrue.merge(mapAFalse);
    }
    @Benchmark
    public HashMap<Key, Boolean> mReplaceAll() {
        return mapATrue.replaceAll((k,v)->!v);
    }
    @Benchmark
    public HashMap<Key, Boolean> mRetainAll() {
        return mapATrue.retainAll(mapB);
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapATrue.keysIterator()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        mapATrue.remove(key).put(key,Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapATrue.put(key,Boolean.FALSE);
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapATrue.containsKey(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapATrue.containsKey(key);
    }

    @Benchmark
    public Key mHead() {
        return mapATrue.head()._1;
    }
}
