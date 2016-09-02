/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Multimaps.OfMap;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An {@link LinkedHashMap}-based implementation of {@link Multimap}
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 * @since 2.1.0
 */
public final class LinkedHashMultimap<K, V> implements Multimap<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<K, Traversable<V>> back;
    private final ContainerType containerType;
    private final Traversable<V> emptyContainer;

    private LinkedHashMultimap(Map<K, Traversable<V>> back, ContainerType containerType, Traversable<V> emptyContainer) {
        this.back = back;
        this.containerType = containerType;
        this.emptyContainer = emptyContainer;
    }

    // -- static API

    public static <V> Builder<V> withSeq() {
        return new Builder<>(ContainerType.SEQ, List.empty());
    }

    public static <V> Builder<V> withSet() {
        return new Builder<>(ContainerType.SET, HashSet.empty());
    }

    public static <V extends Comparable<? super V>> Builder<V> withSortedSet() {
        return new Builder<>(ContainerType.SORTED_SET, TreeSet.empty());
    }

    public static <V> Builder<V> withSortedSet(Comparator<? super V> comparator) {
        return new Builder<>(ContainerType.SORTED_SET, TreeSet.empty(comparator));
    }

    public static class Builder<V> extends Multimaps.Builder<V> {

        private Builder(ContainerType containerType, Traversable<V> emptyContainer) {
            super(containerType, emptyContainer);
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> empty() {
            return ofMap(emptyMap());
        }

        @Override
        @SuppressWarnings("unchecked")
        public <K, V2 extends V> LinkedHashMultimap<K, V2> of(Object... pairs) {
            final Entries<K, V2> entries = new Entries(pairs);
            return build(entries, entries::getKey, entries::getValue, this::ofMap);
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> of(K key, V2 value) {
            return empty().put(key, value);
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> of(Tuple2<? extends K, ? extends V2> entry) {
            return empty().put(entry);
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V2>> entries) {
            return build(entries, Tuple2::_1, Tuple2::_2, (OfMap<K, V2, LinkedHashMultimap<K, V2>>) this::ofMap);
        }

        @Override
        @SafeVarargs
        public final <K, V2 extends V> LinkedHashMultimap<K, V2> ofEntries(Tuple2<? extends K, ? extends V2>... entries) {
            return ofEntries(Iterator.of(entries));
        }

        @Override
        @SafeVarargs
        public final <K, V2 extends V> LinkedHashMultimap<K, V2> ofEntries(java.util.Map.Entry<? extends K, ? extends V2>... entries) {
            return build(Iterator.of(entries), java.util.Map.Entry::getKey, java.util.Map.Entry::getValue, (OfMap<K, V2, LinkedHashMultimap<K, V2>>) this::ofMap);
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> fill(int n, Supplier<Tuple2<? extends K, ? extends V2>> supplier) {
            Objects.requireNonNull(supplier, "s is null");
            return ofEntries(Collections.fill(n, supplier));
        }

        @Override
        public <K, V2 extends V> LinkedHashMultimap<K, V2> tabulate(int n, Function<? super Integer, Tuple2<? extends K, ? extends V2>> f) {
            return ofEntries(Collections.tabulate(n, f));
        }

        @Override
        public <K, V2 extends V> Collector<Tuple2<K, V2>, ArrayList<Tuple2<K, V2>>, LinkedHashMultimap<K, V2>> collector() {
            final Supplier<ArrayList<Tuple2<K, V2>>> supplier = ArrayList::new;
            final BiConsumer<ArrayList<Tuple2<K, V2>>, Tuple2<K, V2>> accumulator = ArrayList::add;
            final BinaryOperator<ArrayList<Tuple2<K, V2>>> combiner = (left, right) -> {
                left.addAll(right);
                return left;
            };
            return Collector.of(supplier, accumulator, combiner, this::ofEntries);
        }

        @Override
        <K, V2 extends V> Map<K, Traversable<V2>> emptyMap() {
            return LinkedHashMap.empty();
        }

        @Override
        <K, V2 extends V> LinkedHashMultimap<K, V2> ofMap(Map<K, Traversable<V2>> back) {
            return new LinkedHashMultimap<>(back, containerType(), emptyContainer());
        }
    }

    /**
     * Narrows a widened {@code HashMultimap<? extends K, ? extends V>} to {@code HashMultimap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param map A {@code Map}.
     * @param <K> Key type
     * @param <V> Value type
     * @return the given {@code multimap} instance as narrowed type {@code Multimap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMultimap<K, V> narrow(LinkedHashMultimap<? extends K, ? extends V> map) {
        return (LinkedHashMultimap<K, V>) map;
    }

    // -- non-static API

    @Override
    public <K2, V2> LinkedHashMultimap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        return Multimaps.bimap(this, builder()::ofEntries, keyMapper, valueMapper);
    }

    @Override
    public boolean containsKey(K key) {
        return back.containsKey(key);
    }

    // TODO: Move this implementation to Multimaps and use Map Builder instead of Multimap.put
    @Override
    public <K2, V2> LinkedHashMultimap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(emptyInstance(), (acc, entry) -> {
            for (Tuple2<? extends K2, V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public Option<Traversable<V>> get(K key) {
        return back.get(key);
    }

    @Override
    public Set<K> keySet() {
        return back.keySet();
    }

    @Override
    public <K2, V2> LinkedHashMultimap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        return Multimaps.map(this, emptyInstance(), mapper);
    }

    @Override
    public <V2> LinkedHashMultimap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        return Multimaps.mapValues(this, emptyInstance(), valueMapper);
    }

    @Override
    public int size() {
        return Multimaps.size(back);
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        return Multimaps.toJavaMap(this);
    }

    @Override
    public Traversable<V> values() {
        return Iterator.concat(back.values()).toStream();
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof LinkedHashMultimap) {
            final LinkedHashMultimap<?, ?> that = (LinkedHashMultimap<?, ?>) o;
            return this.back.equals(that.back);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return back.hashCode();
    }

    @Override
    public String stringPrefix() {
        return getClass().getSimpleName() + "[" + emptyContainer.stringPrefix() + "]";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    // -- helpers

    private <K2, V2> LinkedHashMultimap<K2, V2> emptyInstance() {
        return builder().empty();
    }

    private <V2> Builder<V2> builder() {
        // TODO: THIS IS WRONG - WE NEED TO OBTAIN A NEW EMPTY CONTAINER IN THE CASE OF SORTABLE COLLECTIONS BECAUSE OF THE COMPARATOR!
        return new Builder<>(containerType, emptyContainer);
    }
}
