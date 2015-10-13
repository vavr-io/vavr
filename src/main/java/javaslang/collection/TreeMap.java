/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Map.Entry;
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
        final Comparator<? super K> comparator = naturalComparator();
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
        return of((Comparator<? super K> & Serializable) K::compareTo, entry);
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
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> of(Entry<? extends K, ? extends V>... entries) {
        return of((Comparator<? super K> & Serializable) K::compareTo, entries);
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
    public static <K, V> TreeMap<K, V> of(Comparator<? super K> keyComparator,
                                          Entry<? extends K, ? extends V>... entries) {
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
    public static <K extends Comparable<? super K>, V> TreeMap<K, V> ofAll(
            java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        return ofAll((Comparator<? super K> & Serializable) K::compareTo, entries);
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
    public static <K, V> TreeMap<K, V> ofAll(Comparator<? super K> keyComparator,
                                             java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
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
        final V ignored = null;
        return entries.contains(new Entry<>(key, ignored));
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
        Objects.requireNonNull(predicate, "predicate is null");
        return createTreeMap(entries.comparator(), entries.iterator().filter(predicate));
    }

    @Override
    public Option<Entry<K, V>> findLast(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return entries.iterator().findLast(predicate);
    }

    @Override
    public <U, W> TreeMap<U, W> flatMap(
            BiFunction<? super K, ? super V, ? extends Iterable<? extends Entry<? extends U, ? extends W>>> mapper) {
        return flatMap(naturalComparator(), mapper);
    }

    @Override
    public <U, W> TreeMap<U, W> flatMap(Comparator<? super U> keyComparator,
                                        BiFunction<? super K, ? super V, ? extends Iterable<? extends Entry<? extends U, ? extends W>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return createTreeMap(entryComparator(keyComparator),
                entries.iterator().flatMap(entry -> mapper.apply(entry.key, entry.value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Seq<U> flatMap(Function<? super Entry<K, V>, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return (Seq<U>) entries.iterator().flatMap(mapper).toStream();
    }

    // DEV-NOTE: It is sufficient here to let the mapper return any Iterable, flatMap will do the rest.
    @SuppressWarnings("unchecked")
    @Override
    public TreeMap<Object, Object> flatten() {
        return flatMap((key, value) -> {
            if (value instanceof java.lang.Iterable) {
                final Iterator<?> entries = Iterator
                        .ofAll((java.lang.Iterable<?>) value)
                        .flatten()
                        .filter(e -> e instanceof Entry);
                if (entries.hasNext()) {
                    return (Iterator<? extends Entry<?, ?>>) entries;
                } else {
                    return List.of(new Entry<>(key, value));
                }
            } else if (value instanceof Entry) {
                return HashMap.of((Entry<?, ?>) value).flatten();
            } else {
                return List.of(new Entry<>(key, value));
            }
        });
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super Entry<K, V>, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    public Option<V> get(K key) {
        final V ignored = null;
        return entries.find(new Entry<>(key, ignored)).map(Entry::value);
    }

    @Override
    public <C> Map<C, TreeMap<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return foldLeft(HashMap.empty(), (map, entry) -> {
            final C key = classifier.apply(entry);
            final TreeMap<K, V> values = map.get(key).map(entries -> entries.put(entry.key, entry.value)).orElse(
                    createTreeMap(entries.comparator(), Iterator.of(entry)));
            return map.put(key, values);
        });
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public Entry<K, V> head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty TreeMap");
        } else {
            return entries.min().get();
        }
    }

    @Override
    public Option<Entry<K, V>> headOption() {
        return isEmpty() ? None.instance() : new Some<>(head());
    }

    @Override
    public TreeMap<K, V> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty TreeMap");
        } else {
            final Entry<K, V> max = entries.max().get();
            return new TreeMap<>(entries.delete(max));
        }
    }

    @Override
    public Option<TreeMap<K, V>> initOption() {
        return isEmpty() ? None.instance() : new Some<>(init());
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entries.iterator();
    }

    @Override
    public int length() {
        return entries.size();
    }

    @Override
    public Comparator<? super K> keyComparator() {
        final Comparator<? super Entry<K, V>> comparator = entries.comparator();
        final V ignored = null;
        return (Comparator<? super K> & Serializable) (k1, k2) -> comparator.compare(Entry.of(k1, ignored),
                Entry.of(k2, ignored));
    }

    @Override
    public SortedSet<K> keySet() {
        return entrySet().map(Entry::key);
    }

    @Override
    public <U, W> TreeMap<U, W> map(
            BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper) {
        return map(naturalComparator(), mapper);
    }

    @Override
    public <U, W> TreeMap<U, W> map(Comparator<? super U> keyComparator,
                                    BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return createTreeMap(entryComparator(keyComparator),
                entries.iterator().map(entry -> mapper.apply(entry.key, entry.value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Seq<U> map(Function<? super Entry<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return (Seq<U>) entries.iterator().map(mapper).toStream();
    }

    @Override
    public Option<Entry<K, V>> max() {
        return entries.max();
    }

    @Override
    public TreeMap<K, V> merge(Map<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return createTreeMap(entries.comparator(), that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            return that.foldLeft(this, (map, entry) -> !map.containsKey(entry.key) ? map.put(entry) : map);
        }
    }

    @Override
    public <U extends V> TreeMap<K, V> merge(Map<? extends K, U> that,
                                             BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return createTreeMap(entries.comparator(), that);
        } else if (that.isEmpty()) {
            return this;
        } else {
            return that.foldLeft(this, (map, entry) -> {
                final K key = entry.key;
                final U value = entry.value;
                final V newValue = map.get(key).map(v -> (V) collisionResolution.apply(v, value)).orElse(value);
                return map.put(key, newValue);
            });
        }
    }

    @Override
    public Option<Entry<K, V>> min() {
        return entries.min();
    }

    @Override
    public Tuple2<TreeMap<K, V>, TreeMap<K, V>> partition(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Entry<K, V>>, Iterator<Entry<K, V>>> p = iterator().partition(predicate);
        final TreeMap<K, V> treeMap1 = createTreeMap(entries.comparator(), p._1);
        final TreeMap<K, V> treeMap2 = createTreeMap(entries.comparator(), p._2);
        return Tuple.of(treeMap1, treeMap2);
    }

    @Override
    public TreeMap<K, V> peek(Consumer<? super Entry<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(entries.min().get());
        }
        return this;
    }

    @Override
    public Entry<K, V> reduceRight(BiFunction<? super Entry<K, V>, ? super Entry<K, V>, ? extends Entry<K, V>> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceRight on empty TreeMap");
        } else {
            return iterator().reduceRight(op);
        }
    }

    @Override
    public TreeMap<K, V> put(K key, V value) {
        return new TreeMap<>(entries.insert(Entry.of(key, value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public TreeMap<K, V> put(Entry<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return new TreeMap<>(entries.insert((Entry<K, V>) entry));
    }

    @SuppressWarnings("unchecked")
    @Override
    public TreeMap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return new TreeMap<>(entries.insert((Entry<K, V>) Entry.of(entry)));
    }

    @Override
    public TreeMap<K, V> remove(K key) {
        final V ignored = null;
        final Entry<K, V> entry = Entry.of(key, ignored);
        if (entries.contains(entry)) {
            return new TreeMap<>(entries.delete(entry));
        } else {
            return this;
        }
    }

    @Override
    public TreeMap<K, V> removeAll(Iterable<? extends K> keys) {
        final V ignored = null;
        RedBlackTree<Entry<K, V>> removed = entries;
        for (K key : keys) {
            final Entry<K, V> entry = Entry.of(key, ignored);
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
    public TreeMap<K, V> replace(Entry<K, V> currentElement, Entry<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return containsKey(currentElement.key) ? put(newElement) : this;
    }

    @Override
    public TreeMap<K, V> replaceAll(Entry<K, V> currentElement, Entry<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public TreeMap<K, V> retainAll(Iterable<? extends Entry<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        RedBlackTree<Entry<K, V>> tree = RedBlackTree.empty(entries.comparator());
        for (Entry<K, V> entry : elements) {
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
    public Tuple2<TreeMap<K, V>, TreeMap<K, V>> span(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Entry<K, V>>, Iterator<Entry<K, V>>> t = iterator().span(predicate);
        final TreeMap<K, V> treeMap1 = createTreeMap(entries.comparator(), t._1);
        final TreeMap<K, V> treeMap2 = createTreeMap(entries.comparator(), t._2);
        return Tuple.of(treeMap1, treeMap2);
    }

    @Override
    public TreeMap<K, V> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty TreeMap");
        } else {
            final Entry<K, V> min = entries.min().get();
            return new TreeMap<>(entries.delete(min));
        }
    }

    @Override
    public Option<TreeMap<K, V>> tailOption() {
        return isEmpty() ? None.instance() : new Some<>(tail());
    }

    @Override
    public TreeMap<K, V> take(int n) {
        return createTreeMap(entries.comparator(), entries.iterator().take(n));
    }

    @Override
    public TreeMap<K, V> takeRight(int n) {
        return createTreeMap(entries.comparator(), entries.iterator().takeRight(n));
    }

    @Override
    public TreeMap<K, V> takeUntil(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createTreeMap(entries.comparator(), entries.iterator().takeUntil(predicate));
    }

    @Override
    public TreeMap<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return createTreeMap(entries.comparator(), entries.iterator().takeWhile(predicate));
    }

    @Override
    public Seq<V> values() {
        return iterator().map(Entry::value).toStream();
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
    private static <K, V> TreeMap<K, V> createTreeMap(Comparator<? super Entry<K, V>> comparator,
                                                      java.lang.Iterable<? extends Entry<? extends K, ? extends V>> entries) {
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
        return mkString(", ", "TreeMap(", ")");
    }
}
