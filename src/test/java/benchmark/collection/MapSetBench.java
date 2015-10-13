/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package benchmark.collection;

import javaslang.Function1;
import javaslang.collection.HashMap;
import javaslang.collection.TreeSet;
import javaslang.test.Gen;

import java.util.Iterator;
import java.util.function.Function;

import static benchmark.Benchmark.bench;

public class MapSetBench {

    private static final int MAX_TREE_SIZE = 10000;
    private static final int TREE_COUNT = 1000;
    private static final int ELEMENT_COUNT = 1_000_000;
    private static final int WARMUP = 50;

    private static final Gen<TreeSet<Integer>> treeGen = Gen.choose(0, MAX_TREE_SIZE).map(n -> {
        TreeSet<Integer> treeSet = TreeSet.empty();
        for (int i = 0; i < n; i++) {
            treeSet = treeSet.add(i);
        }
        return treeSet;
    });

    private static final Function<Integer, TreeSet<Integer>> treeCache = Function1
            .lift((Integer i) -> treeGen.get())
            .memoized();

    private static final Iterator<TreeSet<Integer>> trees = new Iterator<TreeSet<Integer>>() {

        int i = 0;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public TreeSet<Integer> next() {
            if (i++ >= TREE_COUNT - 1) {
                i = 0;
            }
            return treeCache.apply(i);
        }
    };

    static {
        // fill tree cache
        for (int i = 0; i < TREE_COUNT; i++) {
            trees.next();
        }
    }

    public static void main(String[] args) {
        benchHashSet_put();
        benchTreeSet_add();
        benchTreeSet_diff_vs_removeAll();
        benchTreeSet_intersect_vs_retainAll();
        benchTreeSet_union_vs_addAll();
    }

    static void benchHashSet_put() {
        bench("Java HashMap.put", ELEMENT_COUNT, WARMUP, new java.util.HashMap<>(), (i, map) -> {
            map.put(i, i);
            return map;
        });
        bench("HashMap.put", ELEMENT_COUNT, WARMUP, HashMap.empty(), (i, map) -> map.put(i, i));
    }

    static void benchTreeSet_add() {
        bench("Java TreeSet.add", ELEMENT_COUNT, WARMUP, new java.util.TreeSet<>(), (i, set) -> {
            set.add(i);
            return set;
        });
        bench("TreeSet.add", ELEMENT_COUNT, WARMUP, TreeSet.<Integer> empty(), (i, set) -> set.add(i));
    }

    static void benchTreeSet_diff_vs_removeAll() {
        for (int i = TREE_COUNT; i > 0; i -= TREE_COUNT / 10) {
            bench("TreeSet.diff(TreeSet)", i, 0, j -> trees.next().diff(trees.next()));
            bench("TreeSet.removeAll(TreeSet)", i, 0, j -> trees.next().removeAll(trees.next()));
        }
    }

    static void benchTreeSet_union_vs_addAll() {
        for (int i = TREE_COUNT; i > 0; i -= TREE_COUNT / 10) {
            bench("TreeSet.union(TreeSet)", i, 0, j -> trees.next().union(trees.next()));
            bench("TreeSet.addAll(TreeSet)", i, 0, j -> trees.next().addAll(trees.next()));
        }
    }

    static void benchTreeSet_intersect_vs_retainAll() {
        for (int i = TREE_COUNT; i > 0; i -= TREE_COUNT / 10) {
            bench("TreeSet.intersect(TreeSet)", i, 0, j -> trees.next().intersect(trees.next()));
            bench("TreeSet.retainAll(TreeSet)", i, 0, j -> trees.next().retainAll(trees.next()));
        }
    }
}
