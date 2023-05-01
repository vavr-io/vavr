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

import static io.vavr.collection.ChampSequencedData.seqHash;

/**
 * Implements an immutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP) and a bit-mapped trie (Vector).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> entries</li>
 *     <li>allows null keys and null values</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which keys were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>put, putFirst, putLast: O(log N) in an amortized sense, because we sometimes have to
 *     renumber the elements.</li>
 *     <li>remove: O(log N) in an amortized sense, because we sometimes have to renumber the elements.</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in
 *     the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(log N)</li>
 *     <li>getFirst, getLast: O(log N)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This map performs read and write operations of single elements in O(log N) time,
 * and in O(log N) space, where N is the number of elements in the set.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other maps.
 * <p>
 * If a write operation is performed on a node, then this set creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1).
 * <p>
 * Insertion Order:
 * <p>
 * This map uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code add} and {@code remove} methods are O(1)
 * only in an amortized sense.
 * <p>
 * To support iteration, we use a Vector. The Vector has the same contents
 * as the CHAMP trie. However, its elements are stored in insertion order.
 * <p>
 * If an element is removed from the CHAMP trie that is not the first or the
 * last element of the Vector, we replace its corresponding element in
 * the Vector by a tombstone. If the element is at the start or end of the Vector,
 * we remove the element and all its neighboring tombstones from the Vector.
 * <p>
 * A tombstone can store the number of neighboring tombstones in ascending and in descending
 * direction. We use these numbers to skip tombstones when we iterate over the vector.
 * Since we only allow iteration in ascending or descending order from one of the ends of
 * the vector, we do not need to keep the number of neighbors in all tombstones up to date.
 * It is sufficient, if we update the neighbor with the lowest index and the one with the
 * highest index.
 * <p>
 * If the number of tombstones exceeds half of the size of the collection, we renumber all
 * sequence numbers, and we create a new Vector.
 * <p>
 * The immutable version of this set extends from the non-public class
 * {@code ChampBitmapIndexNode}. This design safes 16 bytes for every instance,
 * and reduces the number of redirections for finding an element in the
 * collection by 1.
 * <p>
 * References:
 * <p>
 * Portions of the code in this class has been derived from 'vavr' Vector.java.
 * <p>
 * The design of this class is inspired by 'VectorMap.scala'.
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *      </dd>
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 *      </dd>
 * </dl>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@SuppressWarnings("exports")
public class LinkedHashMap<K, V> extends ChampBitmapIndexedNode<ChampSequencedEntry<K, V>>
        implements Map<K, V>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final LinkedHashMap<?, ?> EMPTY = new LinkedHashMap<>(
            ChampBitmapIndexedNode.emptyNode(), Vector.empty(), 0, 0);
    /**
     * Offset of sequence numbers to vector indices.
     *
     * <pre>vector index = sequence number + offset</pre>
     */
    final int offset;
    /**
     * The size of the map.
     */
    final int size;
    /**
     * In this vector we store the elements in the order in which they were inserted.
     */
    final Vector<Object> vector;

    LinkedHashMap(ChampBitmapIndexedNode<ChampSequencedEntry<K, V>> root,
                  Vector<Object> vector,
                  int size, int offset) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
        this.offset = offset;
        this.vector = Objects.requireNonNull(vector);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link LinkedHashMap}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @return A {@link LinkedHashMap} Collector.
     */
    public static <K, V> Collector<Tuple2<K, V>, ArrayList<Tuple2<K, V>>, LinkedHashMap<K, V>> collector() {
        return Collections.toListAndThen(LinkedHashMap::ofEntries);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link LinkedHashMap}.
     *
     * @param keyMapper The key mapper
     * @param <K>       The key type
     * @param <V>       The value type
     * @param <T>       Initial {@link java.util.stream.Stream} elements type
     * @return A {@link LinkedHashMap} Collector.
     */
    public static <K, V, T extends V> Collector<T, ArrayList<T>, LinkedHashMap<K, V>> collector(Function<? super T, ? extends K> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return LinkedHashMap.collector(keyMapper, v -> v);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link LinkedHashMap}.
     *
     * @param keyMapper   The key mapper
     * @param valueMapper The value mapper
     * @param <K>         The key type
     * @param <V>         The value type
     * @param <T>         Initial {@link java.util.stream.Stream} elements type
     * @return A {@link LinkedHashMap} Collector.
     */
    public static <K, V, T> Collector<T, ArrayList<T>, LinkedHashMap<K, V>> collector(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Collections.toListAndThen(arr -> LinkedHashMap.ofEntries(Iterator.ofAll(arr)
                .map(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)))));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> empty() {
        return (LinkedHashMap<K, V>) EMPTY;
    }

    /**
     * Narrows a widened {@code LinkedHashMap<? extends K, ? extends V>} to {@code LinkedHashMap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param linkedHashMap A {@code LinkedHashMap}.
     * @param <K>           Key type
     * @param <V>           Value type
     * @return the given {@code linkedHashMap} instance as narrowed type {@code LinkedHashMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> narrow(LinkedHashMap<? extends K, ? extends V> linkedHashMap) {
        return (LinkedHashMap<K, V>) linkedHashMap;
    }

    /**
     * Returns a singleton {@code LinkedHashMap}, i.e. a {@code LinkedHashMap} of one element.
     *
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> of(Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return LinkedHashMap.<K,V>empty().put(entry._1,entry._2);
    }

    /**
     * Returns a {@code LinkedHashMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given map
     */
    public static <K, V> LinkedHashMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        LinkedHashMap<K, V> result = LinkedHashMap.empty();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result = result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Returns a {@code LinkedHashMap}, from entries mapped from stream.
     *
     * @param stream      the source stream
     * @param entryMapper the entry mapper
     * @param <T>         The stream element type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new Map
     */
    public static <T, K, V> LinkedHashMap<K, V> ofAll(java.util.stream.Stream<? extends T> stream,
                                                      Function<? super T, Tuple2<? extends K, ? extends V>> entryMapper) {
        return Maps.ofStream(empty(), stream, entryMapper);
    }

    /**
     * Returns a {@code LinkedHashMap}, from entries mapped from stream.
     *
     * @param stream      the source stream
     * @param keyMapper   the key mapper
     * @param valueMapper the value mapper
     * @param <T>         The stream element type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new Map
     */
    public static <T, K, V> LinkedHashMap<K, V> ofAll(java.util.stream.Stream<? extends T> stream,
                                                      Function<? super T, ? extends K> keyMapper,
                                                      Function<? super T, ? extends V> valueMapper) {
        return Maps.ofStream(empty(), stream, keyMapper, valueMapper);
    }

    /**
     * Returns a singleton {@code LinkedHashMap}, i.e. a {@code LinkedHashMap} of one element.
     *
     * @param key   A singleton map key.
     * @param value A singleton map value.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K, V> LinkedHashMap<K, V> of(K key, V value) {
        return LinkedHashMap.<K,V>empty().put(key,value);
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
     *
     * @param k1  a key for the map
     * @param v1  the value for k1
     * @param k2  a key for the map
     * @param v2  the value for k2
     * @param k3  a key for the map
     * @param v3  the value for k3
     * @param k4  a key for the map
     * @param v4  the value for k4
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        t.put(k6,v6);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        t.put(k6,v6);
        t.put(k7,v7);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        t.put(k6,v6);
        t.put(k7,v7);
        t.put(k8,v8);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        t.put(k6,v6);
        t.put(k7,v7);
        t.put(k8,v8);
        t.put(k9,v9);
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given list of key-value pairs.
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
    public static <K, V> LinkedHashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        var t = new TransientLinkedHashMap<K,V>();
        t.put(k1,v1);
        t.put(k2,v2);
        t.put(k3,v3);
        t.put(k4,v4);
        t.put(k5,v5);
        t.put(k6,v6);
        t.put(k7,v7);
        t.put(k8,v8);
        t.put(k9,v9);
        t.put(k10,v10);
        return t.toImmutable();
    }

    /**
     * Returns a LinkedHashMap containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the LinkedHashMap
     * @param f   The Function computing element values
     * @return A LinkedHashMap consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> tabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return ofEntries(Collections.tabulate(n, (Function<? super Integer, ? extends Tuple2<K, V>>) f));
    }

    /**
     * Returns a LinkedHashMap containing tuples returned by {@code n} calls to a given Supplier {@code s}.
     *
     * @param <K> The key type
     * @param <V> The value type
     * @param n   The number of elements in the LinkedHashMap
     * @param s   The Supplier computing element values
     * @return A LinkedHashMap of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> fill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        Objects.requireNonNull(s, "s is null");
        return ofEntries(Collections.fill(n, (Supplier<? extends Tuple2<K, V>>) s));
    }

    /**
     * Creates a LinkedHashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> ofEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        var t = new TransientLinkedHashMap<K,V>();
        t.putAll(Arrays.asList(entries));
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        var t = new TransientLinkedHashMap<K,V>();
        t.putAllTuples(Arrays.asList(entries));
        return t.toImmutable();
    }

    /**
     * Creates a LinkedHashMap of the given entries.
     *
     * @param entries Map entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new Map containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        Objects.requireNonNull(entries, "entries is null");
        if (entries instanceof LinkedHashMap) {
            return (LinkedHashMap<K, V>) entries;
        }
        var t = new TransientLinkedHashMap<K,V>();
        t.putAllTuples(entries);
        return t.toImmutable();
    }

    @Override
    public <K2, V2> LinkedHashMap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return LinkedHashMap.ofEntries(entries);
    }

    @Override
    public Tuple2<V, LinkedHashMap<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Maps.computeIfAbsent(this, key, mappingFunction);
    }

    @Override
    public Tuple2<Option<V>, LinkedHashMap<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Maps.computeIfPresent(this, key, remappingFunction);
    }

    @Override
    public boolean containsKey(K key) {
        return find(new ChampSequencedEntry<>(key), Objects.hashCode(key), 0,
                ChampSequencedEntry::keyEquals) != ChampNode.NO_DATA;
    }

    @Override
    public LinkedHashMap<K, V> distinct() {
        return Maps.distinct(this);
    }

    @Override
    public LinkedHashMap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        return Maps.distinctBy(this, this::createFromEntries, comparator);
    }

    @Override
    public <U> LinkedHashMap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        return Maps.distinctBy(this, this::createFromEntries, keyExtractor);
    }

    @Override
    public LinkedHashMap<K, V> drop(int n) {
        return Maps.drop(this, this::createFromEntries, LinkedHashMap::empty, n);
    }

    @Override
    public LinkedHashMap<K, V> dropRight(int n) {
        return Maps.dropRight(this, this::createFromEntries, LinkedHashMap::empty, n);
    }

    @Override
    public LinkedHashMap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.dropUntil(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.dropWhile(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        return Maps.filter(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
        return Maps.filterNot(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.filter(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.filterNot(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterKeys(Predicate<? super K> predicate) {
        return Maps.filterKeys(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterNotKeys(Predicate<? super K> predicate) {
        return Maps.filterNotKeys(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterValues(Predicate<? super V> predicate) {
        return Maps.filterValues(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> filterNotValues(Predicate<? super V> predicate) {
        return Maps.filterNotValues(this, this::createFromEntries, predicate);
    }

    @Override
    public <K2, V2> LinkedHashMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(LinkedHashMap.<K2, V2>empty(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Option<V> get(K key) {
        Object result = find(
                new ChampSequencedEntry<>(key),
                Objects.hashCode(key), 0, ChampSequencedEntry::keyEquals);
        return ((result instanceof ChampSequencedEntry<?, ?> entry) ? Option.some((V) entry.getValue()) : Option.none());
    }

    @Override
    public V getOrElse(K key, V defaultValue) {
        return get(key).getOrElse(defaultValue);
    }

    @Override
    public <C> Map<C, LinkedHashMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return Maps.groupBy(this, this::createFromEntries, classifier);
    }

    @Override
    public Iterator<LinkedHashMap<K, V>> grouped(int size) {
        return Maps.grouped(this, this::createFromEntries, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<K, V> head() {
        java.util.Map.Entry<K, V> entry = (java.util.Map.Entry<K, V>) vector.head();
        return new Tuple2<>(entry.getKey(), entry.getValue());
    }

    @Override
    public LinkedHashMap<K, V> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty LinkedHashMap");
        }
        return remove(last()._1);
    }

    @Override
    public Option<LinkedHashMap<K, V>> initOption() {
        return Maps.initOption(this);
    }

    /**
     * An {@code LinkedHashMap}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * An {@code LinkedHashMap}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    public boolean isLazy() {
        return false;
    }

    @Override
    public boolean isSequential() {
        return true;
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return new ChampIteratorFacade<>(spliterator());
    }

    @Override
    public Set<K> keySet() {
        return LinkedHashSet.ofAll(iterator().map(Tuple2::_1));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tuple2<K, V> last() {
        java.util.Map.Entry<K, V> entry = (java.util.Map.Entry<K, V>) vector.last();
        return new Tuple2<>(entry.getKey(), entry.getValue());
    }

    @Override
    public <K2, V2> LinkedHashMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(LinkedHashMap.empty(), (acc, entry) -> acc.put(entry.map(mapper)));
    }

    @Override
    public <K2> LinkedHashMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return map((k, v) -> Tuple.of(keyMapper.apply(k), v));
    }

    @Override
    public <K2> LinkedHashMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
        return Collections.mapKeys(this, LinkedHashMap.empty(), keyMapper, valueMerge);
    }

    @Override
    public <W> LinkedHashMap<K, W> mapValues(Function<? super V, ? extends W> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return map((k, v) -> Tuple.of(k, mapper.apply(v)));
    }

    @Override
    public LinkedHashMap<K, V> merge(Map<? extends K, ? extends V> that) {
        return Maps.merge(this, this::createFromEntries, that);
    }

    @Override
    public <U extends V> LinkedHashMap<K, V> merge(Map<? extends K, U> that,
            BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        return Maps.merge(this, this::createFromEntries, that, collisionResolution);
    }

    @Override
    public LinkedHashMap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other) {
        return isEmpty() ? ofEntries(other) : this;
    }

    @Override
    public LinkedHashMap<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
        return isEmpty() ? ofEntries(supplier.get()) : this;
    }

    @Override
    public Tuple2<LinkedHashMap<K, V>, LinkedHashMap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.partition(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
        return Maps.peek(this, action);
    }

    @Override
    public <U extends V> LinkedHashMap<K, V> put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, key, value, merge);
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     * <p>
     * Note that this method has a worst-case linear complexity.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return A new Map containing these elements and that entry.
     */
    @Override
    public LinkedHashMap<K, V> put(K key, V value) {
        return putLast(key, value, false);
    }

    private LinkedHashMap<K, V> putLast( K key,  V value, boolean moveToLast) {
        var details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        var newEntry = new ChampSequencedEntry<>(key, value, vector.size() - offset);
        var newRoot = update(null, newEntry,
                Objects.hashCode(key), 0, details,
                moveToLast ? ChampSequencedEntry::updateAndMoveToLast : ChampSequencedEntry::updateWithNewKey,
                ChampSequencedEntry::keyEquals, ChampSequencedEntry::keyHash);
        if (details.isReplaced()
                && details.getOldDataNonNull().getSequenceNumber() == details.getNewDataNonNull().getSequenceNumber()) {
            var newVector = vector.update(details.getNewDataNonNull().getSequenceNumber() - offset, details.getNewDataNonNull());
            return new LinkedHashMap<>(newRoot, newVector, size, offset);
        }
        if (details.isModified()) {
            var newVector = vector;
            int newOffset = offset;
            int newSize = size;
            var mutator = new ChampIdentityObject();
            if (details.isReplaced()) {
                if (moveToLast) {
                    var oldElem = details.getOldDataNonNull();
                    var result = ChampSequencedData.vecRemove(newVector, mutator, oldElem, details, newOffset);
                    newVector = result._1;
                    newOffset = result._2;
                }
            } else {
                newSize++;
            }
            newVector = newVector.append(newEntry);
            return renumber(newRoot, newVector, newSize, newOffset);
        }
        return this;
    }

    @Override
    public LinkedHashMap<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        return Maps.put(this, entry);
    }

    @Override
    public <U extends V> LinkedHashMap<K, V> put(Tuple2<? extends K, U> entry,
                                                 BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, entry, merge);
    }

    @Override
    public LinkedHashMap<K, V> remove(K key) {
        int keyHash = Objects.hashCode(key);
        var details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        ChampBitmapIndexedNode<ChampSequencedEntry<K, V>> newRoot = remove(null,
                new ChampSequencedEntry<>(key),
                keyHash, 0, details, ChampSequencedEntry::keyEquals);
        if (details.isModified()) {
            var oldElem = details.getOldDataNonNull();
            var result = ChampSequencedData.vecRemove(vector, null, oldElem, details, offset);
            return renumber(newRoot, result._1, size - 1, result._2);
        }
        return this;
    }

    @Override
    public LinkedHashMap<K, V> removeAll(Iterable<? extends K> keys) {
        Objects.requireNonNull(keys, "keys is null");
        TransientLinkedHashMap<K, V> t = toTransient();
return        t.removeAll(keys)?t.toImmutable():this;
    }

    private LinkedHashMap<K, V> renumber(
            ChampBitmapIndexedNode<ChampSequencedEntry<K, V>> root,
            Vector<Object> vector,
            int size, int offset) {

        if (ChampSequencedData.vecMustRenumber(size, offset, this.vector.size())) {
            var mutator = new ChampIdentityObject();
            var result = ChampSequencedData.<ChampSequencedEntry<K, V>>vecRenumber(
                    size, root, vector, mutator, ChampSequencedEntry::keyHash, ChampSequencedEntry::keyEquals,
                    (e, seq) -> new ChampSequencedEntry<>(e.getKey(), e.getValue(), seq));
            return new LinkedHashMap<>(
                    result._1, result._2,
                    size, 0);
        }
        return new LinkedHashMap<>(root, vector, size, offset);
    }
    @Override
    public LinkedHashMap<K, V> replace(Tuple2<K, V> currentEntry, Tuple2<K, V> newEntry) {
        // currentEntry and newEntry are the same => do nothing
        if (Objects.equals(currentEntry, newEntry)) {
            return this;
        }

        // try to remove currentEntry from the 'root' trie
        final ChampChangeEvent<ChampSequencedEntry<K, V>> detailsCurrent = new ChampChangeEvent<>();
        ChampIdentityObject mutator = new ChampIdentityObject();
        ChampBitmapIndexedNode<ChampSequencedEntry<K, V>> newRoot = remove(mutator,
                new ChampSequencedEntry<K, V>(currentEntry._1, currentEntry._2),
                Objects.hashCode(currentEntry._1), 0, detailsCurrent, ChampSequencedEntry::keyAndValueEquals);
        // currentElement was not in the 'root' trie => do nothing
        if (!detailsCurrent.isModified()) {
            return this;
        }

        // removedData was in the 'root' trie, and we have just removed it
        // => also remove its entry from the 'sequenceRoot' trie
        var newVector = vector;
        var newOffset = offset;
        ChampSequencedEntry<K, V> removedData = detailsCurrent.getOldData();
        int seq = removedData.getSequenceNumber();
        var result = ChampSequencedData.vecRemove(newVector, mutator, removedData, detailsCurrent, offset);
        newVector=result._1;
        newOffset=result._2;

        // try to update the trie with the newData
        ChampChangeEvent<ChampSequencedEntry<K, V>> detailsNew = new ChampChangeEvent<>();
        ChampSequencedEntry<K, V> newData = new ChampSequencedEntry<>(newEntry._1, newEntry._2, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newEntry._1), 0, detailsNew,
                ChampSequencedEntry::forceUpdate,
                ChampSequencedEntry::keyEquals, ChampSequencedEntry::keyHash);
        boolean isReplaced = detailsNew.isReplaced();

        // there already was data with key newData.getKey() in the trie, and we have just replaced it
        // => remove the replaced data from the vector
        if (isReplaced) {
            ChampSequencedEntry<K, V> replacedData = detailsNew.getOldData();
            result = ChampSequencedData.vecRemove(newVector, mutator, replacedData, detailsCurrent, newOffset);
            newVector=result._1;
            newOffset=result._2;
        }

        // we have just successfully added or replaced the newData
        // => insert the newData in the vector
        newVector = seq+newOffset<newVector.size()?newVector.update(seq+newOffset,newData):newVector.append(newData);

        if (isReplaced) {
            // we reduced the size of the map by one => renumbering may be necessary
            return renumber(newRoot, newVector, size - 1, newOffset);
        } else {
            // we did not change the size of the map => no renumbering is needed
            return new LinkedHashMap<>(newRoot, newVector, size, newOffset);
        }
    }

    @Override
    public LinkedHashMap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return Maps.replaceAll(this, currentElement, newElement);
    }

    @Override
    public LinkedHashMap<K, V> replaceValue(K key, V value) {
        return Maps.replaceValue(this, key, value);
    }

    @Override
    public LinkedHashMap<K, V> replace(K key, V oldValue, V newValue) {
        return Maps.replace(this, key, oldValue, newValue);
    }

    @Override
    public LinkedHashMap<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        return Maps.replaceAll(this, function);
    }

    @Override
    public LinkedHashMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        LinkedHashMap<K, V> result = empty();
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                result = result.put(entry._1, entry._2);
            }
        }
        return result;
    }

    Iterator<Tuple2<K, V>> reverseIterator() {
        return new ChampIteratorFacade<>(reverseSpliterator());
    }

    @SuppressWarnings("unchecked")
    Spliterator<Tuple2<K, V>> reverseSpliterator() {
        return new ChampReverseVectorSpliterator<>(vector,
                e -> new Tuple2<K,V> (((java.util.Map.Entry<K, V>) e).getKey(),((java.util.Map.Entry<K, V>) e).getValue()),
                0, size(),Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public LinkedHashMap<K, V> scan(
            Tuple2<K, V> zero,
            BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        return Maps.scan(this, zero, operation, this::createFromEntries);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<LinkedHashMap<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier) {
        return Maps.slideBy(this, this::createFromEntries, classifier);
    }

    @Override
    public Iterator<LinkedHashMap<K, V>> sliding(int size) {
        return Maps.sliding(this, this::createFromEntries, size);
    }

    @Override
    public Iterator<LinkedHashMap<K, V>> sliding(int size, int step) {
        return Maps.sliding(this, this::createFromEntries, size, step);
    }

    @Override
    public Tuple2<LinkedHashMap<K, V>, LinkedHashMap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.span(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Spliterator<Tuple2<K, V>> spliterator() {
        return new ChampVectorSpliterator<>(vector,
                e -> new Tuple2<K,V> (((java.util.Map.Entry<K, V>) e).getKey(),((java.util.Map.Entry<K, V>) e).getValue()),
               0, size(),Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public LinkedHashMap<K, V> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty LinkedHashMap");
        }
        return remove(head()._1);
    }

    @Override
    public Option<LinkedHashMap<K, V>> tailOption() {
        return Maps.tailOption(this);
    }

    @Override
    public LinkedHashMap<K, V> take(int n) {
        return Maps.take(this, this::createFromEntries, n);
    }

    @Override
    public LinkedHashMap<K, V> takeRight(int n) {
        return Maps.takeRight(this, this::createFromEntries, n);
    }

    @Override
    public LinkedHashMap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.takeUntil(this, this::createFromEntries, predicate);
    }

    @Override
    public LinkedHashMap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.takeWhile(this, this::createFromEntries, predicate);
    }

    @Override
    public java.util.LinkedHashMap<K, V> toJavaMap() {
        return toJavaMap(java.util.LinkedHashMap::new, t -> t);
    }

    TransientLinkedHashMap<K, V> toTransient() {
        return new TransientLinkedHashMap<>(this);
    }
    @Override
    public Seq<V> values() {
        return map(t -> t._2);
    }

    @Override
    public boolean equals(Object o) {
        return Collections.equals(this, o);
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
        return "LinkedHashMap";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    // We need this method to narrow the argument of `ofEntries`.
    // If this method is static with type args <K, V>, the jdk fails to infer types at the call site.
    private LinkedHashMap<K, V> createFromEntries(Iterable<Tuple2<K, V>> tuples) {
        return LinkedHashMap.ofEntries(tuples);
    }

    @Serial
    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap.SerializationProxy<>(this);
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
        private transient LinkedHashMap<K, V> map;

        /**
         * Constructor for the case of serialization, called by {@link LinkedHashMap#writeReplace()}.
         * <p/>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param map a map
         */
        SerializationProxy(LinkedHashMap<K, V> map) {
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
            TransientLinkedHashMap<K, V> t = new TransientLinkedHashMap<>();
            for (int i = 0; i < size; i++) {
                final K key = (K) s.readObject();
                final V value = (V) s.readObject();
               t.put(key,value);
            }
            map =t.toImmutable();
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
