/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.collection.Map.Entry;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * SortedMap implementation, backed by a Red/Black Tree.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public final class TreeMap<K, V> implements SortedMap<K, V>, Iterable<Entry<K, V>>, Serializable {

    private static final long serialVersionUID = 1L;

    private final RedBlackTree<Entry<K, V>> entries;

    private TreeMap(RedBlackTree<Entry<K, V>> entries) {
        this.entries = entries;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a
     * {@link javaslang.collection.TreeMap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link javaslang.collection.TreeMap} Collector.
     */
    public static <K, V> Collector<Entry<K, V>, ArrayList<Entry<K, V>>, TreeMap<K, V>> collector() {
        final Supplier<ArrayList<Entry<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Entry<K, V>>, Entry<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Entry<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Comparator<? super K> comparator = Comparators.naturalComparator();
        final Function<ArrayList<Entry<K, V>>, TreeMap<K, V>> finisher = list -> TreeMap.ofAll(comparator, list);
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the empty TreeMap. The underlying key comparator is the natural comparator of K.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A new empty TreeMap.
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> empty() {
        return empty(K::compareTo);
    }

    /**
     * Returns the empty TreeMap using the given key comparator.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param keyComparator The comparator used to sort the entries by their key.
     * @return A new empty TreeMap.
     */
    public static <K, V> TreeMap<K, V> empty(Comparator<? super K> keyComparator) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        return new TreeMap<>(RedBlackTree.empty(entryComparator(keyComparator)));
    }

    /**
     * Returns a singleton {@code TreeMap}, i.e. a {@code TreeMap} of one entry.
     * The underlying key comparator is the natural comparator of K.
     *
     * @param <K>   The key type
     * @param <V>   The value type
     * @param entry A map entry.
     * @return A new TreeMap containing the given entry.
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(Entry<? extends K, ? extends V> entry) {
        return of(K::compareTo, entry);
    }

    /**
     * Returns a singleton {@code TreeMap}, i.e. a {@code TreeMap} of one entry using a specific key comparator.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param entry         A map entry.
     * @param keyComparator The comparator used to sort the entries by their key.
     * @return A new TreeMap containing the given entry.
     */
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator, Entry<? extends K, ? extends V> entry) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entry, "entry is null");
        return TreeMap.<K, V> empty(keyComparator).put(entry);
    }

    /**
     * Creates a {@code TreeMap} of the given entries using the natural key comparator.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Map entries
     * @return A new TreeMap containing the given entries.
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(Entry<? extends K, ? extends V>... entries) {
        return of(K::compareTo, entries);
    }

    /**
     * Creates a {@code TreeMap} of the given entries using the given key comparator.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param entries       Map entries
     * @param keyComparator A key comparator
     * @return A new TreeMap containing the given entries.
     */
    @SafeVarargs
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator, Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entries, "entries is null");
        TreeMap<K, V> map = TreeMap.empty(keyComparator);
        for (Entry<? extends K, ? extends V> entry : entries) {
            map = map.put(entry);
        }
        return map;
    }

    /**
     * Creates a {@code TreeMap} of the given entries.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Map entries
     * @return A new TreeMap containing the given entries.
     */
    @SuppressWarnings("unchecked")
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofAll(java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        return ofAll(K::compareTo, entries);
    }

    /**
     * Creates a {@code TreeMap} of the given entries.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param entries       Map entries
     * @param keyComparator A key comparator
     * @return A new TreeMap containing the given entries.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> TreeMap<K, V> ofAll(Comparator<? super K> keyComparator, java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof TreeMap) {
            return (TreeMap<K, V>) entries;
        } else {
            TreeMap<K, V> map = TreeMap.empty(keyComparator);
            for (Entry<? extends K, ? extends V> entry : entries) {
                map = map.put(entry);
            }
            return map;
        }
    }

    @Override
    public TreeMap<K, V> clear() {
        return isEmpty() ? this : new TreeMap<>(entries.clear());
    }

    @Override
    public boolean contains(Entry<K, V> element) {
        return entries.contains(element);
    }

    @Override
    public TreeMap<K, V> distinct() {
        return this;
    }

    @Override
    public TreeMap<K, V> distinctBy(Comparator<? super Entry<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return createTreeMap(entries.comparator(), iterator().distinctBy(comparator));
    }

    @Override
    public <U> TreeMap<K, V> distinctBy(Function<? super Entry<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return createTreeMap(entries.comparator(), iterator().distinctBy(keyExtractor));
    }

    @Override
    public TreeMap<K, V> drop(int n) {
        if (n <= 0) {
            return this;
        } else {
            return createTreeMap(entries.comparator(), iterator().drop(n));
        }
    }

    @Override
    public TreeMap<K, V> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else {
            return createTreeMap(entries.comparator(), iterator().dropRight(n));
        }
    }

    @Override
    public TreeMap<K, V> dropWhile(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createTreeMap(entries.comparator(), iterator().dropWhile(predicate));
    }

    @Override
    public boolean containsKey(K key) {
        return entries.contains(new Entry<>(key, null));
    }

    @Override
    public boolean containsValue(V value) {
        return iterator().map(Entry::value).contains(value);
    }

    @Override
    public SortedSet<Entry<K, V>> entrySet() {
        return new TreeSet<>(entries);
    }

    @Override
    public TreeMap<K, V> filter(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Option<Entry<K, V>> findLast(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U> Set<U> flatMap(Function<? super Entry<K, V>, ? extends Iterable<? extends U>> mapper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U, W> TreeMap<U, W> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<? extends Entry<? extends U, ? extends W>>> mapper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Option<V> get(K key) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Set<Object> flatten() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super Entry<K, V>, ? super U, ? extends U> f) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <C> Map<C, TreeMap<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public boolean hasDefiniteSize() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Entry<K, V> head() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Option<Entry<K, V>> headOption() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> init() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Option<TreeMap<K, V>> initOption() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public boolean isTraversableAgain() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entries.iterator();
    }

    @Override
    public int length() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public SortedSet<K> keySet() {
        return entrySet().map(Entry::key);
    }

    @Override
    public <U> Set<U> map(Function<? super Entry<K, V>, ? extends U> mapper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U, W> TreeMap<U, W> map(BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> merge(Map<? extends K, ? extends V> that) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U extends V> TreeMap<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Tuple2<TreeMap<K, V>, TreeMap<K, V>> partition(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> peek(Consumer<? super Entry<K, V>> action) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Entry<K, V> reduceRight(BiFunction<? super Entry<K, V>, ? super Entry<K, V>, ? extends Entry<K, V>> op) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> put(K key, V value) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> put(Entry<? extends K, ? extends V> entry) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> remove(K key) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> removeAll(Iterable<? extends K> keys) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> replace(Entry<K, V> currentElement, Entry<K, V> newElement) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> replaceAll(Entry<K, V> currentElement, Entry<K, V> newElement) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> replaceAll(UnaryOperator<Entry<K, V>> operator) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> retainAll(Iterable<? extends Entry<K, V>> elements) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Tuple2<TreeMap<K, V>, TreeMap<K, V>> span(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> tail() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Option<TreeMap<K, V>> tailOption() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> take(int n) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> takeRight(int n) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> takeUntil(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <K1, V1, K2, V2> Tuple2<TreeMap<K1, V1>, TreeMap<K2, V2>> unzip(Function<? super Entry<? super K, ? super V>, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <K1, V1, K2, V2> Tuple2<TreeMap<K1, V1>, TreeMap<K2, V2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends Entry<? extends K1, ? extends V1>, ? extends Entry<? extends K2, ? extends V2>>> unzipper) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public Seq<V> values() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U> TreeMap<Tuple2<K, V>, U> zip(Iterable<U> that) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public <U> TreeMap<Tuple2<K, V>, U> zipAll(Iterable<U> that, Entry<K, V> thisElem, U thatElem) {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    @Override
    public TreeMap<Tuple2<K, V>, Integer> zipWithIndex() {
        throw new UnsupportedOperationException("TODO"); // TODO
    }

    private static <K, V> Comparator<Entry<K, V>> entryComparator(Comparator<? super K> keyComparator) {
        return (Comparator<Entry<K, V>> & Serializable) (e1, e2) -> keyComparator.compare(e1.key, e2.key);
    }

    /**
     * Internal factory method, used with Entry comparator instead of a key comparator.
     *
     * @param comparator An Entry comparator
     * @param entries    Map entries
     * @param <K>        Key type
     * @param <V>        Value type
     * @return A new TreeMap.
     */
    @SuppressWarnings("unchecked")
    private static <K, V> TreeMap<K, V> createTreeMap(Comparator<? super Entry<K, V>> comparator, java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        RedBlackTree<Entry<K, V>> tree = RedBlackTree.empty(comparator);
        for (Entry<? extends K, ? extends V> entry : entries) {
            tree = tree.insert((Entry<K, V>) entry);
        }
        return new TreeMap<>(tree);
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof TreeMap) {
            final TreeMap<?, ?> that = (TreeMap<?, ?>) o;
            return entries.equals(that.entries);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "TreeMap" + entries.toString();
    }
}
