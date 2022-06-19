package io.vavr.jmh;

import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.VectorMap;
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
import scala.collection.mutable.Builder;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.8
 *
 * Benchmark         (size)  Mode  Cnt    _     Score        Error  Units
 * ContainsFound     1000000  avgt    4        262.398 ±     84.850  ns/op
 * ContainsNotFound  1000000  avgt    4        255.078 ±     11.044  ns/op
 * Head              1000000  avgt    4         38.234 ±      2.455  ns/op
 * Iterate           1000000  avgt    4  284698238.201 ± 950950.509  ns/op
 * Put               1000000  avgt    4        501.840 ±      6.593  ns/op
 * RemoveAdd         1000000  avgt    4       1242.707 ±    503.426  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 4)
@Warmup(iterations = 4)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ScalaVectorMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private VectorMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, VectorMap<Key, Boolean>> b = VectorMap.newBuilder();
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
    public void mRemoveAdd() {
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
}
