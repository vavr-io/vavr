/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.ArrayList;
import java.util.stream.Collector;

import org.junit.Assert;
import org.junit.Test;

import javaslang.Tuple;
import javaslang.Tuple2;

public class HashMapTest extends AbstractMapTest {

    @Override
    protected String className() {
        return "HashMap";
    }

    @Override
    protected <T1, T2> Map<T1, T2> emptyMap() {
        return HashMap.empty();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return HashMap.<Integer, T>collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Map<K, V> mapOf(Tuple2<? extends K, ? extends V>... entries) {
        return HashMap.ofAll(entries);
    }

    @Override
    protected <K  extends Comparable<? super K>, V> Map<K, V> of(K key, V value) {
        return HashMap.of(key, value);
    }

}
