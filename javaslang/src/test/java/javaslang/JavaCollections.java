/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.*;

public interface JavaCollections {

    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> javaMap(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        final Map<K, V> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    static <T> Set<T> javaSet(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Set<T> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }
}
