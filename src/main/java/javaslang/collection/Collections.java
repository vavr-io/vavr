/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Internal class, containing helpers.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class Collections {

    static <K, V> Map<K, V> mergeMaps(Map<K, V> map1, Map<? extends K, ? extends V> map2,
                                      Function<Map<? extends K, ? extends V>, Map<K, V>> converter) {
        Objects.requireNonNull(map2, "that is null");
        if (map1.isEmpty()) {
            return converter.apply(map2);
        } else if (map2.isEmpty()) {
            return map1;
        } else {
            return map2.foldLeft(map1, (map, entry) -> !map.containsKey(entry._1) ? map.put(entry) : map);
        }
    }

    static <K, V, U extends V> Map<K, V> mergeMaps(Map<K, V> map1, Map<? extends K, U> map2,
                                                   Function<Map<? extends K, U>, Map<K, V>> converter,
                                                   BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        Objects.requireNonNull(map2, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (map1.isEmpty()) {
            return converter.apply(map2);
        } else if (map2.isEmpty()) {
            return map1;
        } else {
            return map2.foldLeft(map1, (map, entry) -> {
                final K key = entry._1;
                final U value = entry._2;
                final V newValue = map.get(key).map(v -> (V) collisionResolution.apply(v, value)).orElse(value);
                return map.put(key, newValue);
            });
        }
    }

    static <T, U, C extends Iterable<U>, R extends Traversable<U>> R scanLeft(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super U, ? super T, ? extends U> operation,
            C cumulativeResult, BiFunction<C, U, C> combiner, Function<C, R> finisher) {
        U acc = zero;
        cumulativeResult = combiner.apply(cumulativeResult, acc);
        for (T a : elements) {
            acc = operation.apply(acc, a);
            cumulativeResult = combiner.apply(cumulativeResult, acc);
        }
        return finisher.apply(cumulativeResult);
    }

    static <T, U, C extends Iterable<U>, R extends Traversable<U>> R scanRight(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super T, ? super U, ? extends U> operation,
            C cumulativeResult, BiFunction<C, U, C> combiner, Function<C, R> finisher) {
        final Iterator<? extends T> reversedElements = Seq.ofAll(elements).reverseIterator();
        return scanLeft(reversedElements, zero, (u, t) -> operation.apply(t, u), cumulativeResult, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    static <T> Iterator<Seq<T>> crossProduct(Seq<? extends T> seq, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("negative power");
        }
        return Iterator
                .range(1, power)
                .foldLeft((Iterator<Seq<T>>) seq.sliding(1), (product, ignored) -> product.flatMap(tuple -> seq.map(tuple::append)));
    }
}
