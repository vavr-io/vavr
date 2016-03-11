/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.util.ArrayList;
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
 * @since 2.0.0
 */
public class HashMultimap<K, V> extends AbstractMultimap<K, V, HashMultimap<K, V>> {

    private static final long serialVersionUID = 1L;

    private static final java.util.Map<ContainerType, HashMultimap<?, ?>> EMPTIES;

    static {
        EMPTIES = new java.util.HashMap<>();
        for (ContainerType type : ContainerType.values()) {
            EMPTIES.put(type, new HashMultimap<>(HashMap.empty(), type));
        }
    }

    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> empty(ContainerType containerType) {
        Objects.requireNonNull(containerType, "containerType is null");
        return (HashMultimap<K, V>) EMPTIES.get(containerType);
    }

    /**
     * Creates a {@link HashMultimap} of the given entries.
     *
     * @param containerType  The type of containers
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    static <K, V> HashMultimap<K, V> ofEntries(ContainerType containerType,
                                               Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = empty(containerType);
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param containerType  The type of containers
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    @SafeVarargs
    static <K, V> HashMultimap<K, V> ofEntries(ContainerType containerType,
                                           Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = empty(containerType);
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Creates a {@link HashMultimap} of the given entries.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param containerType  The type of containers
     * @param entries Multimap entries
     * @return A new Multimap containing the given entries
     */
    @SafeVarargs
    static <K, V> HashMultimap<K, V> ofEntries(ContainerType containerType,
                                           java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        Multimap<K, V> result = empty(containerType);
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            result = result.put(entry.getKey(), entry.getValue());
        }
        return (HashMultimap<K, V>) result;
    }

    /**
     * Returns an {@link HashMultimap} containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param containerType  The type of containers
     * @param n   The number of elements in the Multimap
     * @param f   The Function computing element values
     * @return An Multimap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> tabulate(ContainerType containerType,
                                          int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntries(containerType, Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    /**
     * Returns an {@link HashMultimap} containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param containerType  The type of containers
     * @param n        The number of elements in the Multimap
     * @param s        The Supplier computing element values
     * @return An Multimap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> fill(ContainerType containerType,
                                      int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntries(containerType, Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    /**
     * Creates a {@link HashMultimap} of the given list of key-value pairs.
     *
     * @param <K>   The key type
     * @param <V>   The value type
     * @param pairs A list of key-value pairs
     * @param containerType  The type of containers
     * @return A new {@link Multimap} containing the given entries
     */
    @SuppressWarnings("unchecked")
    static <K, V> HashMultimap<K, V> of(ContainerType containerType,
                                    Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        Multimap<K, V> result = empty(containerType);
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
    static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, Multimap<K, V>> collector(ContainerType containerType) {
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Tuple2<K, V>>, Multimap<K, V>> finisher = entries -> ofEntries(containerType, entries);
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

    private HashMultimap(Map<K, Traversable<V>> back, ContainerType containerType) {
        super(back, containerType);
    }

    @Override
    <K2, V2> Map<K2, V2> emptyMapSupplier() {
        return HashMap.empty();
    }

    @Override
    <K2, V2> HashMultimap<K2, V2> emptyInstance() {
        return HashMultimap.empty(getContainerType());
    }

    @Override
    <K2, V2> HashMultimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back) {
        return new HashMultimap<>(back, getContainerType());
    }

    @Override
    public HashMultimap<K, V> distinct() {
        return this;
    }
}
