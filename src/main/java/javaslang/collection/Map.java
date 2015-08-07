/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An immutable {@code Map} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @since 2.0.0
 */
interface Map<K, V> extends /*TODO: Traversable<Map.Entry<K, V>>,*/ Function<K, V>,
    /*TODO:remove Iterable, TraversableOnce*/ Iterable<Map.Entry<K, V>>, TraversableOnce<Map.Entry<K, V>> {

    @Override
    default V apply(K key) {
        return get(key);
    }

    boolean containsKey(K key);

    // TODO
    //boolean containsValue(V value);

    // TODO
    // Set<Entry<K, V>> entrySet();

    V get(K key);

    Option<V> getOption(K key);

    V getOrDefault(K key, V defaultValue);

    // @Override
    Iterator<Entry<K, V>> iterator();

    // TODO
    // Set<K> keySet();

    Map<K, V> put(K key, V value);

    Map<K, V> remove(K key);

    int size();

    // TODO
    // Traversable<V> values();

    // -- Adjusted return types of Traversable methods

    // TODO: @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Entry<? super K, ? super V>, ? extends C> classifier);

    @Override
    Map<K, V> peek(Consumer<? super Entry<K, V>> action);

    // TODO

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
