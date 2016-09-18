/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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
 * @param <M> Multimap type
 * @author Ruslan Sennov
 * @since 2.1.0
 */
abstract class AbstractMultimap<K, V, M extends Multimap<K, V>> implements Multimap<K, V> {

    interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    }

    private static final long serialVersionUID = 1L;

    protected final Map<K, Traversable<V>> back;
    protected final SerializableSupplier<Traversable<?>> emptyContainer;
    private final ContainerType containerType;

    AbstractMultimap(Map<K, Traversable<V>> back, ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
        this.back = back;
        this.containerType = containerType;
        this.emptyContainer = emptyContainer;
    }

    protected abstract <K2, V2> Map<K2, V2> emptyMapSupplier();

    protected abstract <K2, V2> Multimap<K2, V2> emptyInstance();

    protected abstract <K2, V2> Multimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back);

    @SuppressWarnings("unchecked")
    private <K2, V2> Multimap<K2, V2> createFromEntries(Iterable<? extends Tuple2<? extends K2, ? extends V2>> entries) {
        Map<K2, Traversable<V2>> back = emptyMapSupplier();
        for (Tuple2<? extends K2, ? extends V2> entry : entries) {
            if (back.containsKey(entry._1)) {
                back = back.put(entry._1, containerType.add(back.get(entry._1).get(), entry._2));
            } else {
                back = back.put(entry._1, containerType.add(emptyContainer.get(), entry._2));
            }
        }
        return createFromMap(back);
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
    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public <K2, V2> Multimap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.emptyInstance(), (acc, entry) -> {
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
    @SuppressWarnings("unchecked")
    public Traversable<V> getOrElse(K key, Traversable<? extends V> defaultValue) {
        return back.getOrElse(key, (Traversable<V>) defaultValue);
    }

    @Override
    public Set<K> keySet() {
        return back.keySet();
    }

    @Override
    public <K2, V2> Multimap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.emptyInstance(), (acc, entry) -> acc.put(mapper.apply(entry._1, entry._2)));
    }

    @Override
    public <V2> Multimap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M put(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse((Traversable<V>) emptyContainer.get());
        final Traversable<V> newValues = containerType.add(values, value);
        return (M) (newValues == values ? this : createFromMap(back.put(key, newValues)));
    }

    @Override
    public M put(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return put(entry._1, entry._2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key) {
        return (M) (back.containsKey(key) ? createFromMap(back.remove(key)) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse((Traversable<V>) emptyContainer.get());
        final Traversable<V> newValues = containerType.remove(values, value);
        if (newValues == values) {
            return (M) this;
        } else if (newValues.isEmpty()) {
            return (M) createFromMap(back.remove(key));
        } else {
            return (M) createFromMap(back.put(key, newValues));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public M removeAll(Iterable<? extends K> keys) {
        final Map<K, Traversable<V>> result = back.removeAll(keys);
        return (M) (result == back ? this : createFromMap(result));
    }

    @Override
    public int size() {
        return back.foldLeft(0, (s, t) -> s + t._2.size());
    }

    @Override
    public Traversable<V> values() {
        return Iterator.concat(back.values()).toStream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinct() {
        return (M) (containerType == ContainerType.SEQ ? createFromEntries(iterator().distinct()) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return (M) createFromEntries(iterator().distinctBy(comparator));
    }

    @Override
    public <U> Multimap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return createFromEntries(iterator().distinctBy(keyExtractor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public M drop(int n) {
        if (n <= 0) {
            return (M) this;
        } else if (n >= length()) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().drop(n));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public M dropRight(int n) {
        if (n <= 0) {
            return (M) this;
        } else if (n >= length()) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().dropRight(n));
        }
    }

    @Override
    public M dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (M) createFromEntries(iterator().dropWhile(predicate));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M filter(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (M) createFromEntries(iterator().filter(predicate));
    }

    @Override
    public M filter(BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._1, t._2));
    }

    @Override
    public M filterKeys(Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._1));
    }

    @Override
    public M filterValues(Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._2));
    }

    @Override
    public M removeAll(BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    public M removeKeys(Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterKeys(predicate.negate());
    }

    @Override
    public M removeValues(Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterValues(predicate.negate());
    }

    @Override
    public <C> Map<C, Multimap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, this::createFromEntries);
    }

    @Override
    public Iterator<Multimap<K, V>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public M init() {
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
    public Option<M> initOption() {
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
    public Option<M> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M merge(Multimap<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return (M) createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            return that.foldLeft((M) this, (map, entry) -> !map.contains((Tuple2<K, V>) entry) ? (M) map.put(entry) : map);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K2 extends K, V2 extends V> Multimap<K, V> merge(Multimap<K2, V2> that, BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            final Map<K, Traversable<V>> result = that.keySet().foldLeft(this.back, (map, key) -> {
                final Traversable<V> thisValues = map.get(key).getOrElse((Traversable<V>) emptyContainer.get());
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

    @SuppressWarnings("unchecked")
    @Override
    public M peek(Consumer<? super Tuple2<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return (M) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return (M) (containsKey(currentElement._1) ? remove(currentElement._1, currentElement._2).put(newElement) : this);
    }

    @Override
    public M replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return (M) createFromEntries(back.flatMap(t -> t._2.map(v -> Tuple.of(t._1, v))).retainAll(elements));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return (M) Collections.scanLeft(this, zero, operation, Queue.empty(), Queue::append, this::createFromEntries);
    }

    @Override
    public Iterator<Multimap<K, V>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<Multimap<K, V>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(this::createFromEntries);
    }

    @Override
    public Tuple2<Multimap<K, V>, Multimap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = iterator().span(predicate);
        return Tuple.of(createFromEntries(t._1), createFromEntries(t._2));
    }

    @Override
    public M tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty Multimap");
        } else {
            final Tuple2<K, V> head = head();
            return remove(head._1, head._2);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public M take(int n) {
        return (M) (size() <= n ? this : createFromEntries(iterator().take(n)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public M takeRight(int n) {
        return (M) (size() <= n ? this : createFromEntries(iterator().takeRight(n)));
    }

    @Override
    public M takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Multimap<K, V> taken = createFromEntries(iterator().takeWhile(predicate));
        return (M) (taken.length() == length() ? this : taken);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o != null && getClass().isAssignableFrom(o.getClass())) {
            final AbstractMultimap<?, ?, ?> that = (AbstractMultimap<?, ?, ?>) o;
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
        return getClass().getSimpleName() + "[" + emptyContainer.get().stringPrefix() + "]";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        final java.util.Map<K, Collection<V>> javaMap = new java.util.HashMap<>();
        final Supplier<Collection<V>> javaContainerSupplier;
        if (containerType == ContainerType.SEQ) {
            javaContainerSupplier = java.util.ArrayList::new;
        } else if (containerType == ContainerType.SET) {
            javaContainerSupplier = java.util.HashSet::new;
        } else if (containerType == ContainerType.SORTED_SET) {
            javaContainerSupplier = java.util.TreeSet::new;
        } else {
            throw new IllegalStateException("Unknown ContainerType: " + containerType);
        }
        for (Tuple2<K, V> t : this) {
            javaMap.computeIfAbsent(t._1, k -> javaContainerSupplier.get()).add(t._2);
        }
        return javaMap;
    }
}
