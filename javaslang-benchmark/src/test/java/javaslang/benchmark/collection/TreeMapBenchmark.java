/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.benchmark.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.benchmark.JmhRunner;
import javaslang.collection.Array;
import javaslang.collection.SortedMap;
import javaslang.collection.TreeMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import scala.collection.Iterator;
import scala.math.Ordering;
import scala.math.Ordering$;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;

public class TreeMapBenchmark {
    private static final Ordering<Integer> SCALA_ORDERING = Ordering$.MODULE$.comparatorToOrdering(Integer::compareTo);

    public static void main(String... args) { /* main is more reliable than a test */
        JmhRunner.devRun(TreeMapBenchmark.class);
    }

    @State(Scope.Thread)
    public static class Base {
        @Param({"10", "100", "10000"})
        int numItems;

        Random random = new Random(0);

        int minKey;
        int maxKey;

        Array<Integer> keysSorted;
        Array<Integer> keysRandom;
        Array<Integer> allKeys;
        Array<Tuple2<Integer, Integer>> entriesRandom;

        TreeMap<Integer, Integer> slangTreeMap = TreeMap.empty();
        java.util.TreeMap<Integer, Integer> javaTreeMap = new java.util.TreeMap<>();
        scala.collection.immutable.TreeMap<Integer, Integer> scalaTreeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);

        Integer firstSubKey;
        Integer lastSubKey;

        @Setup(Level.Iteration)
        public void setUp() {
            minKey = 1;
            maxKey = numItems * 2;

            // We exclude keys with odd numbers to test methods requiring missing keys, such as floor or ceiling
            keysSorted = Array.rangeBy(minKey, maxKey, 2);
            allKeys = Array.range(minKey, maxKey);
            keysRandom = randomize(keysSorted);
            entriesRandom = keysRandom.map(i -> Tuple.of(i, i));

            // We will create subset with 1/3 of entries in the original map. To calculate offset on each end, we need to divide by 6
            int keyOffset = numItems / 6;
            firstSubKey = keysSorted.get(keyOffset);
            lastSubKey = keysSorted.get(numItems - keyOffset);

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
            allKeys = null;
            entriesRandom = null;
            slangTreeMap = null;
            javaTreeMap = null;
            scalaTreeMap = null;
        }

