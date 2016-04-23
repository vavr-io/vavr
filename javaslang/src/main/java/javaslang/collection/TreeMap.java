/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind2;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;

/**
 * SortedMap implementation, backed by a Red/Black Tree.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
// DEV-NOTE: use entries.min().get() in favor of iterator().next(), it is faster!
public final class TreeMap<K, V> extends AbstractMap<K, V, TreeMap<K, V>> implements Kind2<TreeMap<?, ?>, K, V>, SortedMap<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private final RedBlackTree<Tuple2<K, V>> entries;

    private TreeMap(RedBlackTree<Tuple2<K, V>> entries) {
        this.entries = entries;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a
     * {@link javaslang.collection.TreeMap}.
     * <p>
     * The natural comparator is used to compare TreeMap keys.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link javaslang.collection.TreeMap} Collector.
     */
    public static <K extends Comparable<? super K>, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, TreeMap<K, V>> collector() {
        return collector((Comparator<? super K> & Serializable) K::compareTo);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a
     * {@link javaslang.collection.TreeMap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator A key comparator
     * @return A {@link javaslang.collection.TreeMap} Collector.
     */
    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, TreeMap<K, V>> collector(Comparator<? super K> keyComparator) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        final Supplier<ArrayList<Tuple2<K, V>>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Tuple2<K, V>>, Tuple2<K, V>> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Tuple2<K, V>>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Tuple2<K, V>>, TreeMap<K, V>> finisher = list -> TreeMap.ofEntries(keyComparator, list);
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
        return empty((Comparator<? super K> & Serializable) K::compareTo);
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
        return new TreeMap<>(RedBlackTree.empty(new EntryComparator<>(keyComparator)));
    }

    /**
     * Narrows a widened {@code TreeMap<? extends K, ? extends V>} to {@code TreeMap<K, V>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     * <p>
     * CAUTION: If {@code K} is narrowed, the underlying {@code Comparator} might fail!
     *
     * @param treeMap A {@code TreeMap}.
     * @param <K>     Key type
     * @param <V>     Value type
     * @return the given {@code treeMap} instance as narrowed type {@code TreeMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    static <K, V> TreeMap<K, V> narrow(TreeMap<? extends K, ? extends V> treeMap) {
        return (TreeMap<K, V>) treeMap;
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
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(Tuple2<? extends K, ? extends V> entry) {
        return of((Comparator<? super K> & Serializable) K::compareTo, entry);
    }

    /**
     * Creates a TreeMap of the given list of key-value pairs.
     *
     * @param pairs A list of key-value pairs
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(Object... pairs) {
        return of((Comparator<? super K> & Serializable) K::compareTo, pairs);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator, Object... pairs) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        RedBlackTree<Tuple2<K, V>> result = RedBlackTree.empty(new EntryComparator<>(keyComparator));
        for (int i = 0; i < pairs.length; i += 2) {
            result = result.insert(Tuple.of((K) pairs[i], (V) pairs[i + 1]));
        }
        return new TreeMap<>(result);
    }

    /**
     * Returns a {@code TreeMap}, from a source java.util.Map.
     *
     * @param map A map entry.
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given map
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        RedBlackTree<Tuple2<K, V>> result = RedBlackTree.empty();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result = result.insert(Tuple.of(entry.getKey(), entry.getValue()));
        }
        return new TreeMap<>(result);
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
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator, Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entry, "entry is null");
        return TreeMap.<K, V> empty(keyComparator).put(entry);
    }

    /**
     * Returns a singleton {@code TreeMap}, i.e. a {@code TreeMap} of one element.
     *
     * @param key   A singleton map key.
     * @param value A singleton map value.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(K key, V value) {
        return of((Comparator<? super K> & Serializable) K::compareTo, key, value);
    }

    /**
     * Returns a singleton {@code TreeMap}, i.e. a {@code TreeMap} of one element.
     *
     * @param key           A singleton map key.
     * @param value         A singleton map value.
     * @param <K>           The key type
     * @param <V>           The value type
     * @param keyComparator The comparator used to sort the entries by their key.
     * @return A new Map containing the given entry
     */
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator, K key, V value) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        return TreeMap.<K, V> empty(keyComparator).put(key, value);
    }

    /**
     * Returns a TreeMap containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param keyComparator The comparator used to sort the entries by their key
     * @param n             The number of elements in the TreeMap
     * @param f             The Function computing element values
     * @return A TreeMap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code keyComparator} or {@code f} are null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> TreeMap<K, V> tabulate(Comparator<? super K> keyComparator, int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(f, "f is null");
        return ofEntries(keyComparator, Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    /**
     * Returns a TreeMap containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     * The underlying key comparator is the natural comparator of K.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the TreeMap
     * @param f   The Function computing element values
     * @return A TreeMap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return tabulate((Comparator<? super K> & Serializable) K::compareTo, n, f);
    }

    /**
     * Returns a TreeMap containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <K>           The key type
     * @param <V>           The value type
     * @param keyComparator The comparator used to sort the entries by their key
     * @param n             The number of elements in the TreeMap
     * @param s             The Supplier computing element values
     * @return A TreeMap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code keyComparator} or {@code s} are null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> TreeMap<K, V> fill(Comparator<? super K> keyComparator, int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(s, "s is null");
        return ofEntries(keyComparator, Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    /**
     * Returns a TreeMap containing {@code n} values supplied by a given Supplier {@code s}.
     * The underlying key comparator is the natural comparator of K.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the TreeMap
     * @param s   The Supplier computing element values
     * @return A TreeMap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return fill((Comparator<? super K> & Serializable) K::compareTo, n, s);
    }

    /**
     * Creates a {@code TreeMap} of the given entries using the natural key comparator.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Map entries
     * @return A new TreeMap containing the given entries.
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
    }

    /**
     * Creates a {@code TreeMap} of the given entries using the natural key comparator.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Map entries
     * @return A new TreeMap containing the given entries.
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
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
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <K, V> TreeMap<K, V> ofEntries(Comparator<? super K> keyComparator, Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entries, "entries is null");
        RedBlackTree<Tuple2<K, V>> tree = RedBlackTree.empty(new EntryComparator<>(keyComparator));
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            tree = tree.insert((Tuple2<K, V>) entry);
        }
        return tree.isEmpty() ? TreeMap.empty(keyComparator) : new TreeMap<>(tree);
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
    public static <K, V> TreeMap<K, V> ofEntries(Comparator<? super K> keyComparator, java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entries, "entries is null");
        RedBlackTree<Tuple2<K, V>> tree = RedBlackTree.empty(new EntryComparator<>(keyComparator));
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            tree = tree.insert(Tuple.of(entry.getKey(), entry.getValue()));
        }
        return tree.isEmpty() ? TreeMap.empty(keyComparator) : new TreeMap<>(tree);
    }

    /**
     * Creates a {@code TreeMap} of the given entries.
     *
     * @param <K>     The key type
     * @param <V>     The value type
     * @param entries Map entries
     * @return A new TreeMap containing the given entries.
     */
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofEntries(
            Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return ofEntries((Comparator<? super K> & Serializable) K::compareTo, entries);
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
    public static <K, V> TreeMap<K, V> ofEntries(Comparator<? super K> keyComparator, Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(keyComparator, "keyComparator is null");
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof TreeMap) {
            return (TreeMap<K, V>) entries;
        } else {
            RedBlackTree<Tuple2<K, V>> tree = RedBlackTree.empty(new EntryComparator<>(keyComparator));
            for (Tuple2<? extends K, ? extends V> entry : entries) {
                tree = tree.insert((Tuple2<K, V>) entry);
            }
            return new TreeMap<>(tree);
        }
    }

    @Override
    TreeMap<K, V> createFromEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> it) {
        return createTreeMap(entries.comparator(), it);
    }

    @Override
    TreeMap<K, V> emptyInstance() {
        return isEmpty() ? this : new TreeMap<>(entries.emptyInstance());
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        return bimap(naturalComparator(), keyMapper, valueMapper);
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> bimap(Comparator<? super K2> keyComparator,
                                          Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return createTreeMap(new EntryComparator<>(keyComparator),
                entries.iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2))));
    }

    @Override
    public boolean containsKey(K key) {
        final V ignored = null;
        return entries.contains(new Tuple2<>(key, ignored));
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        return flatMap(naturalComparator(), mapper);
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> flatMap(Comparator<? super K2> keyComparator,
                                            BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return createTreeMap(new EntryComparator<>(keyComparator),
                entries.iterator().flatMap(entry -> mapper.apply(entry._1, entry._2)));
    }

    @Override
    public Option<V> get(K key) {
        final V ignored = null;
        return entries.find(new Tuple2<>(key, ignored)).map(Tuple2::_2);
    }

    @Override
    public Tuple2<K, V> head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty TreeMap");
        } else {
            return entries.min().get();
        }
    }

    @Override
    public Tuple2<K, V> last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty TreeMap");
        } else {
            return entries.max().get();
        }
    }

    @Override
    public TreeMap<K, V> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty TreeMap");
        } else {
            final Tuple2<K, V> max = entries.max().get();
            return new TreeMap<>(entries.delete(max));
        }
    }

    @Override
    public Option<Tuple2<K, V>> max() {
        return entries.max();
    }

    @Override
    public Option<Tuple2<K, V>> min() {
        return entries.min();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return entries.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Comparator<K> keyComparator() {
        return ((EntryComparator<K, V>) entries.comparator()).keyComparator;
    }

    @Override
    public SortedSet<K> keySet() {
        return TreeSet.ofAll(keyComparator(), iterator().map(Tuple2::_1));
    }

    @Override
    public Iterator<Tuple2<K, V>> descendingIterator() {
        return entries.descendingIterator();
    }

    @Override
    public SortedSet<K> descendingKeySet() {
        return descendingMap().keySet();
    }

    @Override
    public SortedMap<K, V> descendingMap() {
        return new TreeMap<>(entries.descendingTree());
    }

    @Override
    public SortedMap<K, V> subMap(Option<K> fromKey, boolean fromInclusive, Option<K> toKey, boolean toInclusive) {
        return new TreeMap<>(
                entries.subTree(
                    fromKey.map(k -> Tuple.of(k, null)), fromInclusive,
                    toKey.map(k -> Tuple.of(k, null)), toInclusive));
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return subMap(Option.some(fromKey), fromInclusive, Option.some(toKey), toInclusive);
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey, boolean inclusive) {
        return subMap(Option.none(), true, Option.some(toKey), inclusive);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return subMap(Option.some(fromKey), inclusive, Option.none(), true);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Option<Tuple2<K, V>> floor(K key) {
        return entries.floor(Tuple.of(key, null));
    }

    @Override
    public Option<K> floorKey(K key) {
        return floor(key).map(e -> e._1);
    }

    @Override
    public Option<Tuple2<K, V>> ceiling(K key) {
        return entries.ceiling(Tuple.of(key, null));
    }

    @Override
    public Option<K> ceilingKey(K key) {
        return ceiling(key).map(e -> e._1);
    }

    @Override
    public Option<Tuple2<K, V>> lower(K key) {
        return entries.lower(Tuple.of(key, null));
    }

    @Override
    public Option<K> lowerKey(K key) {
        return lower(key).map(e -> e._1);
    }

    @Override
    public Option<Tuple2<K, V>> higher(K key) {
        return entries.higher(Tuple.of(key, null));
    }

    @Override
    public Option<K> higherKey(K key) {
        return higher(key).map(e -> e._1);
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        return map(naturalComparator(), mapper);
    }

    @Override
    public <K2, V2> TreeMap<K2, V2> map(Comparator<? super K2> keyComparator,
                                        BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return createTreeMap(new EntryComparator<>(keyComparator),
                entries.iterator().map(entry -> mapper.apply(entry._1, entry._2)));
    }

    @Override
    public <W> TreeMap<K, W> mapValues(Function<? super V, ? extends W> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map(keyComparator(), (k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @Override
    public TreeMap<K, V> put(K key, V value) {
        return new TreeMap<>(entries.insert(new Tuple2<>(key, value)));
    }

    @Override
    public TreeMap<K, V> remove(K key) {
        final V ignored = null;
        final Tuple2<K, V> entry = new Tuple2<>(key, ignored);
        if (entries.contains(entry)) {
            return new TreeMap<>(entries.delete(entry));
        } else {
            return this;
        }
    }

    @Override
    public TreeMap<K, V> removeAll(Iterable<? extends K> keys) {
        final V ignored = null;
        RedBlackTree<Tuple2<K, V>> removed = entries;
        for (K key : keys) {
            final Tuple2<K, V> entry = new Tuple2<>(key, ignored);
            if (removed.contains(entry)) {
                removed = removed.delete(entry);
            }
        }
        if (removed.size() == entries.size()) {
            return this;
        } else {
            return new TreeMap<>(removed);
        }
    }

    @Override
    public TreeMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        RedBlackTree<Tuple2<K, V>> tree = RedBlackTree.empty(entries.comparator());
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                tree = tree.insert(entry);
            }
        }
        return new TreeMap<>(tree);
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public TreeMap<K, V> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty TreeMap");
        } else {
            final Tuple2<K, V> min = entries.min().get();
            return new TreeMap<>(entries.delete(min));
        }
    }

    @Override
    public java.util.TreeMap<K, V> toJavaMap() {
        return toJavaMap(() -> new java.util.TreeMap<>(keyComparator()), t -> t);
    }

    @Override
    public Seq<V> values() {
        return iterator().map(Tuple2::_2).toStream();
    }

    /**
     * Internal factory method, used with Tuple2 comparator instead of a key comparator.
     *
     * @param comparator An Tuple2 comparator
     * @param entries    Map entries
     * @param <K>        Key type
     * @param <V>        Value type
     * @return A new TreeMap.
     */
    @SuppressWarnings("unchecked")
    private static <K, V> TreeMap<K, V> createTreeMap(Comparator<? super Tuple2<K, V>> comparator,
                                                      Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        RedBlackTree<Tuple2<K, V>> tree = RedBlackTree.empty(comparator);
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            tree = tree.insert((Tuple2<K, V>) entry);
        }
        return tree.isEmpty() ? (TreeMap<K, V>) TreeMap.empty()
                              : new TreeMap<>(tree);
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
    public String stringPrefix() {
        return "TreeMap";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    /**
     * Used to compare entries by key and store the keyComparator for later access.
     *
     * @param <K> key type
     * @param <V> value type, needed at compile time for the Comparator interface
     */
    private static class EntryComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {
        private static final long serialVersionUID = 1L;

        final Comparator<K> keyComparator;

        @SuppressWarnings("unchecked")
        EntryComparator(Comparator<? super K> keyComparator) {
            this.keyComparator = (Comparator<K>) keyComparator;
        }

        @Override
        public int compare(Tuple2<K, V> e1, Tuple2<K, V> e2) {
            return keyComparator.compare(e1._1, e2._1);
        }

        @Override
        public Comparator<Tuple2<K, V>> reversed() {
            return new DescendingEntryComparator<>(keyComparator);
        }
    }

    private static class DescendingEntryComparator<K, V> extends EntryComparator<K, V> {
        private static final long serialVersionUID = 1L;

        DescendingEntryComparator(Comparator<? super K> keyComparator) {
            super(keyComparator);
        }

        @Override
        public int compare(Tuple2<K, V> e1, Tuple2<K, V> e2) {
            return keyComparator.compare(e2._1, e1._1);
        }

        @Override
        public Comparator<Tuple2<K, V>> reversed() {
            return new EntryComparator<>(keyComparator);
        }
    }

}
