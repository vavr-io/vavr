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
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An {@link HashMap}-based implementation of {@link Multimap}
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 * @since 2.0.0
 */
public class HashMultimap<K, V> extends AbstractMultimap<K, V, HashMultimap<K, V>> implements Serializable {

    private static final long serialVersionUID = 1L;

    static <K, V> HashMultimap<K, V> emptyWithSeq() {
        return new HashMultimap<>(HashMap.empty(), ContainerType.SEQ, List::empty);
    }

    static <K, V> HashMultimap<K, V> emptyWithSet() {
        return new HashMultimap<>(HashMap.empty(), ContainerType.SET, HashSet::empty);
    }

    static <K, V extends Comparable<? super V>> HashMultimap<K, V> emptyWithSortedSet() {
        return new HashMultimap<>(HashMap.empty(), ContainerType.SORTED_SET, TreeSet::empty);
    }

    static <K, V> HashMultimap<K, V> emptyWithSortedSet(Comparator<? super K> comparator) {
        return new HashMultimap<>(HashMap.empty(), ContainerType.SORTED_SET, () -> TreeSet.empty(comparator));
    }

    /**
     * Creates a {@link HashMultimap} of the given entries with {@link Seq} as values container.
     *
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    static <K, V> HashMultimap<K, V> ofEntriesWithSeq(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSeq();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries with {@link Set} as values container.
     *
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    static <K, V> HashMultimap<K, V> ofEntriesWithSet(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries with {@link SortedSet} as values container.
     *
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    static <K, V extends Comparable<? super V>> HashMultimap<K, V> ofEntriesWithSortedSet(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSortedSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    static <K, V> HashMultimap<K, V> ofEntriesWithSortedSet(Comparator<? super K> comparator, Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSortedSet(comparator);
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries with {@link Seq} as values container.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    @SafeVarargs
    static <K, V> HashMultimap<K, V> ofEntriesWithSeq(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSeq();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries with {@link Seq} as values container.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    @SafeVarargs
    static <K, V> HashMultimap<K, V> ofEntriesWithSeq(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = emptyWithSeq();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            result = result.put(entry.getKey(), entry.getValue());
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Returns an {@link HashMultimap} containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1} with {@link Seq} as values container.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the Multimap
     * @param f   The Function computing element values
     * @return An Multimap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> tabulateWithSeq(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntriesWithSeq(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    /**
     * Returns an {@link HashMultimap} containing {@code n} values supplied by a given Supplier {@code s} with {@link Seq} as values container.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n        The number of elements in the Multimap
     * @param s        The Supplier computing element values
     * @return An Multimap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> fillWithSeq(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntriesWithSeq(Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    /**
     * Creates a {@link HashMultimap} of the given list of key-value pairs with {@link Seq} as values container.
     *
     * @param <K>   The key type
     * @param <V>   The value type
     * @param pairs A list of key-value pairs
     * @return A new {@link Multimap} containing the given entries
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> withSeq(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        Multimap<K, V> result = emptyWithSeq();
        for (int i = 0; i < pairs.length; i += 2) {
            result = result.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link Multimap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link Multimap} Collector.
     */
    static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collectorWithSeq() {
        return collector(HashMultimap::ofEntriesWithSeq);
    }

    static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collectorWithSet() {
        return collector(HashMultimap::ofEntriesWithSet);
    }

    static <K, V extends Comparable<? super V>> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collectorWithSortedSet() {
        return collector(HashMultimap::ofEntriesWithSortedSet);
    }

    static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collectorWithSortedSet(Comparator<? super K> comparator) {
        return collector(entries -> HashMultimap.ofEntriesWithSortedSet(comparator, entries));
    }

    private static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collector(Function<ArrayList<Tuple2<K, V>>, Multimap<K, V>> finisher) {
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        return Collector.of(supplier, accumulator, combiner, finisher);
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
    static <K, V> HashMultimap<K, V> narrow(HashMultimap<? extends K, ? extends V> map) {
        return (HashMultimap<K, V>) map;
    }

    private HashMultimap(Map<K, Traversable<V>> back, ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
        super(back, containerType, emptyContainer);
    }

    @Override
    <K2, V2> Map<K2, V2> emptyMapSupplier() {
        return HashMap.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    <K2, V2> HashMultimap<K2, V2> emptyInstance() {
        return new HashMultimap<>(HashMap.empty(), getContainerType(), emptyContainer);
    }

    @Override
    <K2, V2> HashMultimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back) {
        return new HashMultimap<>(back, getContainerType(), emptyContainer);
    }

    @Override
    public HashMultimap<K, V> distinct() {
        return this;
    }
}
