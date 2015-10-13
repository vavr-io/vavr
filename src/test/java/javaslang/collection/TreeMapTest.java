/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.Map.Entry;

import java.util.ArrayList;
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
    protected <T> Collector<Entry<Integer, T>, ArrayList<Entry<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return TreeMap.<Integer, T> collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOf(Entry<? extends K, ? extends V>... entries) {
        return TreeMap.of(naturalComparator(), entries);
    }

    // -- obsolete tests

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // The empty TreeMap encapsulates a comparator and therefore cannot be a singleton
    }
}
