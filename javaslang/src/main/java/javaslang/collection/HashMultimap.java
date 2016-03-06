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

public final class HashMultimap<K, V, T extends Traversable<V>> extends MultimapImpl<K, V, T, HashMultimap<K, V, T>> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static HashMultimap<?, ?, ?> EMPTY_SET = new HashMultimap<>(HashMap.empty(), setFactory());
    private final static HashMultimap<?, ?, ?> EMPTY_SEQ = new HashMultimap<>(HashMap.empty(), seqFactory());

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Set<V>> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V, Set<V>> result = HashMultimap.emptyWithSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return result;
    }

    @SafeVarargs
    public static <K, V> HashMultimap<K, V, Set<V>> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V, Set<V>> result = HashMultimap.emptyWithSet();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            result = result.put(entry._1, entry._2);
        }
        return result;
    }

    @SafeVarargs
    public static <K, V> HashMultimap<K, V, Set<V>> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMultimap<K, V, Set<V>> result = HashMultimap.emptyWithSet();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            result = result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Set<V>> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntries(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Set<V>> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntries(Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Set<V>> of(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        HashMultimap<K, V, Set<V>> result = HashMultimap.emptyWithSet();
        for (int i = 0; i < pairs.length; i += 2) {
            result = result.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return result;
    }

    private static <K, V> Factory<K, V, Set<V>> setFactory() {
        return new Factory<K, V, Set<V>>() {

            @SuppressWarnings("unchecked")
            @Override
            public <K2, V2, T2 extends Traversable<V2>> Multimap<K2, V2, T2> emptyInstance() {
                return (Multimap<K2, V2, T2>) EMPTY_SET;
            }

            @Override
            public HashMultimap<K, V, Set<V>> createFromMap(Map<K, Set<V>> back) {
                return new HashMultimap<>(back, this);
            }

            @Override
            public HashMultimap<K, V, Set<V>> createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
                Map<K, Set<V>> back = HashMap.empty();
                for (Tuple2<? extends K, ? extends V> entry : entries) {
                    if (back.containsKey(entry._1)) {
                        back = back.put(entry._1, back.get(entry._1).get().add(entry._2));
                    } else {
                        back = back.put(entry._1, HashSet.of(entry._2));
                    }
                }
                return new HashMultimap<>(back, this);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <V2, T2 extends Traversable<V2>> T2 emptyContainer() {
                return (T2) HashSet.empty();
            }

            @Override
            public Set<V> addToContainer(Set<V> container, V value) {
                return container.add(value);
            }

            @Override
            public Set<V> removeFromContainer(Set<V> container, V value) {
                return container.remove(value);
            }

            @Override
            public String containerName() {
                return "Set";
            }
        };
    }

    private static <K, V> Factory<K, V, Seq<V>> seqFactory() {
        return new Factory<K, V, Seq<V>>() {

            @SuppressWarnings("unchecked")
            @Override
            public <K2, V2, T2 extends Traversable<V2>> Multimap<K2, V2, T2> emptyInstance() {
                return (HashMultimap<K2, V2, T2>) EMPTY_SEQ;
            }

            @Override
            public HashMultimap<K, V, Seq<V>> createFromMap(Map<K, Seq<V>> back) {
                return new HashMultimap<>(back, this);
            }

            @Override
            public HashMultimap<K, V, Seq<V>> createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
                Map<K, Seq<V>> back = HashMap.empty();
                for (Tuple2<? extends K, ? extends V> entry : entries) {
                    if (back.containsKey(entry._1)) {
                        back = back.put(entry._1, back.get(entry._1).get().append(entry._2));
                    } else {
                        back = back.put(entry._1, List.of(entry._2));
                    }
                }
                return new HashMultimap<>(back, this);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <V2, T2 extends Traversable<V2>> T2 emptyContainer() {
                return (T2) List.empty();
            }

            @Override
            public Seq<V> addToContainer(Seq<V> container, V value) {
                return container.append(value);
            }

            @Override
            public Seq<V> removeFromContainer(Seq<V> container, V value) {
                return container.remove(value);
            }

            @Override
            public String containerName() {
                return "Seq";
            }
        };
    }

    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, HashMultimap<K, V, Set<V>>> collector() {
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Tuple2<K, V>>, HashMultimap<K, V, Set<V>>> finisher = HashMultimap::ofEntries;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY_SET : this;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Set<V>> emptyWithSet() {
        return (HashMultimap<K, V, Set<V>>) EMPTY_SET;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimap<K, V, Seq<V>> emptyWithSeq() {
        return (HashMultimap<K, V, Seq<V>>) EMPTY_SEQ;
    }

    private HashMultimap(Map<K, T> back, Factory<K, V, T> factory) {
        super(back, factory);
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
    public String stringPrefix() {
        return "HashMultimap[" + factory.containerName() + "]";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }
}
