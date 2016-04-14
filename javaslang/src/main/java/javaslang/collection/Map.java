/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

import java.util.*;
import java.util.function.*;

/**
 * An immutable {@code Map} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich, Ruslan Sennov
 * @since 2.0.0
 */
public interface Map<K, V> extends Traversable<Tuple2<K, V>>, Function1<K, V> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code Map<? extends K, ? extends V>} to {@code Map<K, V>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
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

    @Override
    default V apply(K key) {
        return get(key).getOrElseThrow(NoSuchElementException::new);
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
    <K2, V2> Map<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper);

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
     * FlatMaps this {@code Map} to a new {@code Map} with different component type.
     *
     * @param mapper A mapper
     * @param <K2>   key's component type of the mapped {@code Map}
     * @param <V2>   value's component type of the mapped {@code Map}
     * @return A new {@code Map}.
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Map<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

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
     * Returns the keys contained in this map.
     *
     * @return {@code Set} of the keys contained in this map.
     */
    Set<K> keySet();

    /**
     * Maps the entries of this {@code Map} to form a new {@code Map}.
     *
     * @param <K2>   key's component type of the map result
     * @param <V2>   value's component type of the map result
     * @param mapper a {@code Function} that maps entries of type {@code (K, V)} to entries of type {@code (K2, V2)}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Map<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    /**
     * Maps the values of this {@code Map} while preserving the corresponding keys.
     *
     * @param <V2>        the new value type
     * @param valueMapper a {@code Function} that maps values of type {@code V} to values of type {@code V2}
     * @return a new {@code Map}
     * @throws NullPointerException if {@code valueMapper} is null
     */
    <V2> Map<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper);

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
     * Convenience method for {@code put(entry._1, entry._2)}.
     *
     * @param entry A Tuple2 containing the key and value
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(Tuple2<? extends K, ? extends V> entry);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return A new Map containing these elements without the entry
     * specified by that key.
     */
    Map<K, V> remove(K key);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param keys keys are to be removed from the map
     * @return A new Map containing these elements without the entries
     * specified by that keys.
     */
    Map<K, V> removeAll(Iterable<? extends K> keys);

    @Override
    int size();

    /**
     * Converts this Javaslang {@code Map} to a {@code java.util.Map} while preserving characteristics
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
    default <U> U transform(Function<? super Map<K, V>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    default <U> Seq<U> traverse(BiFunction<K, V, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(List.empty(), (acc, entry) -> acc.append(mapper.apply(entry._1, entry._2)));
    }

    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip(entry -> unzipper.apply(entry._1, entry._2));
    }

    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(BiFunction<? super K, ? super V, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip3(entry -> unzipper.apply(entry._1, entry._2));
    }

    Seq<V> values();

    // -- Adjusted return types of Traversable methods

    @Override
    default boolean contains(Tuple2<K, V> element) {
        return get(element._1).map(v -> Objects.equals(v, element._2)).getOrElse(false);
    }

    @Override
    Map<K, V> distinct();

    @Override
    Map<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> Map<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    Map<K, V> drop(long n);

    @Override
    Map<K, V> dropRight(long n);

    @Override
    Map<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

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
    default <U> Seq<U> flatMap(Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        // don't remove cast, doesn't compile in Eclipse without it
        return (Seq<U>) iterator().flatMap(mapper).toStream();
    }

    @Override
    default <U> U foldRight(U zero, BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends Map<K, V>> grouped(long size);

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    Map<K, V> init();

    @Override
    Option<? extends Map<K, V>> initOption();

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    Iterator<Tuple2<K, V>> iterator();

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
    default <U> Seq<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        // don't remove cast, doesn't compile in Eclipse without it
        return (Seq<U>) iterator().map(mapper).toStream();
    }

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * If collisions occur, the value of {@code this} map is taken.
     *
     * @param that the other map
     * @return A merged map
     * @throws NullPointerException if that map is null
     */
    Map<K, V> merge(Map<? extends K, ? extends V> that);

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
    <U extends V> Map<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Map<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Map<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements);

    @Override
    Map<K, V> scan(Tuple2<K, V> zero,
                   BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    default <U> Seq<U> scanLeft(U zero, BiFunction<? super U, ? super Tuple2<K, V>, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, List.empty(), List::prepend, List::reverse);
    }

    @Override
    default <U> Seq<U> scanRight(U zero, BiFunction<? super Tuple2<K, V>, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, List.empty(), List::prepend, Function.identity());
    }

    @Override
    Iterator<? extends Map<K, V>> sliding(long size);

    @Override
    Iterator<? extends Map<K, V>> sliding(long size, long step);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    default Spliterator<Tuple2<K, V>> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Map<K, V> tail();

    @Override
    Option<? extends Map<K, V>> tailOption();

    @Override
    Map<K, V> take(long n);

    @Override
    Map<K, V> takeRight(long n);

    @Override
    Map<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super Tuple2<K, V>, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    @Override
    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(
            Function<? super Tuple2<K, V>, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(Stream::ofAll, Stream::ofAll, Stream::ofAll);
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zip(Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zip(that));
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zipAll(Iterable<? extends U> that, Tuple2<K, V> thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default Seq<Tuple2<Tuple2<K, V>, Long>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }

    /**
     * Performs an action on key, value pair.
     *
     * @param action A {@code BiConsumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(BiConsumer<K, V> action) {
        Objects.requireNonNull(action, "action is null");
        for (Tuple2<K, V> t : this) {
            action.accept(t._1, t._2);
        }
    }

    /**
     * Turns this map from a partial function into a total function that
     * returns defaultValue for all keys absent from the map.
     *
     * @param defaultValue default value to return for all keys not present in the map
     * @return a total function from K to T
     */
    default Function1<K, V> withDefaultValue(V defaultValue) {
        return k -> get(k).getOrElse(defaultValue);
    }

    /**
     * Turns this map from a partial function into a total function that
     * returns a value computed by defaultFunction for all keys
     * absent from the map.
     *
     * @param defaultFunction function to evaluate for all keys not present in the map
     * @return a total function from K to T
     */
    default Function1<K, V> withDefault(Function<? super K, ? extends V> defaultFunction) {
        return k -> get(k).getOrElse(() -> defaultFunction.apply(k));
    }
}
