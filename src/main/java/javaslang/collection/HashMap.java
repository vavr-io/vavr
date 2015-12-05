/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @author Ruslan Sennov, Patryk Najda, Daniel Dietrich
 * @since 2.0.0
 */
public final class HashMap<K, V> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashMap<?, ?> EMPTY = new HashMap<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<K, V> trie;
    private final transient Lazy<Integer> hash;

    private HashMap(HashArrayMappedTrie<K, V> trie) {
        this.trie = trie;
        this.hash = Lazy.of(() -> Traversable.hash(trie::iterator));
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
        final Function<ArrayList<Tuple2<K, V>>, HashMap<K, V>> finisher = HashMap::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) EMPTY;
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
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> of(java.util.Map<? extends K, ? extends V> map) {
        return ((HashMap<K, V>) empty()).put(map);
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
     * Creates a HashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SafeVarargs
    public static <K, V> HashMap<K, V> ofAll(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            trie = trie.put(entry._1, entry._2);
        }
        return new HashMap<>(trie);
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
    public static <K, V> HashMap<K, V> ofAll(java.lang.Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof HashMap) {
            return (HashMap<K, V>) entries;
        } else {
            HashArrayMappedTrie<K, V> trie = HashArrayMappedTrie.empty();
            for (Tuple2<? extends K, ? extends V> entry : entries) {
                trie = trie.put(entry._1, entry._2);
            }
            return new HashMap<>(trie);
        }
    }

    @Override
    public HashMap<K, V> clear() {
        return HashMap.empty();
    }

    @Override
    public boolean containsKey(K key) {
        return trie.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return iterator().map(entry -> entry._2).contains(value);
    }

    @Override
    public HashMap<K, V> distinct() {
        return this;
    }

    @Override
    public HashMap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return HashMap.ofAll(iterator().distinctBy(comparator));
    }

    @Override
    public <U> HashMap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return HashMap.ofAll(iterator().distinctBy(keyExtractor));
    }

    @Override
    public HashMap<K, V> drop(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        return HashMap.ofAll(iterator().drop(n));
    }

    @Override
    public HashMap<K, V> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        return HashMap.ofAll(iterator().dropRight(n));
    }

    @Override
    public HashMap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public HashMap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return HashMap.ofAll(iterator().dropWhile(predicate));
    }

    @Override
    public HashMap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return foldLeft(HashMap.<K, V> empty(), (acc, entry) -> {
            if (predicate.test(entry)) {
                return acc.put(entry);
            } else {
                return acc;
            }
        });
    }

    @Override
    public <U, W> HashMap<U, W> flatMap(BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Tuple2<? extends U, ? extends W>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.<U, W> empty(), (acc, entry) -> {
            for (Tuple2<? extends U, ? extends W> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    public <U> Seq<U> flatMap(Function<? super Tuple2<K, V>, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return iterator().flatMap(mapper).toStream();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return foldLeft(zero, (u, t) -> f.apply(t, u));
    }

    @Override
    public Option<V> get(K key) {
        return trie.get(key);
    }

    @Override
    public <C> Map<C, HashMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return foldLeft(HashMap.empty(), (map, entry) -> {
            final C key = classifier.apply(entry);
            final HashMap<K, V> values = map
                    .get(key)
                    .map(entries -> entries.put(entry))
                    .orElse(HashMap.of(entry));
            return map.put(key, values);
        });
    }

    @Override
    public Iterator<HashMap<K, V>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
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
    public Option<Tuple2<K, V>> headOption() {
        return isEmpty() ? None.instance() : new Some<>(head());
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
    public Option<HashMap<K, V>> initOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(init());
        }
    }

    @Override
    public boolean isEmpty() {
        return trie.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
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
    public int length() {
        return trie.size();
    }

    @Override
    public <U> Seq<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return iterator().map(mapper).toStream();
    }

    @Override
    public <U, W> HashMap<U, W> map(BiFunction<? super K, ? super V, ? extends Tuple2<? extends U, ? extends W>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.empty(), (acc, entry) -> acc.put(entry.flatMap((BiFunction<K, V, Tuple2<? extends U, ? extends W>>) mapper::apply)));
    }

    @Override
    public <W> HashMap<K, W> mapValues(Function<? super V, ? extends W> mapper)  {
        Objects.requireNonNull(mapper, "mapper is null");
        return map((k, v) -> Tuple.of(k, mapper.apply(v)));
    }

    @Override
    public HashMap<K, V> merge(Map<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return HashMap.ofAll(that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            return that.foldLeft(this, (map, entry) -> !map.containsKey(entry._1) ? map.put(entry) : map);
        }
    }

    @Override
    public <U extends V> HashMap<K, V> merge(Map<? extends K, U> that,
                                             BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return HashMap.ofAll(that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            return that.foldLeft(this, (map, entry) -> {
                final K key = entry._1;
                final U value = entry._2;
                final V newValue = map.get(key).map(v -> (V) collisionResolution.apply(v, value)).orElse(value);
                return map.put(key, newValue);
            });
        }
    }

    @Override
    public Tuple2<HashMap<K, V>, HashMap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> p = iterator().partition(predicate);
        return Tuple.of(HashMap.ofAll(p._1), HashMap.ofAll(p._2));
    }

    @Override
    public HashMap<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(iterator().next());
        }
        return this;
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        return new HashMap<>(trie.put(key, value));
    }

    @Override
    public HashMap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        return put(entry._1, entry._2);
    }

    @Override
    public HashMap<K, V> put(java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        HashArrayMappedTrie<K, V> tree = HashArrayMappedTrie.empty();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            tree = tree.put(entry.getKey(), entry.getValue());
        }
        return tree.isEmpty() ? empty() : new HashMap<>(tree);
    }

    @Override
    public HashMap<K, V> remove(K key) {
        return new HashMap<>(trie.remove(key));
    }

    @Override
    public HashMap<K, V> removeAll(java.lang.Iterable<? extends K> keys) {
        Objects.requireNonNull(keys, "keys is null");
        HashArrayMappedTrie<K, V> result = trie;
        for (K key : keys) {
            result = result.remove(key);
        }
        return result.isEmpty() ? empty() : new HashMap<>(result);
    }

    @Override
    public HashMap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return containsKey(currentElement._1) ? remove(currentElement._1).put(newElement) : this;
    }

    @Override
    public HashMap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public HashMap<K, V> retainAll(java.lang.Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<K, V> tree = HashArrayMappedTrie.empty();
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                tree = tree.put(entry._1, entry._2);
            }
        }
        return tree.isEmpty() ? empty() : new HashMap<>(tree);
    }

    @Override
    public HashMap<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, HashMap.empty(), HashMap::put, Function.identity());
    }

    @Override
    public <U> Seq<U> scanLeft(U zero, BiFunction<? super U, ? super Tuple2<K, V>, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, List.empty(), List::prepend, List::reverse);
    }

    @Override
    public <U> Seq<U> scanRight(U zero, BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, List.empty(), List::prepend, Function.identity());
    }

    @Override
    public int size() {
        return trie.size();
    }

    @Override
    public Iterator<HashMap<K, V>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<HashMap<K, V>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(HashMap::ofAll);
    }

    @Override
    public Tuple2<HashMap<K, V>, HashMap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = iterator().span(predicate);
        return Tuple.of(HashMap.ofAll(t._1), HashMap.ofAll(t._2));
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
    public Option<HashMap<K, V>> tailOption() {
        if (trie.isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(tail());
        }
    }

    @Override
    public HashMap<K, V> take(int n) {
        if (trie.size() <= n) {
            return this;
        } else {
            return HashMap.ofAll(trie.iterator().take(n));
        }
    }

    @Override
    public HashMap<K, V> takeRight(int n) {
        if (trie.size() <= n) {
            return this;
        } else {
            return HashMap.ofAll(trie.iterator().takeRight(n));
        }
    }

    @Override
    public HashMap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public HashMap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final HashMap<K, V> taken = HashMap.ofAll(iterator().takeWhile(predicate));
        return taken.length() == length() ? this : taken;
    }

    @Override
    public Seq<V> values() {
        return map(Tuple2::_2);
    }

    @Override
    public int hashCode() {
        return hash.get();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof HashMap) {
            final HashMap<?, ?> that = (HashMap<?, ?>) o;
            return this.corresponds(that, Objects::equals);
        } else {
            return false;
        }
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public String toString() {
        return mkString("HashMap(", ", ", ")");
    }
}
