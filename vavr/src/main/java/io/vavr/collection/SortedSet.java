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

import io.vavr.PartialFunction;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Option;
import java.util.Comparator;
import java.util.function.*;

/**
 * An immutable {@code SortedSet} interface.
 *
 * <p>
 * Specific SortedSet operations:
 *
 * <ul>
 * <li>{@link #comparator()}</li>
 * <li>{@link #flatMap(Comparator, Function)}</li>
 * <li>{@link #map(Comparator, Function)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 */
public interface SortedSet<T> extends Set<T>, Ordered<T> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code SortedSet<? extends T>} to {@code SortedSet<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     * <p>
     * CAUTION: The underlying {@code Comparator} might fail!
     *
     * @param sortedSet A {@code SortedSet}.
     * @param <T>       Component type of the {@code SortedSet}.
     * @return the given {@code sortedSet} instance as narrowed type {@code SortedSet<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> SortedSet<T> narrow(SortedSet<? extends T> sortedSet) {
        return (SortedSet<T>) sortedSet;
    }

    /**
     * Same as {@link #flatMap(Function)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param comparator A comparator for values of type U
     * @param mapper     A function which maps values of type T to Iterables of values of type U
     * @param <U>        Type of flat-mapped values
     * @return A new Set instance containing mapped values
     */
    <U> SortedSet<U> flatMap(Comparator<? super U> comparator, Function<? super T, ? extends Iterable<? extends U>> mapper);

    /**
     * Same as {@link #map(Function)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param comparator A comparator for values of type U
     * @param mapper     A function which maps values of type T to values of type U
     * @param <U>        Type of mapped values
     * @return A new Set instance containing mapped values
     */
    <U> SortedSet<U> map(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    // -- Adjusted return types of Set methods

    @Override
    SortedSet<T> add(T element);

    @Override
    SortedSet<T> addAll(Iterable<? extends T> elements);

    @Override
    <R> SortedSet<R> collect(PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    SortedSet<T> diff(Set<? extends T> elements);

    @Override
    SortedSet<T> distinct();

    @Override
    SortedSet<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> SortedSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    SortedSet<T> drop(int n);

    @Override
    SortedSet<T> dropRight(int n);

    @Override
    SortedSet<T> dropUntil(Predicate<? super T> predicate);

    @Override
    SortedSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    SortedSet<T> filter(Predicate<? super T> predicate);

    @Override
    SortedSet<T> reject(Predicate<? super T> predicate);

    @Override
    <U> SortedSet<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C> Map<C, ? extends SortedSet<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Iterator<? extends SortedSet<T>> grouped(int size);

    @Override
    SortedSet<T> init();

    @Override
    Option<? extends SortedSet<T>> initOption();

    @Override
    SortedSet<T> intersect(Set<? extends T> elements);

    @Override
    default boolean isOrdered() {
        return true;
    }

    @Override
    <U> SortedSet<U> map(Function<? super T, ? extends U> mapper);

    @Override
    SortedSet<T> orElse(Iterable<? extends T> other);

    @Override
    SortedSet<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> partition(Predicate<? super T> predicate);

    @Override
    SortedSet<T> peek(Consumer<? super T> action);

    @Override
    SortedSet<T> remove(T element);

    @Override
    SortedSet<T> removeAll(Iterable<? extends T> elements);

    @Override
    SortedSet<T> replace(T currentElement, T newElement);

    @Override
    SortedSet<T> replaceAll(T currentElement, T newElement);

    @Override
    SortedSet<T> retainAll(Iterable<? extends T> elements);

    @Override
    SortedSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    // DEV-NOTE: The return type is either Set or SortedSet, depending whether U is Comparable
    @Override
    <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    // DEV-NOTE: The return type is either Set or SortedSet, depending whether U is Comparable
    @Override
    <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends SortedSet<T>> slideBy(Function<? super T, ?> classifier);

    @Override
    Iterator<? extends SortedSet<T>> sliding(int size);

    @Override
    Iterator<? extends SortedSet<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> span(Predicate<? super T> predicate);

    @Override
    SortedSet<T> tail();

    @Override
    Option<? extends SortedSet<T>> tailOption();

    @Override
    SortedSet<T> take(int n);

    @Override
    SortedSet<T> takeRight(int n);

    @Override
    SortedSet<T> takeUntil(Predicate<? super T> predicate);

    @Override
    SortedSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    java.util.SortedSet<T> toJavaSet();

    @Override
    SortedSet<T> union(Set<? extends T> elements);

    @Override
    <T1, T2> Tuple2<? extends SortedSet<T1>, ? extends SortedSet<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends SortedSet<T1>, ? extends SortedSet<T2>, ? extends SortedSet<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> SortedSet<Tuple2<T, U>> zip(Iterable<? extends U> that);

    @Override
    <U, R> SortedSet<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> SortedSet<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    SortedSet<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> SortedSet<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);
}
