/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import org.jspecify.annotations.Nullable;

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
public interface Set<T extends @Nullable Object> extends Traversable<T>, Function1<T, Boolean>, Serializable {

    /**
     * The serial version UID for serialization.
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
    static <T extends @Nullable Object> Set<T> narrow(Set<? extends T> set) {
        return (Set<T>) set;
    }

    /**
     * Returns a set containing all elements of this set plus the given element,
     * if it was not already present.
     *
     * @param element the element to add
     * @return a set including the element; may be this instance if the element was already present
     */
    Set<T> add(T element);

    /**
     * Returns a set containing all elements of this set plus the given elements,
     * excluding duplicates.
     *
     * @param elements the elements to add
     * @return a set including the additional elements; may be this instance (or {@code elements} itself) if no change was necessary
     */
    Set<T> addAll(Iterable<? extends T> elements);

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
     * Returns a set containing all elements of this set except those in the given set.
     *
     * @param that the set of elements to remove
     * @return a set without the specified elements; may be this instance if none of them was present
     */
    Set<T> diff(Set<? extends T> that);

    /**
     * Returns a set containing only the elements present in both this set and the given set.
     *
     * @param that the set to intersect with
     * @return a set with elements common to both sets; may be this instance if unchanged
     */
    Set<T> intersect(Set<? extends T> that);

    /**
     * Returns a set with the given element removed, if it was present.
     *
     * @param element the element to remove
     * @return a set without the specified element; may be this instance if the element was not present
     */
    Set<T> remove(T element);

    /**
     * Returns a set with all given elements removed, if present.
     *
     * @param elements the elements to remove
     * @return a set without the specified elements; may be this instance if none of them was present
     */
    Set<T> removeAll(Iterable<? extends T> elements);

    /**
     * Converts this Vavr set to a {@code java.util.Set}. Ordered implementations ({@code LinkedHashSet},
     * {@code SortedSet}) preserve their insertion or sort order in the returned set; {@code HashSet} makes
     * no ordering guarantee.
     *
     * @return a new {@code java.util.Set} instance
     */
    @Override
    java.util.Set<T> toJavaSet();

    /**
     * Returns a set containing all distinct elements from this set and the given set.
     *
     * @param that the set to union with
     * @return a set with all elements from both sets; may be this instance (or {@code that} itself) if no change was necessary
     */
    Set<T> union(Set<? extends T> that);

    // -- Adjusted return types of Traversable methods

    @Override
    <R extends @Nullable Object> Set<R> collect(PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    boolean contains(T element);

    @Override
    Set<T> distinct();

    @Override
    Set<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U extends @Nullable Object> Set<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    Set<T> drop(int n);

    @Override
    Set<T> dropRight(int n);

    @Override
    Set<T> dropUntil(Predicate<? super T> predicate);

    @Override
    Set<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Set<T> filter(Predicate<? super T> predicate);

    @Override
    Set<T> reject(Predicate<? super T> predicate);

    @Override
    <U extends @Nullable Object> Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C extends @Nullable Object> Map<C, ? extends Set<T>> groupBy(Function<? super T, ? extends C> classifier);

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
    
    Iterator<T> iterator();

    @Override
    int length();

    @Override
    <U extends @Nullable Object> Set<U> map(Function<? super T, ? extends U> mapper);

    @Override
    default <U extends @Nullable Object> Set<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Set<@Nullable Void> mapToVoid() {
        return this.<@Nullable Void>map(ignored -> null);
    }

    @Override
    Set<T> orElse(Iterable<? extends T> other);

    @Override
    Set<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> partition(Predicate<? super T> predicate);

    @Override
    Set<T> peek(Consumer<? super T> action);

    @Override
    Set<T> replace(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(T currentElement, T newElement);

    @Override
    Set<T> retainAll(Iterable<? extends T> elements);

    @Override
    Set<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U extends @Nullable Object> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U extends @Nullable Object> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends Set<T>> slideBy(Function<? super T, ?> classifier);

    @Override
    Iterator<? extends Set<T>> sliding(int size);

    @Override
    Iterator<? extends Set<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> span(Predicate<? super T> predicate);

    @Override
    Set<T> tail();

    @Override
    Option<? extends Set<T>> tailOption();

    @Override
    Set<T> take(int n);

    @Override
    Set<T> takeRight(int n);

    @Override
    Set<T> takeUntil(Predicate<? super T> predicate);

    @Override
    Set<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1 extends @Nullable Object, T2 extends @Nullable Object> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1 extends @Nullable Object, T2 extends @Nullable Object, T3 extends @Nullable Object> Tuple3<? extends Set<T1>, ? extends Set<T2>, ? extends Set<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U extends @Nullable Object> Set<Tuple2<T, U>> zip(Iterable<? extends U> that);

    @Override
    <U extends @Nullable Object, R extends @Nullable Object> Set<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U extends @Nullable Object> Set<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    Set<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U extends @Nullable Object> Set<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);
}
