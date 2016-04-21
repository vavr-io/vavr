/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * An {@link HashMap}-based implementation of {@link Multimap}
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 * @since 2.1.0
 */
public final class HashMultimap<K, V> extends AbstractMultimap<K, V, HashMultimap<K, V>> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static <V> Builder<V> withSeq() {
        return new Builder<>(ContainerType.SEQ, List::empty);
    }

    public static <V> Builder<V> withSet() {
        return new Builder<>(ContainerType.SET, HashSet::empty);
    }

    public static <V extends Comparable<? super V>> Builder<V> withSortedSet() {
        return new Builder<>(ContainerType.SORTED_SET, TreeSet::empty);
    }

    public static <V> Builder<V> withSortedSet(Comparator<? super V> comparator) {
        return new Builder<>(ContainerType.SORTED_SET, () -> TreeSet.empty(comparator));
    }

    public static class Builder<V> {

        private final ContainerType containerType;
        private final SerializableSupplier<Traversable<?>> emptyContainer;

        private Builder(ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
            this.containerType = containerType;
            this.emptyContainer = emptyContainer;
        }

        public <K, V2 extends V> HashMultimap<K, V2> empty() {
            return new HashMultimap<>(HashMap.empty(), containerType, emptyContainer);
        }

        public <K, V2 extends V> HashMultimap<K, V2> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V2>> entries) {
            Objects.requireNonNull(entries, "entries is null");
            HashMultimap<K, V2> result = empty();
            for (Tuple2<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry._1, entry._2);
            }
            return result;
        }

        @SafeVarargs
        public final <K, V2 extends V> HashMultimap<K, V2> ofEntries(Tuple2<? extends K, ? extends V2>... entries) {
            Objects.requireNonNull(entries, "entries is null");
            HashMultimap<K, V2> result = empty();
            for (Tuple2<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry._1, entry._2);
            }
            return result;
        }

        @SafeVarargs
        public final <K, V2 extends V> HashMultimap<K, V2> ofEntries(java.util.Map.Entry<? extends K, ? extends V2>... entries) {
            Objects.requireNonNull(entries, "entries is null");
            HashMultimap<K, V2> result = empty();
            for (java.util.Map.Entry<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        public <K, V2 extends V> HashMultimap<K, V2> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V2>> f) {
            Objects.requireNonNull(f, "f is null");
            return ofEntries(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V2>>) f));
        }

        @SuppressWarnings("unchecked")
        public <K, V2 extends V> HashMultimap<K, V2> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V2>> s) {
            Objects.requireNonNull(s, "s is null");
            return ofEntries(Collections.fill(n, (Supplier<? extends Tuple2<K, V2>>) s));
        }

        public <K, V2 extends V> HashMultimap<K, V2> of(K key, V2 value) {
            final HashMultimap<K, V2> e = empty();
            return e.put(key, value);
        }

        public <K, V2 extends V> HashMultimap<K, V2> of(Tuple2<? extends K, ? extends V2> entry) {
            final HashMultimap<K, V2> e = empty();
            return e.put(entry._1, entry._2);
        }

        @SuppressWarnings("unchecked")
        public <K, V2 extends V> HashMultimap<K, V2> of(Object... pairs) {
            Objects.requireNonNull(pairs, "pairs is null");
            if ((pairs.length & 1) != 0) {
                throw new IllegalArgumentException("Odd length of key-value pairs list");
            }
            HashMultimap<K, V2> result = empty();
            for (int i = 0; i < pairs.length; i += 2) {
                result = result.put((K) pairs[i], (V2) pairs[i + 1]);
            }
            return result;
        }

        public <K, V2 extends V> Collector<Tuple2<K, V2>, ArrayList<Tuple2<K, V2>>, Multimap<K, V2>> collector() {
            final Supplier<ArrayList<Tuple2<K, V2>>> supplier = ArrayList::new;
            final BiConsumer<ArrayList<Tuple2<K, V2>>, Tuple2<K, V2>> accumulator = ArrayList::add;
            final BinaryOperator<ArrayList<Tuple2<K, V2>>> combiner = (left, right) -> {
                left.addAll(right);
                return left;
            };
            return Collector.of(supplier, accumulator, combiner, this::ofEntries);
        }
    }

    /**
     * Narrows a widened {@code HashMultimap<? extends K, ? extends V>} to {@code HashMultimap<K, V>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param map A {@code Map}.
     * @param <K> Key type
     * @param <V> Value type
     * @return the given {@code multimap} instance as narrowed type {@code Multimap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> narrow(HashMultimap<? extends K, ? extends V> map) {
        return (HashMultimap<K, V>) map;
    }

    private HashMultimap(Map<K, Traversable<V>> back, ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
        super(back, containerType, emptyContainer);
    }

    @Override
    protected <K2, V2> Map<K2, V2> emptyMapSupplier() {
        return HashMap.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <K2, V2> HashMultimap<K2, V2> emptyInstance() {
        return new HashMultimap<>(HashMap.empty(), getContainerType(), emptyContainer);
    }

    @Override
    protected <K2, V2> HashMultimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back) {
        return new HashMultimap<>(back, getContainerType(), emptyContainer);
    }

}
