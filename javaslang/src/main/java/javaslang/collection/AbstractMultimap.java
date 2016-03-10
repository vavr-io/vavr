/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.API;
import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

/**
 * An {@link Multimap} implementation (not intended to be public).
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 * @since 2.0.0
 */
 final class AbstractMultimap<K, V> implements Multimap<K, V>, Serializable {

    @SuppressWarnings("unchecked")
    enum ContainerSupplier {

        SET(HashSet::empty,
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem)
        ),
        SORTED_SET(TreeSet::empty,
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem)
        ),
        SEQ(List::empty,
                (Traversable<?> seq, Object elem) -> ((List<Object>) seq).append(elem),
                (Traversable<?> seq, Object elem) -> ((List<Object>) seq).remove(elem)
        );

        public static ContainerSupplier of(ContainerType type) {
            switch (type) {
                case SEQ:
                    return SEQ;
                case SET:
                    return SET;
                case SORTED_SET:
                    return SORTED_SET;
                default:
                    return null;
            }
        }

        final Supplier<Traversable<?>> emptySupplier;
        final BiFunction<Traversable<?>, Object, Traversable<?>> add;
        final BiFunction<Traversable<?>, Object, Traversable<?>> remove;

        <T> ContainerSupplier(Supplier<Traversable<?>> emptySupplier,
                              BiFunction<Traversable<?>, Object, Traversable<?>> add,
                              BiFunction<Traversable<?>, Object, Traversable<?>> remove) {
            this.emptySupplier = emptySupplier;
            this.add = add;
            this.remove = remove;
        }

        <T> Traversable<T> empty() {
            return (Traversable<T>) emptySupplier.get();
        }

        <T> Traversable<T> add(Traversable<T> container, T elem) {
            return (Traversable<T>) add.apply(container, elem);
        }

        <T> Traversable<T> remove(Traversable<T> container, T elem) {
            return (Traversable<T>) remove.apply(container, elem);
        }
    }

    @SuppressWarnings("unchecked")
    enum MapSupplier {

        HASH_MAP(HashMap::empty),
        LINKED_HASH_MAP(LinkedHashMap::empty),
        TREE_MAP(TreeMap::empty);

        public static MapSupplier of(MapType type) {
            switch (type) {
                case HASH_MAP:
                    return HASH_MAP;
                case LINKED_HASH_MAP:
                    return LINKED_HASH_MAP;
                case TREE_MAP:
                    return TREE_MAP;
                default:
                    return null;
            }
        }

        final Supplier<Map<?,?>> emptySupplier;

        MapSupplier(Supplier<Map<?, ?>> emptySupplier) {
            this.emptySupplier = emptySupplier;
        }

        <K, V> Map<K, V> empty() {
            return (Map<K, V>) emptySupplier.get();
        }
    }

    private static final long serialVersionUID = 1L;

    private static final java.util.Map<MapSupplier, java.util.Map<ContainerSupplier, Multimap<?, ?>>> EMPTIES;

    static {
        EMPTIES = new java.util.HashMap<>();
        for (MapSupplier mapSupplier : MapSupplier.values()) {
            java.util.Map<ContainerSupplier, Multimap<?, ?>> containers = new java.util.HashMap<>();
            for (ContainerSupplier containerSupplier : ContainerSupplier.values()) {
                containers.put(containerSupplier, new AbstractMultimap<>(mapSupplier.empty(), mapSupplier, containerSupplier));
            }
            EMPTIES.put(mapSupplier, containers);
        }
    }

    @SuppressWarnings("unchecked")
    static <K, V> Multimap<K, V> empty(MapType mapType, ContainerType containerType) {
        Objects.requireNonNull(mapType, "mapType is null");
        Objects.requireNonNull(containerType, "containerType is null");
        final MapSupplier mapSupplier = MapSupplier.of(mapType);
        final ContainerSupplier containerSupplier = ContainerSupplier.of(containerType);
        return (Multimap<K, V>) EMPTIES.get(mapSupplier).get(containerSupplier);
    }

    private final Map<K, Traversable<V>> back;
    private final Lazy<Integer> size;
    private final MapSupplier mapSupplier;
    private final ContainerSupplier containerSupplier;

    AbstractMultimap(Map<K, Traversable<V>> back, MapSupplier mapSupplier, ContainerSupplier containerSupplier) {
        this.back = back;
        this.size = Lazy.of(() -> back.foldLeft(0, (s, t) -> s + t._2.size()));
        this.mapSupplier = mapSupplier;
        this.containerSupplier = containerSupplier;
    }

    @SuppressWarnings("unchecked")
    private <K2, V2> Multimap<K2, V2> createFromEntries(Iterable<? extends Tuple2<? extends K2, ? extends V2>> entries) {
        Map<K2, Traversable<V2>> back = mapSupplier.empty();
        for (Tuple2<? extends K2, ? extends V2> entry : entries) {
            if (back.containsKey(entry._1)) {
                back = back.put(entry._1, containerSupplier.add(back.get(entry._1).get(), entry._2));
            } else {
                back = back.put(entry._1, containerSupplier.add(containerSupplier.empty(), entry._2));
            }
        }
        return createFromMap(back);
    }

    private <K2, V2> Multimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back) {
        return new AbstractMultimap<>(back, mapSupplier, containerSupplier);
    }

    @SuppressWarnings("unchecked")
    private <K2, V2> Multimap<K2, V2> emptyInstance() {
        return (Multimap<K2, V2>) EMPTIES.get(mapSupplier).get(containerSupplier);
    }

    @Override
    public <K2, V2> Multimap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return createFromEntries(entries);
    }

    @Override
    public boolean containsKey(K key) {
        return back.containsKey(key);
    }

    @Override
    public <K2, V2> Multimap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.<K2, V2>emptyInstance(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
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
    public <K2, V2> Multimap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.<K2, V2>emptyInstance(), (acc, entry) -> acc.put(mapper.apply(entry._1, entry._2)));
    }

    @Override
    public <V2> Multimap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @Override
    public Multimap<K, V> put(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse(containerSupplier.empty());
        final Traversable<V> newValues = containerSupplier.add(values, value);
        return newValues == values ? this : createFromMap(back.put(key, newValues));
    }

    @Override
    public Multimap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return put(entry._1, entry._2);
    }

    @Override
    public Multimap<K, V> remove(K key) {
        return back.containsKey(key) ? createFromMap(back.remove(key)) : this;
    }

    @Override
    public Multimap<K, V> remove(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse(containerSupplier.empty());
        final Traversable<V> newValues = containerSupplier.remove(values, value);
        return newValues == values ? this : newValues.isEmpty() ? createFromMap(back.remove(key)): createFromMap(back.put(key, newValues));
    }

    @Override
    public Multimap<K, V> removeAll(Iterable<? extends K> keys) {
        Map<K, Traversable<V>> result = back.removeAll(keys);
        return result == back ? this : createFromMap(result);
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public Traversable<V> values() {
        return Iterator.concat(back.values()).toStream();
    }

    @Override
    public Multimap<K, V> distinct() {
        return this;
    }

    @Override
    public Multimap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return createFromEntries(iterator().distinctBy(comparator));
    }

    @Override
    public <U> Multimap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return createFromEntries(iterator().distinctBy(keyExtractor));
    }

    @Override
    public Multimap<K, V> drop(long n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return this.emptyInstance();
        }
        return createFromEntries(iterator().drop(n));
    }

    @Override
    public Multimap<K, V> dropRight(long n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return this.emptyInstance();
        }
        return createFromEntries(iterator().dropRight(n));
    }

    @Override
    public Multimap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public Multimap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createFromEntries(iterator().dropWhile(predicate));
    }

    @Override
    public Multimap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createFromEntries(iterator().filter(predicate));
    }

    @Override
    public <C> Map<C, Multimap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return foldLeft(HashMap.empty(), (map, entry) -> {
            final C key = classifier.apply(entry);
            final Multimap<K, V> values = map.get(key)
                    .map(entries -> entries.put(entry._1, entry._2))
                    .getOrElse(createFromEntries(Iterator.of(entry)));
            return map.put(key, values);
        });
    }

    @Override
    public Iterator<Multimap<K, V>> grouped(long size) {
        return sliding(size, size);
    }

    @Override
    public Multimap<K, V> init() {
        if (back.isEmpty()) {
            throw new UnsupportedOperationException("init of empty HashMap");
        } else {
            final Tuple2<K, V> last = last();
            return remove(last._1, last._2);
        }
    }

    @Override
    public Tuple2<K, V> head() {
        final Tuple2<K, Traversable<V>> head = back.head();
        return Tuple.of(head._1, head._2.head());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Option<Multimap<K, V>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public boolean isEmpty() {
        return back.isEmpty();
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return back.iterator().flatMap(t -> t._2.map(v -> Tuple.of(t._1, v)));
    }

    @Override
    public Option<Multimap<K, V>> tailOption() {
        if (isEmpty()) {
            return Option.none();
        } else {
            return Option.some(tail());
        }
    }

    @Override
    public API.Match<Multimap<K, V>> match() {
        return API.Match(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Multimap<K, V> merge(Multimap<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            return that.foldLeft((Multimap<K, V>) this, (map, entry) -> !map.contains((Tuple2<K, V>) entry) ? map.put(entry) : map);
        }
    }

    @Override
    public <K2 extends  K, V2 extends V> Multimap<K, V> merge(Multimap<K2, V2> that, BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            Map<K, Traversable<V>> result = that.keySet().foldLeft(this.back, (map, key) -> {
                final Traversable<V> thisValues = map.get(key).getOrElse(containerSupplier.empty());
                final Traversable<V2> thatValues = that.get(key).get();
                final Traversable<V> newValues = collisionResolution.apply(thisValues, thatValues);
                return map.put(key, newValues);
            });
            return createFromMap(result);
        }
    }

    @Override
    public Tuple2<Multimap<K, V>, Multimap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> p = iterator().partition(predicate);
        return Tuple.of(createFromEntries(p._1), createFromEntries(p._2));
    }

    @Override
    public Multimap<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    public String stringPrefix() {
        return "Multimap[" + mapSupplier.empty().stringPrefix() + "," + containerSupplier.empty().stringPrefix() + "]";
    }

    @Override
    public Multimap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return containsKey(currentElement._1) ? remove(currentElement._1).put(newElement) : this;
    }

    @Override
    public Multimap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public Multimap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return createFromEntries(back.flatMap(t -> t._2.map(v -> Tuple.of(t._1, v))).retainAll(elements));
    }

    @Override
    public Multimap<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, Queue.<Tuple2<K, V>>empty(), Queue::append, this::createFromEntries);
    }

    @Override
    public Iterator<Multimap<K, V>> sliding(long size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<Multimap<K, V>> sliding(long size, long step) {
        return iterator().sliding(size, step).map(this::createFromEntries);
    }

    @Override
    public Tuple2<Multimap<K, V>, Multimap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = iterator().span(predicate);
        return Tuple.of(createFromEntries(t._1), createFromEntries(t._2));
    }

    @Override
    public Multimap<K, V> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty Multimap");
        } else {
            final Tuple2<K, V> head = head();
            return remove(head._1, head._2);
        }
    }

    @Override
    public Multimap<K, V> take(long n) {
        if (size() <= n) {
            return this;
        } else {
            return createFromEntries(iterator().take(n));
        }
    }

    @Override
    public Multimap<K, V> takeRight(long n) {
        if (size() <= n) {
            return this;
        } else {
            return createFromEntries(iterator().takeRight(n));
        }
    }

    @Override
    public Multimap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public Multimap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Multimap<K, V> taken = createFromEntries(iterator().takeWhile(predicate));
        return taken.length() == length() ? this : taken;
    }

    @Override
    public int hashCode() {
        return back.hashCode();
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        final java.util.Map<K, Collection<V>> javaMap = new java.util.HashMap<>();
        for (Tuple2<K, V> t : this) {
            javaMap.computeIfAbsent(t._1, k -> {
                if(containerSupplier.empty() instanceof Set) {
                    return new java.util.HashSet<>();
                } else {
                    return new java.util.ArrayList<>();
                }
            }).add(t._2);
        }
        return javaMap;
    }
}