        Array<Integer> randomize(Array<Integer> list) {
            java.util.List<Integer> integers = list.toJavaList();
            Collections.shuffle(integers, random);
            return Array.ofAll(integers);
        }
    }

    public static class PutRandomValues extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            TreeMap<Integer, Integer> treeMap = TreeMap.empty();
            for (Integer key : state.keysRandom) {
                treeMap = treeMap.put(key, key);
            }
            return treeMap;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            java.util.TreeMap<Integer, Integer> treeMap = new java.util.TreeMap<>();
            for (Integer key : state.keysRandom) {
                treeMap.put(key, key);
            }
            return treeMap;
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            scala.collection.immutable.TreeMap<Integer, Integer> treeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);
            for (Integer key : state.keysRandom) {
                treeMap = treeMap.$plus(new scala.Tuple2<>(key, key));
            }
            return treeMap;
        }
    }

    public static class PutSortedValues extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            TreeMap<Integer, Integer> treeMap = TreeMap.empty();
            for (Integer key : state.keysSorted) {
                treeMap = treeMap.put(key, key);
            }
            return treeMap;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            java.util.TreeMap<Integer, Integer> treeMap = new java.util.TreeMap<>();
            for (Integer key : state.keysSorted) {
                treeMap.put(key, key);
            }
            return treeMap;
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            scala.collection.immutable.TreeMap<Integer, Integer> treeMap = new scala.collection.immutable.TreeMap<>(SCALA_ORDERING);
            for (Integer key : state.keysSorted) {
                treeMap = treeMap.$plus(new scala.Tuple2<>(key, key));
            }
            return treeMap;
        }
    }

    public static class GetValue extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            int result = 0;
            for (Integer key : state.keysRandom) {
                result ^= state.slangTreeMap.get(key).get();
            }
            return result;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            int result = 0;
            for (Integer key : state.keysRandom) {
                result ^= state.javaTreeMap.get(key);
            }
            return result;
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            int result = 0;
            for (Integer key : state.keysRandom) {
                result ^= state.scalaTreeMap.get(key).get();
            }
            return result;
        }
    }

    public static class FirstValue extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            return state.slangTreeMap.min();
        }

        @Benchmark
        public Object java_mutable(Base state) {
            return state.javaTreeMap.firstKey();
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            return state.scalaTreeMap.firstKey();
        }
    }

    public static class LastValue extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            return state.slangTreeMap.max();
        }

        @Benchmark
        public Object java_mutable(Base state) {
            return state.javaTreeMap.lastKey();
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            return state.scalaTreeMap.lastKey();
        }
    }

    public static class Floor extends Base {
        @Benchmark
        public void slang_persistent(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.slangTreeMap.floor(key));
            }
        }

        @Benchmark
        public void java_mutable(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.javaTreeMap.floorEntry(key));
            }
        }
    }

    public static class Ceiling extends Base {
        @Benchmark
        public void slang_persistent(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.slangTreeMap.ceiling(key));
            }
        }

        @Benchmark
        public void java_mutable(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.javaTreeMap.ceilingEntry(key));
            }
        }
    }

    public static class Higher extends Base {
        @Benchmark
        public void slang_persistent(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.slangTreeMap.higher(key));
            }
        }

        @Benchmark
        public void java_mutable(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.javaTreeMap.higherEntry(key));
            }
        }
    }

    public static class Lower extends Base {
        @Benchmark
        public void slang_persistent(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.slangTreeMap.lower(key));
            }
        }

        @Benchmark
        public void java_mutable(Base state, Blackhole blackhole) {
            for (Integer key : state.allKeys) {
                blackhole.consume(state.javaTreeMap.lowerEntry(key));
            }
        }
    }

    public static class IterateAscending extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            int result = 0;
            for (Tuple2<Integer, Integer> entry : state.slangTreeMap) {
                result ^= entry._1;
            }
            return result;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            int result = 0;
            for (Map.Entry<Integer, Integer> aTreeMap : state.javaTreeMap.entrySet()) {
                result ^= aTreeMap.getKey();
            }
            return result;
        }

        @Benchmark
        public Object scala_persistent(Base state) {
            int result = 0;
            Iterator<scala.Tuple2<Integer, Integer>> iterator = state.scalaTreeMap.iterator();
            while (iterator.hasNext()) {
                result ^= iterator.next()._1;
            }
            return result;
        }
    }

    public static class IterateDescening extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            int result = 0;
            for (Tuple2<Integer, Integer> aTreeMap : state.slangTreeMap.descendingIterator()) {
                result ^= aTreeMap._1;
            }
            return result;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            int result = 0;
            for (Map.Entry<Integer, Integer> aTreeMap : state.javaTreeMap.descendingMap().entrySet()) {
                result ^= aTreeMap.getKey();
            }
            return result;
        }
    }

    public static class CreateSubMapAndIterateOneThirdElements extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            SortedMap<Integer, Integer> subMap = state.slangTreeMap.subMap(firstSubKey, true, lastSubKey, true);
            int result = 0;
            for (Tuple2<Integer, Integer> entry : subMap) {
                result ^= entry._1;
            }
            return result;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            NavigableMap<Integer, Integer> subMap = state.javaTreeMap.subMap(firstSubKey, true, lastSubKey, true);
            int result = 0;
            for (Map.Entry<Integer, Integer> entry : subMap.entrySet()) {
                result ^= entry.getKey();
            }
            return result;
        }
    }

    public static class CreateDescendingSubMapAndIterateOneThirdElements extends Base {
        @Benchmark
        public Object slang_persistent(Base state) {
            SortedMap<Integer, Integer> subMap = state.slangTreeMap.descendingMap().subMap(lastSubKey, true, firstSubKey, true);
            int result = 0;
            for (Tuple2<Integer, Integer> entry : subMap) {
                result ^= entry._1;
            }
            return result;
        }

        @Benchmark
        public Object java_mutable(Base state) {
            NavigableMap<Integer, Integer> subMap = state.javaTreeMap.descendingMap().subMap(lastSubKey, true, firstSubKey, true);
            int result = 0;
            for (Map.Entry<Integer, Integer> entry : subMap.entrySet()) {
                result ^= entry.getKey();
            }
            return result;
        }
    }
}
