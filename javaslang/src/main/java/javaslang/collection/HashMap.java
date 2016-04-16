/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @author Ruslan Sennov, Patryk Najda, Daniel Dietrich
 * @since 2.0.0
 */
public final class HashMap<K, V> extends AbstractMap<K, V, HashMap<K, V>> implements Kind2<HashMap<?, ?>, K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashMap<?, ?> EMPTY = new HashMap<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<K, V> trie;

    private HashMap(HashArrayMappedTrie<K, V> trie) {
        this.trie = trie;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.HashMap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link javaslang.collection.HashMap} Collector.
     */
    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, HashMap<K, V>> collector() {
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Tuple2<K, V>>, HashMap<K, V>> finisher = HashMap::ofEntries;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) EMPTY;
    }

    /**
     * Narrows a widened {@code HashMap<? extends K, ? extends V>} to {@code HashMap<K, V>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param hashMap A {@code HashMap}.
     * @param <K>     Key type
     * @param <V>     Value type
     * @return the given {@code hashMap} instance as narrowed type {@code HashMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> narrow(HashMap<? extends K, ? extends V> hashMap) {
        return (HashMap<K, V>) hashMap;
    }

    /**
     * Returns a singleton {@code HashMap}, i.e. a {@code HashMap} of one element.
     *
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K, V> HashMap<K, V> of(Tuple2<? extends K, ? extends V> entry) {
        return new HashMap<>(HashArrayMappedTrie.<K, V> empty().put(entry._1, entry._2));
    }

    /**
     * Returns a {@code HashMap}, from a source java.util.Map.
     *
     * @param map A map entry.
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given map
     */
    public static <K, V> HashMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        HashArrayMappedTrie<K, V> tree = HashArrayMappedTrie.empty();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            tree = tree.put(entry.getKey(), entry.getValue());
        }
        return wrap(tree);
    }

    /**
     * Returns a singleton {@code HashMap}, i.e. a {@code HashMap} of one element.
     *
     * @param key   A singleton map key.
     * @param value A singleton map value.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K, V> HashMap<K, V> of(K key, V value) {
        return new HashMap<>(HashArrayMappedTrie.<K, V> empty().put(key, value));
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param pairs A list of key-value pairs
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> of(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < pairs.length; i += 2) {
            trie = trie.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return wrap(trie);
    }

    /**
     * Returns an HashMap containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the HashMap
     * @param f   The Function computing element values
     * @return An HashMap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntries(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    /**
     * Returns an HashMap containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the HashMap
     * @param s   The Supplier computing element values
     * @return An HashMap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntries(Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    /**
     * Creates a HashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SafeVarargs
    public static <K, V> HashMap<K, V> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            trie = trie.put(entry.getKey(), entry.getValue());
        }
        return wrap(trie);
    }

    /**
     * Creates a HashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SafeVarargs
    public static <K, V> HashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            trie = trie.put(entry._1, entry._2);
        }
        return wrap(trie);
    }

    /**
     * Creates a HashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof HashMap) {
            return (HashMap<K, V>) entries;
        } else {
            HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
            for (Tuple2<? extends K, ? extends V> entry : entries) {
                trie = trie.put(entry._1, entry._2);
            }
            return trie.isEmpty() ? empty() : wrap(trie);
        }
    }

    @Override
    HashMap<K, V> createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return HashMap.ofEntries(entries);
    }

    @Override
    HashMap<K, V> emptyInstance() {
        return HashMap.empty();
    }

    @Override
    public <K2, V2> HashMap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return HashMap.ofEntries(entries);
    }

    @Override
    public boolean containsKey(K key) {
        return trie.containsKey(key);
    }

    @Override
    public <K2, V2> HashMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.<K2, V2> empty(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    public Option<V> get(K key) {
        return trie.get(key);
    }

    @Override
    public Tuple2<K, V> head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty HashMap");
        } else {
            return iterator().next();
        }
    }

    @Override
    public HashMap<K, V> init() {
        if (trie.isEmpty()) {
            throw new UnsupportedOperationException("init of empty HashMap");
        } else {
            return remove(last()._1);
        }
    }

    @Override
    public boolean isEmpty() {
        return trie.isEmpty();
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return trie.iterator();
    }

    @Override
    public Set<K> keySet() {
        return HashSet.ofAll(iterator().map(Tuple2::_1));
    }

    @Override
    public <K2, V2> HashMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.empty(), (acc, entry) -> acc.put(entry.map(mapper)));
    }

    @Override
    public <V2> HashMap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        return new HashMap<>(trie.put(key, value));
    }

    @Override
    public HashMap<K, V> remove(K key) {
        final HashArrayMappedTrie<K, V> result = trie.remove(key);
        return result.size() == trie.size() ? this : wrap(result);
    }

    @Override
    public HashMap<K, V> removeAll(Iterable<? extends K> keys) {
        Objects.requireNonNull(keys, "keys is null");
        HashArrayMappedTrie<K, V> result = trie;
        for (K key : keys) {
            result = result.remove(key);
        }

        if (result.isEmpty()) {
            return empty();
        } else if (result.size() == trie.size()) {
            return this;
        } else {
            return wrap(result);
        }
    }

    @Override
    public HashMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<K, V> tree = HashArrayMappedTrie.empty();
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                tree = tree.put(entry._1, entry._2);
            }
        }
        return wrap(tree);
    }

    @Override
    public int size() {
        return trie.size();
    }

    @Override
    public HashMap<K, V> tail() {
        if (trie.isEmpty()) {
            throw new UnsupportedOperationException("tail of empty HashMap");
        } else {
            return remove(head()._1);
        }
    }

    @Override
    public java.util.HashMap<K, V> toJavaMap() {
        return toJavaMap(java.util.HashMap::new, t -> t);
    }

    @Override
    public Seq<V> values() {
        return map(Tuple2::_2);
    }

    @Override
    public int hashCode() {
        return trie.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof HashMap) {
            final HashMap<?, ?> that = (HashMap<?, ?>) o;
            return this.trie.equals(that.trie);
        } else {
            return false;
        }
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public String stringPrefix() {
        return "HashMap";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    private static <K, V> HashMap<K, V> wrap(HashArrayMappedTrie<K, V> trie) {
        return trie.isEmpty() ? empty() : new HashMap<>(trie);
    }
}
