package io.vavr.jmh;

import org.openjdk.jmh.annotations.*;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.HashMap;
import scala.collection.immutable.Map;
import scala.collection.immutable.Vector;
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
 * Benchmark                          (mask)    (size)  Mode  Cnt    _        Score   Error  Units
 * ScalaHashMapJmh.mAddAll               -65        10  avgt         _      467.142          ns/op
 * ScalaHashMapJmh.mAddAll               -65      1000  avgt         _   114499.940          ns/op
 * ScalaHashMapJmh.mAddAll               -65    100000  avgt         _ 23510614.310          ns/op
 * ScalaHashMapJmh.mAddAll               -65  10000000  avgt        7_447239207.500          ns/op
 * ScalaHashMapJmh.mAddOneByOne          -65        10  avgt         _      432.536          ns/op
 * ScalaHashMapJmh.mAddOneByOne          -65      1000  avgt         _   138463.447          ns/op
 * ScalaHashMapJmh.mAddOneByOne          -65    100000  avgt         _ 35389172.339          ns/op
 * ScalaHashMapJmh.mAddOneByOne          -65  10000000  avgt       10_663694719.000          ns/op
 * ScalaHashMapJmh.mRemoveAll            -65        10  avgt         _      384.790          ns/op
 * ScalaHashMapJmh.mRemoveAll            -65      1000  avgt         _   126641.616          ns/op
 * ScalaHashMapJmh.mRemoveAll            -65    100000  avgt         _ 32877551.174          ns/op
 * ScalaHashMapJmh.mRemoveAll            -65  10000000  avgt       14_457074260.000          ns/op
 * ScalaHashMapJmh.mRemoveOneByOne       -65        10  avgt         _      373.129          ns/op
 * ScalaHashMapJmh.mRemoveOneByOne       -65      1000  avgt         _   134244.683          ns/op
 * ScalaHashMapJmh.mRemoveOneByOne       -65    100000  avgt         _ 34034988.668          ns/op
 * ScalaHashMapJmh.mRemoveOneByOne       -65  10000000  avgt       12_629623452.000          ns/op
 *
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
@Fork(value = 0, jvmArgsAppend = {"-Xmx28g"})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaHashMapJmh {
    @Param({"10", "1000", "100000", "10000000"})
    private int size;

    @Param({"-65"})
    private int mask;

    private BenchmarkData data;
    private HashMap<Key, Boolean> mapA;
    private Vector<Tuple2<Key, Boolean>> listA;
    private Vector<Key> listAKeys;
    private Method appended;


    @SuppressWarnings("unchecked")
    @Setup
    public void setup() throws InvocationTargetException, IllegalAccessException {
        try {
            appended = Vector.class.getDeclaredMethod("appended", Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        data = new BenchmarkData(size, mask);
        Builder<Tuple2<Key, Boolean>, HashMap<Key, Boolean>> b = HashMap.newBuilder();
        for (Key key : data.setA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            b.addOne(elem);
        }
        listA = Vector.<Tuple2<Key, Boolean>>newBuilder().result();
        listAKeys = Vector.<Key>newBuilder().result();
        for (Key key : data.listA) {
            Tuple2<Key, Boolean> elem = new Tuple2<>(key, Boolean.TRUE);
            listA = (Vector<Tuple2<Key, Boolean>>) appended.invoke(listA, elem);
            listAKeys = (Vector<Key>) appended.invoke(listAKeys, key);
        }
        mapA = b.result();

    }

    @Benchmark
    public HashMap<Key, Boolean> mAddAll() {
        return HashMap.from(listA);
    }

    @Benchmark
    public HashMap<Key, Boolean> mAddOneByOne() {
        HashMap<Key, Boolean> set = HashMap.<Key, Boolean>newBuilder().result();
        for (Key key : data.listA) {
            set = set.updated(key, Boolean.TRUE);
        }
        return set;
    }

    @Benchmark
    public HashMap<Key, Boolean> mRemoveOneByOne() {
        HashMap<Key, Boolean> set = mapA;
        for (Key key : data.listA) {
            set = set.removed(key);
        }
        return set;
    }

    @Benchmark
    public Map<Key, Boolean> mRemoveAll() {
        HashMap<Key, Boolean> set = mapA;
        return set.removedAll(listAKeys);
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
