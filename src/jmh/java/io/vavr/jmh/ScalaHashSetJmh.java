package io.vavr.jmh;

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
import scala.collection.Iterator;
import scala.collection.immutable.HashSet;
import scala.collection.mutable.ReusableBuilder;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 1.8.0_345, OpenJDK 64-Bit Server VM, 25.345-b01
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.10
 *
 * ScalaHashSetJmh.mContainsFound          -65  100000  avgt            101.833          ns/op
 * ScalaHashSetJmh.mContainsNotFound       -65  100000  avgt            101.225          ns/op
 * ScalaHashSetJmh.mHead                   -65  100000  avgt             19.545          ns/op
 * ScalaHashSetJmh.mIterate                -65  100000  avgt        3504486.602          ns/op
 * ScalaHashSetJmh.mRemoveThenAdd          -65  100000  avgt            398.521          ns/op
 * ScalaHashSetJmh.mTail                   -65  100000  avgt             98.564          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaHashSetJmh {
    @Param({/*"10","1000",*/"100000"/*,"10000000"*/})
    private int size;

    @Param({"-65"})
    private int mask;

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
        for (Iterator<Key> i = setA.iterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key = data.nextKeyInA();
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
