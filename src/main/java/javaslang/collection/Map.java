/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;

/**
 * An immutable {@code Map} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @since 2.0.0
 */
public interface Map<K, V> extends Traversable<Map.Entry<K, V>>, Function<K, V> {

    @Override
    default V apply(K key) {
        return get(key);
    }

    boolean containsKey(K key);

    boolean containsValue(V value);

    Set<Entry<K, V>> entrySet();

    <U, W> Map<U, W> flatMap(BiFunction<? super K, ? super V, ? extends Map<? extends U, ? extends W>> mapper);

    V get(K key);

    Option<V> getOption(K key);

    V getOrDefault(K key, V defaultValue);

    Set<K> keySet();

    <U, W> Map<U, W> map(BiFunction<? super K, ? super V, ? extends Entry<U, W>> mapper);

    Map<K, V> put(K key, V value);

    Map<K, V> remove(K key);

    Map<K, V> removeAll(Iterable<? extends K> keys);

    int size();

    <K1, V1, K2, V2> Tuple2<? extends Map<K1, V1>, ? extends Map<K2, V2>> unzip(Function<? super Entry<K, V>, Tuple2<? extends Entry<K1, V1>, ? extends Entry<K2, V2>>> unzipper);

    Traversable<V> values();

    <U> Map<Tuple2<K, V>, U> zip(Iterable<U> that);

    <U> Map<Tuple2<K, V>, U> zipAll(Iterable<U> that, Entry<K, V> thisElem, U thatElem);

    Map<Tuple2<K, V>, Integer> zipWithIndex();

    // -- Adjusted return types of Traversable methods

    @Override
    Map<K, V> clear();

    @Override
    boolean contains(Entry<K, V> element);

    @Override
    Map<K, V> distinct();

    @Override
    Map<K, V> distinctBy(Comparator<? super Entry<K, V>> comparator);

    @Override
    <U> Map<K, V> distinctBy(Function<? super Entry<K, V>, ? extends U> keyExtractor);

    @Override
    Map<K, V> drop(int n);

    @Override
    Map<K, V> dropRight(int n);

    @Override
    Map<K, V> dropWhile(Predicate<? super Entry<K, V>> predicate);

    @Override
    Map<K, V> filter(Predicate<? super Entry<K, V>> predicate);

    @Override
    Map<K, V> findAll(Predicate<? super Entry<K, V>> predicate);

    @Override
    <U> Set<U> flatMap(Function<? super Entry<K, V>, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> Set<U> flatMapM(Function<? super Entry<K, V>, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper);

    @Override
    Set<Object> flatten();

    @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier);

    @Override
    Set<? extends Map<K, V>> grouped(int size);

    @Override
    Map<K, V> init();

    @Override
    Option<? extends Map<K, V>> initOption();

    @Override
    Map<K, V> intersperse(Entry<K, V> element);

    @Override
    Iterator<Entry<K, V>> iterator();

    @Override
    int length();

    @Override
    <U> Set<U> map(Function<? super Entry<K, V>, ? extends U> mapper);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(Predicate<? super Entry<K, V>> predicate);

    @Override
    Map<K, V> peek(Consumer<? super Entry<K, V>> action);

    @Override
    Map<K, V> replace(Entry<K, V> currentElement, Entry<K, V> newElement);

    @Override
    Map<K, V> replaceAll(Entry<K, V> currentElement, Entry<K, V> newElement);

    @Override
    Map<K, V> replaceAll(UnaryOperator<Entry<K, V>> operator);

    @Override
    Map<K, V> retainAll(Iterable<? extends Entry<K, V>> elements);

    @Override
    Set<? extends Map<K, V>> sliding(int size);

    @Override
    Set<? extends Map<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(Predicate<? super Entry<K, V>> predicate);

    @Override
    default Spliterator<Entry<K, V>> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Map<K, V> tail();

    @Override
    Option<? extends Map<K, V>> tailOption();

    @Override
    Map<K, V> take(int n);

    @Override
    Map<K, V> takeRight(int n);

    @Override
    Map<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate);

    /**
     * Representation of a Map entry.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class Entry<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        public final K key;
        public final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public static <K, V> Entry<K, V> of(Tuple2<K, V> t) {
            return new Entry<>(t._1, t._2);
        }

        @SuppressWarnings("unchecked")
        public <X, Y> Entry<X, Y> flatMap(BiFunction<? super K, ? super V, ? extends Entry<? extends X, ? extends Y>> mapper) {
            return (Entry<X, Y>) mapper.apply(key, value);
        }

        public <X, Y> Entry<X, Y> map(Function<? super K, ? extends X> keyMapper, Function<? super V, ? extends Y> valueMapper) {
            return new Entry<>(keyMapper.apply(key), valueMapper.apply(value));
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Entry) {
                final Entry<?, ?> that = (Entry<?, ?>) o;
                return Objects.equals(this.key, that.key)
                        && Objects.equals(this.value, that.value);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return key + " -> " + value;
        }
    }
}
