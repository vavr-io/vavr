package io.vavr.jmh;

import scala.collection.Iterator;
import scala.collection.immutable.HashSet;
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
import scala.collection.mutable.ReusableBuilder;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.8
 *
 * Benchmark          (size)  Mode  Cnt    _     Score         Error  Units
 * ContainsFound     1000000  avgt    4       213.926 ±       0.885  ns/op
 * ContainsNotFound  1000000  avgt    4       212.304 ±       1.445  ns/op
 * Head              1000000  avgt    4        24.136 ±       0.929  ns/op
 * Iterate           1000000  avgt    4  39136478.695 ± 1245379.552  ns/op
 * RemoveThenAdd     1000000  avgt    4       626.577 ±      12.087  ns/op
 * Tail              1000000  avgt    4       116.357 ±       5.528  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ScalaHashSetJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private HashSet<Key> setA;

    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        ReusableBuilder<Key, HashSet<Key>> b = HashSet.newBuilder();
        for (Key key : data.setA) {
            b.addOne(key);
        }
        setA = b.result();
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for(Iterator<Key> i = setA.iterator();i.hasNext();){
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        setA.$minus(key).$plus(key);
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
