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
import scala.collection.immutable.VectorMap;
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
 * ScalaVectorMapJmh.mContainsFound          10  avgt    4          7.010 ±       0.070  ns/op
 * ScalaVectorMapJmh.mContainsFound     1000000  avgt    4        286.636 ±     163.132  ns/op
 * ScalaVectorMapJmh.mContainsNotFound       10  avgt    4          6.475 ±       0.454  ns/op
 * ScalaVectorMapJmh.mContainsNotFound  1000000  avgt    4        299.524 ±       2.474  ns/op
 * ScalaVectorMapJmh.mHead                   10  avgt    4          7.291 ±       0.549  ns/op
 * ScalaVectorMapJmh.mHead              1000000  avgt    4         26.498 ±       0.175  ns/op
 * ScalaVectorMapJmh.mIterate                10  avgt    4         88.927 ±       6.506  ns/op
 * ScalaVectorMapJmh.mIterate           1000000  avgt    4  341379733.683 ± 3030428.490  ns/op
 * ScalaVectorMapJmh.mPut                    10  avgt    4         31.937 ±       1.585  ns/op
 * ScalaVectorMapJmh.mPut               1000000  avgt    4        502.505 ±       9.940  ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd          10  avgt    4        140.745 ±       2.629  ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd     1000000  avgt    4       1212.184 ±      27.835  ns/op * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0, jvmArgsAppend = {"-Xmx24g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaVectorMapJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private VectorMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, VectorMap<Key, Boolean>> b = VectorMap.newBuilder();
        for (Key key : data.setA) {
            b.addOne(new Tuple2<>(key, Boolean.TRUE));
        }
        mapA = b.result();
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Iterator<Key> i = mapA.keysIterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public VectorMap<Key, Boolean> mRemoveThenAdd() {
        Key key = data.nextKeyInA();
        return (VectorMap<Key, Boolean>) mapA.$minus(key).$plus(new Tuple2<>(key, Boolean.TRUE));

    }

    @Benchmark
    public VectorMap<Key, Boolean> mPut() {
        Key key = data.nextKeyInA();
        return (VectorMap<Key, Boolean>) mapA.$plus(new Tuple2<>(key, Boolean.FALSE));
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
