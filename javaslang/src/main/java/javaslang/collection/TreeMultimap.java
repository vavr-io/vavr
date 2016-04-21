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
public final class TreeMultimap<K, V> extends AbstractMultimap<K, V, TreeMultimap<K, V>> implements Serializable {

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

        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> empty() {
            return empty((Comparator<? super K> & Serializable) K::compareTo);
        }

        public <K, V2 extends V> TreeMultimap<K, V2> empty(Comparator<? super K> keyComparator) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            return new TreeMultimap<>(TreeMap.empty(keyComparator), containerType, emptyContainer);
        }

        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V2>> entries) {
            return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
        }

        public <K, V2 extends V> TreeMultimap<K, V2> ofEntries(Comparator<? super K> keyComparator, Iterable<? extends Tuple2<? extends K, ? extends V2>> entries) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(entries, "entries is null");
            TreeMultimap<K, V2> result = empty(keyComparator);
            for (Tuple2<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry._1, entry._2);
            }
            return result;
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> ofEntries(Tuple2<? extends K, ? extends V2>... entries) {
            return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <K, V2 extends V> TreeMultimap<K, V2> ofEntries(Comparator<? super K> keyComparator, Tuple2<? extends K, ? extends V2>... entries) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(entries, "entries is null");
            TreeMultimap<K, V2> result = empty(keyComparator);
            for (Tuple2<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry._1, entry._2);
            }
            return result;
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> ofEntries(java.util.Map.Entry<? extends K, ? extends V2>... entries) {
            return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <K, V2 extends V> TreeMultimap<K, V2> ofEntries(Comparator<? super K> keyComparator, java.util.Map.Entry<? extends K, ? extends V2>... entries) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(entries, "entries is null");
            TreeMultimap<K, V2> result = empty(keyComparator);
            for (java.util.Map.Entry<? extends K, ? extends V2> entry : entries) {
                result = result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V2>> f) {
            return tabulate((Comparator<? super K> & Serializable) K::compareTo, n, f);
        }

        @SuppressWarnings("unchecked")
        public <K, V2 extends V> TreeMultimap<K, V2> tabulate(Comparator<? super K> keyComparator, int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V2>> f) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(f, "f is null");
            return ofEntries(keyComparator, Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V2>>) f));
        }

        @SuppressWarnings("unchecked")
        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V2>> s) {
            return fill((Comparator<? super K> & Serializable) K::compareTo, n, s);
        }

        @SuppressWarnings("unchecked")
        public <K, V2 extends V> TreeMultimap<K, V2> fill(Comparator<? super K> keyComparator, int n, Supplier<? extends Tuple2<? extends K, ? extends V2>> s) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(s, "s is null");
            return ofEntries(keyComparator, Collections.fill(n, (Supplier<? extends Tuple2<K, V2>>) s));
        }

        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> of(K key, V2 value) {
            return of((Comparator<? super K> & Serializable) K::compareTo, key, value);
        }

        public <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> of(Tuple2<? extends K, ? extends V2> entry) {
            return of((Comparator<? super K> & Serializable) K::compareTo, entry);
        }

        public <K, V2 extends V> TreeMultimap<K, V2> of(Comparator<? super K> keyComparator, K key, V2 value) {
            final TreeMultimap<K, V2> e = empty(keyComparator);
            return e.put(key, value);
        }

        public <K, V2 extends V> TreeMultimap<K, V2> of(Comparator<? super K> keyComparator, Tuple2<? extends K, ? extends V2> entry) {
            final TreeMultimap<K, V2> e = empty(keyComparator);
            return e.put(entry._1, entry._2);
        }

        @SuppressWarnings("unchecked")
        public final <K extends Comparable<? super K>, V2 extends V> TreeMultimap<K, V2> of(Object... pairs) {
            return of((Comparator<? super K> & Serializable) K::compareTo, pairs);
        }

        @SuppressWarnings("unchecked")
        public final <K, V2 extends V> TreeMultimap<K, V2> of(Comparator<? super K> keyComparator, Object... pairs) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            Objects.requireNonNull(pairs, "pairs is null");
            if ((pairs.length & 1) != 0) {
                throw new IllegalArgumentException("Odd length of key-value pairs list");
            }
            TreeMultimap<K, V2> result = empty(keyComparator);
            for (int i = 0; i < pairs.length; i += 2) {
                result = result.put((K) pairs[i], (V2) pairs[i + 1]);
            }
            return result;
        }

        public <K extends Comparable<? super K>, V2 extends V> Collector<Tuple2<K, V2>, ArrayList<Tuple2<K, V2>>, TreeMultimap<K, V2>> collector() {
            return collector((Comparator<? super K> & Serializable) K::compareTo);
        }

        public <K, V2 extends V> Collector<Tuple2<K, V2>, ArrayList<Tuple2<K, V2>>, TreeMultimap<K, V2>> collector(Comparator<? super K> keyComparator) {
            Objects.requireNonNull(keyComparator, "keyComparator is null");
            final Supplier<ArrayList<Tuple2<K, V2>>> supplier = ArrayList::new;
            final BiConsumer<ArrayList<Tuple2<K, V2>>, Tuple2<K, V2>> accumulator = ArrayList::add;
            final BinaryOperator<ArrayList<Tuple2<K, V2>>> combiner = (left, right) -> {
                left.addAll(right);
                return left;
            };
            final Function<ArrayList<Tuple2<K, V2>>, TreeMultimap<K, V2>> finisher = list -> ofEntries(keyComparator, list);
            return Collector.of(supplier, accumulator, combiner, finisher);
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
    public static <K, V> TreeMultimap<K, V> narrow(TreeMultimap<? extends K, ? extends V> map) {
        return (TreeMultimap<K, V>) map;
    }

    private TreeMultimap(Map<K, Traversable<V>> back, ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
        super(back, containerType, emptyContainer);
    }

    @Override
    protected <K2, V2> Map<K2, V2> emptyMapSupplier() {
        return TreeMap.empty(Comparators.naturalComparator());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <K2, V2> TreeMultimap<K2, V2> emptyInstance() {
        return new TreeMultimap<>(emptyMapSupplier(), getContainerType(), emptyContainer);
    }

    @Override
    protected <K2, V2> TreeMultimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back) {
        return new TreeMultimap<>(back, getContainerType(), emptyContainer);
    }

}
