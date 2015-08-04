/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

interface Map<K, V> extends /*TODO: Traversable<Map.Entry<K, V>>,*/ Function<K, V> {

    static <K, V> Map<K, V> empty() {
        return HashMap.empty();
    }

    /**
     * Returns a singleton {@code List}, i.e. a {@code List} of one element.
     *
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    static <K, V> Map<K, V> of(Entry<? extends K, ? extends V> entry) {
        final HashMap<K, V> map = HashMap.empty();
        return map.put(entry.key, entry.value);
    }

    /**
     * Creates a Map of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SafeVarargs
    static <K, V> Map<K, V> of(Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashMap<K, V> map = HashMap.empty();
        for (Entry<? extends K, ? extends V> entry : entries) {
            map = map.put(entry.key, entry.value);
        }
        return map;
    }

    /**
     * Creates a Map of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> ofAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof HashMap) {
            return (HashMap<K, V>) entries;
        } else {
            HashMap<K, V> map = HashMap.empty();
            for (Entry<? extends K, ? extends V> entry : entries) {
                map = map.put(entry.key, entry.value);
            }
            return map;
        }
    }

    @Override
    default V apply(K key) {
        return get(key);
    }

    int size();

    Iterator<Entry<K, V>> iterator();

    boolean containsKey(K key);

    // TODO
    //boolean containsValue(V value);

    // TODO
    // Set<Entry<K, V>> entrySet();

    V get(K key);

    V getOrDefault(K key, V defaultValue);

    // TODO
    // Set<K> keySet();

    Map<K, V> put(K key, V value);

    Map<K, V> remove(K key);

    // TODO
    // Traversable<V> values();

    final class Entry<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        public final K key;
        public final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
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
            return Objects.hash(key) * 31 + Objects.hash(value);
        }

        @Override
        public String toString() {
            return key + " -> " + value;
        }
    }
}