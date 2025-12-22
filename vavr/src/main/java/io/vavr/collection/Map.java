/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.*;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

/**
 * An immutable {@code Map} interface.
 *
 * <p>
 * Represents a collection of key-value pairs with immutable operations. 
 * Supports typical map operations such as querying, updating, filtering,
 * transforming, and iterating over entries. Provides convenient methods 
 * for converting to standard Java maps and for working with default values.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich, Ruslan Sennov, Grzegorz Piwowarek
 */
public interface Map<K, V> extends Traversable<Tuple2<K, V>>, PartialFunction<K, V>, Serializable {

    /**
     * The serial version UID for serialization.
     */
    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code Map<? extends K, ? extends V>} to {@code Map<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param map A {@code Map}.
     * @param <K> Key type
     * @param <V> Value type
     * @return the given {@code map} instance as narrowed type {@code Map<K, V>}.
     */
    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> narrow(Map<? extends K, ? extends V> map) {
        return (Map<K, V>) map;
    }

    /**
     * Creates a key/value pair for use with {@link Map} factories.
     *
     * <p>
     * When imported statically, this method enables constructing maps in a
     * readable and type-safe way, for example:
     * <pre>{@code
     * HashMap.ofEntries(
     *     entry(k1, v1),
     *     entry(k2, v2),
     *     entry(k3, v3)
     * );
     * }</pre>
     *
     * @param key   the entry's key
     * @param value the entry's value
     * @param <K>   key type
     * @param <V>   value type
     * @return a key/value pair
     */
    static <K, V> Tuple2<K, V> entry(K key, V value) {
        return Tuple.of(key, value);
    }


    @Deprecated
    @Override
    default V apply(K key) {
        return get(key).getOrElseThrow(() -> new NoSuchElementException(String.valueOf(key)));
    }

    /**
     * Turns this {@code Map} into a {@link PartialFunction} which is defined at a specific index, if this {@code Map}
     * contains the given key. When applied to a defined key, the partial function will return
     * the value of this {@code Map} that is associated with the key.
     *
     * @return a new {@link PartialFunction}
     * @throws NoSuchElementException when a non-existing key is applied to the partial function
     */
    default PartialFunction<K, V> asPartialFunction() throws IndexOutOfBoundsException {
        return new PartialFunction<K, V>() {
            private static final long serialVersionUID = 1L;
            @Override
            public V apply(K key) {
                return get(key).getOrElseThrow(() -> new NoSuchElementException(String.valueOf(key)));
            }
            @Override
            public boolean isDefinedAt(K key) {
                return containsKey(key);
            }
        };
    }

    @Override
    default <R> Seq<R> collect(@NonNull PartialFunction<? super Tuple2<K, V>, ? extends R> partialFunction) {
        return io.vavr.collection.Vector.ofAll(iterator().<R> collect(partialFunction));
    }

    /**
     * Maps this {@code Map} to a new {@code Map} with different component type by applying a function to its elements.
     *
     * @param <K2>        key's component type of the map result
     * @param <V2>        value's component type of the map result
     * @param keyMapper   a {@code Function} that maps the keys of type {@code K} to keys of type {@code K2}
     * @param valueMapper a {@code Function} that the values of type {@code V} to values of type {@code V2}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code keyMapper} or {@code valueMapper} is null
     */
    <K2, V2> Map<K2, V2> bimap(@NonNull Function<? super K, ? extends K2> keyMapper, @NonNull Function<? super V, ? extends V2> valueMapper);

    @Override
    default boolean contains(@NonNull Tuple2<K, V> element) {
        return get(element._1()).map(v -> Objects.equals(v, element._2())).getOrElse(false);
    }

    /**
     * If the specified key is not already associated with a value,
     * attempts to compute its value using the given mapping
     * function and enters it into this map.
     *
     * @param key             key whose presence in this map is to be tested
     * @param mappingFunction mapping function
     * @return the {@link Tuple2} of current or modified map and existing or computed value associated with the specified key
     */
    Tuple2<V, ? extends Map<K, V>> computeIfAbsent(K key, @NonNull Function<? super K, ? extends V> mappingFunction);

    /**
     * If the value for the specified key is present, attempts to
     * compute a new mapping given the key and its current mapped value.
     *
     * @param key               key whose presence in this map is to be tested
     * @param remappingFunction remapping function
     * @return the {@link Tuple2} of current or modified map and the {@code Some} of the value associated
     * with the specified key, or {@code None} if none
     */
    Tuple2<Option<V>, ? extends Map<K, V>> computeIfPresent(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction);

    /**
     * Returns <code>true</code> if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return <code>true</code> if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);

    /**
     * Returns <code>true</code> if this map maps one or more keys to the
     * specified value. This operation will require time linear in the map size.
     *
     * @param value value whose presence in this map is to be tested
     * @return <code>true</code> if this map maps one or more keys to the
     * specified value
     */
    default boolean containsValue(V value) {
        return iterator().map(Tuple2::_2).contains(value);
    }

    /**
     * Returns a new Map consisting of all elements which satisfy the given predicate.
     *
     * @param predicate the predicate used to test elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> filter(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Returns a new Map consisting of all elements which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> reject(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Returns a new Map consisting of all elements with keys which satisfy the given predicate.
     *
     * @param predicate the predicate used to test keys of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> filterKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Map consisting of all elements with keys which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test keys of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> rejectKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Map consisting of all elements with values which satisfy the given predicate.
     *
     * @param predicate the predicate used to test values of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> filterValues(@NonNull Predicate<? super V> predicate);

    /**
     * Returns a new Map consisting of all elements with values which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test values of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    Map<K, V> rejectValues(@NonNull Predicate<? super V> predicate);

    /**
     * FlatMaps this {@code Map} to a new {@code Map} with different component type.
     *
     * @param mapper A mapper
     * @param <K2>   key's component type of the mapped {@code Map}
     * @param <V2>   value's component type of the mapped {@code Map}
     * @return A new {@code Map}.
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Map<K2, V2> flatMap(@NonNull BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    /**
     * Flat-maps this entries to a sequence of values.
     * <p>
     * Please use {@link #flatMap(BiFunction)} if the result should be a {@code Map}
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A sequence of flat-mapped values.
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Seq<U> flatMap(@NonNull Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return iterator().flatMap(mapper).toStream();
    }

    @Override
    default <U> U foldRight(U zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    /**
     * Performs an action on key, value pair.
     *
     * @param action A {@code BiConsumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(@NonNull BiConsumer<K, V> action) {
        Objects.requireNonNull(action, "action is null");
        for (Tuple2<K, V> t : this) {
            action.accept(t._1(), t._2());
        }
    }

    /**
     * Returns the {@code Some} of value to which the specified key
     * is mapped, or {@code None} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the {@code Some} of value to which the specified key
     * is mapped, or {@code None} if this map contains no mapping
     * for the key
     */
    Option<V> get(K key);

    /**
     * Returns the value associated with a key, or a default value if the key is not contained in the map.
     *
     * @param key          the key
     * @param defaultValue a default value
     * @return the value associated with key if it exists, otherwise the default value.
     */
    V getOrElse(K key, V defaultValue);

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    @NonNull
    Iterator<Tuple2<K, V>> iterator();

    /**
     * Iterates this Map sequentially, mapping the (key, value) pairs to elements.
     *
     * @param mapper A function that maps (key, value) pairs to elements of type U
     * @param <U> The type of the resulting elements
     * @return An iterator through the mapped elements.
     */
    default <U> Iterator<U> iterator(@NonNull BiFunction<K, V, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return iterator().map(t -> mapper.apply(t._1(), t._2()));
    }

    /**
     * Returns the keys contained in this map.
     *
     * @return {@code Set} of the keys contained in this map.
     */
    io.vavr.collection.Set<K> keySet();

    /**
     * Returns the keys contained in this map as an iterator.
     *
     * @return {@code Iterator} of the keys contained in this map.
     */
    default Iterator<K> keysIterator() {
        return iterator().map(Tuple2::_1);
    }

    @Override
    default int length() {
        return size();
    }

    /**
     * Turns this map into a plain function returning an Option result.
     *
     * @return a function that takes a key k and returns its value in a Some if found, otherwise a None.
     */
    default Function1<K, Option<V>> lift() {
        return this::get;
    }

    /**
     * Maps the {@code Map} entries to a sequence of values.
     * <p>
     * Please use {@link #map(BiFunction)} if the result has to be of type {@code Map}.
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A sequence of mapped values.
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Seq<U> map(@NonNull Function<? super Tuple2<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return (Seq<U>) iterator().map(mapper).toStream();
    }

    @Override
    default <U> Seq<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Seq<Void> mapToVoid() {
        return map(ignored -> null);
    }

    /**
     * Maps the entries of this {@code Map} to form a new {@code Map}.
     *
     * @param <K2>   key's component type of the map result
     * @param <V2>   value's component type of the map result
     * @param mapper a {@code Function} that maps entries of type {@code (K, V)} to entries of type {@code (K2, V2)}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Map<K2, V2> map(@NonNull BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    /**
     * Maps the keys of this {@code Map} while preserving the corresponding values.
     * <p>
     * The size of the result map may be smaller if {@code keyMapper} maps two or more distinct keys to the same new key.
     * In this case the value at the {@code latest} of the original keys is retained.
     * Order of keys is predictable in {@code TreeMap} (by comparator) and {@code LinkedHashMap} (insertion-order) and not predictable in {@code HashMap}.
     *
     * @param <K2>      the new key type
     * @param keyMapper a {@code Function} that maps keys of type {@code V} to keys of type {@code V2}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code keyMapper} is null
     */
    <K2> Map<K2, V> mapKeys(@NonNull Function<? super K, ? extends K2> keyMapper);

    /**
     * Maps the keys of this {@code Map} while preserving the corresponding values and applying a value merge function on collisions.
     * <p>
     * The size of the result map may be smaller if {@code keyMapper} maps two or more distinct keys to the same new key.
     * In this case the associated values will be combined using {@code valueMerge}.
     *
     * @param <K2>       the new key type
     * @param keyMapper  a {@code Function} that maps keys of type {@code V} to keys of type {@code V2}
     * @param valueMerge a {@code BiFunction} that merges values
     * @return a new {@code Map}
     * @throws NullPointerException if {@code keyMapper} is null
     */
    <K2> Map<K2, V> mapKeys(@NonNull Function<? super K, ? extends K2> keyMapper, @NonNull BiFunction<? super V, ? super V, ? extends V> valueMerge);

    /**
     * Maps the values of this {@code Map} while preserving the corresponding keys.
     *
     * @param <V2>        the new value type
     * @param valueMapper a {@code Function} that maps values of type {@code V} to values of type {@code V2}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code valueMapper} is null
     */
    <V2> Map<K, V2> mapValues(@NonNull Function<? super V, ? extends V2> valueMapper);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * If collisions occur, the value of {@code this} map is taken.
     *
     * @param that the other map
     * @return A merged map
     * @throws NullPointerException if that map is null
     */
    Map<K, V> merge(@NonNull Map<? extends K, ? extends V> that);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> map
     * and the second from <code>that</code> map.
     *
     * @param <U>                 value type of that Map
     * @param that                the other map
     * @param collisionResolution the collision resolution function
     * @return A merged map
     * @throws NullPointerException if that map or the given collision resolution function is null
     */
    <U extends V> Map<K, V> merge(@NonNull Map<? extends K, U> that, @NonNull BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(K key, V value);

    /**
     * Convenience method for {@code put(entry._1(), entry._2())}.
     *
     * @param entry A Tuple2 containing the key and value
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(@NonNull Tuple2<? extends K, ? extends V> entry);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the merge
     * function is used to combine the previous value to the value to
     * be inserted, and the result of that call is inserted in the map.
     *
     * @param <U>   the value type
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param merge function taking the old and new values and merging them.
     * @return A new Map containing these elements and that entry.
     */
    <U extends V> Map<K, V> put(K key, U value, @NonNull BiFunction<? super V, ? super U, ? extends V> merge);

    /**
     * Convenience method for {@code put(entry._1(), entry._2(), merge)}.
     *
     * @param <U>   the value type
     * @param entry A Tuple2 containing the key and value
     * @param merge function taking the old and new values and merging them.
     * @return A new Map containing these elements and that entry.
     */
    <U extends V> Map<K, V> put(@NonNull Tuple2<? extends K, U> entry, @NonNull BiFunction<? super V, ? super U, ? extends V> merge);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return A new Map containing these elements without the entry
     * specified by that key.
     */
    Map<K, V> remove(K key);

    /**
     * Returns a new Map consisting of all elements which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #reject(BiPredicate)}
     * @param predicate the predicate used to test elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Map<K, V> removeAll(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param keys keys are to be removed from the map
     * @return A new Map containing these elements without the entries
     * specified by that keys.
     */
    Map<K, V> removeAll(@NonNull Iterable<? extends K> keys);

    /**
     * Returns a new Map consisting of all elements with keys which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #rejectKeys(Predicate)}
     * @param predicate the predicate used to test keys of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Map<K, V> removeKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Map consisting of all elements with values which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #rejectValues(Predicate)}
     * @param predicate the predicate used to test values of elements
     * @return a new Map
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Map<K, V> removeValues(@NonNull Predicate<? super V> predicate);

    @Override
    default <U> Seq<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super Tuple2<K, V>, ? extends U> operation) {
        return io.vavr.collection.Collections.scanLeft(this, zero, operation, io.vavr.collection.Iterator::toVector);
    }

    @Override
    default <U> Seq<U> scanRight(U zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> operation) {
        return io.vavr.collection.Collections.scanRight(this, zero, operation, io.vavr.collection.Iterator::toVector);
    }

    @Override
    int size();

    /**
     * Converts this Vavr {@code Map} to a {@code java.util.Map} while preserving characteristics
     * like insertion order ({@code LinkedHashMap}) and sort order ({@code SortedMap}).
     *
     * @return a new {@code java.util.Map} instance
     */
    java.util.Map<K, V> toJavaMap();

    /**
     * Transforms this {@code Map}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Map<K, V>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Unzips the entries of this {@code Map} by treating each key-value pair as an element,
     * and splitting them into two separate {@code Seq} collections - one for keys and one for values.
     *
     * @return a {@code Tuple2} containing two {@code Seq} collections: first with all keys, second with all values
     */
    default Tuple2<Seq<K>, Seq<V>> unzip() {
        return unzip(Function.identity());
    }

    /**
     * Unzips the entries of this {@code Map} by mapping each key-value pair to a tuple.
     * The unzipper function transforms each entry into a {@code Tuple2}, and then all first elements
     * are collected into the first {@code Seq} and all second elements into the second {@code Seq}.
     *
     * @param unzipper a function that maps key-value pairs of this {@code Map} to tuples
     * @param <T1>     type of the first element in the resulting pairs
     * @param <T2>     type of the second element in the resulting pairs
     * @return a {@code Tuple2} containing two {@code Seq} collections with the split elements
     * @throws NullPointerException if {@code unzipper} is null
     */
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(@NonNull BiFunction<? super K, ? super V, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip(entry -> unzipper.apply(entry._1(), entry._2()));
    }

    @Override
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(@NonNull Function<? super Tuple2<K, V>, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    /**
     * Unzips the entries of this {@code Map} by mapping each key-value pair to a triple.
     * The unzipper function transforms each entry into a {@code Tuple3}, and then elements are
     * distributed to respective {@code Seq} collections by their position in the tuple: all first
     * elements into the first {@code Seq}, all second elements into the second {@code Seq},
     * and all third elements into the third {@code Seq}.
     *
     * @param unzipper a function that maps key-value pairs of this {@code Map} to triples
     * @param <T1>     type of the first element in the resulting triples
     * @param <T2>     type of the second element in the resulting triples
     * @param <T3>     type of the third element in the resulting triples
     * @return a {@code Tuple3} containing three {@code Seq} collections with the split elements
     * @throws NullPointerException if {@code unzipper} is null
     */
    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(@NonNull BiFunction<? super K, ? super V, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip3(entry -> unzipper.apply(entry._1(), entry._2()));
    }

    @Override
    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(
      @NonNull Function<? super Tuple2<K, V>, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(Stream::ofAll, Stream::ofAll, Stream::ofAll);
    }

    /**
     * Returns a new {@link Seq} that contains the values of this {@code Map}.
     *
     * <pre>{@code
     * // = Seq("a", "b", "c")
     * HashMap.of(1, "a", 2, "b", 3, "c").values()
     * }</pre>
     *
     * @return a new {@link Seq}
     */
    Seq<V> values();

    /**
     * Returns the values in this map.
     *
     * <pre>{@code
     * // = Iterator.of("a", "b", "c")
     * HashMap.of(1, "a", 2, "b", 3, "c").values()
     * }</pre>
     *
     * @return a new {@link Iterator}
     */
    default Iterator<V> valuesIterator() {
        return iterator().map(Tuple2::_2);
    }

    /**
     * Turns this map from a partial function into a total function that
     * returns a value computed by defaultFunction for all keys
     * absent from the map.
     *
     * @param defaultFunction function to evaluate for all keys not present in the map
     * @return a total function from K to T
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<K, V> withDefault(@NonNull Function<? super K, ? extends V> defaultFunction) {
        return k -> get(k).getOrElse(() -> defaultFunction.apply(k));
    }

    /**
     * Turns this map from a partial function into a total function that
     * returns defaultValue for all keys absent from the map.
     *
     * @param defaultValue default value to return for all keys not present in the map
     * @return a total function from K to T
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<K, V> withDefaultValue(V defaultValue) {
        return k -> get(k).getOrElse(defaultValue);
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zip(@NonNull Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    default <U, R> Seq<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super Tuple2<K, V>, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return Stream.ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zipAll(@NonNull Iterable<? extends U> that, Tuple2<K, V> thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default Seq<Tuple2<Tuple2<K, V>, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    default <U> Seq<U> zipWithIndex(@NonNull BiFunction<? super Tuple2<K, V>, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return Stream.ofAll(iterator().zipWithIndex(mapper));
    }

    // -- Adjusted return types of Traversable methods

    @Override
    Map<K, V> distinct();

    @Override
    Map<K, V> distinctBy(@NonNull Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> Map<K, V> distinctBy(@NonNull Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    Map<K, V> drop(int n);

    @Override
    Map<K, V> dropRight(int n);

    @Override
    Map<K, V> dropUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> dropWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> filter(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> reject(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(@NonNull Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    io.vavr.collection.Iterator<? extends Map<K, V>> grouped(int size);

    @Deprecated
    @Override
    default boolean isDefinedAt(K key) {
        return containsKey(key);
    }

    @Override
    default boolean isDistinct() {
        return true;
    }

    @Override
    Map<K, V> init();

    @Override
    Option<? extends Map<K, V>> initOption();

    @Override
    Map<K, V> orElse(Iterable<? extends Tuple2<K, V>> other);

    @Override
    Map<K, V> orElse(@NonNull Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> peek(@NonNull Consumer<? super Tuple2<K, V>> action);

    @Override
    Map<K, V> replace(@NonNull Tuple2<K, V> currentElement, @NonNull Tuple2<K, V> newElement);

    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     *
     * @param key   the key of the element to be substituted.
     * @param value the new value to be associated with the key
     * @return a new map containing key mapped to value if key was contained before. The old map otherwise.
     */
    Map<K, V> replaceValue(K key, V value);

    /**
     * Replaces the entry for the specified key only if currently mapped to the specified value.
     *
     * @param key      the key of the element to be substituted.
     * @param oldValue the expected current value that the key is currently mapped to
     * @param newValue the new value to be associated with the key
     * @return a new map containing key mapped to newValue if key was contained before and oldValue matched. The old map otherwise.
     */
    Map<K, V> replace(K key, V oldValue, V newValue);

    /**
     * Replaces each entry's value with the result of invoking the given function on that entry until all entries have been processed or the function throws an exception.
     *
     * @param function function transforming key and current value to a new value
     * @return a new map with the same keySet but transformed values.
     */
    Map<K, V> replaceAll(@NonNull BiFunction<? super K, ? super V, ? extends V> function);

    @Override
    Map<K, V> replaceAll(@NonNull Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Map<K, V> retainAll(@NonNull Iterable<? extends Tuple2<K, V>> elements);

    @Override
    Map<K, V> scan(@NonNull Tuple2<K, V> zero,
                   @NonNull BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    io.vavr.collection.Iterator<? extends Map<K, V>> slideBy(@NonNull Function<? super Tuple2<K, V>, ?> classifier);

    @Override
    io.vavr.collection.Iterator<? extends Map<K, V>> sliding(int size);

    @Override
    io.vavr.collection.Iterator<? extends Map<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> tail();

    @Override
    Option<? extends Map<K, V>> tailOption();

    @Override
    Map<K, V> take(int n);

    @Override
    Map<K, V> takeRight(int n);

    @Override
    Map<K, V> takeUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> takeWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

}
