/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract {@link Multimap} implementation (not intended to be public).
 *
 * @param <K> Key type
 * @param <V> Value type
 * @param <M> Map type
 * @author Ruslan Sennov
 * @since 2.0.0
 */
 class MultimapImpl<K, V, M extends MultimapImpl<K, V, M>> implements Multimap<K, V> {

    interface Factory {

        <K, V> Multimap<K, V> emptyInstance();

        <K, V> Multimap<K, V> createFromMap(Map<K, Traversable<V>> back);

        <K, V> Map<K, V> emptyMap();

        <V> Traversable<V> emptyContainer();

        <V> Traversable<V> addToContainer(Traversable<V> container, V value);

        <V> Traversable<V> removeFromContainer(Traversable<V> container, V value);

        String containerName();
    }

    private static final long serialVersionUID = 1L;

    private final Map<K, Traversable<V>> back;
    private final Lazy<Integer> size;
    final transient Factory factory;

    MultimapImpl(Map<K, Traversable<V>> back, Factory factory) {
        this.back = back;
        this.size = Lazy.of(() -> back.foldLeft(0, (s, t) -> s + t._2.size()));
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    private M createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Map<K, Traversable<V>> back = factory.emptyMap();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            if (back.containsKey(entry._1)) {
                back = back.put(entry._1, factory.addToContainer(back.get(entry._1).get(), entry._2));
            } else {
                back = back.put(entry._1, factory.addToContainer(factory.emptyContainer(), entry._2));
            }
        }
        return (M) factory.createFromMap(back);
    }

    @Override
    public <K2, V2> Multimap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return back.containsKey(key);
    }

    @Override
    public <K2, V2> Multimap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(factory.<K2, V2>emptyInstance(), (acc, entry) -> {
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
        return foldLeft(factory.<K2, V2>emptyInstance(), (acc, entry) -> acc.put(mapper.apply(entry._1, entry._2)));
    }

    @Override
    public <V2> Multimap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M put(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse(factory.<V>emptyContainer());
        final Traversable<V> newValues = factory.addToContainer(values, value);
        return newValues == values ? (M) this : (M) factory.createFromMap(back.put(key, newValues));
    }

    @Override
    public M put(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return put(entry._1, entry._2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key) {
        return back.containsKey(key) ? (M) factory.createFromMap(back.remove(key)) : (M) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse(factory.<V>emptyContainer());
        final Traversable<V> newValues = factory.removeFromContainer(values, value);
        return newValues == values ? (M) this : newValues.isEmpty() ? (M) factory.createFromMap(back.remove(key)): (M) factory.createFromMap(back.put(key, newValues));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M removeAll(Iterable<? extends K> keys) {
        Map<K, Traversable<V>> result = back.removeAll(keys);
        return result == back ? (M) this : (M) factory.createFromMap(result);
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        return null;
    }

    @Override
    public Traversable<V> values() {
        return Iterator.concat(back.values()).toStream();
    }

    @Override
    public Multimap<K, V> distinct() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return createFromEntries(iterator().distinctBy(comparator));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> M distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return createFromEntries(iterator().distinctBy(keyExtractor));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M drop(long n) {
        if (n <= 0) {
            return (M) this;
        }
        if (n >= length()) {
            return (M) factory.emptyInstance();
        }
        return createFromEntries(iterator().drop(n));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M dropRight(long n) {
        if (n <= 0) {
            return (M) this;
        }
        if (n >= length()) {
            return (M) factory.emptyInstance();
        }
        return createFromEntries(iterator().dropRight(n));
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
        return createFromEntries(iterator().dropWhile(predicate));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M filter(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createFromEntries(iterator().filter(predicate));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> Map<C, M> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return foldLeft(HashMap.empty(), (map, entry) -> {
            final C key = classifier.apply(entry);
            final Multimap<K, V> values = map.get(key)
                    .map(entries -> entries.put(entry._1, entry._2))
                    .getOrElse(createFromEntries(Iterator.of(entry)));
            return map.put(key, (M) values);
        });
    }

    @Override
    public Iterator<M> grouped(long size) {
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
    public Option<M> initOption() {
        return isEmpty() ? Option.none() : Option.some((M) init());
    }

    @Override
    public boolean isEmpty() {
        return back.isEmpty();
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return back.iterator().flatMap(t -> t._2.map(v -> Tuple.of(t._1, v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Option<M> tailOption() {
        if (isEmpty()) {
            return Option.none();
        } else {
            return Option.some((M) tail());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public API.Match<M> match() {
        return API.Match((M) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M merge(Multimap<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            return that.foldLeft((M) this, (map, entry) -> !map.contains((Tuple2<K, V>) entry) ? map.put(entry) : map);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K2 extends  K, V2 extends V> Multimap<K, V> merge(Multimap<K2, V2> that, BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            Map<K, Traversable<V>> result = that.keySet().foldLeft(this.back, (map, key) -> {
                final Traversable<V> thisValues = map.get(key).getOrElse(factory.emptyContainer());
                final Traversable<V2> thatValues = that.get(key).get();
                final Traversable<V> newValues = collisionResolution.apply(thisValues, thatValues);
                return map.put(key, newValues);
            });
            return (M) factory.createFromMap(result);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<M, M> partition(Predicate<? super Tuple2<K, V>> predicate) {
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

    @Override
    public String stringPrefix() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return containsKey(currentElement._1) ? remove(currentElement._1).put(newElement) : (M) this;
    }

    @Override
    public M replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return createFromEntries(back.flatMap(t -> t._2.map(v -> Tuple.of(t._1, v))).retainAll(elements));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, Queue.<Tuple2<K, V>>empty(), Queue::append, this::createFromEntries);
    }

    @Override
    public Iterator<M> sliding(long size) {
        return sliding(size, 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<M> sliding(long size, long step) {
        return iterator().sliding(size, step).map(this::createFromEntries);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<M, M> span(Predicate<? super Tuple2<K, V>> predicate) {
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

    @SuppressWarnings("unchecked")
    @Override
    public M take(long n) {
        if (size() <= n) {
            return (M) this;
        } else {
            return createFromEntries(iterator().take(n));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public M takeRight(long n) {
        if (size() <= n) {
            return (M) this;
        } else {
            return createFromEntries(iterator().takeRight(n));
        }
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
        final M taken = createFromEntries(iterator().takeWhile(predicate));
        return taken.length() == length() ? (M) this : taken;
    }

    @Override
    public int hashCode() {
        return back.hashCode();
    }
}
