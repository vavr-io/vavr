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
 * A/B micro-benchmark comparing the classic HAMT ({@link HashArrayMappedTrie}) against the
 * experimental CHAMP ({@link CompressedHashArrayMappedPrefixTrie}) through the identical {@link HashArrayMappedTrie}
 * interface, so the only variable is the node representation.
 *
 * <p>Run via {@code io.vavr.JmhRunner}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class ChampBenchmark {

    public enum Impl { HAMT, CHAMP }

    @Param({"HAMT", "CHAMP"})
    public Impl impl;

    @Param({"1000", "100000"})
    public int size;

    private Integer[] keys;
    private Integer[] absentKeys;
    private HashArrayMappedTrie<Integer, Integer> trie;

    private HashArrayMappedTrie<Integer, Integer> empty() {
        return impl == Impl.HAMT ? HashArrayMappedTrie.empty() : CompressedHashArrayMappedPrefixTrie.empty();
    }

    @Setup(Level.Trial)
    public void setup() {
        final Random r = new Random(0xC0FFEE);
        keys = new Integer[size];
        absentKeys = new Integer[size];
        // distinct keys in [0, 2*size); even-ish split present/absent
        final java.util.LinkedHashSet<Integer> present = new java.util.LinkedHashSet<>();
        while (present.size() < size) {
            present.add(r.nextInt(size * 4));
        }
        int i = 0;
        for (Integer k : present) {
            keys[i++] = k;
        }
        for (int j = 0; j < size; j++) {
            Integer candidate;
            do {
                candidate = r.nextInt(size * 4);
            } while (present.contains(candidate));
            absentKeys[j] = candidate;
        }
        HashArrayMappedTrie<Integer, Integer> t = empty();
        for (Integer k : keys) {
            t = t.put(k, k);
        }
        trie = t;
    }

    @Benchmark
    public HashArrayMappedTrie<Integer, Integer> build() {
        HashArrayMappedTrie<Integer, Integer> t = empty();
        for (Integer k : keys) {
            t = t.put(k, k);
        }
        return t;
    }

    @Benchmark
    public void getHits(Blackhole bh) {
        for (Integer k : keys) {
            bh.consume(trie.getOrElse(k, -1));
        }
    }

    @Benchmark
    public void getMisses(Blackhole bh) {
        for (Integer k : absentKeys) {
            bh.consume(trie.getOrElse(k, -1));
        }
    }

    @Benchmark
    public HashArrayMappedTrie<Integer, Integer> removeAll() {
        HashArrayMappedTrie<Integer, Integer> t = trie;
        for (Integer k : keys) {
            t = t.remove(k);
        }
        return t;
    }

    @Benchmark
    public void iterate(Blackhole bh) {
        final Iterator<Tuple2<Integer, Integer>> it = trie.iterator();
        while (it.hasNext()) {
            bh.consume(it.next());
        }
    }
}
