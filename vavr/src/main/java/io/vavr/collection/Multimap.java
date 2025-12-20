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
 * An immutable {@code Multimap} interface.
 *
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #containsKey(Object)}</li>
 * <li>{@link #containsValue(Object)}</li>
 * <li>{@link #get(Object)}</li>
 * <li>{@link #getContainerType()}</li>
 * <li>{@link #keySet()}</li>
 * <li>{@link #merge(Multimap)}</li>
 * <li>{@link #merge(Multimap, BiFunction)}</li>
 * <li>{@link #put(Object, Object)}</li>
 * <li>{@link #put(Tuple2)}</li>
 * <li>{@link #values()}</li>
 * </ul>
 *
 * Conversion:
 * <ul>
 * <li>{@link #toJavaMap()}</li>
 * </ul>
 *
 * Filtering:
 *
 * <ul>
 * <li>{@link #filter(BiPredicate)}</li>
 * <li>{@link #filterKeys(Predicate)}</li>
 * <li>{@link #filterValues(Predicate)}</li>
 * <li>{@link #reject(BiPredicate)}</li>
 * <li>{@link #rejectKeys(Predicate)}</li>
 * <li>{@link #rejectValues(Predicate)}</li>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #remove(Object, Object)}</li>
 * <li>{@link #removeAll(Iterable)}</li>
 * </ul>
 *
 * Iteration:
 *
 * <ul>
 * <li>{@link #forEach(BiConsumer)}</li>
 * <li>{@link #iterator(BiFunction)}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #bimap(Function, Function)}</li>
 * <li>{@link #flatMap(BiFunction)}</li>
 * <li>{@link #map(BiFunction)}</li>
 * <li>{@link #mapValues(Function)}</li>
 * <li>{@link #transform(Function)}</li>
 * <li>{@link #unzip(BiFunction)}</li>
 * <li>{@link #unzip3(BiFunction)}</li>
 * </ul>
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 */
public interface Multimap<K, V> extends Traversable<Tuple2<K, V>>, PartialFunction<K, Traversable<V>>, Serializable {

    long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    enum ContainerType {
        SET(
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem),
                java.util.HashSet::new

        ),
        SORTED_SET(
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem),
                java.util.TreeSet::new
        ),
        SEQ(
                (Traversable<?> seq, Object elem) -> ((io.vavr.collection.List<Object>) seq).append(elem),
                (Traversable<?> seq, Object elem) -> ((io.vavr.collection.List<Object>) seq).remove(elem),
                java.util.ArrayList::new
        );

        private final BiFunction<Traversable<?>, Object, Traversable<?>> add;
        private final BiFunction<Traversable<?>, Object, Traversable<?>> remove;
        private final Supplier<Collection<?>> instantiate;

        ContainerType(
                BiFunction<Traversable<?>, Object, Traversable<?>> add,
                BiFunction<Traversable<?>, Object, Traversable<?>> remove,
                Supplier<Collection<?>> instantiate) {
            this.add = add;
            this.remove = remove;
            this.instantiate = instantiate;
        }


        <T> Traversable<T> add(Traversable<?> container, T elem) {
            return (Traversable<T>) add.apply(container, elem);
        }

        <T> Traversable<T> remove(Traversable<?> container, T elem) {
            return (Traversable<T>) remove.apply(container, elem);
        }

        <T> Collection<T> instantiate() {
            return (Collection<T>) instantiate.get();
        }
    }

    /**
     * Narrows a widened {@code Multimap<? extends K, ? extends V>} to {@code Multimap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param map A {@code Multimap}.
     * @param <K> Key type
     * @param <V> Value type
     * @return the given {@code multimap} instance as narrowed type {@code Multimap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    static <K, V> Multimap<K, V> narrow(Multimap<? extends K, ? extends V> map) {
        return (Multimap<K, V>) map;
    }

    // -- non-static API

    @Deprecated
    @Override
    default Traversable<V> apply(K key) {
        return get(key).getOrElseThrow(NoSuchElementException::new);
    }

    /**
     * Converts this {@code Multimap} to a {@code Map}
     *
     * @return {@code Map<K, Traversable<V>>}
     */
    Map<K, Traversable<V>> asMap();

    /**
     * Turns this {@code Multimap} into a {@link PartialFunction} which is defined at a specific index, if this {@code Multimap}
     * contains the given key. When applied to a defined key, the partial function will return
     * the {@link Traversable} of this {@code Multimap} that is associated with the key.
     *
     * @return a new {@link PartialFunction}
     * @throws NoSuchElementException when a non-existing key is applied to the partial function
     */
    default PartialFunction<K, Traversable<V>> asPartialFunction() throws IndexOutOfBoundsException {
        return new PartialFunction<K, Traversable<V>>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Traversable<V> apply(K key) {
                return get(key).getOrElseThrow(NoSuchElementException::new);
            }
            @Override
            public boolean isDefinedAt(K key) {
                return containsKey(key);
            }
        };
    }

    /**
     * Maps this {@code Multimap} to a new {@code Multimap} with different component type by applying a function to its elements.
     *
     * @param <K2>        key's component type of the multimap result
     * @param <V2>        value's component type of the multimap result
     * @param keyMapper   a {@code Function} that maps the keys of type {@code K} to keys of type {@code K2}
     * @param valueMapper a {@code Function} that the values of type {@code V} to values of type {@code V2}
     * @return a new {@code Multimap}
     * @throws NullPointerException if {@code keyMapper} or {@code valueMapper} is null
     */
    <K2, V2> Multimap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper);

    @Override
    default <R> Seq<R> collect(@NonNull PartialFunction<? super Tuple2<K, V>, ? extends R> partialFunction) {
        return Vector.ofAll(iterator().<R> collect(partialFunction));
    }

    @Override
    default boolean contains(@NonNull Tuple2<K, V> element) {
        return get(element._1).map(v -> v.contains(element._2)).getOrElse(false);
    }

    /**
     * Returns <code>true</code> if this multimap contains a mapping for the specified key.
     *
     * @param key key whose presence in this multimap is to be tested
     * @return <code>true</code> if this multimap contains a mapping for the specified key
     */
    boolean containsKey(K key);

    /**
     * Returns <code>true</code> if this multimap maps one or more keys to the
     * specified value. This operation will require time linear in the map size.
     *
     * @param value value whose presence in this multimap is to be tested
     * @return <code>true</code> if this multimap maps one or more keys to the
     * specified value
     */
    default boolean containsValue(V value) {
        return iterator().map(Tuple2::_2).contains(value);
    }

    /**
     * Returns a new Multimap consisting of all elements which satisfy the given predicate.
     *
     * @param predicate the predicate used to test elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> filter(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Returns a new Multimap consisting of all elements which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> reject(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Returns a new Multimap consisting of all elements with keys which satisfy the given predicate.
     *
     * @param predicate the predicate used to test keys of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> filterKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Multimap consisting of all elements with keys which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test keys of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> rejectKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Multimap consisting of all elements with values which satisfy the given predicate.
     *
     * @param predicate the predicate used to test values of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> filterValues(@NonNull Predicate<? super V> predicate);

    /**
     * Returns a new Multimap consisting of all elements with values which do not satisfy the given predicate.
     *
     * @param predicate the predicate used to test values of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    Multimap<K, V> rejectValues(@NonNull Predicate<? super V> predicate);

    /**
     * FlatMaps this {@code Multimap} to a new {@code Multimap} with different component type.
     *
     * @param mapper A mapper
     * @param <K2>   key's component type of the mapped {@code Multimap}
     * @param <V2>   value's component type of the mapped {@code Multimap}
     * @return A new {@code Multimap}.
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Multimap<K2, V2> flatMap(@NonNull BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    /**
     * Performs an action on key, value pair.
     *
     * @param action A {@code BiConsumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(@NonNull BiConsumer<K, V> action) {
        Objects.requireNonNull(action, "action is null");
        for (Tuple2<K, V> t : this) {
            action.accept(t._1, t._2);
        }
    }

    /**
     * Returns the {@code Some} of value to which the specified key
     * is mapped, or {@code None} if this multimap contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the {@code Some} of value to which the specified key
     * is mapped, or {@code None} if this multimap contains no mapping
     * for the key
     */
    Option<Traversable<V>> get(K key);

    /**
     * Returns the value associated with a key, or a default value if the key is not contained in the map
     *
     * @param key          the key
     * @param defaultValue a default value
     * @return the value associated with key if it exists, otherwise the result of the default value
     */
    Traversable<V> getOrElse(K key, Traversable<? extends V> defaultValue);

    /**
     * Returns the type of the {@code Traversable} value container of this {@code MultiMap}.
     *
     * @return an enum value representing the container type
     */
    ContainerType getContainerType();

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

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
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    @NonNull
    Iterator<Tuple2<K, V>> iterator();

    /**
     * Iterates this Multimap sequentially, mapping the (key, value) pairs to elements.
     *
     * @param mapper A function that maps (key, value) pairs to elements of type U
     * @param <U> The type of the resulting elements
     * @return An iterator through the mapped elements.
     */
    default <U> Iterator<U> iterator(@NonNull BiFunction<K, V, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return iterator().map(t -> mapper.apply(t._1, t._2));
    }

    /**
     * Returns the keys contained in this multimap.
     *
     * @return {@code Set} of the keys contained in this multimap.
     */
    Set<K> keySet();

    @Override
    default int length() {
        return size();
    }

    /**
     * Maps the entries of this {@code Multimap} to form a new {@code Multimap}.
     *
     * @param <K2>   key's component type of the multimap result
     * @param <V2>   value's component type of the multimap result
     * @param mapper a {@code Function} that maps entries of type {@code (K, V)} to entries of type {@code (K2, V2)}
     * @return a new {@code Multimap}
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Multimap<K2, V2> map(@NonNull BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    /**
     * Maps the {@code Multimap} entries to a sequence of values.
     * <p>
     * Please use {@link #map(BiFunction)} if the result has to be of type {@code Multimap}.
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A sequence of mapped values.
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Seq<U> map(@NonNull Function<? super Tuple2<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        // don't remove cast, doesn't compile in Eclipse without it
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
     * Maps the values of this {@code Multimap} while preserving the corresponding keys.
     *
     * @param <V2>        the new value type
     * @param valueMapper a {@code Function} that maps values of type {@code V} to values of type {@code V2}
     * @return a new {@code Multimap}
     * @throws NullPointerException if {@code valueMapper} is null
     */
    <V2> Multimap<K, V2> mapValues(@NonNull Function<? super V, ? extends V2> valueMapper);

    /**
     * Creates a new multimap which by merging the entries of {@code this} multimap and {@code that} multimap.
     * <p>
     * If collisions occur, the value of {@code this} multimap is taken.
     *
     * @param that the other multimap
     * @return A merged multimap
     * @throws NullPointerException if that multimap is null
     */
    Multimap<K, V> merge(@NonNull Multimap<? extends K, ? extends V> that);

    /**
     * Creates a new multimap which by merging the entries of {@code this} multimap and {@code that} multimap.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> multimap
     * and the second from <code>that</code> multimap.
     *
     * @param <K2>                key type of that Multimap
     * @param <V2>                value type of that Multimap
     * @param that                the other multimap
     * @param collisionResolution the collision resolution function
     * @return A merged multimap
     * @throws NullPointerException if that multimap or the given collision resolution function is null
     */
    <K2 extends K, V2 extends V> Multimap<K, V> merge(@NonNull Multimap<K2, V2> that, @NonNull BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution);

    /**
     * Associates the specified value with the specified key in this multimap.
     * If the map previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return A new Multimap containing these elements and that entry.
     */
    Multimap<K, V> put(K key, V value);

    /**
     * Convenience method for {@code put(entry._1, entry._2)}.
     *
     * @param entry A Tuple2 containing the key and value
     * @return A new Multimap containing these elements and that entry.
     */
    Multimap<K, V> put(@NonNull Tuple2<? extends K, ? extends V> entry);

    /**
     * Removes the mapping for a key from this multimap if it is present.
     *
     * @param key key whose mapping is to be removed from the multimap
     * @return A new Multimap containing these elements without the entry
     * specified by that key.
     */
    Multimap<K, V> remove(K key);

    /**
     * Removes the key-value pair from this multimap if it is present.
     *
     * @param key   key whose mapping is to be removed from the multimap
     * @param value value whose mapping is to be removed from the multimap
     * @return A new Multimap containing these elements without the entry
     * specified by that key and value.
     */
    Multimap<K, V> remove(K key, V value);

    /**
     * Returns a new Multimap consisting of all elements which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #reject(BiPredicate)}
     * @param predicate the predicate used to test elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Multimap<K, V> removeAll(@NonNull BiPredicate<? super K, ? super V> predicate);

    /**
     * Removes the mapping for a key from this multimap if it is present.
     *
     * @param keys keys are to be removed from the multimap
     * @return A new Multimap containing these elements without the entries
     * specified by that keys.
     */
    Multimap<K, V> removeAll(@NonNull Iterable<? extends K> keys);

    /**
     * Returns a new Multimap consisting of all elements with keys which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #rejectKeys(Predicate)}
     * @param predicate the predicate used to test keys of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Multimap<K, V> removeKeys(@NonNull Predicate<? super K> predicate);

    /**
     * Returns a new Multimap consisting of all elements with values which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #rejectValues(Predicate)}
     * @param predicate the predicate used to test values of elements
     * @return a new Multimap
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Multimap<K, V> removeValues(@NonNull Predicate<? super V> predicate);

    @Override
    default <U> Seq<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super Tuple2<K, V>, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, Iterator::toVector);
    }

    @Override
    default <U> Seq<U> scanRight(U zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, Iterator::toVector);
    }

    @Override
    int size();

    /**
     * Converts this Vavr {@code Map} to a {@code java.util.Map} while preserving characteristics
     * like insertion order ({@code LinkedHashMultimap}) and sort order ({@code SortedMultimap}).
     *
     * @return a new {@code java.util.Map} instance
     */
    java.util.Map<K, java.util.Collection<V>> toJavaMap();

    /**
     * Transforms this {@code Multimap}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Multimap<K, V>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(@NonNull BiFunction<? super K, ? super V, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip(entry -> unzipper.apply(entry._1, entry._2));
    }

    @Override
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(@NonNull Function<? super Tuple2<K, V>, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(@NonNull BiFunction<? super K, ? super V, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip3(entry -> unzipper.apply(entry._1, entry._2));
    }

    @Override
    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(
      @NonNull Function<? super Tuple2<K, V>, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(Stream::ofAll, Stream::ofAll, Stream::ofAll);
    }

    /**
     * Returns a {@link Traversable} containing all the values of this {@code Multimap}.
     *
     * @return a new {@link Traversable}
     */
    Traversable<V> values();

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zip(@NonNull Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zipAll(@NonNull Iterable<? extends U> that, Tuple2<K, V> thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default <U, R> Seq<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super Tuple2<K, V>, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return Stream.ofAll(iterator().zipWith(that, mapper));
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
    Multimap<K, V> distinct();

    @Override
    Multimap<K, V> distinctBy(@NonNull Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> Multimap<K, V> distinctBy(@NonNull Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    Multimap<K, V> drop(int n);

    @Override
    Multimap<K, V> dropRight(int n);

    @Override
    Multimap<K, V> dropUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> dropWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> filter(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> reject(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    /**
     * Flat-maps this entries to a sequence of values.
     * <p>
     * Please use {@link #flatMap(BiFunction)} if the result should be a {@code Multimap}
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A sequence of flat-mapped values.
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Seq<U> flatMap(@NonNull Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        // don't remove cast, doesn't compile in Eclipse without it
        return (Seq<U>) iterator().flatMap(mapper).toStream();
    }

    @Override
    default <U> U foldRight(U zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    <C> Map<C, ? extends Multimap<K, V>> groupBy(@NonNull Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends Multimap<K, V>> grouped(int size);

    @Override
    Multimap<K, V> init();

    @Override
    Option<? extends Multimap<K, V>> initOption();

    @Override
    Multimap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other);

    @Override
    Multimap<K, V> orElse(@NonNull Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier);

    @Override
    Tuple2<? extends Multimap<K, V>, ? extends Multimap<K, V>> partition(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> peek(@NonNull Consumer<? super Tuple2<K, V>> action);

    @Override
    Multimap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Multimap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     *
     * @param key   the key of the element to be substituted
     * @param value the new value to be associated with the key
     * @return a new map containing key mapped to value if key was contained before. The old map otherwise
     */
    Multimap<K, V> replaceValue(K key, V value);

    /**
     * Replaces the entry with the specified key and oldValue.
     *
     * @param key      the key of the element to be substituted
     * @param oldValue the expected current value associated with the key
     * @param newValue the new value to be associated with the key
     * @return a new map containing key mapped to newValue if key was contained before and oldValue was associated with the key. The old map otherwise.
     */
    Multimap<K, V> replace(K key, V oldValue, V newValue);

    /**
     * Replaces each entry's values with the result of invoking the given function on that each tuple until all entries have been processed or the function throws an exception.
     *
     * @param function function transforming key and current value to a new value
     * @return a new map with the same keySet but transformed values
     */
    Multimap<K, V> replaceAll(@NonNull BiFunction<? super K, ? super V, ? extends V> function);

    @Override
    Multimap<K, V> retainAll(@NonNull Iterable<? extends Tuple2<K, V>> elements);

    @Override
    Multimap<K, V> scan(Tuple2<K, V> zero,
                        @NonNull BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    Iterator<? extends Multimap<K, V>> slideBy(@NonNull Function<? super Tuple2<K, V>, ?> classifier);

    @Override
    Iterator<? extends Multimap<K, V>> sliding(int size);

    @Override
    Iterator<? extends Multimap<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends Multimap<K, V>, ? extends Multimap<K, V>> span(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> tail();

    @Override
    Option<? extends Multimap<K, V>> tailOption();

    @Override
    Multimap<K, V> take(int n);

    @Override
    Multimap<K, V> takeRight(int n);

    @Override
    Multimap<K, V> takeUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> takeWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

}
