package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.IdentityObject;
import io.vavr.collection.champ.KeyIterator;
import io.vavr.collection.champ.KeySpliterator;
import io.vavr.collection.champ.MapEntries;
import io.vavr.collection.champ.MapSerializationProxy;
import io.vavr.collection.champ.MappedIterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.NonNull;
import io.vavr.collection.champ.SequencedData;
import io.vavr.collection.champ.SequencedEntry;
import io.vavr.collection.champ.VavrIteratorFacade;
import io.vavr.collection.champ.VavrMapMixin;
import io.vavr.collection.champ.VavrSetFacade;
import io.vavr.control.Option;

import java.io.ObjectStreamException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static io.vavr.collection.champ.SequencedData.seqHash;

/**
 * Implements an immutable map using two Compressed Hash-Array Mapped Prefix-trees
 * (CHAMP), with predictable iteration order.
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
 *     <li>copyPut, copyPutFirst, copyPutLast: O(1) amortized, due to
 *     renumbering</li>
 *     <li>copyRemove: O(1) amortized, due to renumbering</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in
 *     the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(1) with bucket sort, O(log N) with heap sort</li>
 *     <li>getFirst, getLast: O(1)</li>
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
 * This map can create a mutable copy of itself in O(1) time and O(1) space
 * using method {@link #toMutable()}}. The mutable copy shares its nodes
 * with this map, until it has gradually replaced the nodes with exclusively
 * owned nodes.
 * <p>
 * All operations on this set can be performed concurrently, without a need for
 * synchronisation.
 * <p>
 * Insertion Order:
 * <p>
 * This map uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code put} and {@code remove} methods are
 * O(1) only in an amortized sense.
 * <p>
 * To support iteration, a second CHAMP trie is maintained. The second CHAMP
 * trie has the same contents as the first. However, we use the sequence number
 * for computing the hash code of an element.
 * <p>
 * In this implementation, a hash code has a length of
 * 32 bits, and is split up in little-endian order into 7 parts of
 * 5 bits (the last part contains the remaining bits).
 * <p>
 * We convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE
 * to it. And then we reorder its bits from
 * 66666555554444433333222221111100 to 00111112222233333444445555566666.
 * <p>
 * References:
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. BSD-2-Clause License</dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class LinkedHashMap<K, V> extends BitmapIndexedNode<SequencedEntry<K, V>>
        implements VavrMapMixin<K, V, LinkedHashMap<K, V>> {
    private static final LinkedHashMap<?, ?> EMPTY = new LinkedHashMap<>(BitmapIndexedNode.emptyNode(), BitmapIndexedNode.emptyNode(), 0, -1, 0);
    @Serial
    private final static long serialVersionUID = 0L;
    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement after a new entry has been added to the start of the sequence.
     */
    final int first;
    /**
     * Counter for the sequence number of the last entry.
     * The counter is incremented after a new entry is added to the end of the
     * sequence.
     */
    final int last;
    /**
     * This champ trie stores the map entries by their sequence number.
     */
    final @NonNull BitmapIndexedNode<SequencedEntry<K, V>> sequenceRoot;
    final int size;

    LinkedHashMap(BitmapIndexedNode<SequencedEntry<K, V>> root,
                  BitmapIndexedNode<SequencedEntry<K, V>> sequenceRoot,
                  int size,
                  int first, int last) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        assert (long) last - first >= size : "size=" + size + " first=" + first + " last=" + last;
        this.size = size;
        this.first = first;
        this.last = last;
        this.sequenceRoot = Objects.requireNonNull(sequenceRoot);
    }

    static <K, V> BitmapIndexedNode<SequencedEntry<K, V>> buildSequenceRoot(@NonNull BitmapIndexedNode<SequencedEntry<K, V>> root, @NonNull IdentityObject mutator) {
        BitmapIndexedNode<SequencedEntry<K, V>> seqRoot = emptyNode();
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        for (KeyIterator<SequencedEntry<K, V>> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            SequencedEntry<K, V> elem = i.next();
            seqRoot = seqRoot.update(mutator, elem, seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, SequencedData::seqEquals, SequencedData::seqHash);
        }
        return seqRoot;
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
     * @param <K> The key type
     * @param <V> The value type
     * @param <T> Initial {@link java.util.stream.Stream} elements type
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
     * @param keyMapper The key mapper
     * @param valueMapper The value mapper
     * @param <K> The key type
     * @param <V> The value type
     * @param <T> Initial {@link java.util.stream.Stream} elements type
     * @return A {@link LinkedHashMap} Collector.
     */
    public static <K, V, T> Collector<T, ArrayList<T>, LinkedHashMap<K, V>> collector(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Collections.toListAndThen(arr -> LinkedHashMap.ofEntries(Iterator.ofAll(arr)
                .map(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)))));
    }

    /**
     * Returns an empty immutable map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty immutable map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> empty() {
        return (LinkedHashMap<K, V>) LinkedHashMap.EMPTY;
    }

    /**
     * Narrows a widened {@code HashMap<? extends K, ? extends V>} to {@code ChampMap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param hashMap A {@code HashMap}.
     * @param <K>     Key type
     * @param <V>     Value type
     * @return the given {@code hashMap} instance as narrowed type {@code ChampMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> narrow(LinkedHashMap<? extends K, ? extends V> hashMap) {
        return (LinkedHashMap<K, V>) hashMap;
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
        return LinkedHashMap.<K, V>empty().putAllEntries(Arrays.asList(entries));
    }

    /**
     * Returns a {@code LinkedChampMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new LinkedChampMap containing the given map
     */
    public static <K, V> LinkedHashMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        return LinkedHashMap.<K, V>empty().putAllEntries(map.entrySet());
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
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K, V> LinkedHashMap<K, V> of(Tuple2<? extends K, ? extends V> entry) {
        return LinkedHashMap.<K, V>empty().put(entry._1, entry._2);
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
        return ofJavaMapEntries(MapEntries.of(key, value));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    /**
     * Creates a LinkedChampMap of the given entries.
     *
     * @param entries Entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given entries
     */
    static <K, V> LinkedHashMap<K, V> ofJavaMapEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        return LinkedHashMap.<K, V>empty().putAllEntries(entries);
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
        return LinkedHashMap.<K, V>empty().putAllTuples(Arrays.asList(entries));
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
        return LinkedHashMap.<K, V>empty().putAllTuples(entries);
    }

    /**
     * Creates a LinkedChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given tuples
     */
    public static <K, V> LinkedHashMap<K, V> ofTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return LinkedHashMap.<K, V>empty().putAllTuples(entries);
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

    @Override
    public boolean containsKey(K key) {
        Object result = find(
                new SequencedEntry<>(key),
                Objects.hashCode(key), 0, SequencedEntry::keyEquals);
        return result != Node.NO_DATA;
    }

    /**
     * Creates an empty map of the specified key and value types.
     *
     * @param <L> the key type of the map
     * @param <U> the value type of the map
     * @return a new empty map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <L, U> LinkedHashMap<L, U> create() {
        return isEmpty() ? (LinkedHashMap<L, U>) this : empty();
    }

    /**
     * Creates an empty map of the specified key and value types,
     * and adds all the specified entries.
     *
     * @param entries the entries
     * @param <L>     the key type of the map
     * @param <U>     the value type of the map
     * @return a new map contains the specified entries.
     */
    @Override
    public <L, U> Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries) {
        return LinkedHashMap.<L, U>empty().putAllTuples(entries);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof LinkedHashMap) {
            LinkedHashMap<?, ?> that = (LinkedHashMap<?, ?>) other;
            return size == that.size && equivalent(that);
        } else {
            return Collections.equals(this, other);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = find(
                new SequencedEntry<>(key),
                Objects.hashCode(key), 0, SequencedEntry::keyEquals);
        return (result instanceof SequencedEntry<?, ?>)
                ? Option.some(((SequencedEntry<K, V>) result).getValue())
                : Option.none();
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(this);
    }

    @Override
    public boolean isSequential() {
        return true;
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return new VavrIteratorFacade<>(new KeySpliterator<SequencedEntry<K, V>,
                Tuple2<K, V>>(sequenceRoot,
                e -> new Tuple2<>(e.getKey(), e.getValue()),
                Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE, size()), null);
    }

    @Override
    public Set<K> keySet() {
        return new VavrSetFacade<>(this);
    }

    @Override
    public LinkedHashMap<K, V> put(K key, V value) {
        return putLast(key, value, false);
    }

    public LinkedHashMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final MutableLinkedHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry.getKey(), entry.getValue(), false);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    public LinkedHashMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        final MutableLinkedHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry._1, entry._2, false);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    private LinkedHashMap<K, V> putLast(K key, V value, boolean moveToLast) {
        var details = new ChangeEvent<SequencedEntry<K, V>>();
        var newEntry = new SequencedEntry<>(key, value, last);
        var newRoot = update(null,
                newEntry,
                Objects.hashCode(key), 0, details,
                moveToLast ? SequencedEntry::updateAndMoveToLast : SequencedEntry::updateWithNewKey,
                SequencedEntry::keyEquals, SequencedEntry::keyHash);
        if (details.isModified()) {
            var newSeqRoot = sequenceRoot;
            int newFirst = first;
            int newLast = last;
            int newSize = size;
            var mutator = new IdentityObject();
            if (details.isReplaced()) {
                if (moveToLast) {
                    var oldEntry = details.getOldDataNonNull();
                    newSeqRoot = SequencedData.seqRemove(newSeqRoot, mutator, oldEntry, details);
                    newFirst = oldEntry.getSequenceNumber() == newFirst + 1 ? newFirst + 1 : newFirst;
                    newLast++;
                }
            } else {
                newSize++;
                newLast++;
            }
            newSeqRoot = SequencedData.seqUpdate(newSeqRoot, mutator, details.getNewDataNonNull(), details,
                    SequencedEntry::updateWithNewKey);
            return renumber(newRoot, newSeqRoot, newSize, newFirst, newLast);
        }
        return this;
    }

    private LinkedHashMap<K, V> remove(K key, int newFirst, int newLast) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot =
                remove(null, new SequencedEntry<>(key), keyHash, 0, details, SequencedEntry::keyEquals);
        BitmapIndexedNode<SequencedEntry<K, V>> newSeqRoot = sequenceRoot;
        if (details.isModified()) {
            var oldEntry = details.getOldData();
            int seq = oldEntry.getSequenceNumber();
            newSeqRoot = newSeqRoot.remove(null,
                    oldEntry,
                    seqHash(seq), 0, details, SequencedData::seqEquals);
            if (seq == newFirst) {
                newFirst++;
            }
            if (seq == newLast - 1) {
                newLast--;
            }
            return renumber(newRoot, newSeqRoot, size - 1, newFirst, newLast);
        }
        return this;
    }

    @Override
    public LinkedHashMap<K, V> remove(K key) {
        return remove(key, first, last);
    }

    @Override
    public LinkedHashMap<K, V> removeAll(Iterable<? extends K> c) {
        if (this.isEmpty()) {
            return this;
        }
        final MutableLinkedHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (K key : c) {
            ChangeEvent<SequencedEntry<K, V>> details = t.removeAndGiveDetails(key);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @NonNull
    private LinkedHashMap<K, V> renumber(
            BitmapIndexedNode<SequencedEntry<K, V>> root,
            BitmapIndexedNode<SequencedEntry<K, V>> seqRoot,
            int size, int first, int last) {
        if (SequencedData.mustRenumber(size, first, last)) {
            IdentityObject mutator = new IdentityObject();
            BitmapIndexedNode<SequencedEntry<K, V>> renumberedRoot = SequencedData.renumber(
                    size, root, seqRoot, mutator,
                    SequencedEntry::keyHash, SequencedEntry::keyEquals,
                    (e, seq) -> new SequencedEntry<>(e.getKey(), e.getValue(), seq));
            BitmapIndexedNode<SequencedEntry<K, V>> renumberedSeqRoot = buildSequenceRoot(renumberedRoot, mutator);
            return new LinkedHashMap<>(renumberedRoot, renumberedSeqRoot,
                    size, -1, size);
        }
        return new LinkedHashMap<>(root, seqRoot, size, first, last);
    }

    @Override
    public LinkedHashMap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        // currentElement and newElem are the same => do nothing
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }

        // try to remove currentElem from the 'root' trie
        final ChangeEvent<SequencedEntry<K, V>> detailsCurrent = new ChangeEvent<>();
        IdentityObject mutator = new IdentityObject();
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot = remove(mutator,
                new SequencedEntry<K, V>(currentElement._1, currentElement._2),
                Objects.hashCode(currentElement._1), 0, detailsCurrent, SequencedEntry::keyAndValueEquals);
        // currentElement was not in the 'root' trie => do nothing
        if (!detailsCurrent.isModified()) {
            return this;
        }

        // currentElement was in the 'root' trie, and we have just removed it
        // => also remove its entry from the 'sequenceRoot' trie
        var newSeqRoot = sequenceRoot;
        SequencedEntry<K, V> currentData = detailsCurrent.getOldData();
        int seq = currentData.getSequenceNumber();
        newSeqRoot = newSeqRoot.remove(mutator, currentData, seqHash(seq), 0,
                detailsCurrent, SequencedData::seqEquals);

        // try to update the trie with the newElement
        ChangeEvent<SequencedEntry<K, V>> detailsNew = new ChangeEvent<>();
        SequencedEntry<K, V> newData = new SequencedEntry<>(newElement._1, newElement._2, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement._1), 0, detailsNew,
                SequencedEntry::forceUpdate,
                SequencedEntry::keyEquals, SequencedEntry::keyHash);
        boolean isReplaced = detailsNew.isReplaced();

        // there already was an element with key newElement._1 in the trie, and we have just replaced it
        // => remove the replaced entry from the 'sequenceRoot' trie
        if (isReplaced) {
            SequencedEntry<K, V> replacedEntry = detailsNew.getOldData();
            newSeqRoot = newSeqRoot.remove(mutator, replacedEntry, seqHash(replacedEntry.getSequenceNumber()), 0, detailsNew, SequencedData::seqEquals);
        }

        // we have just successfully added or replaced the newElement
        // => insert the new entry in the 'sequenceRoot' trie
        newSeqRoot = newSeqRoot.update(mutator,
                newData, seqHash(seq), 0, detailsNew,
                SequencedEntry::forceUpdate,
                SequencedData::seqEquals, SequencedData::seqHash);

        if (isReplaced) {
            // we reduced the size of the map by one => renumbering may be necessary
            return renumber(newRoot, newSeqRoot, size - 1, first, last);
        } else {
            // we did not change the size of the map => no renumbering is needed
            return new LinkedHashMap<>(newRoot, newSeqRoot, size, first, last);
        }
    }

    @Override
    public Map<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        if (elements == this) {
            return this;
        }
        Objects.requireNonNull(elements, "elements is null");
        MutableHashMap<K, V> m = new MutableHashMap<>();
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                m.put(entry._1, entry._2);
            }
        }
        return m.toImmutable();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Map<K, V> tail() {
        // XXX ChampMapTest.shouldThrowWhenTailEmpty wants us to throw
        //       UnsupportedOperationException instead of NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(iterator().next()._1);
    }

    @Override
    public java.util.Map<K, V> toJavaMap() {
        return toMutable();
    }

    /**
     * Creates a mutable copy of this map.
     *
     * @return a mutable sequenced CHAMP map
     */
    public MutableLinkedHashMap<K, V> toMutable() {
        return new MutableLinkedHashMap<>(this);
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public Stream<V> values() {
        return new MappedIterator<>(iterator(), Tuple2::_2).toStream();
    }


    @Serial
    private Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy<>(this.toMutable());
    }

    static class SerializationProxy<K, V> extends MapSerializationProxy<K, V> {
        @Serial
        private final static long serialVersionUID = 0L;

        SerializationProxy(java.util.Map<K, V> target) {
            super(target);
        }

        @Serial
        @Override
        protected Object readResolve() {
            return LinkedHashMap.empty().putAllEntries(deserialized);
        }
    }
}
