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
import scala.collection.immutable.Vector;
import scala.collection.mutable.ReusableBuilder;

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
 * Benchmark                          (size)  Mode  Cnt         Score   Error  Units
 * ScalaVectorJmh.mAddFirst               10  avgt             27.796          ns/op
 * ScalaVectorJmh.mAddFirst          1000000  avgt            320.989          ns/op
 * ScalaVectorJmh.mAddLast                10  avgt             24.118          ns/op
 * ScalaVectorJmh.mAddLast           1000000  avgt            207.482          ns/op
 * ScalaVectorJmh.mContainsNotFound       10  avgt             14.826          ns/op
 * ScalaVectorJmh.mContainsNotFound  1000000  avgt       20864102.835          ns/op
 * ScalaVectorJmh.mGet                    10  avgt              4.311          ns/op
 * ScalaVectorJmh.mGet               1000000  avgt            198.885          ns/op
 * ScalaVectorJmh.mHead                   10  avgt              1.082          ns/op
 * ScalaVectorJmh.mHead              1000000  avgt              1.082          ns/op
 * ScalaVectorJmh.mIterate                10  avgt             11.180          ns/op
 * ScalaVectorJmh.mIterate           1000000  avgt       32438888.398          ns/op
 * ScalaVectorJmh.mRemoveLast             10  avgt             18.567          ns/op
 * ScalaVectorJmh.mRemoveLast        1000000  avgt            103.234          ns/op
 * ScalaVectorJmh.mReversedIterate        10  avgt             10.555          ns/op
 * ScalaVectorJmh.mReversedIterate   1000000  avgt       43129266.738          ns/op
 * ScalaVectorJmh.mTail                   10  avgt             18.878          ns/op
 * ScalaVectorJmh.mTail              1000000  avgt             46.531          ns/op
 * ScalaVectorJmh.mSet                    10  avgt             33.717          ns/op
 * ScalaVectorJmh.mSet               1000000  avgt            847.992          ns/op
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class ScalaVectorJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private Vector<Key> listA;


    private Method updated;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        ReusableBuilder<Key, Vector<Key>> b = Vector.newBuilder();
        for (Key key : data.setA) {
            b.addOne(key);
        }
        listA = b.result();

        data.nextKeyInA();
        try {
            updated = Vector.class.getDeclaredMethod("updated", Integer.TYPE, Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

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
        for (Iterator<Key> i = listA.reverseIterator(); i.hasNext(); ) {
            sum += i.next().value;
        }
        return sum;
    }

    @Benchmark
    public Vector<Key> mTail() {
        return listA.tail();
    }

    @Benchmark
    public Vector<Key> mAddLast() {
        Key key = data.nextKeyInB();
        return (Vector<Key>) (listA).$colon$plus(key);
    }

    @Benchmark
    public Vector<Key> mAddFirst() {
        Key key = data.nextKeyInB();
        return (Vector<Key>) (listA).$plus$colon(key);
    }

    @Benchmark
    public Vector<Key> mRemoveLast() {
        return listA.dropRight(1);
    }

    @Benchmark
    public Key mGet() {
        int index = data.nextIndexInA();
        return listA.apply(index);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return listA.contains(key);
    }

    @Benchmark
    public Key mHead() {
        return listA.head();
    }

    @Benchmark
    public Vector<Key> mSet() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int index = data.nextIndexInA();
        Key key = data.nextKeyInB();

        return (Vector<Key>) updated.invoke(listA, index, key);
    }

}
