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

import io.vavr.control.Option;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import org.jspecify.annotations.NonNull;

/**
 * Represents a data structure that can be folded (reduced) into a single value.
 *
 * <p>Folding is the process of combining the elements of a structure using a provided function,
 * typically accumulating a result.
 *
 * <p><strong>Example:</strong>
 *
 * <pre>{@code
 * // Concatenates all elements into a single String: "123"
 * Stream.of("1", "2", "3")
 *       .fold("", (acc, element) -> acc + element);
 * }</pre>
 *
 * @param <T> the type of elements contained in this foldable structure
 * @author Daniel Dietrich
 */
public interface Foldable<T> {

  /**
   * Folds the elements of this structure using the given associative binary operator, starting with
   * the provided {@code zero} element and successively applying {@code combine}.
   *
   * <p>The order in which elements are combined is non-deterministic. Therefore, {@code combine}
   * must be associative to guarantee a consistent result regardless of traversal order.
   *
   * <p>The fold operations differ in how elements are combined:
   *
   * <ul>
   *   <li>{@link #foldLeft(Object, BiFunction)}: combines elements from left to right.
   *   <li>{@link #foldRight(Object, BiFunction)}: combines elements from right to left.
   *   <li>{@code fold}: requires an associative combine operation, as the element traversal is
   *       unordered. Associativity ensures the result is the same regardless of combination order.
   *       Note that most binary operators are not associative, so the result may vary if elements
   *       are combined in a different order.
   *       <p>Together, this {@code Foldable} and the associative {@code combine} operation form a
   *       <a href="https://en.wikipedia.org/wiki/Monoid" target="_blank">Monoid</a>.
   * </ul>
   *
   * <strong>Example:</strong>
   *
   * <pre>{@code
   * // Result: 6
   * Set.of(1, 2, 3).fold(0, (a, b) -> a + b);
   * }</pre>
   *
   * @param zero the initial value to start folding with
   * @param combine the function to combine two elements
   * @return the folded result
   * @throws NullPointerException if {@code combine} is null
   */
  default T fold(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> combine) {
    Objects.requireNonNull(combine, "combine is null");
    return foldLeft(zero, combine);
  }

  /**
   * Folds the elements of this structure from the left, starting with the given {@code zero} value
   * and successively applying the {@code combine} function to each element.
   *
   * <p>Folding from the left means that elements are combined in the order they are encountered,
   * associating each step with the accumulated result so far.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Result: "cba!"
   * List.of("a", "b", "c").foldLeft("!", (acc, x) -> x + acc);
   * }</pre>
   *
   * @param <U> the type of the accumulated result
   * @param zero the initial value to start folding with
   * @param combine a function that combines the accumulated value and the next element
   * @return the folded result
   * @throws NullPointerException if {@code combine} is null
   */
  <U> U foldLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> combine);

  /**
   * Folds the elements of this structure from the right, starting with the given {@code zero} value
   * and successively applying the {@code combine} function to each element.
   *
   * <p>Folding from the right means that elements are combined starting from the last element and
   * associating each step with the accumulated result so far.
   *
   * <p><strong>Example:</strong>
   *
   * <pre>{@code
   * // Result: "!cba"
   * List.of("a", "b", "c").foldRight("!", (x, acc) -> acc + x);
   * }</pre>
   *
   * @param <U> the type of the accumulated result
   * @param zero the initial value to start folding with
   * @param combine a function that combines the next element and the accumulated value
   * @return the folded result
   * @throws NullPointerException if {@code combine} is null
   */
  <U> U foldRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> combine);

  /**
   * Reduces the elements of this Foldable by repeatedly applying the given binary operation {@code
   * op}.
   *
   * <p>The order in which elements are combined is non-deterministic, so {@code op} should be
   * associative to guarantee a consistent result.
   *
   * <p>This method throws {@link NoSuchElementException} if the Foldable is empty.
   *
   * @param op a binary function to combine two elements
   * @return the reduced result
   * @throws NoSuchElementException if this Foldable is empty
   * @throws NullPointerException if {@code op} is null
   */
  default T reduce(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
    Objects.requireNonNull(op, "op is null");
    return reduceLeft(op);
  }

  /**
   * Reduces the elements of this Foldable by repeatedly applying the given binary operation {@code
   * op}.
   *
   * <p>The order of element combination is non-deterministic, so {@code op} should be associative
   * to guarantee a consistent result.
   *
   * @param op a binary function to combine two elements
   * @return an {@link Option} containing the reduced result, or {@link Option#none()} if this
   *     Foldable is empty
   * @throws NullPointerException if {@code op} is null
   */
  default Option<T> reduceOption(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
    Objects.requireNonNull(op, "op is null");
    return reduceLeftOption(op);
  }

  /**
   * Reduces the elements of this Foldable from the left by successively applying the given
   * operation {@code op}.
   *
   * <p>Elements are combined in encounter order, starting from the left.
   *
   * @param op a binary function to combine two elements
   * @return the reduced result
   * @throws NoSuchElementException if this Foldable is empty
   * @throws NullPointerException if {@code op} is null
   */
  T reduceLeft(@NonNull BiFunction<? super T, ? super T, ? extends T> op);

  /**
   * Reduces the elements of this Foldable from the left by successively applying the given
   * operation {@code op}.
   *
   * <p>Returns an {@link Option} instead of throwing an exception if the Foldable is empty.
   *
   * @param op a binary function to combine two elements
   * @return an {@link Option} containing the reduced result, or {@link Option#none()} if empty
   * @throws NullPointerException if {@code op} is null
   */
  Option<T> reduceLeftOption(@NonNull BiFunction<? super T, ? super T, ? extends T> op);

  /**
   * Reduces the elements of this Foldable from the right by successively applying the given
   * operation {@code op}.
   *
   * <p>Elements are combined starting from the rightmost element.
   *
   * @param op a binary function to combine two elements
   * @return the reduced result
   * @throws NoSuchElementException if this Foldable is empty
   * @throws NullPointerException if {@code op} is null
   */
  T reduceRight(@NonNull BiFunction<? super T, ? super T, ? extends T> op);

  /**
   * Reduces the elements of this Foldable from the right by successively applying the given
   * operation {@code op}.
   *
   * <p>Returns an {@link Option} instead of throwing an exception if the Foldable is empty.
   *
   * @param op a binary function to combine two elements
   * @return an {@link Option} containing the reduced result, or {@link Option#none()} if empty
   * @throws NullPointerException if {@code op} is null
   */
  Option<T> reduceRightOption(@NonNull BiFunction<? super T, ? super T, ? extends T> op);
}
