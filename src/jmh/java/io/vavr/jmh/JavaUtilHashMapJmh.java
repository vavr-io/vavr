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

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark               (size)  Mode  Cnt    _     Score        Error  Units
 * ContainsFound          1000000  avgt    4        93.098 ±      2.658  ns/op
 * ContainsNotFound       1000000  avgt    4        93.507 ±      0.773  ns/op
 * Iterate                1000000  avgt    4  33816828.875 ± 907645.391  ns/op
 * Put                    1000000  avgt    4       203.074 ±      7.930  ns/op
 * RemoveThenAdd          1000000  avgt    4       164.366 ±      2.594  ns/op
 * Head                   1000000  avgt    4        12.922 ±      0.437  ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class JavaUtilHashMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private Set<Key> setA;
    private HashMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
         mapA = new HashMap<>();
        setA = Collections.newSetFromMap(mapA);
        setA.addAll(data.setA);
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : setA) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        setA.remove(key);
        setA.add(key);
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapA.put(key,Boolean.FALSE);
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

    @Benchmark
    public Key mHead() {
        return mapA.keySet().iterator().next();
    }
}
