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
 * Benchmark                               (mask)    (size)  Mode  Cnt          Score   Error  Units
 * VavrHashMapJmh.mContainsFound              -65        10  avgt               5.252          ns/op
 * VavrHashMapJmh.mContainsFound              -65      1000  avgt              17.711          ns/op
 * VavrHashMapJmh.mContainsFound              -65    100000  avgt              68.994          ns/op
 * VavrHashMapJmh.mContainsFound              -65  10000000  avgt             295.599          ns/op
 * VavrHashMapJmh.mContainsNotFound           -65        10  avgt               5.590          ns/op
 * VavrHashMapJmh.mContainsNotFound           -65      1000  avgt              17.722          ns/op
 * VavrHashMapJmh.mContainsNotFound           -65    100000  avgt              71.793          ns/op
 * VavrHashMapJmh.mContainsNotFound           -65  10000000  avgt             290.069          ns/op
 * VavrHashMapJmh.mHead                       -65        10  avgt               1.808          ns/op
 * VavrHashMapJmh.mHead                       -65      1000  avgt               4.061          ns/op
 * VavrHashMapJmh.mHead                       -65    100000  avgt               8.863          ns/op
 * VavrHashMapJmh.mHead                       -65  10000000  avgt              11.486          ns/op
 * VavrHashMapJmh.mIterate                    -65        10  avgt              81.728          ns/op
 * VavrHashMapJmh.mIterate                    -65      1000  avgt           16242.070          ns/op
 * VavrHashMapJmh.mIterate                    -65    100000  avgt         2318004.075          ns/op
 * VavrHashMapJmh.mIterate                    -65  10000000  avgt       736796617.143          ns/op
 * VavrHashMapJmh.mPut                        -65        10  avgt              25.985          ns/op
 * VavrHashMapJmh.mPut                        -65      1000  avgt              73.851          ns/op
 * VavrHashMapJmh.mPut                        -65    100000  avgt             199.785          ns/op
 * VavrHashMapJmh.mPut                        -65  10000000  avgt             557.019          ns/op
 * VavrHashMapJmh.mRemoveThenAdd              -65        10  avgt              67.185          ns/op
 * VavrHashMapJmh.mRemoveThenAdd              -65      1000  avgt             186.535          ns/op
 * VavrHashMapJmh.mRemoveThenAdd              -65    100000  avgt             357.417          ns/op
 * VavrHashMapJmh.mRemoveThenAdd              -65  10000000  avgt             850.202          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrHashMapJmh {
    @Param({"10","1000","100000","10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private HashMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA =  HashMap.empty();
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
