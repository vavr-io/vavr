/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;


import static javaslang.collection.Comparators.naturalComparator;

import java.util.ArrayList;
import java.util.stream.Collector;

import org.junit.Assert;
import org.junit.Test;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

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
    protected <K extends Comparable<? super K>, V> Map<K, V> of(K key, V value) {
        return TreeMap.of(key, value);
    }

    @Test
    public void shouldScan() {
        TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        TreeMap<String, Integer> result = tm.scan(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        Option<Integer> value = result.get("z");
        Assert.assertTrue(value.isDefined());
        Assert.assertEquals(0, value.get().intValue());
        
        value = result.get("zone");
        Assert.assertTrue(value.isDefined());
        Assert.assertEquals(1, value.get().intValue());

        
        value = result.get("zonetwo");
        Assert.assertTrue(value.isDefined());
        Assert.assertEquals(3, value.get().intValue());
    }
    
    @Test
    public void shouldScanLeft() {
        TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        List<Tuple2<String, Integer>> result = tm.scanLeft(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        
        Assert.assertEquals(Tuple.of("z", 0), result.head());
        result = result.tail();
        Assert.assertEquals(Tuple.of("zone", 1), result.head());
        result = result.tail();
        Assert.assertEquals(Tuple.of("zonetwo", 3), result.head());        
    }

    @Test
    public void shouldScanRight() {
        TreeMap<String, Integer> tm = TreeMap.ofAll(Tuple.of("one", 1), Tuple.of("two", 2));
        List<String> result = tm.scanRight("z", (t1, acc) -> acc + CharSeq.of(t1._1).reverse());
        Assert.assertEquals("zowteno", result.head());
        result = result.tail();
        Assert.assertEquals("zowt", result.head());
        result = result.tail();
        Assert.assertEquals("z", result.head());
    }
    
    @Test
    public void test() {
        TreeMap<String, Integer> tm = TreeMap.of(Tuple.of("one", 1));
        System.out.println(tm.contains(Tuple.of("one", 0)));
    }
    
    // -- obsolete tests

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // The empty TreeMap encapsulates a comparator and therefore cannot be a singleton
    }
}
