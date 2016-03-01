/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.util.Collection;

public class HashMultimap<K, V, T extends Traversable<V>> extends MultimapImpl<K, V, T, HashMultimap<K, V, T>> {

    private static final long serialVersionUID = 1L;

    private final static HashMultimap<?, ?, ?> EMPTY_SET = new HashMultimap<>(HashMap.empty(), setFactory());
    private final static HashMultimap<?, ?, ?> EMPTY_SEQ = new HashMultimap<>(HashMap.empty(), seqFactory());

    private static <K, V> Factory<K, V, Set<V>> setFactory() {
        return new Factory<K, V, Set<V>>() {

            @SuppressWarnings("unchecked")
            @Override
            public HashMultimap<K, V, Set<V>> emptyInstance() {
                return (HashMultimap<K, V, Set<V>>) EMPTY_SET;
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

            @Override
            public Set<V> emptyContainer() {
                return HashSet.empty();
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
            public HashMultimap<K, V, Seq<V>> emptyInstance() {
                return (HashMultimap<K, V, Seq<V>>) EMPTY_SEQ;
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

            @Override
            public Seq<V> emptyContainer() {
                return List.empty();
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

    public static <K, V> HashMultimap<K, V, Set<V>> emptyWithSet() {
        return new HashMultimap<>(HashMap.empty(), setFactory());
    }

    public static <K, V> HashMultimap<K, V, Seq<V>> emptyWithSeq() {
        return new HashMultimap<>(HashMap.empty(), seqFactory());
    }

    private HashMultimap(Map<K, T> back, Factory<K, V, T> factory) {
        super(back, factory);
    }

    @Override
    public int size() {
        return 0;
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
