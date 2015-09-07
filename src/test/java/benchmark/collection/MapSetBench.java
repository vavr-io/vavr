/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package benchmark.collection;

import javaslang.collection.HashMap;
import javaslang.collection.TreeSet;
import benchmark.Benchmark;

public class MapSetBench {

    private static final int COUNT = 1_000_000;
    private static final int WARMUP = 1000;

    public static void main(String[] args) {

        // -- Java

        Benchmark.bench("Java HashMap", COUNT, WARMUP, new java.util.HashMap<>(), (i, map) -> {
            map.put(i, i);
            return map;
        });

        Benchmark.bench("Java TreeSet", COUNT, WARMUP, new java.util.TreeSet<>(), (i, set) -> {
            set.add(i);
            return set;
        });

        // -- Javaslang

        Benchmark.bench("Javaslang HashMap", COUNT, WARMUP, HashMap.empty(), (i, map) -> map.put(i, i));

        Benchmark.bench("Javaslang TreeSet", COUNT, WARMUP, TreeSet.<Integer> empty(), (i, set) -> set.add(i));
    }
}
