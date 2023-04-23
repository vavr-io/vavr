package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.KeyIterator;
import io.vavr.collection.champ.MapEntries;
import io.vavr.collection.champ.MapSerializationProxy;
import io.vavr.collection.champ.MappedIterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.VavrMapMixin;
import io.vavr.collection.champ.VavrSetFacade;
import io.vavr.control.Option;

import java.io.ObjectStreamException;
import java.io.Serial;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
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
 *     <li>copyPut: O(1)</li>
 *     <li>copyRemove: O(1)</li>
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
 * The CHAMP tree contains nodes that may be shared with other maps.
 * <p>
 * If a write operation is performed on a node, then this map creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1).
 * <p>
 * This map can create a mutable copy of itself in O(1) time and O(1) space
 * using method {@link #toMutable()}}. The mutable copy shares its nodes
 * with this map, until it has gradually replaced the nodes with exclusively
 * owned nodes.
 * <p>
 * All operations on this set can be performed concurrently, without a need for
 * synchronisation.
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
public class HashMap<K, V> extends BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>>
        implements VavrMapMixin<K, V, HashMap<K, V>> {
    private static final HashMap<?, ?> EMPTY = new HashMap<>(BitmapIndexedNode.emptyNode(), 0);
    @Serial
    private final static long serialVersionUID = 0L;
    private final int size;

    HashMap(BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> root, int size) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
    }

    /**
     * Returns an empty immutable map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty immutable map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) HashMap.EMPTY;
    }

    static <V, K> boolean keyEquals(AbstractMap.SimpleImmutableEntry<K, V> a, AbstractMap.SimpleImmutableEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    static <V, K> int keyHash(AbstractMap.SimpleImmutableEntry<K, V> e) {
        return Objects.hashCode(e.getKey());
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
    public static <K, V> HashMap<K, V> narrow(HashMap<? extends K, ? extends V> hashMap) {
        return (HashMap<K, V>) hashMap;
    }

    /**
     * Returns a {@code ChampMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new ChampMap containing the given map
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
     * @param entry A map entry.
     * @param <K>   The key type
     * @param <V>   The value type
     * @return A new Map containing the given entry
     */
    public static <K, V> HashMap<K, V> of(Tuple2<? extends K, ? extends V> entry) {
        return HashMap.<K, V>empty().put(entry._1, entry._2);
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
        return ofJavaMapEntries(MapEntries.of(key, value));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3));
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
     * @param <K> The key type
     * @param <V> The value type
     * @return A new Map containing the given entries
     */
    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
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
        return ofJavaMapEntries(MapEntries.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    /**
     * Creates a ChampMap of the given entries.
     *
     * @param entries Entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new ChampMap containing the given entries
     */
    public static <K, V> HashMap<K, V> ofJavaMapEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        return HashMap.<K, V>empty().putAllEntries(entries);
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
        return HashMap.<K, V>empty().putAllEntries(Arrays.asList(entries));
    }

    /**
     * Creates a ChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new ChampMap containing the given tuples
     */
    public static <K, V> HashMap<K, V> ofEntries(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return HashMap.<K, V>empty().putAllTuples(entries);
    }

    /**
     * Creates a ChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new ChampMap containing the given tuples
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V>... entries) {
        return HashMap.<K, V>empty().putAllTuples(Arrays.asList(entries));
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

    @Override
    public boolean containsKey(K key) {
        return find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0,
                HashMap::keyEquals) != Node.NO_DATA;
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
    public <L, U> HashMap<L, U> create() {
        return isEmpty() ? (HashMap<L, U>) this : empty();
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
        return HashMap.<L, U>empty().putAllTuples(entries);
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
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0, HashMap::keyEquals);
        return result == Node.NO_DATA || result == null
                ? Option.none()
                : Option.some(((AbstractMap.SimpleImmutableEntry<K, V>) result).getValue());
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(this);
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

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return new MappedIterator<>(new KeyIterator<>(this, null),
                e -> new Tuple2<>(e.getKey(), e.getValue()));
    }

    @Override
    public Set<K> keySet() {
        return new VavrSetFacade<>(this);
    }

    @Override
    public HashMap<K, V> put(K key, V value) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode = update(null, new AbstractMap.SimpleImmutableEntry<>(key, value),
                keyHash, 0, details,
                HashMap::updateWithNewKey, HashMap::keyEquals, HashMap::keyHash);
        if (details.isModified()) {
            if (details.isReplaced()) {
                return new HashMap<>(newRootNode, size);
            }
            return new HashMap<>(newRootNode, size + 1);
        }
        return this;
    }

    private HashMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final MutableHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details =
                    t.putAndGiveDetails(entry.getKey(), entry.getValue());
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    private HashMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        final MutableHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details =
                    t.putAndGiveDetails(entry._1(), entry._2());
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public HashMap<K, V> remove(K key) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode =
                remove(null, new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                        HashMap::keyEquals);
        if (details.isModified()) {
            return new HashMap<>(newRootNode, size - 1);
        }
        return this;
    }

    @Override
    public HashMap<K, V> removeAll(Iterable<? extends K> keys) {
        if (this.isEmpty()) {
            return this;
        }
        final MutableHashMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (K key : keys) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = t.removeAndGiveDetails(key);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public Map<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
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
    public HashMap<K, V> tail() {
        // XXX ChampMapTest.shouldThrowWhenTailEmpty wants us to throw
        //       UnsupportedOperationException instead of NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(iterator().next()._1);
    }

    @Override
    public MutableHashMap<K, V> toJavaMap() {
        return toMutable();
    }

    /**
     * Creates a mutable copy of this map.
     *
     * @return a mutable CHAMP map
     */
    public MutableHashMap<K, V> toMutable() {
        return new MutableHashMap<>(this);
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
            return HashMap.empty().putAllEntries(deserialized);
        }
    }
}
