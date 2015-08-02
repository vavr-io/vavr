/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Objects;

interface Map<K, V>  {

    boolean containsKey(K key);

    // TODO
    //boolean containsValue(V value);

    // we will add this when we have Set
    // Set<Entry<K, V>> entrySet();

    V get(K key);

    V getOrDefault(K key, V defaultValue);

    // we will add this when we have Set
    // Set<K> keySet();

    Map<K, V> put(K key, V value);

    Map<K, V> remove(K key);

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
            return key + " -> "+ value;
        }
    }
}