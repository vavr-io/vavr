/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.util.Iterator;

/**
 * A {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 */
class HashMap<K, V> implements Map<K, V> {

    private static final HashMap<?, ?> INSTANCE = new HashMap<>(HashArrayMappedTrie.empty());

    @SuppressWarnings("unchecked")
    static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) INSTANCE;
    }

    private final HashArrayMappedTrie<K, V> tree;

    private HashMap(HashArrayMappedTrie<K, V> tree) {
        this.tree = tree;
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public V get(K key) {
        return getOrDefault(key, null);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return tree.get(key).orElse(defaultValue);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            Iterator<Tuple2<K, V>> it = tree.iterator();
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                Tuple2<K, V> t = it.next();
                return new Entry<>(t._1, t._2);
            }
        };
    }

    @Override
    public boolean containsKey(K key) {
        return tree.containsKey(key);
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        return new HashMap<>(tree.put(key, value));
    }

    @Override
    public HashMap<K, V> remove(K key) {
        return new HashMap<>(tree.remove(key));
    }

}
