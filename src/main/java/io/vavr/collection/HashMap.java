/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * Implements an immutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> entries</li>
 *     <li>allows null keys and null values</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>does not guarantee a specific iteration order</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>put: O(1)</li>
 *     <li>remove: O(1)</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator.next(): O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This map performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other maps.
 * <p>
 * If a write operation is performed on a node, then this map creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1).
 * <p>
 * All operations on this map can be performed concurrently, without a need for
 * synchronisation.
 * <p>
 * The immutable version of this map extends from the non-public class
 * {@code ChampBitmapIndexNode}. This design safes 16 bytes for every instance,
 * and reduces the number of redirections for finding an element in the
 * collection by 1.
 * <p>
 * References:
 * <p>
 * Portions of the code in this class have been derived from 'The Capsule Hash Trie Collections Library', and from
 * 'JHotDraw 8'.
 * <dl>
 *     <dt>Michael J. Steindorfer (2017).
 *     Efficient Immutable Collections.</dt>
 *     <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *     </dd>
 *     <dt>The Capsule Hash Trie Collections Library.
 *     <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *     <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 *     </dd>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a>
 *     </dd>
 * </dl>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public final class HashMap<K, V> extends ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashMap<?, ?> EMPTY = new HashMap<>(ChampBitmapIndexedNode.emptyNode(), 0);

    /**
     * We do not guarantee an iteration order. Make sure that nobody accidentally relies on it.
     * <p>
     * XXX HashSetTest requires a specific iteration order of HashSet! Therefore, we can not use SALT here.
     */
    static final int SALT = 0;//new java.util.Random().nextInt();
    /**
     * The size of the map.
     */
    final int size;

    HashMap(ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> root, int size) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link HashMap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link HashMap} Collector.
     */
    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, HashMap<K, V>> collector() {
        return Collections.toListAndThen(HashMap::ofEntries);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link HashMap}.
     *
     * @param keyMapper The key mapper
     * @param <K>       The key type
     * @param <V>       The value type
     * @param <T>       Initial {@link java.util.stream.Stream} elements type
     * @return A {@link HashMap} Collector.
     */
    public static <K, V, T extends V> Collector<T, ArrayList<T>, HashMap<K, V>> collector(Function<? super T, ? extends K> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return HashMap.collector(keyMapper, v -> v);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link HashMap}.
     *
     * @param keyMapper   The key mapper
     * @param valueMapper The value mapper
     * @param <K>         The key type
     * @param <V>         The value type
     * @param <T>         Initial {@link java.util.stream.Stream} elements type
     * @return A {@link HashMap} Collector.
     */
    public static <K, V, T> Collector<T, ArrayList<T>, HashMap<K, V>> collector(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Collections.toListAndThen(arr -> HashMap.ofEntries(Iterator.ofAll(arr)
                .map(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)))));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) EMPTY;
    }

    /**
     * Narrows a widened {@code HashMap<? extends K, ? extends V>} to {@code HashMap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
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
        return HashMap.<K, V>empty().put(entry._1, entry._2);
    }

    /**
     * Returns a {@code HashMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given map
     */
    public static <K, V> HashMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        return HashMap.<K, V>empty().putAllEntries(map.entrySet());
    }

    /**
     * Returns a {@code HashMap}, from entries mapped from stream.
     *
     * @param stream      the source stream
     * @param keyMapper   the key mapper
     * @param valueMapper the value mapper
     * @param <T>         The stream element type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new Map
     */
    public static <T, K, V> HashMap<K, V> ofAll(java.util.stream.Stream<? extends T> stream,
                                                Function<? super T, ? extends K> keyMapper,
                                                Function<? super T, ? extends V> valueMapper) {
        return Maps.ofStream(empty(), stream, keyMapper, valueMapper);
    }

    /**
     * Returns a {@code HashMap}, from entries mapped from stream.
     *
     * @param stream      the source stream
     * @param entryMapper the entry mapper
     * @param <T>         The stream element type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new Map
     */
    public static <T, K, V> HashMap<K, V> ofAll(java.util.stream.Stream<? extends T> stream,
                                                Function<? super T, Tuple2<? extends K, ? extends V>> entryMapper) {
        return Maps.ofStream(empty(), stream, entryMapper);
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
        return HashMap.<K, V>empty().put(key, value);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2) {
        return of(k1, v1).put(k2, v2);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return of(k1, v1, k2, v2).put(k3, v3);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return of(k1, v1, k2, v2, k3, v3).put(k4, v4);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4).put(k5, v5);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param k6  a key for the map
     * @param v6  the value for k6
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5).put(k6, v6);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param k6  a key for the map
     * @param v6  the value for k6
     * @param k7  a key for the map
     * @param v7  the value for k7
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6).put(k7, v7);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param k6  a key for the map
     * @param v6  the value for k6
     * @param k7  a key for the map
     * @param v7  the value for k7
     * @param k8  a key for the map
     * @param v8  the value for k8
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7).put(k8, v8);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param k6  a key for the map
     * @param v6  the value for k6
     * @param k7  a key for the map
     * @param v7  the value for k7
     * @param k8  a key for the map
     * @param v8  the value for k8
     * @param k9  a key for the map
     * @param v9  the value for k9
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8).put(k9, v9);
    }

    /**
     * Creates a HashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param k5  a key for the map
     * @param v5  the value for k5
     * @param k6  a key for the map
     * @param v6  the value for k6
     * @param k7  a key for the map
     * @param v7  the value for k7
     * @param k8  a key for the map
     * @param v8  the value for k8
     * @param k9  a key for the map
     * @param v9  the value for k9
     * @param k10 a key for the map
     * @param v10 the value for k10
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9).put(k10, v10);
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
     * Returns a HashMap containing tuples returned by {@code n} calls to a given Supplier {@code s}.
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
    @SuppressWarnings("varargs")
    public static <K, V> HashMap<K, V> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        return HashMap.<K, V>empty().putAllEntries(Arrays.asList(entries));
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
    @SuppressWarnings("varargs")
    public static <K, V> HashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        Objects.requireNonNull(entries, "entries is null");
        return HashMap.<K, V>empty().putAllTuples(Arrays.asList(entries));
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
        return HashMap.<K, V>empty().putAllTuples(entries);
    }

    @Override
    public <K2, V2> HashMap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return HashMap.ofEntries(entries);
    }

    @Override
    public Tuple2<V, HashMap<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Maps.computeIfAbsent(this, key, mappingFunction);
    }

    @Override
    public Tuple2<Option<V>, HashMap<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Maps.computeIfPresent(this, key, remappingFunction);
    }

    @Override
    public boolean containsKey(K key) {
        return find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0,
                HashMap::keyEquals) != ChampNode.NO_DATA;
    }

    @Override
    public HashMap<K, V> distinct() {
        return Maps.distinct(this);
    }

    @Override
    public HashMap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        return Maps.distinctBy(this, this::createFromEntries, comparator);
    }

    @Override
    public <U> HashMap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        return Maps.distinctBy(this, this::createFromEntries, keyExtractor);
    }

    @Override
    public HashMap<K, V> drop(int n) {
        return Maps.drop(this, this::createFromEntries, HashMap::empty, n);
    }

    @Override
    public HashMap<K, V> dropRight(int n) {
        return Maps.dropRight(this, this::createFromEntries, HashMap::empty, n);
    }

    @Override
    public HashMap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.dropUntil(this, this::createFromEntries, predicate);
    }

    @Override
    public HashMap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.dropWhile(this, this::createFromEntries, predicate);
    }

    @Override
    public HashMap<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        TransientHashMap<K, V> t = toTransient();
        t.filterAll(e->predicate.test(e.getKey(),e.getValue()));
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public HashMap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
        TransientHashMap<K, V> t = toTransient();
        t.filterAll(e->predicate.test(new Tuple2<>(e.getKey(),e.getValue())));
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public HashMap<K, V> filterKeys(Predicate<? super K> predicate) {
        TransientHashMap<K, V> t = toTransient();
        t.filterAll(e->predicate.test(e.getKey()));
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> filterNotKeys(Predicate<? super K> predicate) {
        return filterKeys(predicate.negate());
    }

    @Override
    public HashMap<K, V> filterValues(Predicate<? super V> predicate) {
        TransientHashMap<K, V> t = toTransient();
        t.filterAll(e->predicate.test(e.getValue()));
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> filterNotValues(Predicate<? super V> predicate) {
        return filterValues(predicate.negate());
    }

    @Override
    public <K2, V2> HashMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.<K2, V2>empty(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0, HashMap::keyEquals);
        return result == ChampNode.NO_DATA || result == null
                ? Option.none()
                : Option.some(((AbstractMap.SimpleImmutableEntry<K, V>) result).getValue());
    }

    @Override
    public V getOrElse(K key, V defaultValue) {
        return get(key).getOrElse(defaultValue);
    }

    @Override
    public <C> Map<C, HashMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return Maps.groupBy(this, this::createFromEntries, classifier);
    }

    @Override
    public Iterator<HashMap<K, V>> grouped(int size) {
        return Maps.grouped(this, this::createFromEntries, size);
    }

    @Override
    public Tuple2<K, V> head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty HashMap");
        }
        AbstractMap.SimpleImmutableEntry<K, V> entry = ChampNode.getFirst(this);
        return new Tuple2<>(entry.getKey(), entry.getValue());
    }

    /**
     * XXX We return tail() here. I believe that this is correct.
     * See identical code in {@link HashSet#init}
     */
    @Override
    public HashMap<K, V> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty HashMap");
        }
        return remove(last()._1);
    }

    @Override
    public Option<HashMap<K, V>> initOption() {
        return Maps.initOption(this);
    }

    /**
     * A {@code HashMap} is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * A {@code HashMap} is computed eagerly.
     *
     * @return false
     */
    @Override
    public boolean isLazy() {
        return false;
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return new ChampIteratorFacade<>(spliterator());
    }

    @Override
    public Set<K> keySet() {
        return HashSet.ofAll(iterator().map(Tuple2::_1));
    }

    @Override
    public Iterator<K> keysIterator() {
        return new ChampIteratorFacade<>(keysSpliterator());
    }

    private Spliterator<K> keysSpliterator() {
        return new ChampSpliterator<>(this, AbstractMap.SimpleImmutableEntry::getKey,
                Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE, size);
    }

    @Override
    public Tuple2<K, V> last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty HashMap");
        }
        AbstractMap.SimpleImmutableEntry<K, V> entry = ChampNode.getLast(this);
        return new Tuple2<>(entry.getKey(), entry.getValue());
    }

    @Override
    public <K2, V2> HashMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(HashMap.empty(), (acc, entry) -> acc.put(entry.map(mapper)));
    }

    @Override
    public <K2> HashMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return map((k, v) -> Tuple.of(keyMapper.apply(k), v));
    }

    @Override
    public <K2> HashMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
        return Collections.mapKeys(this, HashMap.empty(), keyMapper, valueMerge);
    }

    @Override
    public <V2> HashMap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @Override
    public HashMap<K, V> merge(Map<? extends K, ? extends V> that) {
        return Maps.merge(this, this::createFromEntries, that);
    }

    @Override
    public <U extends V> HashMap<K, V> merge(Map<? extends K, U> that,
                                             BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        return Maps.merge(this, this::createFromEntries, that, collisionResolution);
    }

    @Override
    public HashMap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other) {
        return isEmpty() ? ofEntries(other) : this;
    }

    @Override
    public HashMap<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
        return isEmpty() ? ofEntries(supplier.get()) : this;
    }

    @Override
    public Tuple2<HashMap<K, V>, HashMap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.partition(this, this::createFromEntries, predicate);
    }

    @Override
    public HashMap<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
        return Maps.peek(this, action);
    }

    @Override
    public <U extends V> HashMap<K, V> put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, key, value, merge);
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        final ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChampChangeEvent<>();
        final ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode = put(null, new AbstractMap.SimpleImmutableEntry<>(key, value),
                Objects.hashCode(key), 0, details,
                HashMap::updateWithNewKey, HashMap::keyEquals, HashMap::entryKeyHash);
        if (details.isModified()) {
            if (details.isReplaced()) {
                return new HashMap<>(newRootNode, size);
            }
            return new HashMap<>(newRootNode, size + 1);
        }
        return this;
    }

    @Override
    public HashMap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        return Maps.put(this, entry);
    }

    @Override
    public <U extends V> HashMap<K, V> put(Tuple2<? extends K, U> entry,
                                           BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, entry, merge);
    }

    private HashMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> c) {
        TransientHashMap<K,V> t=toTransient();
        t.putAllEntries(c);
        return t.toImmutable();
    }

    @SuppressWarnings("unchecked")
    private HashMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> c) {
        if (isEmpty()&&c instanceof HashMap<?,?> that){
            return (HashMap<K, V>)that;
        }
        TransientHashMap<K,V> t=toTransient();
        t.putAllTuples(c);
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> remove(K key) {
        final int keyHash = Objects.hashCode(key);
        final ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChampChangeEvent<>();
        final ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode =
                remove(null, new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                        HashMap::keyEquals);
        if (details.isModified()) {
            return new HashMap<>(newRootNode, size - 1);
        }
        return this;
    }

    @Override
    public HashMap<K, V> removeAll(Iterable<? extends K> c) {
        TransientHashMap<K,V> t=toTransient();
        t.removeAll(c);
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return Maps.replace(this, currentElement, newElement);
    }

    @Override
    public HashMap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return Maps.replaceAll(this, currentElement, newElement);
    }

    @Override
    public HashMap<K, V> replaceValue(K key, V value) {
        return Maps.replaceValue(this, key, value);
    }

    @Override
    public HashMap<K, V> replace(K key, V oldValue, V newValue) {
        return Maps.replace(this, key, oldValue, newValue);
    }

    @Override
    public HashMap<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        return Maps.replaceAll(this, function);
    }

    @Override
    public HashMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        TransientHashMap<K,V> t=toTransient();
        t.retainAllTuples(elements);
        return t.toImmutable();
    }

    @Override
    public HashMap<K, V> scan(
            Tuple2<K, V> zero,
            BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        return Maps.scan(this, zero, operation, this::createFromEntries);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<HashMap<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier) {
        return Maps.slideBy(this, this::createFromEntries, classifier);
    }

    @Override
    public Iterator<HashMap<K, V>> sliding(int size) {
        return Maps.sliding(this, this::createFromEntries, size);
    }

    @Override
    public Iterator<HashMap<K, V>> sliding(int size, int step) {
        return Maps.sliding(this, this::createFromEntries, size, step);
    }

    @Override
    public Tuple2<HashMap<K, V>, HashMap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.span(this, this::createFromEntries, predicate);
    }

    @Override
    public Spliterator<Tuple2<K, V>> spliterator() {
        return new ChampSpliterator<>(this, entry -> new Tuple2<>(entry.getKey(), entry.getValue()),
                Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE, size);
    }

    @Override
    public HashMap<K, V> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty HashMap");
        }
        return remove(head()._1);
    }

    @Override
    public Option<HashMap<K, V>> tailOption() {
        return Maps.tailOption(this);
    }

    @Override
    public HashMap<K, V> take(int n) {
        return Maps.take(this, this::createFromEntries, n);
    }

    @Override
    public HashMap<K, V> takeRight(int n) {
        return Maps.takeRight(this, this::createFromEntries, n);
    }

    @Override
    public HashMap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.takeUntil(this, this::createFromEntries, predicate);
    }

    @Override
    public HashMap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.takeWhile(this, this::createFromEntries, predicate);
    }

    @Override
    public java.util.HashMap<K, V> toJavaMap() {
        return toJavaMap(java.util.HashMap::new, t -> t);
    }

    TransientHashMap<K,V> toTransient() {
        return new TransientHashMap<>(this);
    }

    @Override
    public Stream<V> values() {
        return valuesIterator().toStream();
    }

    @Override
    public Iterator<V> valuesIterator() {
        return new ChampIteratorFacade<>(valuesSpliterator());
    }

    private Spliterator<V> valuesSpliterator() {
        return new ChampSpliterator<>(this, AbstractMap.SimpleImmutableEntry::getValue,
                Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE, size);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof HashMap) {
            HashMap<?, ?> that = (HashMap<?, ?>) other;
            return size == that.size && equivalent(that);
        } else {
            return Collections.equals(this, other);
        }
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(this);
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

    // We need this method to narrow the argument of `ofEntries`.
    // If this method is static with type args <K, V>, the jdk fails to infer types at the call site.
    private HashMap<K, V> createFromEntries(Iterable<Tuple2<K, V>> tuples) {
        return HashMap.ofEntries(tuples);
    }

    static <V, K> boolean keyEquals(AbstractMap.SimpleImmutableEntry<K, V> a, AbstractMap.SimpleImmutableEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }
    static <V, K> int keyHash(Object e) {
        return SALT ^ Objects.hashCode(e);
    }
    static <V, K> int entryKeyHash(AbstractMap.SimpleImmutableEntry<K, V> e) {
        return SALT^Objects.hashCode(e.getKey());
    }

    static <V, K> boolean entryKeyEquals(AbstractMap.SimpleImmutableEntry<K, V> a, AbstractMap.SimpleImmutableEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    // FIXME This behavior is enforced by AbstractMapTest.shouldPutExistingKeyAndNonEqualValue().<br>
    //     This behavior replaces the existing key with the new one if it has not the same identity.<br>
    //     This behavior does not match the behavior of java.util.HashMap.put().
    //     This behavior violates the contract of the map: we do create a new instance of the map,
    //     although it is equal to the previous instance.
    static <K, V> AbstractMap.SimpleImmutableEntry<K, V> updateWithNewKey(AbstractMap.SimpleImmutableEntry<K, V> oldv, AbstractMap.SimpleImmutableEntry<K, V> newv) {
        return Objects.equals(oldv.getValue(), newv.getValue())
                && oldv.getKey() == newv.getKey()
                ? oldv
                : newv;
    }

    static <K, V> AbstractMap.SimpleImmutableEntry<K, V> updateEntry(AbstractMap.SimpleImmutableEntry<K, V> oldv, AbstractMap.SimpleImmutableEntry<K, V> newv) {
        return Objects.equals(oldv.getValue(), newv.getValue()) ? oldv : newv;
    }

    @Serial
    private Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy<>(this);
    }

    /**
     * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
     * instance fields.
     *
     * @param <K> The key type
     * @param <V> The value type
     */
    // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
    // classes. Also, it may not be compatible with circular object graphs.
    private static final class SerializationProxy<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        // the instance to be serialized/deserialized
        private transient HashMap<K, V> map;

        /**
         * Constructor for the case of serialization, called by {@link HashMap#writeReplace()}.
         * <p/>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param map a map
         */
        SerializationProxy(HashMap<K, V> map) {
            this.map = map;
        }

        /**
         * Write an object to a serialization stream.
         *
         * @param s An object serialization stream.
         * @throws java.io.IOException If an error occurs writing to the stream.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(map.size());
            for (var e : map) {
                s.writeObject(e._1);
                s.writeObject(e._2);
            }
        }

        /**
         * Read an object from a deserialization stream.
         *
         * @param s An object deserialization stream.
         * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
         * @throws InvalidObjectException If the stream contains no list elements.
         * @throws IOException            If an error occurs reading from the stream.
         */
        @SuppressWarnings("unchecked")
        private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
            s.defaultReadObject();
            final int size = s.readInt();
            if (size < 0) {
                throw new InvalidObjectException("No elements");
            }
            var owner = new ChampIdentityObject();
            ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRoot = emptyNode();
            ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChampChangeEvent<>();
            int newSize = 0;
            for (int i = 0; i < size; i++) {
                final K key = (K) s.readObject();
                final V value = (V) s.readObject();
                int keyHash = Objects.hashCode(key);
                newRoot = newRoot.put(owner, new AbstractMap.SimpleImmutableEntry<K, V>(key, value), keyHash, 0, details, HashMap::updateEntry, Objects::equals, Objects::hashCode);
                if (details.isModified()) newSize++;
            }
            map = newSize == 0 ? empty() : new HashMap<>(newRoot, newSize);
        }

        /**
         * {@code readResolve} method for the serialization proxy pattern.
         * <p>
         * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
         * serialization system to translate the serialization proxy back into an instance of the enclosing class
         * upon deserialization.
         *
         * @return A deserialized instance of the enclosing class.
         */
        private Object readResolve() {
            return map;
        }
    }
}
