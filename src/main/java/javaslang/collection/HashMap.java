/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @since 2.0.0
 */
class HashMap<K, V> implements Map<K, V> {

    private static final HashMap<?, ?> EMPTY = new HashMap<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<K, V> tree;

    private HashMap(HashArrayMappedTrie<K, V> tree) {
        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) EMPTY;
    }

    /**
     * Returns a singleton {@code List}, i.e. a {@code List} of one element.
     *
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    static <K, V> HashMap<K, V> of(Entry<? extends K, ? extends V> entry) {
        return HashMap.<K, V> empty().put(entry.key, entry.value);
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
    static <K, V> HashMap<K, V> of(Entry<? extends K, ? extends V>... entries) {
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
    static <K, V> HashMap<K, V> ofAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
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
    public boolean containsKey(K key) {
        return tree.containsKey(key);
    }

    @Override
    public V get(K key) {
        return getOrDefault(key, null);
    }

    @Override
    public Option<V> getOption(K key) {
        if (containsKey(key)) {
            return new Some<>(get(key));
        } else {
            return None.instance();
        }
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return tree.get(key).orElse(defaultValue);
    }

    @Override
    public <C> Map<C, HashMap<K, V>> groupBy(Function<? super Entry<? super K, ? super V>, ? extends C> classifier) {
        Map<C, HashMap<K, V>> result = HashMap.empty();
        for (Entry<K, V> entry : this) {
            final C key = classifier.apply(entry);
            final HashMap<K, V> map = result.get(key);
            result = result.put(key, (map == null) ? HashMap.of(entry) : map.put(entry.key, entry.value));
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return tree.iterator().map(Entry::of);
    }

    @Override
    public HashMap<K, V> peek(Consumer<? super Entry<K, V>> action) {
        if (!isEmpty()) {
            action.accept(iterator().next());
        }
        return this;
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        return new HashMap<>(tree.put(key, value));
    }

    @Override
    public HashMap<K, V> remove(K key) {
        return new HashMap<>(tree.remove(key));
    }

    @Override
    public int size() {
        return tree.size();
    }
}
