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

    static <K, V> int size(Map<K, Traversable<V>> back) {
        return back.foldLeft(0, (s, t) -> s + t._2.size());
    }

    static <K, V> java.util.Map<K, Collection<V>> toJavaMap(Multimap<K, V> multimap) {
        final java.util.Map<K, Collection<V>> javaMap = new java.util.HashMap<>();
        final Supplier<Collection<V>> javaContainerSupplier;
        final ContainerType containerType = multimap.getContainerType();
        if (containerType == ContainerType.SEQ) {
            javaContainerSupplier = java.util.ArrayList::new;
        } else if (containerType == ContainerType.SET) {
            javaContainerSupplier = java.util.HashSet::new;
        } else if (containerType == ContainerType.SORTED_SET) {
            javaContainerSupplier = java.util.TreeSet::new;
        } else {
            throw new IllegalStateException("Unknown ContainerType: " + containerType);
        }
        for (Tuple2<K, V> t : multimap) {
            javaMap.computeIfAbsent(t._1, k -> javaContainerSupplier.get()).add(t._2);
        }
        return javaMap;
    }

    abstract static class Builder<V> {

        private final ContainerType containerType;
        private final Traversable<V> emptyContainer;

        protected Builder(ContainerType containerType, Traversable<V> emptyContainer) {
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

        // Returns the empty Map used as back for Multimap
        abstract <K, V2 extends V> Map<K, Traversable<V2>> emptyMap();

        // Implementations must override the return type. Typically passed to `build()` using `this::ofMap`.
        abstract <K, V2 extends V> Multimap<K, V2> ofMap(Map<K, Traversable<V2>> back);

        // Use by factory methods. Automatically casts to Multimap type.
        final <T, K, V2 extends V, M extends Multimap<K, V2>> M build(
                Iterable<T> entries,
                Function<T, K> getKey,
                Function<T, V2> getValue,
                OfMap<K, V2, M> ofMap) {
            Objects.requireNonNull(entries, "entries is null");
            Map<K, Traversable<V2>> back = emptyMap();
            for (T entry : entries) {
                final K key = getKey.apply(entry);
                final V2 value = getValue.apply(entry);
                // TODO(#1543): replace with `back.getOrElse(key, emptyContainer());`
                final Traversable<V2> container = back.containsKey(key) ? back.get(key).get() : emptyContainer();
                back = back.put(key, containerType.add(container, value));
            }
            return ofMap.apply(back);
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

        static class Entries<K, V> implements Iterator<Object[]> {

            int index = 0;
            Object[] pairs;

            Entries(Object[] pairs) {
                Objects.requireNonNull(pairs, "pairs is null");
                if ((pairs.length & 1) != 0) {
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
        }
    }

    @FunctionalInterface
    interface OfEntries<K, V, M extends Multimap<K, V>> extends Function<Iterable<Tuple2<K, V>>, M> {
    }

    @FunctionalInterface
    interface OfMap<K, V, M extends Multimap<K, V>> extends Function<Map<K, Traversable<V>>, M> {
    }
}
