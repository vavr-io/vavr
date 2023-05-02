package io.vavr.jmh;

import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.collections.immutable.PersistentList;
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
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.36
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 * # org.scala-lang:scala-library:2.13.8
 *
 * Benchmark                          (size)  Mode  Cnt         Score   Error  Units
 * Benchmark                                    (size)  Mode  Cnt         Score   Error  Units
 * KotlinxPersistentListJmh.mAddFirst               10  avgt             37.240          ns/op
 * KotlinxPersistentListJmh.mAddFirst          1000000  avgt        4336671.001          ns/op
 * KotlinxPersistentListJmh.mAddLast                10  avgt             30.976          ns/op
 * KotlinxPersistentListJmh.mAddLast           1000000  avgt            378.535          ns/op
 * KotlinxPersistentListJmh.mContainsNotFound       10  avgt              9.256          ns/op
 * KotlinxPersistentListJmh.mContainsNotFound  1000000  avgt       33750606.182          ns/op
 * KotlinxPersistentListJmh.mGet                    10  avgt              4.423          ns/op
 * KotlinxPersistentListJmh.mGet               1000000  avgt            333.608          ns/op
 * KotlinxPersistentListJmh.mHead                   10  avgt              1.617          ns/op
 * KotlinxPersistentListJmh.mHead              1000000  avgt              4.963          ns/op
 * KotlinxPersistentListJmh.mIterate                10  avgt              9.897          ns/op
 * KotlinxPersistentListJmh.mIterate           1000000  avgt       57524400.138          ns/op
 * KotlinxPersistentListJmh.mRemoveLast             10  avgt             24.612          ns/op
 * KotlinxPersistentListJmh.mRemoveLast        1000000  avgt             52.131          ns/op
 * KotlinxPersistentListJmh.mReversedIterate        10  avgt             10.665          ns/op
 * KotlinxPersistentListJmh.mReversedIterate   1000000  avgt       56937509.432          ns/op
 * KotlinxPersistentListJmh.mSet                    10  avgt             27.375          ns/op
 * KotlinxPersistentListJmh.mSet               1000000  avgt            923.214          ns/op
 * KotlinxPersistentListJmh.mTail                   10  avgt             35.463          ns/op
 * KotlinxPersistentListJmh.mTail              1000000  avgt        3364941.624          ns/op
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@SuppressWarnings("unchecked")
public class KotlinxPersistentListJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private PersistentList<Key> listA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        listA = ExtensionsKt.persistentListOf();
        for (Key key : data.setA) {
            listA = listA.add(key);
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
        for (ListIterator<Key> i = listA.listIterator(listA.size()); i.hasPrevious(); ) {
            sum += i.previous().value;
        }
        return sum;
    }

    @Benchmark
    public PersistentList<Key> mTail() {
        return listA.removeAt(0);
    }

    @Benchmark
    public PersistentList<Key> mAddLast() {
        Key key = data.nextKeyInB();
        return (listA).add(key);
    }

    @Benchmark
    public PersistentList<Key> mAddFirst() {
        Key key = data.nextKeyInB();
        return (listA).add(0, key);
    }

    @Benchmark
    public PersistentList<Key> mRemoveLast() {
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
    public PersistentList<Key> mSet() {
        int index = data.nextIndexInA();
        Key key = data.nextKeyInB();
        return listA.set(index, key);
    }

}
