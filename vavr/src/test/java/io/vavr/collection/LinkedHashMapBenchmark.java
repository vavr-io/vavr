/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple2;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Benchmarks {@link LinkedHashMap} through its public API only, so the identical suite can be run
 * against different internal representations (e.g. before/after the tombstoned-order-slots change).
 *
 * <p>{@code removeDrain*}/{@code removeSingle} cover the improved operations; {@code put*},
 * {@code getHits} and {@code iterate} guard the operations that must not regress.
 *
 * <p>Run via {@code io.vavr.JmhRunner}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class LinkedHashMapBenchmark {

    @Param({ "1000", "100000" })
    public int size;

    private Integer[] keys;
    private Integer[] shuffledKeys;
    private LinkedHashMap<Integer, Integer> map;
    private int cursor;

    @Setup(Level.Trial)
    public void setup() {
        final Random random = new Random(0xC0FFEE);
        final java.util.LinkedHashSet<Integer> distinct = new java.util.LinkedHashSet<>();
        while (distinct.size() < size) {
            distinct.add(random.nextInt(size * 4));
        }
        keys = distinct.toArray(new Integer[0]);
        shuffledKeys = keys.clone();
        final java.util.List<Integer> shuffled = java.util.Arrays.asList(shuffledKeys);
        java.util.Collections.shuffle(shuffled, random);
        LinkedHashMap<Integer, Integer> m = LinkedHashMap.empty();
        for (Integer key : keys) {
            m = m.put(key, key);
        }
        map = m;
    }

    // -- improved by the tombstone change

    @Benchmark
    public LinkedHashMap<Integer, Integer> removeSingle() {
        return map.remove(shuffledKeys[cursor++ % size]);
    }

    @Benchmark
    public LinkedHashMap<Integer, Integer> removeDrainReverseOrder() {
        LinkedHashMap<Integer, Integer> m = map;
        for (int i = size - 1; i >= 0; i--) {
            m = m.remove(keys[i]);
        }
        return m;
    }

    @Benchmark
    public LinkedHashMap<Integer, Integer> removeDrainRandomOrder() {
        LinkedHashMap<Integer, Integer> m = map;
        for (Integer key : shuffledKeys) {
            m = m.remove(key);
        }
        return m;
    }

    @Benchmark
    public LinkedHashMap<Integer, Integer> putRemoveChurn() {
        LinkedHashMap<Integer, Integer> m = map;
        for (int i = 0; i < size; i++) {
            m = (i & 1) == 0 ? m.remove(shuffledKeys[i]) : m.put(shuffledKeys[i - 1], i);
        }
        return m;
    }

    // -- must not regress

    @Benchmark
    public LinkedHashMap<Integer, Integer> putFreshKeys() {
        LinkedHashMap<Integer, Integer> m = LinkedHashMap.empty();
        for (Integer key : keys) {
            m = m.put(key, key);
        }
        return m;
    }

    @Benchmark
    public LinkedHashMap<Integer, Integer> putOverwrite() {
        LinkedHashMap<Integer, Integer> m = map;
        for (Integer key : shuffledKeys) {
            m = m.put(key, 42);
        }
        return m;
    }

    @Benchmark
    public void getHits(Blackhole bh) {
        for (Integer key : shuffledKeys) {
            bh.consume(map.getOrElse(key, -1));
        }
    }

    @Benchmark
    public void iterate(Blackhole bh) {
        for (Tuple2<Integer, Integer> entry : map) {
            bh.consume(entry);
        }
    }
}
