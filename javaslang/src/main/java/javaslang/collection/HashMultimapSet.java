/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang contributors
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashMultimapSet<K, V> extends AbstractMultimap<K, V, Set<V>, HashMultimapSet<K, V>> {

    private static final long serialVersionUID = 1L;

    private static final HashMultimapSet<?, ?> EMPTY = new HashMultimapSet<>(HashMap.empty());

    @SuppressWarnings("unchecked")
    public static <K, V> HashMultimapSet<K, V> empty() {
        return (HashMultimapSet<K, V>) EMPTY;
    }

    private final Map<K, Set<V>> back;

    private HashMultimapSet(Map<K, Set<V>> back) {
        this.back = back;
    }

    @Override
    Map<K, Set<V>> back() {
        return back;
    }

    @Override
    HashMultimapSet<K, V> createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Map<K, Set<V>> back = HashMap.empty();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            if(back.containsKey(entry._1)) {
                back = back.put(entry._1, back.get(entry._1).get().add(entry._2));
            } else {
                back = back.put(entry._1, HashSet.of(entry._2));
            }
        }
        return new HashMultimapSet<>(back);
    }

    @Override
    HashMultimapSet<K, V> createFromMap(Map<K, Set<V>> back) {
        return new HashMultimapSet<>(back);
    }

    @Override
    Set<V> emptyContainer() {
        return HashSet.empty();
    }

    @Override
    Set<V> addToContainer(Set<V> container, V value) {
        return container.add(value);
    }

    @Override
    Set<V> removeFromContainer(Set<V> container, V value) {
        return container.remove(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    HashMultimapSet<K, V> emptyInstance() {
        return (HashMultimapSet<K, V>) EMPTY;
    }

    @Override
    public <K2, V2, T2 extends Traversable<V2>> Multimap<K2, V2, T2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        return null;
    }

    @Override
    public <K2, V2, T2 extends Traversable<V2>> Multimap<K2, V2, T2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        return null;
    }

    @Override
    public <K2, V2, T2 extends Traversable<V2>> Multimap<K2, V2, T2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        return null;
    }

    @Override
    public <V2, T2 extends Traversable<V2>> Multimap<K, V2, T2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        return null;
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
    public Multimap<K, V, Set<V>> distinct() {
        return null;
    }

    @Override
    public Multimap<K, V, Set<V>> init() {
        return null;
    }

    @Override
    public Multimap<K, V, Set<V>> tail() {
        return null;
    }

    @Override
    public String stringPrefix() {
        return "HashMultimapSet";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }
}
