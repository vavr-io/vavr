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
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.HashMap;
import scala.collection.mutable.Builder;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.8
 *
 * Benchmark                             (size)  Mode  Cnt          Score         Error  Units
 * ScalaHashMapJmh.mContainsFound            10  avgt    4          6.163 ±       0.096  ns/op
 * ScalaHashMapJmh.mContainsFound       1000000  avgt    4        271.014 ±      11.496  ns/op
 * ScalaHashMapJmh.mContainsNotFound         10  avgt    4          6.169 ±       0.107  ns/op
 * ScalaHashMapJmh.mContainsNotFound    1000000  avgt    4        273.811 ±      19.868  ns/op
 * ScalaHashMapJmh.mHead                     10  avgt    4          1.699 ±       0.024  ns/op
 * ScalaHashMapJmh.mHead                1000000  avgt    4         23.117 ±       0.496  ns/op
 * ScalaHashMapJmh.mIterate                  10  avgt    4          9.599 ±       0.077  ns/op
 * ScalaHashMapJmh.mIterate             1000000  avgt    4   38578271.355 ± 1380759.932  ns/op
 * ScalaHashMapJmh.mPut                      10  avgt    4         14.226 ±       0.364  ns/op
 * ScalaHashMapJmh.mPut                 1000000  avgt    4        399.880 ±       5.722  ns/op
 * ScalaHashMapJmh.mRemoveThenAdd            10  avgt    4         81.323 ±       8.510  ns/op
 * ScalaHashMapJmh.mRemoveThenAdd       1000000  avgt    4        684.429 ±       8.141  ns/op
 * ScalaHashMapJmh.mTail                     10  avgt    4         37.080 ±       1.845  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaHashMapJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private HashMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, HashMap<Key, Boolean>> b = HashMap.newBuilder();
        for (Key key : data.setA) {
            b.addOne(new Tuple2<>(key,Boolean.TRUE));
        }
        mapA = b.result();
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for(Iterator<Key> i = mapA.keysIterator();i.hasNext();){
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        mapA.$minus(key).$plus(new Tuple2<>(key,Boolean.TRUE));
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapA.$plus(new Tuple2<>(key,Boolean.FALSE));
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapA.contains(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapA.contains(key);
    }

    @Benchmark
    public Key mHead() {
        return mapA.head()._1;
    }

    @Benchmark
    public HashMap<Key, Boolean> mTail() {
        return mapA.tail();
    }

}
