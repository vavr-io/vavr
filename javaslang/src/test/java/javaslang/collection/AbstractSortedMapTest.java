/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;

public abstract class AbstractSortedMapTest extends AbstractMapTest {

    @Override
    abstract protected <K extends Comparable<? super K>, V> SortedMap<K, V> mapOf(K key, V value);

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final SortedMap<Integer, Double> int2doubleMap = mapOf(1, 1.0d);
        final SortedMap<Integer, Number> number2numberMap = SortedMap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(2, new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }
}
