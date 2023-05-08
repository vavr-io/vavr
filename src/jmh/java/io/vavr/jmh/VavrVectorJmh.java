package io.vavr.jmh;


import io.vavr.collection.Vector;
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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark                         (size)  Mode  Cnt         Score   Error  Units
 * VavrVectorJmh.mAddAll                 -65        10  avgt                60.704          ns/op
 * VavrVectorJmh.mAddAll                 -65      1000  avgt              3797.950          ns/op
 * VavrVectorJmh.mAddAll                 -65    100000  avgt           1217961.885          ns/op
 * VavrVectorJmh.mAddOneByOne            -65        10  avgt               478.864          ns/op
 * VavrVectorJmh.mAddOneByOne            -65      1000  avgt             72287.227          ns/op
 * VavrVectorJmh.mAddOneByOne            -65    100000  avgt          14675402.151          ns/op
 * VavrVectorJmh.mRemoveAll              -65        10  avgt               372.183          ns/op
 * VavrVectorJmh.mRemoveAll              -65      1000  avgt             94281.357          ns/op
 * VavrVectorJmh.mRemoveAll              -65    100000  avgt          25100217.195          ns/op
 * VavrVectorJmh.mRemoveOneByOne         -65        10  avgt               574.894          ns/op
 * VavrVectorJmh.mRemoveOneByOne         -65      1000  avgt           2458636.840          ns/op
 * VavrVectorJmh.mRemoveOneByOne         -65    100000  avgt       45182726826.000          ns/op
 * VavrVectorJmh.mAddFirst               10  avgt            174.163          ns/op
 * VavrVectorJmh.mAddFirst          1000000  avgt            529.346          ns/op
 * VavrVectorJmh.mAddLast                10  avgt             68.351          ns/op
 * VavrVectorJmh.mAddLast           1000000  avgt            307.219          ns/op
 * VavrVectorJmh.mContainsNotFound       10  avgt             28.607          ns/op
 * VavrVectorJmh.mContainsNotFound  1000000  avgt       23724943.217          ns/op
 * VavrVectorJmh.mGet                    10  avgt              4.525          ns/op
 * VavrVectorJmh.mGet               1000000  avgt            208.204          ns/op
 * VavrVectorJmh.mHead                   10  avgt              2.538          ns/op
 * VavrVectorJmh.mHead              1000000  avgt              6.269          ns/op
 * VavrVectorJmh.mIterate                10  avgt             15.098          ns/op
 * VavrVectorJmh.mIterate           1000000  avgt       28222928.468          ns/op
 * VavrVectorJmh.mRemoveLast             10  avgt             12.306          ns/op
 * VavrVectorJmh.mRemoveLast        1000000  avgt             12.386          ns/op
 * VavrVectorJmh.mReversedIterate        10  avgt            215.448          ns/op
 * VavrVectorJmh.mReversedIterate   1000000  avgt       69195515.703          ns/op
 * VavrVectorJmh.mSet                    10  avgt             29.279          ns/op
 * VavrVectorJmh.mSet               1000000  avgt            563.290          ns/op
 * VavrVectorJmh.mTail                   10  avgt             12.132          ns/op
 * VavrVectorJmh.mTail              1000000  avgt             13.528          ns/op
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrVectorJmh {
    @Param({"10","1000","100000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private Vector<Key> listA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        listA = Vector.of();
        for (Key key : data.setA) {
            listA = listA.append(key);
        }
    }
    @Benchmark
    public Vector<Key> mAddAll() {
        return Vector.ofAll(data.setA);
    }
    @Benchmark
    public Vector<Key> mAddOneByOne() {
        Vector<Key> set = Vector.of();
        for (Key key : data.listA) {
            set = set.append(key);
        }
        return set;
    }    @Benchmark
    public Vector<Key> mRemoveOneByOne() {
        var map = listA;
        for (var e : data.listA) {
            map = map.remove(e);
        }
        if (!map.isEmpty()) throw new AssertionError("map: " + map);
        return map;
    }
    @Benchmark
    public Vector<Key> mRemoveAll() {
        Vector<Key> set = listA;
        return set.removeAll(data.listA);
    }
    /*

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Iterator<Key> i = listA.iterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public int mReversedIterate() {
        int sum = 0;
        for (Iterator<Key> i = listA.reverse().iterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public Vector<Key> mTail() {
        return listA.removeAt(0);
    }

    @Benchmark
    public Vector<Key> mAddLast() {
        Key key = data.nextKeyInB();
        return (listA).append(key);
    }

    @Benchmark
    public Vector<Key> mAddFirst() {
        Key key = data.nextKeyInB();
        return (listA).prepend(key);
    }

    @Benchmark
    public Vector<Key> mRemoveLast() {
        return listA.removeAt(listA.size() - 1);
    }

    @Benchmark
    public Key mGet() {
        int index = data.nextIndexInA();
        return listA.get(index);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return listA.contains(key);
    }

    @Benchmark
    public Key mHead() {
        return listA.get(0);
    }

    @Benchmark
    public Vector<Key> mSet() {
        int index = data.nextIndexInA();
        Key key = data.nextKeyInB();
        return listA.update(index, key);
    }
    
     */

}
