/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;


import javaslang.Tuple;
import javaslang.Tuple2;
import org.junit.Test;

import java.util.*;
import java.util.HashMap;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;

public class TreeMapTest extends AbstractMapTest {

    @Override
    protected String className() {
        return "TreeMap";
    }

    @Override
    protected <T1, T2> Map<T1, T2> emptyMap() {
        return TreeMap.empty(naturalComparator());
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return TreeMap.<Integer, T> collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOf(Tuple2<? extends K, ? extends V>... entries) {
        return TreeMap.ofAll(naturalComparator(), entries);
    }

    @Override
    protected <K extends Comparable<? super K>, V> Map<K, V> mapOf(K key, V value) {
        return TreeMap.of(key, value);
    }

    @Test
    public void shouldScan() {
        final TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        final TreeMap<String, Integer> result = tm.scan(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(TreeMap.ofAll(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanLeft() {
        final TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        final Seq<Tuple2<String, Integer>> result = tm.scanLeft(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(List.of(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanRight() {
        final TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        final Seq<String> result = tm.scanRight("z", (t1, acc) -> acc + CharSeq.of(t1._1).reverse());
        assertThat(result).isEqualTo(List.of("zowteno", "zowt", "z"));
    }

    @Test
    public void shouldWrapMap() {
        java.util.Map<Integer, Integer> source = new HashMap<>();
        source.put(1, 2);
        source.put(3, 4);
        assertThat(TreeMap.ofAll(source)).isEqualTo(emptyIntInt().put(1, 2).put(3, 4));
    }

    // -- obsolete tests

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // The empty TreeMap encapsulates a comparator and therefore cannot be a singleton
    }
}
