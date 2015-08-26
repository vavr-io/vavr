/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
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
        return get(key).orElseThrow(NoSuchElementException::new);
    }

    boolean containsKey(K key);

    boolean containsValue(V value);

    Set<Entry<K, V>> entrySet();

    <U, W> Map<U, W> flatMap2(BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Entry<? extends U, ? extends W>>> mapper);

    Option<V> get(K key);

    Set<K> keySet();

    <U, W> Map<U, W> map2(BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper);

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

    <K1, V1, K2, V2> Tuple2<? extends Map<K1, V1>, ? extends Map<K2, V2>> unzip(Function<? super Entry<? super K, ? super V>, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper);

    <K1, V1, K2, V2> Tuple2<? extends Map<K1, V1>, ? extends Map<K2, V2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper);

    Seq<V> values();

    <U> Map<Tuple2<K, V>, U> zip(java.lang.Iterable<U> that);

    <U> Map<Tuple2<K, V>, U> zipAll(java.lang.Iterable<U> that, Entry<K, V> thisElem, U thatElem);

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
    <U> Set<U> flatMap(Function<? super Entry<K, V>, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> Set<U> flatMapVal(Function<? super Entry<K, V>, ? extends Value<? extends U>> mapper);

    @Override
    Set<Object> flatten();

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
    <U> Set<U> map(Function<? super Entry<K, V>, ? extends U> mapper);

    // TODO: ? extends K or K?
    Map<K, V> merge(Map<K, ? extends V> that);

    /**
     * Creates a new map which is the merge of this and the argument hash map.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> hash map
     * and the second from <code>that</code>.
     *
     * @param <U> value type of that HashMap
     * @param that the other hash map
     * @param mergef the merge function or null if the first key-value pair is to be picked
     * @return A merged map
     */
    // TODO: ? extends K or K?
    <U extends V> Map<K, V> merge(Map<K, U> that, BiFunction<? super V, ? super U, ? extends V> mergef);

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

        public static <K, V> Entry<K, V> of(K key, V value) {
            return new Entry<>(key, value);
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
