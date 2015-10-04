/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

/**
 * An immutable {@code Map} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich, Ruslan Sennov
 * @since 2.0.0
 */
public interface Map<K, V> extends Traversable<Map.Entry<K, V>>, Function<K, V> {

    @Override
    default V apply(K key) {
        return get(key).orElseThrow(NoSuchElementException::new);
    }

    boolean containsKey(K key);

    boolean containsValue(V value);

    Set<Entry<K, V>> entrySet();

    <U, W> Map<U, W> flatMap(BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Entry<? extends U, ? extends W>>> mapper);

    Option<V> get(K key);

    Set<K> keySet();

    <U, W> Map<U, W> map(BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper);

    Map<K, V> put(K key, V value);

    /**
     * Convenience method for {@code put(entry.key, entry.value)}.
     *
     * @param entry A Map.Entry
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(Entry<? extends K, ? extends V> entry);

    /**
     * Convenience method for {@code put(entry._1, entry._2)}.
     *
     * @param entry A Map.Entry
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(Tuple2<? extends K, ? extends V> entry);

    Map<K, V> remove(K key);

    Map<K, V> removeAll(java.lang.Iterable<? extends K> keys);

    int size();

    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip(entry -> unzipper.apply(entry.key, entry.value));
    }

    Seq<V> values();

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
    <U> Seq<U> flatMap(Function<? super Entry<K, V>, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    Map<Object, Object> flatten();

    @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier);

    @Override
    Map<K, V> init();

    @Override
    Option<? extends Map<K, V>> initOption();

    @Override
    Iterator<Entry<K, V>> iterator();

    @Override
    int length();

    @Override
    <U> Seq<U> map(Function<? super Entry<K, V>, ? extends U> mapper);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * If collisions occur, the value of {@code this} map is taken.
     *
     * @param that the other map
     * @return A merged map
     * @throws NullPointerException if that map is null
     */
    Map<K, V> merge(Map<? extends K, ? extends V> that);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> map
     * and the second from <code>that</code> map.
     *
     * @param <U> value type of that Map
     * @param that the other map
     * @param collisionResolution the collision resolution function
     * @return A merged map
     * @throws NullPointerException if that map or the given collision resolution function is null
     */
    <U extends V> Map<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

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
    Map<K, V> retainAll(java.lang.Iterable<? extends Entry<K, V>> elements);

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
    Map<K, V> takeUntil(Predicate<? super Entry<K, V>> predicate);

    @Override
    Map<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate);

    @Override
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super Entry<K, V>, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    @Override
    default <U> Seq<Tuple2<Entry<K, V>, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zip(that));
    }

    @Override
    default <U> Seq<Tuple2<Entry<K, V>, U>> zipAll(java.lang.Iterable<U> that, Entry<K, V> thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default Seq<Tuple2<Entry<K, V>, Integer>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }

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

        /**
         * Convenience method for {@code new Entry<>(t._1, t._2)}.
         *
         * @param t A tuple of arity 2
         * @param <K> Key type
         * @param <V> Value type
         * @return A new Entry instance containing the first tuple component as key and the second tuple component as value.
         */
        public static <K, V> Entry<K, V> of(Tuple2<K, V> t) {
            return new Entry<>(t._1, t._2);
        }

        /**
         * Convenience method for {@code new Entry<>(key, value)}.
         *
         * @param key An map entry key
         * @param value An map entry value
         * @param <K> Key type
         * @param <V> Value type
         * @return A new Entry instance containing the specified key and value.
         */
        public static <K, V> Entry<K, V> of(K key, V value) {
            return new Entry<>(key, value);
        }

        /**
         * Returns the key.
         * <p>
         * Convenience method, intended to be used as method reference {@code Entry::key}.
         * Use {@code entry.key} to access the key directly.
         *
         * @return The key of this entry.
         */
        public K key() {
            return key;
        }

        /**
         * Returns the value.
         * <p>
         * Convenience method, intended to be used as method reference {@code Entry::value}.
         * Use {@code entry.value} to access the value directly.
         *
         * @return The value of this entry.
         */
        public V value() {
            return value;
        }

        @SuppressWarnings("unchecked")
        public <X, Y> Entry<X, Y> flatMap(BiFunction<? super K, ? super V, ? extends Entry<? extends X, ? extends Y>> mapper) {
            return (Entry<X, Y>) mapper.apply(key, value);
        }

        @SuppressWarnings("unchecked")
        public <X, Y> Entry<X, Y> map(BiFunction<? super K, ? super V, ? extends Entry<? extends X, ? extends Y>> mapper) {
            return (Entry<X, Y>) mapper.apply(key, value);
        }

        public <X, Y> Entry<X, Y> map(Function<? super K, ? extends X> keyMapper, Function<? super V, ? extends Y> valueMapper) {
            return new Entry<>(keyMapper.apply(key), valueMapper.apply(value));
        }

        public Tuple2<K, V> toTuple() {
            return Tuple.of(key, value);
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
