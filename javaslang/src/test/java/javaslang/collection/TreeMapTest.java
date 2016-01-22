/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;


import javaslang.Tuple;
import javaslang.Tuple2;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;
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
    protected boolean emptyMapShouldBeSingleton() {
        return false;
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return TreeMap.<Integer, T> collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(naturalComparator(), entries);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(naturalComparator(), entries);
    }

    @Override
    protected <K, V> Map<K, V> mapOfPairs(Object... pairs) {
        return TreeMap.of(pairs);
    }

    @Override
    protected <K extends Comparable<? super K>, V> Map<K, V> mapOf(K key, V value) {
        return TreeMap.of(key, value);
    }

    @Override
    protected <K, V> TreeMap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        return TreeMap.tabulate(toStringComparator(), n, f);
    }

    @Override
    protected <K, V> TreeMap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        return TreeMap.fill(toStringComparator(), n, s);
    }

    @Test
    public void shouldScan() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
        final TreeMap<String, Integer> result = tm.scan(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(TreeMap.ofEntries(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanLeft() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
        final Seq<Tuple2<String, Integer>> result = tm.scanLeft(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(List.of(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanRight() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
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

    private static Comparator<Object> toStringComparator() { // moveup
        return (Comparator<Object> & Serializable) (o1, o2) -> String.valueOf(o1).compareTo(String.valueOf(o2));
    }
}
