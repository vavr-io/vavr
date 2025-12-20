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
import org.jspecify.annotations.NonNull;

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

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
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
    <U> SortedSet<U> flatMap(@NonNull Comparator<? super U> comparator, Function<? super T, ? extends Iterable<? extends U>> mapper);

    /**
     * Same as {@link #map(Function)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param comparator A comparator for values of type U
     * @param mapper     A function which maps values of type T to values of type U
     * @param <U>        Type of mapped values
     * @return A new Set instance containing mapped values
     */
    <U> SortedSet<U> map(@NonNull Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    // -- Adjusted return types of Set methods

    @Override
    SortedSet<T> add(T element);

    @Override
    SortedSet<T> addAll(@NonNull Iterable<? extends T> elements);

    @Override
    <R> SortedSet<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    SortedSet<T> diff(@NonNull Set<? extends T> elements);

    @Override
    SortedSet<T> distinct();

    @Override
    SortedSet<T> distinctBy(@NonNull Comparator<? super T> comparator);

    @Override
    <U> SortedSet<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor);

    @Override
    SortedSet<T> drop(int n);

    @Override
    SortedSet<T> dropRight(int n);

    @Override
    SortedSet<T> dropUntil(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> dropWhile(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> filter(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> reject(@NonNull Predicate<? super T> predicate);

    @Override
    <U> SortedSet<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C> Map<C, ? extends SortedSet<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier);

    @Override
    Iterator<? extends SortedSet<T>> grouped(int size);

    @Override
    SortedSet<T> init();

    @Override
    Option<? extends SortedSet<T>> initOption();

    @Override
    SortedSet<T> intersect(@NonNull Set<? extends T> elements);

    @Override
    default boolean isOrdered() {
        return true;
    }

    @Override
    <U> SortedSet<U> map(@NonNull Function<? super T, ? extends U> mapper);

    @Override
    default <U> SortedSet<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default SortedSet<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    SortedSet<T> orElse(@NonNull Iterable<? extends T> other);

    @Override
    SortedSet<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> partition(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> peek(@NonNull Consumer<? super T> action);

    @Override
    SortedSet<T> remove(T element);

    @Override
    SortedSet<T> removeAll(@NonNull Iterable<? extends T> elements);

    @Override
    SortedSet<T> replace(T currentElement, T newElement);

    @Override
    SortedSet<T> replaceAll(T currentElement, T newElement);

    @Override
    SortedSet<T> retainAll(@NonNull Iterable<? extends T> elements);

    @Override
    SortedSet<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation);

    // DEV-NOTE: The return type is either Set or SortedSet, depending whether U is Comparable
    @Override
    <U> Set<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation);

    // DEV-NOTE: The return type is either Set or SortedSet, depending whether U is Comparable
    @Override
    <U> Set<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends SortedSet<T>> slideBy(@NonNull Function<? super T, ?> classifier);

    @Override
    Iterator<? extends SortedSet<T>> sliding(int size);

    @Override
    Iterator<? extends SortedSet<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> span(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> tail();

    @Override
    Option<? extends SortedSet<T>> tailOption();

    @Override
    SortedSet<T> take(int n);

    @Override
    SortedSet<T> takeRight(int n);

    @Override
    SortedSet<T> takeUntil(@NonNull Predicate<? super T> predicate);

    @Override
    SortedSet<T> takeWhile(@NonNull Predicate<? super T> predicate);

    @Override
    java.util.SortedSet<T> toJavaSet();

    @Override
    SortedSet<T> union(@NonNull Set<? extends T> elements);

    @Override
    <T1, T2> Tuple2<? extends SortedSet<T1>, ? extends SortedSet<T2>> unzip(@NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends SortedSet<T1>, ? extends SortedSet<T2>, ? extends SortedSet<T3>> unzip3(@NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> SortedSet<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that);

    @Override
    <U, R> SortedSet<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> SortedSet<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    SortedSet<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> SortedSet<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper);
}
