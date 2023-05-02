package io.vavr.jmh;

import io.vavr.collection.LinkedHashMap;
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
 * Benchmark                               (mask)    (size)  Mode  Cnt          Score   Error  Units
 * VavrLinkedHashMapJmh.mContainsFound        -65        10  avgt               5.292          ns/op
 * VavrLinkedHashMapJmh.mContainsFound        -65      1000  avgt              17.472          ns/op
 * VavrLinkedHashMapJmh.mContainsFound        -65    100000  avgt              65.758          ns/op
 * VavrLinkedHashMapJmh.mContainsFound        -65  10000000  avgt             317.979          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound     -65        10  avgt               5.565          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound     -65      1000  avgt              17.763          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound     -65    100000  avgt              87.567          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound     -65  10000000  avgt             379.739          ns/op
 * VavrLinkedHashMapJmh.mHead                 -65        10  avgt               3.094          ns/op
 * VavrLinkedHashMapJmh.mHead                 -65      1000  avgt               3.897          ns/op
 * VavrLinkedHashMapJmh.mHead                 -65    100000  avgt               6.876          ns/op
 * VavrLinkedHashMapJmh.mHead                 -65  10000000  avgt               9.080          ns/op
 * VavrLinkedHashMapJmh.mIterate              -65        10  avgt             106.434          ns/op
 * VavrLinkedHashMapJmh.mIterate              -65      1000  avgt           16789.174          ns/op
 * VavrLinkedHashMapJmh.mIterate              -65    100000  avgt         2535320.127          ns/op
 * VavrLinkedHashMapJmh.mIterate              -65  10000000  avgt       812445990.846          ns/op
 * VavrLinkedHashMapJmh.mPut                  -65        10  avgt              34.365          ns/op
 * VavrLinkedHashMapJmh.mPut                  -65      1000  avgt             115.985          ns/op
 * VavrLinkedHashMapJmh.mPut                  -65    100000  avgt             315.287          ns/op
 * VavrLinkedHashMapJmh.mPut                  -65  10000000  avgt            1222.364          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd        -65        10  avgt             157.790          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd        -65      1000  avgt             308.487          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd        -65    100000  avgt             618.236          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd        -65  10000000  avgt            1328.448          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrLinkedHashMapJmh {
    @Param({"10","1000","100000","10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private LinkedHashMap<Key, Boolean> mapA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA =  LinkedHashMap.empty();
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
        return mapA.head()._1;
    }
}
