package io.vavr.jmh;

import io.vavr.collection.HashSet;
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
    @Param({"10","1000","100000","10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private HashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        setA =  HashSet.ofAll(data.setA);
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
