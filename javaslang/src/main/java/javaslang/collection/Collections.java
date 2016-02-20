/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang contributors
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
                final V newValue = map.get(key).map(v -> (V) collisionResolution.apply(v, value)).getOrElse(value);
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
        final Iterator<? extends T> reversedElements = seq(elements).reverseIterator();
        return scanLeft(reversedElements, zero, (u, t) -> operation.apply(t, u), cumulativeResult, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    static <T, S extends Seq<T>> Iterator<S> crossProduct(S empty, S seq, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("negative power");
        }
        return Iterator
                .range(0, power)
                .foldLeft(Iterator.of(empty), (product, ignored) -> product.flatMap(el -> seq.map(t -> (S) el.append(t))));
    }

    static <C extends Traversable<T>, T> C tabulate(int n, Function<? super Integer, ? extends T> f, C empty, Function<T[], C> of) {
        Objects.requireNonNull(f, "f is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        if (n <= 0) {
            return empty;
        } else {
            @SuppressWarnings("unchecked")
            T[] elements = (T[]) new Object[n];
            for (int i = 0; i < n; i++) {
                elements[i] = f.apply(i);
            }
            return of.apply(elements);
        }
    }

    static <C extends Traversable<T>, T> C fill(int n, Supplier<? extends T> s, C empty, Function<T[], C> of) {
        Objects.requireNonNull(s, "s is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        return tabulate(n, anything -> s.get(), empty, of);
    }

    static <T> Iterator<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        if (n <= 0) {
            return Iterator.empty();
        } else {
            return new AbstractIterator<T>() {

                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < n;
                }

                @Override
                protected T getNext() {
                    return f.apply(i++);
                }
            };
        }
    }

    static <T> Iterator<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return tabulate(n, anything -> s.get());
    }

    @SuppressWarnings("unchecked")
    private static <T> Seq<T> seq(Iterable<? extends T> iterable) {
        if (iterable instanceof Seq) {
            return (Seq<T>) iterable;
        } else {
            return List.ofAll(iterable);
        }
    }
}
