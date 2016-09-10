/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Multimap.ContainerType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Implementations of common {@code Map} functions (not intended to be public).
 *
 * @author Ruslan Sennov, Daniel Dietrich
 * @since 2.1.0
 */
final class Multimaps {

    static <K, V, K2, V2, M extends Multimap<K2, V2>> M bimap(
            Multimap<K, V> multimap,
            OfEntries<K2, V2, M> ofEntries,
            Function<? super K, ? extends K2> keyMapper,
            Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = multimap.iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return ofEntries.apply(entries);
    }

    static <K, V, K2, V2, M extends Multimap<K2, V2>> M map(Multimap<K, V> multimap, M emptyInstance, BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return multimap.foldLeft(emptyInstance, (acc, entry) -> acc.put(mapper.apply(entry._1, entry._2)));
    }

    static <K, V, V2, M extends Multimap<K, V2>> M mapValues(Multimap<K, V> multimap, M emptyInstance, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map(multimap, emptyInstance, (k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    static <K, V> int size(Map<K, Traversable<V>> delegate) {
        return delegate.foldLeft(0, (s, t) -> s + t._2.size());
    }

    static <K, V> java.util.Map<K, Collection<V>> toJavaMap(Multimap<K, V> multimap) {
        final java.util.Map<K, Collection<V>> javaMap = new java.util.HashMap<>();
        final Supplier<Collection<V>> javaContainerSupplier = multimap.getContainerType().getJavaContainerSupplier();
        for (Tuple2<K, V> t : multimap) {
            javaMap.computeIfAbsent(t._1, k -> javaContainerSupplier.get()).add(t._2);
        }
        return javaMap;
    }

    /**
     * An abstract Multimap Builder that extracts common logic and defines a common builder interface for
     * improved maintainability.
     *
     * @param <V> Value type
     */
    abstract static class Builder<V> {

        private final ContainerType containerType;
        private final Traversable<V> emptyContainer;

        Builder(ContainerType containerType, Traversable<V> emptyContainer) {
            this.containerType = containerType;
            this.emptyContainer = emptyContainer;
        }

        public abstract <K, V2 extends V> Multimap<K, V2> empty();

        public abstract <K, V2 extends V> Multimap<K, V2> of(Object... pairs);

        public abstract <K, V2 extends V> Multimap<K, V2> of(K key, V2 value);

        public abstract <K, V2 extends V> Multimap<K, V2> of(Tuple2<? extends K, ? extends V2> entry);

        public abstract <K, V2 extends V> Multimap<K, V2> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V2>> entries);

        @SuppressWarnings("unchecked")
        public abstract <K, V2 extends V> Multimap<K, V2> ofEntries(Tuple2<? extends K, ? extends V2>... entries);

        @SuppressWarnings("unchecked")
        public abstract <K, V2 extends V> Multimap<K, V2> ofEntries(java.util.Map.Entry<? extends K, ? extends V2>... entries);

        public abstract <K, V2 extends V> Multimap<K, V2> fill(int n, Supplier<Tuple2<? extends K, ? extends V2>> supplier);

        public abstract <K, V2 extends V> Multimap<K, V2> tabulate(int n, Function<? super Integer, Tuple2<? extends K, ? extends V2>> f);

        public abstract <K, V2 extends V> Collector<Tuple2<K, V2>, ArrayList<Tuple2<K, V2>>, ? extends Multimap<K, V2>> collector();

        // -- Non-public, internal API. Builder implementations should no increase visibility.

        // Returns the empty Map used as delegate for Multimap
        abstract <K, V2 extends V> Map<K, Traversable<V2>> emptyMap();

        // Implementations must override the return type. Typically passed to `build()` using `this::ofMap`.
        abstract <K, V2 extends V> Multimap<K, V2> ofMap(Map<K, Traversable<V2>> delegate);

        // Use by factory methods. Automatically casts to Multimap type.
        final <T, K, V2 extends V, M extends Multimap<K, V2>> M build(
                Iterable<T> entries,
                Function<T, K> getKey,
                Function<T, V2> getValue,
                OfMap<K, V2, M> ofMap) {
            Objects.requireNonNull(entries, "entries is null");
            // TODO: this can be further optimized by building backing structures directly (HAMT or RedBlackTree)
            Map<K, Traversable<V2>> delegate = emptyMap();
            for (T entry : entries) {
                final K key = getKey.apply(entry);
                final V2 value = getValue.apply(entry);
                // TODO(#1543): replace with `delegate.getOrElse(key, emptyContainer());`
                final Traversable<V2> container = delegate.containsKey(key) ? delegate.get(key).get() : emptyContainer();
                delegate = delegate.put(key, containerType.add(container, value));
            }
            return ofMap.apply(delegate);
        }

        ContainerType containerType() {
            return containerType;
        }

        // Notes on type-safety of widening the generic argument of the empty container:
        // - This should be ok for empty instances because no breaking up-casts take place.
        // - Also a Comparator should be reusable that is covariant in its argument type.
        @SuppressWarnings("unchecked")
        <V2 extends V> Traversable<V2> emptyContainer() {
            return (Traversable<V2>) emptyContainer;
        }

        /**
         * An entry iterator that operates on untyped arrays of key-value pairs.
         *
         * @param <K> Key type
         * @param <V> Value type
         */
        static class Entries<K, V> implements Iterator<Object[]> {

            int index = 0;
            Object[] pairs;

            Entries(Object[] pairs) {
                Objects.requireNonNull(pairs, "pairs is null");
                if (!hasEvenSize(pairs)) {
                    throw new IllegalArgumentException("Odd length of key-value pairs");
                }
                this.pairs = pairs;
            }

            @Override
            public boolean hasNext() {
                return index < pairs.length;
            }
            @Override
            public Object[] next() {
                index += 2;
                return pairs;
            }

            @SuppressWarnings("unchecked")
            K getKey(Object[] pair) {
                return (K) pair[index - 2];
            }

            @SuppressWarnings("unchecked")
            V getValue(Object[] pair) {
                return (V) pair[index - 1];
            }

            static boolean hasEvenSize(Object[] pairs) { return (pairs.length & 1) == 0; }
        }
    }

    /**
     * Represents a function that creates a Multimap of an Iterable of key-value pairs.
     * <p>
     * This interface allows us to abstract over Multimap operations by moving creational logic into the Multimap
     * implementations.
     *
     * @param <K> Key type
     * @param <V> Value type
     * @param <M> Multimap type
     */
    @FunctionalInterface
    interface OfEntries<K, V, M extends Multimap<K, V>> extends Function<Iterable<Tuple2<K, V>>, M> {
    }

    /**
     * Represents a function that transforms a Map that has multiple values into a Multimap.
     * <p>
     * This interface allows us to abstract over Multimap operations by moving creational logic into the Multimap
     * implementations.
     *
     * @param <K> Key type
     * @param <V> Value type
     * @param <M> Multimap type
     */
    @FunctionalInterface
    interface OfMap<K, V, M extends Multimap<K, V>> extends Function<Map<K, Traversable<V>>, M> {
    }
}
