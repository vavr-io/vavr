/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

/**
 * An abstract {@link Map} implementation (not intended to be public).
 *
 * @param <K> Key type
 * @param <V> Value type
 * @param <M> Map type
 * @author Ruslan Sennov
 * @since 2.0.0
 */
abstract class AbstractMap<K, V, M extends AbstractMap<K, V, M>> implements Map<K, V> {

    private static final long serialVersionUID = 1L;

    abstract M createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries);

    /**
     * Returns an empty version of this traversable, i.e. {@code empty().isEmpty() == true}.
     *
     * @return an empty instance of this Map.
     */
    abstract M emptyInstance();

    @SuppressWarnings("unchecked")
    @Override
    public M put(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return (M) put(entry._1, entry._2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends V> M put(K key, U value,
                               BiFunction<? super V, ? super U, ? extends V> merge) {
        Objects.requireNonNull(merge, "the merge function is null");
        final Option<V> currentValue = get(key);
        if (currentValue.isEmpty()) {
            return (M) put(key, value);
        } else {
            return (M) put(key, merge.apply(currentValue.get(), value));
        }
    }

    @Override
    public <U extends V> M put(Tuple2<? extends K, U> entry,
                               BiFunction<? super V, ? super U, ? extends V> merge) {
        Objects.requireNonNull(merge, "the merge function is null");
        final Option<V> currentValue = get(entry._1);
        if (currentValue.isEmpty()) {
            return put(entry);
        } else {
            return put(entry.map2(
                    value -> merge.apply(currentValue.get(), value)));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinct() {
        return (M) this;
    }

    @Override
    public M distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return createFromEntries(iterator().distinctBy(comparator));
    }

    @Override
    public <U> M distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return createFromEntries(iterator().distinctBy(keyExtractor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public M drop(int n) {
        if (n <= 0) {
            return (M) this;
        } else if (n >= length()) {
            return emptyInstance();
        } else {
            return createFromEntries(iterator().drop(n));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public M dropRight(int n) {
        if (n <= 0) {
            return (M) this;
        } else if (n >= length()) {
            return emptyInstance();
        } else {
            return createFromEntries(iterator().dropRight(n));
        }
    }

    @Override
    public M dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public M dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createFromEntries(iterator().dropWhile(predicate));
    }

    @Override
    public M filter(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createFromEntries(iterator().filter(predicate));
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

    @SuppressWarnings("unchecked")
    @Override
    public <C> Map<C, M> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, this::createFromEntries);
    }

    @Override
    public Iterator<M> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public abstract M init();

    @Override
    public Option<M> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public abstract M tail();

    @Override
    public Option<M> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M take(int n) {
        if (size() <= n) {
            return (M) this;
        } else {
            return createFromEntries(iterator().take(n));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public M takeRight(int n) {
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

    @SuppressWarnings("unchecked")
    @Override
    public M merge(Map<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            return that.foldLeft((M) this, (map, entry) -> !map.containsKey(entry._1) ? map.put(entry) : map);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends V> M merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            return that.foldLeft((M) this, (map, entry) -> {
                final K key = entry._1;
                final U value = entry._2;
                final V newValue = map.get(key).map(v -> (V) collisionResolution.apply(v, value)).getOrElse(value);
                return (M) map.put(key, newValue);
            });
        }
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public M replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return (M) (containsKey(currentElement._1) ? remove(currentElement._1).put(newElement) : this);
    }

    @Override
    public M replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public M scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, emptyInstance(), (m, e) -> m.put(e), Function.identity());
    }

    @Override
    public Iterator<M> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<M> sliding(int size, int step) {
        return iterator().sliding(size, step).map(this::createFromEntries);
    }

    @Override
    public Tuple2<M, M> span(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = iterator().span(predicate);
        return Tuple.of(createFromEntries(t._1), createFromEntries(t._2));
    }
}
