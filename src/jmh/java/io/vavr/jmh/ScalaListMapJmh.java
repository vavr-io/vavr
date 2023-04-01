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
import scala.collection.immutable.ListMap;
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
 * ContainsFound     1000000  avgt    4             ? ± ?  ns/op
 * ContainsNotFound  1000000  avgt    4             ? ± ?  ns/op
 * Iterate           1000000  avgt    4             ? ± ?  ns/op
 * Put               1000000  avgt    4             ? ± ?  ns/op
 * RemoveThenAdd     1000000  avgt    4             ? ± ?  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaListMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ListMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, ListMap<Key, Boolean>> b = ListMap.newBuilder();
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
}
