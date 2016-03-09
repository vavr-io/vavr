/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class HashMultimap<K, V> extends AbstractMultimap<K, V, HashMultimap<K, V>> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static HashMultimap<?, ?> EMPTY_SET = new HashMultimap<>(HashMap.empty(), setFactory(), MapSupplier.HASH_MAP, ContainerSupplier.SET);
    private final static HashMultimap<?, ?> EMPTY_SEQ = new HashMultimap<>(HashMap.empty(), seqFactory(), MapSupplier.HASH_MAP, ContainerSupplier.SEQ);

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V> result = HashMultimap.emptyWithSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return result;
    }

    @SafeVarargs
    public static <K, V> HashMultimap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V> result = HashMultimap.emptyWithSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return result;
    }

    @SafeVarargs
    public static <K, V> HashMultimap<K, V> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V> result = HashMultimap.emptyWithSet();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            result = result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntries(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntries(Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> of(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        HashMultimap<K, V> result = HashMultimap.emptyWithSet();
        for (int i = 0; i < pairs.length; i += 2) {
            result = result.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return result;
    }

    private static Factory setFactory() {
        return new Factory() {

            @SuppressWarnings("unchecked")
            @Override
            public <K2, V2> Multimap<K2, V2> emptyInstance() {
                return (Multimap<K2, V2>) EMPTY_SET;
            }

            @Override
            public <K, V> HashMultimap<K, V> createFromMap(Map<K, Traversable<V>> back) {
                return new HashMultimap<>(back, this, MapSupplier.HASH_MAP, ContainerSupplier.SET);
            }
        };
    }

    private static Factory seqFactory() {
        return new Factory() {

            @SuppressWarnings("unchecked")
            @Override
            public <K2, V2> Multimap<K2, V2> emptyInstance() {
                return (HashMultimap<K2, V2>) EMPTY_SEQ;
            }

            @Override
            public <K, V> HashMultimap<K, V> createFromMap(Map<K, Traversable<V>> back) {
                return new HashMultimap<>(back, this, MapSupplier.HASH_MAP, ContainerSupplier.SEQ);
            }
        };
    }

    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, HashMultimap<K, V>> collector() {
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Tuple2<K, V>>, HashMultimap<K, V>> finisher = HashMultimap::ofEntries;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY_SET : this;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> emptyWithSet() {
        return (HashMultimap<K, V>) EMPTY_SET;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V> emptyWithSeq() {
        return (HashMultimap<K, V>) EMPTY_SEQ;
    }

    private HashMultimap(Map<K, Traversable<V>> back, Factory factory, MapSupplier mapSupplier, ContainerSupplier container) {
        super(back, factory, mapSupplier, container);
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        final java.util.Map<K, Collection<V>> javaMap = new java.util.HashMap<>();
        for (Tuple2<K, V> t : this) {
            javaMap.computeIfAbsent(t._1, k -> new java.util.HashSet<>()).add(t._2);
        }
        return javaMap;
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }
}
