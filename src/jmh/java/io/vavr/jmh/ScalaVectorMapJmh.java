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
import scala.collection.immutable.Map;
import scala.collection.immutable.Vector;
import scala.collection.immutable.VectorMap;
import scala.collection.mutable.Builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.8
 *
 * Benchmark                             (size)  Mode  Cnt          Score         Error  Units
 * ScalaVectorMapJmh.mAddAll             -65        10  avgt         _      891.588          ns/op
 * ScalaVectorMapJmh.mAddAll             -65      1000  avgt         _   131598.312          ns/op
 * ScalaVectorMapJmh.mAddAll             -65    100000  avgt         _ 27222417.883          ns/op
 * ScalaVectorMapJmh.mAddAll             -65  10000000  avgt        8_754590718.500          ns/op
 * ScalaVectorMapJmh.mAddOneByOne        -65        10  avgt         _     1351.565          ns/op
 * ScalaVectorMapJmh.mAddOneByOne        -65      1000  avgt         _   230505.086          ns/op
 * ScalaVectorMapJmh.mAddOneByOne        -65    100000  avgt         _ 38519331.004          ns/op
 * ScalaVectorMapJmh.mAddOneByOne        -65  10000000  avgt       11_514203632.500          ns/op
 * ScalaVectorMapJmh.mRemoveAll          -65        10  avgt         _      747.927          ns/op
 * ScalaVectorMapJmh.mRemoveAll          -65      1000  avgt         _   275620.950          ns/op
 * ScalaVectorMapJmh.mRemoveAll          -65    100000  avgt         _ 90461796.234          ns/op
 * ScalaVectorMapJmh.mRemoveAll          -65  10000000  avgt       23_798649411.000          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne     -65        10  avgt         _      716.848          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne     -65      1000  avgt         _   271883.379          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne     -65    100000  avgt         _ 86520238.974          ns/op
 * ScalaVectorMapJmh.mRemoveOneByOne     -65  10000000  avgt       20_752733783.000          ns/op
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
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaVectorMapJmh {
    @Param({"10","1000","100000","10000000"})
    private int size;

    @Param({"-65"})
    private  int mask;

    private BenchmarkData data;
    private VectorMap<Key, Boolean> mapA;
    private Vector<Tuple2<Key, Boolean>> listA;
    private Vector<Key> listAKeys;
    private Method appended;


    @SuppressWarnings("unchecked")
    @Setup
    public void setup() throws InvocationTargetException, IllegalAccessException {
        try {
            appended = Vector.class.getDeclaredMethod("appended",  Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, VectorMap<Key, Boolean>> b = VectorMap.newBuilder();
        for (Key key : data.setA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            b.addOne(elem);
        }
        listA=Vector.<Tuple2<Key,Boolean>>newBuilder().result();
        listAKeys=Vector.<Key>newBuilder().result();
        for (Key key : data.listA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            listA= (Vector<Tuple2<Key, Boolean>>) appended.invoke(listA,elem);
            listAKeys= (Vector<Key>) appended.invoke(listAKeys,key);
        }
        mapA = b.result();
    }

    @Benchmark
    public VectorMap<Key,Boolean> mAddAll() {
        return VectorMap.from(listA);
    }

    @Benchmark
    public VectorMap<Key,Boolean> mAddOneByOne() {
        VectorMap<Key,Boolean> set =  VectorMap.<Key,Boolean>newBuilder().result();
        for (Key key : data.listA) {
            set=set.updated(key,Boolean.TRUE);
        }
        return set;
    }

    @Benchmark
    public VectorMap<Key,Boolean> mRemoveOneByOne() {
        VectorMap<Key,Boolean> set = mapA;
        for (Key key : data.listA) {
            set=set.removed(key);
        }
        return set;
    }

    @Benchmark
    public Object mRemoveAll() {
        VectorMap<Key,Boolean> set = mapA;
        return set.removedAll(listAKeys);
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
