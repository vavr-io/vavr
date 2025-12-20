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

import io.vavr.Function1;
import io.vavr.PartialFunction;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.Comparator;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

/**
 * An immutable {@code Set} interface.
 * <p>
 * Vavr {@code Set} implementations generally support {@code null} elements,
 * but {@code SortedSet} implementations require a {@link Comparator}, which may not support {@code null}.
 * <p>
 * Supports standard set operations like addition, removal, union, intersection, and difference.
 * Can be converted to a Java {@link java.util.Set}.
 *
 * @param <T> component type
 * @author Daniel Dietrich
 */
public interface Set<T> extends Traversable<T>, Function1<T, Boolean>, Serializable {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code Set<? extends T>} to {@code Set<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param set A {@code Set}.
     * @param <T> Component type of the {@code Set}.
     * @return the given {@code set} instance as narrowed type {@code Set<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Set<T> narrow(Set<? extends T> set) {
        return (Set<T>) set;
    }

    /**
     * Returns a new set containing all elements of this set plus the given element,
     * if it was not already present.
     *
     * @param element the element to add
     * @return a new set including the element
     */
    Set<T> add(T element);

    /**
     * Returns a new set containing all elements of this set plus the given elements,
     * excluding duplicates.
     *
     * @param elements the elements to add
     * @return a new set including the additional elements
     */
    Set<T> addAll(@NonNull Iterable<? extends T> elements);

    /**
     * Tests if a given {@code element} is contained in this {@code Set}.
     * <p>
     * This method is equivalent to {@link #contains(Object)}.
     *
     * @param element the element to test for membership.
     * @return {@code true} if the given {@code element} is contained, {@code false} otherwise.
     * @deprecated Will be removed
     */
    @Override
    @Deprecated
    default Boolean apply(T element) {
        return contains(element);
    }

    /**
     * Returns a new set containing all elements of this set except those in the given set.
     *
     * @param that the set of elements to remove
     * @return a new set without the specified elements
     */
    Set<T> diff(@NonNull Set<? extends T> that);

    /**
     * Returns a new set containing only the elements present in both this set and the given set.
     *
     * @param that the set to intersect with
     * @return a new set with elements common to both sets
     */
    Set<T> intersect(@NonNull Set<? extends T> that);

    /**
     * Returns a new set with the given element removed, if it was present.
     *
     * @param element the element to remove
     * @return a new set without the specified element
     */
    Set<T> remove(T element);

    /**
     * Returns a new set with all given elements removed, if present.
     *
     * @param elements the elements to remove
     * @return a new set without the specified elements
     */
    Set<T> removeAll(@NonNull Iterable<? extends T> elements);

    /**
     * Converts this Vavr set to a {@code java.util.Set}, preserving insertion or sort order.
     *
     * @return a new {@code java.util.Set} instance
     */
    @Override
    java.util.Set<T> toJavaSet();

    /**
     * Returns a new set containing all distinct elements from this set and the given set.
     *
     * @param that the set to union with
     * @return a new set with all elements from both sets
     */
    Set<T> union(@NonNull Set<? extends T> that);

    // -- Adjusted return types of Traversable methods

    @Override
    <R> Set<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    boolean contains(T element);

    @Override
    Set<T> distinct();

    @Override
    Set<T> distinctBy(@NonNull Comparator<? super T> comparator);

    @Override
    <U> Set<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor);

    @Override
    Set<T> drop(int n);

    @Override
    Set<T> dropRight(int n);

    @Override
    Set<T> dropUntil(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> dropWhile(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> filter(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> reject(@NonNull Predicate<? super T> predicate);

    @Override
    <U> Set<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C> Map<C, ? extends Set<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier);

    @Override
    Iterator<? extends Set<T>> grouped(int size);

    @Override
    Set<T> init();

    @Override
    Option<? extends Set<T>> initOption();

    @Override
    default boolean isDistinct() {
        return true;
    }

    @Override
    @NonNull
    Iterator<T> iterator();

    @Override
    int length();

    @Override
    <U> Set<U> map(@NonNull Function<? super T, ? extends U> mapper);

    @Override
    default <U> Set<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Set<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    Set<T> orElse(@NonNull Iterable<? extends T> other);

    @Override
    Set<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> partition(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> peek(@NonNull Consumer<? super T> action);

    @Override
    Set<T> replace(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(T currentElement, T newElement);

    @Override
    Set<T> retainAll(@NonNull Iterable<? extends T> elements);

    @Override
    Set<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> Set<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> Set<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends Set<T>> slideBy(@NonNull Function<? super T, ?> classifier);

    @Override
    Iterator<? extends Set<T>> sliding(int size);

    @Override
    Iterator<? extends Set<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> span(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> tail();

    @Override
    Option<? extends Set<T>> tailOption();

    @Override
    Set<T> take(int n);

    @Override
    Set<T> takeRight(int n);

    @Override
    Set<T> takeUntil(@NonNull Predicate<? super T> predicate);

    @Override
    Set<T> takeWhile(@NonNull Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(@NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends Set<T1>, ? extends Set<T2>, ? extends Set<T3>> unzip3(@NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> Set<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that);

    @Override
    <U, R> Set<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> Set<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    Set<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> Set<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper);
}
