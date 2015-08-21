/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Lazy;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @since 2.0.0
 */
public final class HashMap<K, V> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashMap<?, ?> EMPTY = new HashMap<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<K, V> tree;
    private final transient Lazy<Integer> hash;

    private HashMap(HashArrayMappedTrie<K, V> tree) {
        this.tree = tree;
        this.hash = Lazy.of(() -> Traversable.hash(tree::iterator));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
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
    public static <K, V> HashMap<K, V> of(Entry<? extends K, ? extends V> entry) {
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
    public static <K, V> HashMap<K, V> of(Entry<? extends K, ? extends V>... entries) {
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
    public static <K, V> HashMap<K, V> ofAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
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
    public HashMap<K, V> clear() {
        return HashMap.empty();
    }

    @Override
    public boolean contains(Entry<K, V> element) {
        return get(element.key).map(v -> Objects.equals(v, element.value)).orElse(false);
    }

    @Override
    public boolean containsKey(K key) {
        return tree.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public HashMap<K, V> distinct() {
        return this;
    }

    @Override
    public HashMap<K, V> distinctBy(Comparator<? super Entry<K, V>> comparator) {
        return null;
    }

    @Override
    public <U> HashMap<K, V> distinctBy(Function<? super Entry<K, V>, ? extends U> keyExtractor) {
        return null;
    }

    @Override
    public HashMap<K, V> drop(int n) {
        return null;
    }

    @Override
    public HashMap<K, V> dropRight(int n) {
        return null;
    }

    @Override
    public HashMap<K, V> dropWhile(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public HashMap<K, V> filter(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public HashMap<K, V> findAll(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public Option<Entry<K, V>> findLast(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public <U, W> Map<U, W> flatMap(BiFunction<? super K, ? super V, ? extends Map<? extends U, ? extends W>> mapper) {
        return null;
    }

    @Override
    public <U> Set<U> flatMap(Function<? super Entry<K, V>, ? extends Iterable<? extends U>> mapper) {
        return null;
    }

    @Override
    public <U> Set<U> flatMapM(Function<? super Entry<K, V>, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper) {
        return null;
    }

    @Override
    public Set<Object> flatten() {
        return null;
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super Entry<K, V>, ? super U, ? extends U> f) {
        return null;
    }

    @Override
    public Option<V> get(K key) {
        return tree.get(key);
    }

    @Override
    public <C> Map<C, HashMap<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, entry) -> {
            final C key = classifier.apply(entry);
            final HashMap<K, V> values = map
                    .get(key)
                    .map(entries -> entries.put(entry.key, entry.value))
                    .orElse(HashMap.of(entry));
            return map.put(key, values);
        });
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public Entry<K, V> head() {
        return null;
    }

    @Override
    public Option<Entry<K, V>> headOption() {
        return null;
    }

    @Override
    public HashMap<K, V> init() {
        return null;
    }

    @Override
    public Option<HashMap<K, V>> initOption() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return tree.iterator().map(Entry::of);
    }

    @Override
    public Set<K> keySet() {
        return map(entry -> entry.key);
    }

    @Override
    public int length() {
        return tree.size();
    }

    @Override
    public <U, W> Map<U, W> map(BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper) {
        return null;
    }

    @Override
    public <U> Set<U> map(Function<? super Entry<K, V>, ? extends U> mapper) {
        return null;
    }

    @Override
    public HashMap<K, V> merged(HashMap<K, ? extends V> that) {
        return merged(that, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends V> HashMap<K, V> merged(HashMap<K, U> that, BiFunction<? super V, ? super U, ? extends V> mergef) {
        if(isEmpty()) {
            return (HashMap<K, V>) that;
        }
        if(that.isEmpty()) {
            return this;
        }
        HashMap<K, V> result = this;
        for (Entry<K, U> e : that) {
            Option<V> old = result.get(e.key);
            if (old.isDefined() && mergef != null) {
                result = result.put(e.key, mergef.apply(old.get(), e.value));
            } else {
                result = result.put(e.key, e.value);
            }
        }
        return result;
    }

    @Override
    public Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(Predicate<? super Entry<K, V>> predicate) {
        return null;
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
    public Entry<K, V> reduceRight(BiFunction<? super Entry<K, V>, ? super Entry<K, V>, ? extends Entry<K, V>> op) {
        return null;
    }

    @Override
    public HashMap<K, V> remove(K key) {
        return new HashMap<>(tree.remove(key));
    }

    @Override
    public Map<K, V> removeAll(Iterable<? extends K> keys) {
        return null;
    }

    @Override
    public Map<K, V> replace(Entry<K, V> currentElement, Entry<K, V> newElement) {
        return null;
    }

    @Override
    public Map<K, V> replaceAll(Entry<K, V> currentElement, Entry<K, V> newElement) {
        return null;
    }

    @Override
    public Map<K, V> replaceAll(UnaryOperator<Entry<K, V>> operator) {
        return null;
    }

    @Override
    public Map<K, V> retainAll(Iterable<? extends Entry<K, V>> elements) {
        return null;
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public Set<? extends Map<K, V>> sliding(int size) {
        return null;
    }

    @Override
    public Set<? extends Map<K, V>> sliding(int size, int step) {
        return null;
    }

    @Override
    public Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public Map<K, V> tail() {
        return null;
    }

    @Override
    public Option<? extends Map<K, V>> tailOption() {
        return null;
    }

    @Override
    public Map<K, V> take(int n) {
        return null;
    }

    @Override
    public Map<K, V> takeRight(int n) {
        return null;
    }

    @Override
    public Map<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate) {
        return null;
    }

    @Override
    public <K1, V1, K2, V2> Tuple2<HashMap<K1, V1>, HashMap<K2, V2>> unzip(Function<? super Entry<? super K, ? super V>, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper) {
        return null;
    }

    @Override
    public <K1, V1, K2, V2> Tuple2<HashMap<K1, V1>, HashMap<K2, V2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper) {
        return null;
    }

    @Override
    public Seq<V> values() {
        return null;
    }

    @Override
    public <U> HashMap<Tuple2<K, V>, U> zip(Iterable<U> that) {
        return null;
    }

    @Override
    public <U> HashMap<Tuple2<K, V>, U> zipAll(Iterable<U> that, Entry<K, V> thisElem, U thatElem) {
        return null;
    }

    @Override
    public HashMap<Tuple2<K, V>, Integer> zipWithIndex() {
        return null;
    }

    @Override
    public int hashCode() {
        return hash.get();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof HashSet) {
            final HashSet<?> that = (HashSet<?>) o;
            return this.iterator().equals(that.iterator());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return mkString(", ", "HashMap(", ")");
    }
}
