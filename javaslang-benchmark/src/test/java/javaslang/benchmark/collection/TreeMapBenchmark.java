/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.benchmark.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.benchmark.BenchmarkPerformanceReporter;
import javaslang.collection.Array;
import javaslang.collection.List;
import javaslang.collection.SortedMap;
import javaslang.collection.Stack;
import javaslang.collection.TreeMap;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import scala.collection.Iterator;
import scala.math.Ordering;
import scala.math.Ordering$;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1, jvmArgsAppend = { "-XX:+UseG1GC", "-Xss500m", "-Xms4g", "-Xmx4g", "-ea" })
@Threads(1)
public class TreeMapBenchmark {
    private static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);

    @Test
    public void launchBenchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        Collection<RunResult> results = new Runner(opt).run();
        BenchmarkPerformanceReporter reporter = BenchmarkPerformanceReporter.of(results);
        reporter.printDetailedPerformanceReport();
        reporter.printPerformanceRatiosReport();
    }

    // The JMH samples are the best documentation for how to use it
    // http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"10", "1000", "100000"})
        int numItems;

        Random random = new Random();

        int minKey;
        int maxKey;

        Array<Integer> keysSorted;
        Array<Integer> keysRandom;
        Array<Tuple2<Integer, String>> entriesRandom;

        TreeMap<Integer, String> slangTreeMap = TreeMap.empty();
        java.util.TreeMap<Integer, String> javaTreeMap = new java.util.TreeMap<>();
        scala.collection.immutable.TreeMap<Integer, String> scalaTreeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);

        @Setup(Level.Iteration)
        public void setUp() {
            minKey = 1;
            maxKey = numItems * 2;

            // We exclude keys with odd numbers to test methods requiring missing keys, such as floor or ceiling
            keysSorted = Array.rangeBy(minKey, maxKey, 2);
            keysRandom = randomize(keysSorted);
            entriesRandom = keysRandom.map(i -> Tuple.of(i, Integer.toString(i)));

            entriesRandom.forEach(e -> {
                slangTreeMap = slangTreeMap.put(e._1, e._2);
                javaTreeMap.put(e._1, e._2);
                scalaTreeMap = scalaTreeMap.$plus(new scala.Tuple2<>(e._1, e._2));
            });
        }

        @TearDown
        public void tearDown() {
            keysRandom = null;
            keysRandom = null;
            entriesRandom = null;
            slangTreeMap = null;
            javaTreeMap = null;
            scalaTreeMap = null;
        }

        // Valid keys are only even numbers
        int nextValidRandomKey() {
            return keysRandom.get(random.nextInt(numItems));
        }

        int nextRandomKey() {
            return random.nextInt(maxKey);
        }

        Array<Integer> randomize(Array<Integer> list) {
            java.util.List<Integer> integers = list.toJavaList();
            Collections.shuffle(integers);
            return Array.ofAll(integers);
        }
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putRandomValues_Slang(BenchmarkState state) {
        TreeMap<Integer, String> treeMap = TreeMap.empty();
        for (Integer key : state.keysRandom) {
            treeMap = treeMap.put(key, Integer.toString(key));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putRandomValues_Java(BenchmarkState state) {
        java.util.TreeMap<Integer, String> treeMap = new java.util.TreeMap<>();
        for (Integer key : state.keysRandom) {
            treeMap.put(key, Integer.toString(key));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putRandomValues_Scala(BenchmarkState state) {
        scala.collection.immutable.TreeMap<Integer, String> treeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);
        for (Integer key : state.keysRandom) {
            treeMap = treeMap.$plus(new scala.Tuple2<>(key, Integer.toString(key)));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putSortedValues_Slang(BenchmarkState state) {
        TreeMap<Integer, String> treeMap = TreeMap.empty();
        for (Integer key : state.keysSorted) {
            treeMap = treeMap.put(key, Integer.toString(key));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putSortedValues_Java(BenchmarkState state) {
        java.util.TreeMap<Integer, String> treeMap = new java.util.TreeMap<>();
        for (Integer key : state.keysSorted) {
            treeMap.put(key, Integer.toString(key));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object putSortedValues_Scala(BenchmarkState state) {
        scala.collection.immutable.TreeMap<Integer, String> treeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);
        for (Integer key : state.keysSorted) {
            treeMap = treeMap.$plus(new scala.Tuple2<>(key, Integer.toString(key)));
        }
        return treeMap;
    }

    @Benchmark
    @Group("GetAndPut")
    public Object getValue_Slang(BenchmarkState state) {
        return state.slangTreeMap.get(state.nextValidRandomKey());
    }

    @Benchmark
    @Group("GetAndPut")
    public Object getValue_Java(BenchmarkState state) {
        return state.javaTreeMap.get(state.nextValidRandomKey());
    }

    @Benchmark
    @Group("GetAndPut")
    public Object getValue_Scala(BenchmarkState state) {
        return state.scalaTreeMap.get(state.nextValidRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object firstValue_Slang(BenchmarkState state) {
        return state.slangTreeMap.min();
    }

    @Benchmark
    @Group("Navigate")
    public Object firstValue_Java(BenchmarkState state) {
        return state.javaTreeMap.firstKey();
    }

    @Benchmark
    @Group("Navigate")
    public Object firstValue_Scala(BenchmarkState state) {
        return state.scalaTreeMap.firstKey();
    }

    @Benchmark
    @Group("Navigate")
    public Object lastValue_Slang(BenchmarkState state) {
        return state.slangTreeMap.max();
    }

    @Benchmark
    @Group("Navigate")
    public Object lastValue_Java(BenchmarkState state) {
        return state.javaTreeMap.lastKey();
    }

    @Benchmark
    @Group("Navigate")
    public Object lastValue_Scala(BenchmarkState state) {
        return state.scalaTreeMap.lastKey();
    }

    @Benchmark
    @Group("Navigate")
    public Object floor_Slang(BenchmarkState state) {
        return state.slangTreeMap.floor(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object floor_Java(BenchmarkState state) {
        return state.javaTreeMap.floorEntry(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object ceiling_Slang(BenchmarkState state) {
        return state.slangTreeMap.ceiling(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object ceiling_Java(BenchmarkState state) {
        return state.javaTreeMap.ceilingEntry(state.nextRandomKey());
    }
    @Benchmark
    @Group("Navigate")
    public Object higher_Slang(BenchmarkState state) {
        return state.slangTreeMap.higher(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object higher_Java(BenchmarkState state) {
        return state.javaTreeMap.higherEntry(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object lower_Slang(BenchmarkState state) {
        return state.slangTreeMap.lower(state.nextRandomKey());
    }

    @Benchmark
    @Group("Navigate")
    public Object lower_Java(BenchmarkState state) {
        return state.javaTreeMap.lowerEntry(state.nextRandomKey());
    }

    @Benchmark
    @Group("IterateAscending")
    public Object iterateAscending_Slang(BenchmarkState state) {
        Stack<Integer> values = List.empty();
        for (Tuple2<Integer, String> entry : state.slangTreeMap) {
            values.push(entry._1);
        }
        return values;
    }

    @Benchmark
    @Group("IterateAscending")
    public Object iterateAscending_Java(BenchmarkState state) {
        Stack<Integer> values = List.empty();
        for (Map.Entry<Integer, String> aTreeMap : state.javaTreeMap.entrySet()) {
            values.push(aTreeMap.getKey());
        }
        return values;
    }

    @Benchmark
    @Group("IterateAscending")
    public Object iterateAscending_Scala(BenchmarkState state) {
        Stack<Integer> values = List.empty();
        Iterator<scala.Tuple2<Integer, String>> iterator = state.scalaTreeMap.iterator();
        while (iterator.hasNext()) {
            values.push(iterator.next()._1);
        }
        return values;
    }

    @Benchmark
    @Group("IterateDescending")
    public Object iterateDescending_Slang(BenchmarkState state) {
        Stack<Integer> values = List.empty();
        for (Tuple2<Integer, String> aTreeMap : state.slangTreeMap.descendingIterator()) {
            values.push(aTreeMap._1);
        }
        return values;
    }

    @Benchmark
    @Group("IterateDescending")
    public Object iterateDescending_Java(BenchmarkState state) {
        Stack<Integer> values = List.empty();
        for (Map.Entry<Integer, String> aTreeMap : state.javaTreeMap.descendingMap().entrySet()) {
            values.push(aTreeMap.getKey());
        }
        return values;
    }

    @Benchmark
    @Group("SubMap")
    public Object createSubMap_WithOneThirdElements_AndIterate_Slang(BenchmarkState state) {
        // We will create subset with 1/3 of entries in the original map
        int keyOffset = state.numItems / 6;
        SortedMap<Integer, String> subMap = state.slangTreeMap.subMap(state.keysSorted.get(keyOffset), true, state.keysSorted.get(state.numItems - keyOffset), true);
        List<Integer> values = List.empty();
        for (Tuple2<Integer, String> entry : subMap) {
            values.push(entry._1);
        }
        return values;
    }

    @Benchmark
    @Group("SubMap")
    public Object createSubMap_WithOneThirdElements_AndIterate_Java(BenchmarkState state) {
        // We will create subset with 1/3 of entries in the original map
        int keyOffset = state.numItems / 6;
        NavigableMap<Integer, String> subMap = state.javaTreeMap.subMap(state.keysSorted.get(keyOffset), true, state.keysSorted.get(state.numItems - keyOffset), true);
        List<Integer> values = List.empty();
        for (Map.Entry<Integer, String> entry : subMap.entrySet()) {
            values.push(entry.getKey());
        }
        return values;
    }

    @Benchmark
    @Group("SubMap")
    public Object createDescendingSubMap_WithOneThirdElements_AndIterate_Slang(BenchmarkState state) {
        // We will create subset with 1/3 of entries in the original map
        int keyOffset = state.numItems / 6;
        SortedMap<Integer, String> subMap = state.slangTreeMap.descendingMap().subMap(state.keysSorted.get(state.numItems - keyOffset), true, state.keysSorted.get(keyOffset), true);
        assert subMap.size() > 0;
        List<Integer> values = List.empty();
        for (Tuple2<Integer, String> entry : subMap) {
            values.push(entry._1);
        }
        return values;
    }

    @Benchmark
    @Group("SubMap")
    public Object createDescendingSubMap_WithOneThirdElements_AndIterate_Java(BenchmarkState state) {
        // We will create subset with 1/3 of entries in the original map
        int keyOffset = state.numItems / 6;
        NavigableMap<Integer, String> subMap = state.javaTreeMap.descendingMap().subMap(state.keysSorted.get(state.numItems - keyOffset), true, state.keysSorted.get(keyOffset), true);
        List<Integer> values = List.empty();
        for (Map.Entry<Integer, String> entry : subMap.entrySet()) {
            values.push(entry.getKey());
        }
        return values;
    }

}
