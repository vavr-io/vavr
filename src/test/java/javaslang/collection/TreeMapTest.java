/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.Map.Entry;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeMapTest {

    @Test
    public void shouldFlatMapUsingBiFunction() {
        final TreeMap<Integer, Integer> testee = TreeMap.of($(1, 11), $(2, 22), $(3, 33));
        final TreeMap<String, String> actual = testee.flatMap((k, v) -> List.of($(String.valueOf(k), String.valueOf(v)), $(String.valueOf(k * 10), String.valueOf(v * 10))));
        final TreeMap<String, String> expected = TreeMap.of($("1", "11"), $("10", "110"), $("2", "22"), $("20", "220"), $("3", "33"), $("30", "330"));
        assertThat(actual).isEqualTo(expected);

    }

    static <K, V> Entry<K, V> $(K key, V value) {
        return new Map.Entry<>(key, value);
    }
}
