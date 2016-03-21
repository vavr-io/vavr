/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code Multimap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Ruslan Sennov
 * @since 2.0.0
 */
public interface Multimap<K, V> extends Traversable<Tuple2<K, V>>, Function1<K, Traversable<V>>, Kind2<Multimap<?, ?>, K, V> {

    long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    enum ContainerType {

        SET(
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem)
        ),
        SORTED_SET(
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).add(elem),
                (Traversable<?> set, Object elem) -> ((Set<Object>) set).remove(elem)
        ),
        SEQ(
                (Traversable<?> seq, Object elem) -> ((List<Object>) seq).append(elem),
                (Traversable<?> seq, Object elem) -> ((List<Object>) seq).remove(elem)
        );

        final BiFunction<Traversable<?>, Object, Traversable<?>> add;
        final BiFunction<Traversable<?>, Object, Traversable<?>> remove;

        ContainerType(BiFunction<Traversable<?>, Object, Traversable<?>> add,
                              BiFunction<Traversable<?>, Object, Traversable<?>> remove) {
            this.add = add;
            this.remove = remove;
        }

        <T> Traversable<T> add(Traversable<T> container, T elem) {
            return (Traversable<T>) add.apply(container, elem);
        }

        <T> Traversable<T> remove(Traversable<T> container, T elem) {
            return (Traversable<T>) remove.apply(container, elem);
        }
    }

    @Override
    default Traversable<V> apply(K key) {
        return get(key).getOrElseThrow(NoSuchElementException::new);
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

    /**
     * Returns <code>true</code> if this multimap contains a mapping for the specified key.
     *
     * @param key key whose presence in this multimap is to be tested
     * @return <code>true</code> if this multimap contains a mapping for the specified key
     */
    boolean containsKey(K key);

    ContainerType getContainerType();

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
     * FlatMaps this {@code Multimap} to a new {@code Multimap} with different component type.
     *
     * @param mapper A mapper
     * @param <K2>   key's component type of the mapped {@code Multimap}
     * @param <V2>   value's component type of the mapped {@code Multimap}
     * @return A new {@code Multimap}.
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Multimap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

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
     * Returns the keys contained in this multimap.
     *
     * @return {@code Set} of the keys contained in this multimap.
     */
    Set<K> keySet();

    /**
     * Maps the entries of this {@code Multimap} to form a new {@code Multimap}.
     *
     * @param <K2>   key's component type of the multimap result
     * @param <V2>   value's component type of the multimap result
     * @param mapper a {@code Function} that maps entries of type {@code (K, V)} to entries of type {@code (K2, V2)}
     * @return a new {@code Multimap}
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> Multimap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    /**
     * Maps the values of this {@code Multimap} while preserving the corresponding keys.
     *
     * @param <V2>        the new value type
     * @param valueMapper a {@code Function} that maps values of type {@code V} to values of type {@code V2}
     * @return a new {@code Multimap}
     * @throws NullPointerException if {@code valueMapper} is null
     */
    <V2> Multimap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper);

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
    Multimap<K, V> put(Tuple2<? extends K, ? extends V> entry);

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
     * @param key key whose mapping is to be removed from the multimap
     * @param value value whose mapping is to be removed from the multimap
     * @return A new Multimap containing these elements without the entry
     * specified by that key and value.
     */
    Multimap<K, V> remove(K key, V value);

    /**
     * Removes the mapping for a key from this multimap if it is present.
     *
     * @param keys keys are to be removed from the multimap
     * @return A new Multimap containing these elements without the entries
     * specified by that keys.
     */
    Multimap<K, V> removeAll(Iterable<? extends K> keys);

    @Override
    int size();

    /**
     * Converts this Javaslang {@code Map} to a {@code java.util.Map} while preserving characteristics
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
    default <U> U transform(Function<? super Multimap<K, V>, ? extends U> f) {
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

    Traversable<V> values();

    // -- Adjusted return types of Traversable methods

    @Override
    default boolean contains(Tuple2<K, V> element) {
        return get(element._1).map(v -> Objects.equals(v, element._2)).getOrElse(false);
    }

    @Override
    Multimap<K, V> distinct();

    @Override
    Multimap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> Multimap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    Multimap<K, V> drop(long n);

    @Override
    Multimap<K, V> dropRight(long n);

    @Override
    Multimap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

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
    <C> Map<C, ? extends Multimap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends Multimap<K, V>> grouped(long size);

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    default Option<Tuple2<K, V>> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    @Override
    Multimap<K, V> init();

    @Override
    Option<? extends Multimap<K, V>> initOption();

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
    default <U> Seq<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        // don't remove cast, doesn't compile in Eclipse without it
        return (Seq<U>) iterator().map(mapper).toStream();
    }

    /**
     * Creates a new multimap which by merging the entries of {@code this} multimap and {@code that} multimap.
     * <p>
     * If collisions occur, the value of {@code this} multimap is taken.
     *
     * @param that the other multimap
     * @return A merged multimap
     * @throws NullPointerException if that multimap is null
     */
    Multimap<K, V> merge(Multimap<? extends K, ? extends V> that);

    /**
     * Creates a new multimap which by merging the entries of {@code this} multimap and {@code that} multimap.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> multimap
     * and the second from <code>that</code> multimap.
     *
     * @param <K2>                 key type of that Multimap
     * @param <V2>                 value type of that Multimap
     * @param that                the other multimap
     * @param collisionResolution the collision resolution function
     * @return A merged multimap
     * @throws NullPointerException if that multimap or the given collision resolution function is null
     */
    <K2 extends  K, V2 extends V> Multimap<K, V> merge(Multimap<K2, V2> that, BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution);

    @Override
    Tuple2<? extends Multimap<K, V>, ? extends Multimap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    Multimap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Multimap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Multimap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements);

    @Override
    Multimap<K, V> scan(Tuple2<K, V> zero,
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
    Iterator<? extends Multimap<K, V>> sliding(long size);

    @Override
    Iterator<? extends Multimap<K, V>> sliding(long size, long step);

    @Override
    Tuple2<? extends Multimap<K, V>, ? extends Multimap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    default Spliterator<Tuple2<K, V>> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Multimap<K, V> tail();

    @Override
    Option<? extends Multimap<K, V>> tailOption();

    @Override
    Multimap<K, V> take(long n);

    @Override
    Multimap<K, V> takeRight(long n);

    @Override
    Multimap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Multimap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

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

}
